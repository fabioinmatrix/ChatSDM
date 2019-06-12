package br.edu.ifspsaocarlos.sdm.chatsdm.view;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.chatsdm.R;
import br.edu.ifspsaocarlos.sdm.chatsdm.adapter.PostListAdapter;
import br.edu.ifspsaocarlos.sdm.chatsdm.api.Api;
import br.edu.ifspsaocarlos.sdm.chatsdm.model.Mensagem;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    public static ArrayList<Mensagem> corpoMensagens;
    private Gson gson;
    private Retrofit retrofit;
    private Api mensageiroApi;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    SharedPreferences sharedPreferences;
    private EditText etPosts;
    private String etAssunto = null;
    private String idSharedPreferences;

    public static final String RECEIVER_ID = "RECEIVER_ID";
    public static final String RECEIVER_NAME = "RECEIVER_NAME";

    private String idContatoDestino;
    private String nomeContatoDestino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etPosts = findViewById(R.id.et_post);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_posts);

        idContatoDestino = getIntent().getStringExtra(RECEIVER_ID);
        nomeContatoDestino = getIntent().getStringExtra(RECEIVER_NAME);
        this.setTitle("Contato: " + nomeContatoDestino);

        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        idSharedPreferences = sharedPreferences.getString("id", null);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        gson = gsonBuilder.create();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(Api.URL_BASE);
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        retrofit = builder.build();
        mensageiroApi = retrofit.create(Api.class);
        chat();

        thread.start();
    }

    public void sendMessage(View view) {
        if (!etPosts.getText().toString().trim().equals("")) {
            Mensagem mensagem = new Mensagem();
            mensagem.setCorpo(etPosts.getText().toString());
            mensagem.setAssunto(etAssunto);
            mensagem.setOrigemId(idSharedPreferences);
            mensagem.setDestinoId(idContatoDestino);
            RequestBody mensagemRb = RequestBody.create(MediaType.parse("application/json"), gson.toJson(mensagem));
            mensageiroApi.postMensagem(mensagemRb).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    etPosts.setText("");
                    Toast.makeText(ChatActivity.this, "Mensagem enviada!", Toast.LENGTH_LONG).show();
                    chat();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(this, "Digite a mensagem a ser enviada.", Toast.LENGTH_SHORT).show();
        }
    }

    private void chat() {
        Call<List<Mensagem>> listaMensagensCall =
                mensageiroApi.getMensagens("1", idContatoDestino, idSharedPreferences);
        listaMensagensCall.enqueue(new Callback<List<Mensagem>>() {
            @Override
            public void onResponse(Call<List<Mensagem>> call, Response<List<Mensagem>> response) {
                List<Mensagem> listaMensagens = response.body();
                ChatActivity.corpoMensagens = new ArrayList<>();
                for (Mensagem mensagem : listaMensagens) {
                    ChatActivity.corpoMensagens.add(mensagem);
                }

                Call<List<Mensagem>> listaMensagensCallInt = mensageiroApi.getMensagens("1", idSharedPreferences, idContatoDestino);
                listaMensagensCallInt.enqueue(new Callback<List<Mensagem>>() {
                    @Override
                    public void onResponse(Call<List<Mensagem>> call, Response<List<Mensagem>> response) {
                        List<Mensagem> novolistaMensagens = response.body();
                        for (Mensagem mensagem : novolistaMensagens) {
                            ChatActivity.corpoMensagens.add(mensagem);
                        }

                        Collections.sort(ChatActivity.corpoMensagens, new Comparator<Mensagem>() {
                            public int compare(Mensagem msg1, Mensagem msg2) {
                                return msg1.getId().compareToIgnoreCase(msg2.getId());
                            }
                        });

                        recyclerView.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(null);
                        adapter = new PostListAdapter(corpoMensagens, idSharedPreferences);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<Mensagem>> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "Erro na recuperação das mensagens!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Mensagem>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Erro na recuperação das mensagens!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                while (true) {
                    sleep(5000);
                    chat();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }
}

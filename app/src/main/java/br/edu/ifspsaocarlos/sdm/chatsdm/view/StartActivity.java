package br.edu.ifspsaocarlos.sdm.chatsdm.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import br.edu.ifspsaocarlos.sdm.chatsdm.R;
import br.edu.ifspsaocarlos.sdm.chatsdm.api.Api;
import br.edu.ifspsaocarlos.sdm.chatsdm.model.Contato;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    private String nameSharedPreferences;
    private String nicknameSharedPreferences;
    private String idSharedPreferences;
    private EditText etName;
    private EditText etNickname;
    private Button buttonStart;
    private final String URL_BASE = "http://www.nobile.pro.br/sdm4/mensageiro/";
    private Retrofit retrofit;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        etName = (EditText) findViewById(R.id.et_name);
        etNickname = (EditText) findViewById(R.id.et_nickname);

        buttonStart = (Button) findViewById(R.id.btn_start);
        buttonStart.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        nameSharedPreferences = sharedPreferences.getString("name", null);
        nicknameSharedPreferences = sharedPreferences.getString("nickname", null);

        gson = new Gson();
        retrofit = new Retrofit.Builder().baseUrl(URL_BASE).build();

        if (nameSharedPreferences != null && nicknameSharedPreferences != null) {
            etName.setText(nameSharedPreferences);
            etName.setEnabled(false);
            etNickname.setText(nicknameSharedPreferences);
            etNickname.setEnabled(false);
            buttonStart.setText("Iniciar");
        } else {
            buttonStart.setText("Cadastrar");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                if (etName.getText().length() != 0 && etNickname.getText().length() != 0) {
                    if (nameSharedPreferences == null && nicknameSharedPreferences == null) {
                        Contato contato = new Contato();
                        contato.setNomeCompleto(etName.getText().toString());
                        contato.setApelido(etNickname.getText().toString());

                        try {
                            JSONObject novoContatoJson = new JSONObject(gson.toJson(contato));
                            RequestBody corpoRequisicao = RequestBody.create(MediaType.parse("application/json"), novoContatoJson.toString());

                            Api mensageiroApi = retrofit.create(Api.class);
                            Call<ResponseBody> chamada = mensageiroApi.postContato(corpoRequisicao);

                            AsyncTask<Call<ResponseBody>, Void, Contato> executaRequisicaoAt = new AsyncTask<Call<ResponseBody>, Void, Contato>() {

                                @Override
                                protected Contato doInBackground(Call<ResponseBody>[] calls) {
                                    Call<ResponseBody> chamadaSincrona = calls[0];
                                    try {
                                        Response<ResponseBody> resposta = chamadaSincrona.execute();
                                        Contato contatoCadastrado = gson.fromJson(resposta.body().string(), Contato.class);
                                        return contatoCadastrado;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Contato contato) {
                                    super.onPostExecute(contato);
                                    if (contato.getId() != null) {
                                        idSharedPreferences = contato.getId();
                                        nameSharedPreferences = etName.getText().toString();
                                        nicknameSharedPreferences = etNickname.getText().toString();
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("name", nameSharedPreferences);
                                        editor.putString("nickname", nicknameSharedPreferences);
                                        editor.putString("id", idSharedPreferences);
                                        editor.commit();
                                    }
                                }
                            };

                            executaRequisicaoAt.execute(chamada);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

                    finish();
                    break;

                } else {
                    Toast.makeText(this, "Dados incompletos.", Toast.LENGTH_SHORT).show();
                }
        }
    }
}

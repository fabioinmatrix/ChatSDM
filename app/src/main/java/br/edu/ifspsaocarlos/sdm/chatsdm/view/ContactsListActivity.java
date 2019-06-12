package br.edu.ifspsaocarlos.sdm.chatsdm.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.chatsdm.R;
import br.edu.ifspsaocarlos.sdm.chatsdm.adapter.ContactsListAdapter;
import br.edu.ifspsaocarlos.sdm.chatsdm.api.Api;
import br.edu.ifspsaocarlos.sdm.chatsdm.model.Contato;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactsListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Contato> contactsList;
    private List<String> contactsName;
    private String separation[];
    private Gson gson;
    private Retrofit retrofit;
    private Api mensageiroApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_chat);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        gson = gsonBuilder.create();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(Api.URL_BASE);
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        retrofit = builder.build();
        mensageiroApi = retrofit.create(Api.class);
        Call<List<Contato>> listaContatosCall = mensageiroApi.getContatos();

        listaContatosCall.enqueue(new Callback<List<Contato>>() {
            @Override
            public void onResponse(Call<List<Contato>> call, Response<List<Contato>> response) {

                List<Contato> WSList = response.body();
                contactsList = new ArrayList<>();
                contactsName = new ArrayList<>();

                for (Contato contato : WSList) {
                    String getApelido = contato.getApelido().toString();
                    separation = getApelido.split(Contato.getSEPARADOR());
                    String separationZero = separation[0];

                    if (separationZero.contentEquals(Contato.getUUID())) {
                        contactsName.add(contato.getNomeCompleto());
                        contactsList.add(contato);
                    }
                }
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(null);
                adapter = new ContactsListAdapter(contactsList);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Contato>> call, Throwable t) {
                Toast.makeText(ContactsListActivity.this, "Erro AQUI na recuperação dos contatos!",Toast.LENGTH_SHORT).show();
            }
        });
    }

}

package br.edu.ifspsaocarlos.sdm.chatsdm.view;

import android.content.Intent;
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

public class AddContactActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName;
    private EditText etNickname;
    private Button buttonAddContact;
    private final String URL_BASE = "http://www.nobile.pro.br/sdm4/mensageiro/";
    private Retrofit retrofit;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        etName = (EditText) findViewById(R.id.et_name);
        etNickname = (EditText) findViewById(R.id.et_nickname);

        buttonAddContact = (Button) findViewById(R.id.btn_add_contact);
        buttonAddContact.setOnClickListener(this);

        gson = new Gson();
        retrofit = new Retrofit.Builder().baseUrl(URL_BASE).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_contact:
                if (buttonAddContact.getText().equals("Adicionar Contato")) {
                    if (etName.getText().length() != 0 && etNickname.getText().length() != 0) {

                        Contato contato = new Contato();
                        contato.setNomeCompleto(etName.getText().toString());
                        contato.setApelidoLista(etNickname.getText().toString());

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
                                        Toast.makeText(AddContactActivity.this, "Contato adicionado! Id: " + contato.getId(), Toast.LENGTH_SHORT).show();
                                        etName.setEnabled(false);
                                        etNickname.setEnabled(false);
                                        buttonAddContact.setText("Voltar");
                                    }
                                }
                            };

                            executaRequisicaoAt.execute(chamada);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "Dados incompletos.", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        if (buttonAddContact.getText().equals("Voltar")) {
            Intent intentThisToMainActivity = new Intent(this, MainActivity.class);
            startActivity(intentThisToMainActivity);
        }
    }
}

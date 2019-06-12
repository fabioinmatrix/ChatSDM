package br.edu.ifspsaocarlos.sdm.chatsdm.api;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.chatsdm.model.Contato;
import br.edu.ifspsaocarlos.sdm.chatsdm.model.Mensagem;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    public static final String URL_BASE = "http://www.nobile.pro.br/sdm4/mensageiro/";

    @POST("mensagem")
    Call<ResponseBody> postMensagem(@Body RequestBody novoContato);

    @GET("rawmensagens/{ultimaMensagemId}/{origemId}/{destinoId}")
    Call<List<Mensagem>> getMensagens(@Path("ultimaMensagemId") String ultimaMensagemId,
                                      @Path("origemId") String origemId,
                                      @Path("destinoId") String destinoId);

    @GET("rawcontatos")
    Call<List<Contato>> getContatos();

    @POST("contato")
    Call<ResponseBody> postContato(@Body RequestBody novoContato);
}

package br.edu.ifspsaocarlos.sdm.chatsdm.model;

import com.google.gson.annotations.SerializedName;

public class Contato {
    @SerializedName("nome_completo")
    private String nomeCompleto;
    private String apelido;
    private String id;
    private static final String UUID = "9f349992-7efb-11e8-adc0-fa7ae01bbebc";
    private static final String SEPARADOR = ";";

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelidoLista(String apelido) {
        this.apelido = UUID + SEPARADOR + apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String getUUID() {
        return UUID;
    }

    public static String getSEPARADOR() {
        return SEPARADOR;
    }
}

package br.fib.eleicaoapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiServices {

    @GET("/candidato/list")
    Call<List<Candidato>> findAllCandidatos(@Header("Content-Type") String content_type);

    @GET("/candidato/{id}")
    Call<Candidato> getCandidato(@Header("Content-Type") String content_type, @Path("id") Long id);

    @GET("/candidato/{id}/vota")
    Call<Candidato> sendVota(@Header("Content-Type") String content_type, @Path("id") Long id);
}

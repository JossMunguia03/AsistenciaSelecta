package com.example.controlasistencia.modelo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface IGoogleSheets {
    @GET("exec") // Ahora /exec es la ruta relativa al baseUrl
    Call<String> getData(@QueryMap Map<String, String> queryParameters); // Usamos @QueryMap para pasar parámetros dinámicamente

    @POST("exec") // Igual para el método POST
    Call<String> sendData(@Body DataPost dataPost);

    @POST("exec")
    Call<String> updateData(@Body UpdateData updateData);

    @POST("exec")
    Call<String> deleteData(@Body DeleteRequest deleteRequest);
}

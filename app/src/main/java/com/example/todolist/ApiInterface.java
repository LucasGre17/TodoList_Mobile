package com.example.todolist;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("taches.json")
    Call<List<Task>> getTask();

    @FormUrlEncoded
    @POST("tache")
    Call<String> postTask(@Field("nom") String nom);

    @DELETE("tache/{id}")
    Call<Task> deleteTask(@Path("id") int id);

    @FormUrlEncoded
    @PUT("tache/{id}")
    Call<Task> putTask(@Path("id") int id, @Field("title") String nom);
}
package com.example.todolist;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("taches.json")
    Call<List<Task>> getTask();

    @POST("/tache")
    Call<Task> postTask(@Body Task task);

    @DELETE("tache/{id}")
    Call<Task> deleteTask(@Path("id") int id);
}
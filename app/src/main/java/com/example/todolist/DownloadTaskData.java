package com.example.todolist;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class DownloadTaskData extends AsyncTask<String, Integer, String> {

	private Context context;
	private ArrayList<Task> list;
	private ArrayAdapter<Task> adapter;
	private String url;
	
	public DownloadTaskData(Context context, ArrayList<Task> list, ArrayAdapter<Task> adapter, String url) {
		this.context = context;
		this.list = list;
		this.adapter = adapter;
		this.url = url;
	}
	
	/**
	 * Called before doInBackground() method
	 * This method runs on the UI thread
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.list.clear();											// Supprime toutes les taches de la vue
		this.adapter.notifyDataSetChanged();						// Notifie la vue d'un changement de nos donneees (suppression => voir list.clear())
		Toast.makeText(this.context, "Telechargement des taches en cours...", Toast.LENGTH_SHORT).show();	// Notification UI
	}

	/** 
	 * This method runs on another thread than the UI thread 
	 */
	@Override
	protected String doInBackground(String... url) {
		return this.getListTask();									// Recupere les taches au format JSON sur le service web de notre application
	}
	
	/**
	 * Called after doInBackground() method
	 * This method runs on the UI thread
	 */
	@Override
	protected void onPostExecute(String result) {
		try {
			JSONArray jsonArray = new JSONArray(result);
			for (int i = 0; i < jsonArray.length(); i++) {			// Ajoute les taches a la vue
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				this.list.add(new Task(jsonObject.getLong("id"), jsonObject.getBoolean("isDone"), jsonObject.getString("nom")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.adapter.notifyDataSetChanged();						// Notifie la vue d'un changement de nos donnees (ajout)
		Toast.makeText(this.context, "Telechargement termine !", Toast.LENGTH_SHORT).show();	// Notification UI
	}
	
    public String getListTask() {
		String json = "";

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(url)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		ApiInterface apiInterface = retrofit.create(ApiInterface.class);
		Call call = apiInterface.getTask();
		call.enqueue(new Callback() {
			@Override
			public void onResponse(Call call, Response response) {
				List<Task> jsonc = (List<Task>) response.body();
				for( Task task : jsonc){
					list.add(task);
				}
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Call call, Throwable t) {
				Log.e("TAG", "onFailure: "+t.toString() );
			}
		});

        return json;
    }



}

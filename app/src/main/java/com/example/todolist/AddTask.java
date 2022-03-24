package com.example.todolist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddTask extends AsyncTask<String, Integer, Task> {
	
	private Context context;
	private String url;
	private ArrayList<Task> list;
	private ArrayAdapter<Task> adapter;

	public AddTask(Context context, String url, ArrayList<Task> list, ArrayAdapter<Task> adapter) {
		super();
		this.context = context;
		this.url = url;
		this.list = list;
		this.adapter = adapter;
	}

	@Override
	protected Task doInBackground(String... titles) {
		return this.addTask(titles[0]);
	}
	
	@Override
	protected void onPostExecute(Task result) {
		if(result != null) {
			this.list.add(result);																// Ajout dans la listView (1/2)
			this.adapter.notifyDataSetChanged();												// Ajout dans la listView (2/2)
			Toast.makeText(this.context, "Ajout termine !", Toast.LENGTH_SHORT).show();			// Notification UI
		}
		else {
			Toast.makeText(this.context, "Erreur lors de l'ajout !", Toast.LENGTH_SHORT).show();
		}
		
	}

	public Task addTask(String title) {
		Task task = new Task(title);

		Log.d("TEST nom : ", task.getTitle());

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(url)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		ApiInterface apiInterface = retrofit.create(ApiInterface.class);
		Call<String> call = apiInterface.postTask(task.getTitle());
		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {

			}
			@Override
			public void onFailure(Call<String> call, Throwable t) {
				DownloadTaskData downloadTaskData = new DownloadTaskData(context, list, adapter, url);	//Appel asynchrone pour recuperer les taches depuis l'application en ligne
				downloadTaskData.execute();
			}
		});

		return task;
	}
}

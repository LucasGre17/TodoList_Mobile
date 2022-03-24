package com.example.todolist;

import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

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

public class DeleteTask extends AsyncTask<Task, Integer, Task> {
	
	private Context context;
	private String url;
	private ArrayList<Task> list;
	private ArrayAdapter<Task> adapter;
	
	public DeleteTask(Context context, ArrayList<Task> list, ArrayAdapter<Task> adapter, String url) {
		this.context = context;
		this.list = list;
		this.adapter = adapter;
		this.url = url;
	}

	@Override
	protected Task doInBackground(Task... tasks) {
		return this.deleteTask(tasks[0]);
	}
	
	@Override
	protected void onPostExecute(Task result) {
		this.list.remove(result);																	// Supprime de la listView (1/2)
		this.adapter.notifyDataSetChanged();														// Supprime de la listView (2/2)
		Toast.makeText(this.context, "Suppression termine !", Toast.LENGTH_SHORT).show();			// Notification UI
	}
	
	public Task deleteTask(Task task) {

		Log.d("TEST API :", url);
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(url)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		ApiInterface apiInterface = retrofit.create(ApiInterface.class);
		Call<Task> call = apiInterface.deleteTask((int) task.getId());
		call.enqueue(new Callback() {
			@Override
			public void onResponse(Call call, Response response) {
				Log.e("TAG", "Success: "+ response.toString() );
			}

			@Override
			public void onFailure(Call call, Throwable t) {
				Log.e("TAG", "onFailureDelete: "+t.toString() );
			}
		});
		return task;
	}

}

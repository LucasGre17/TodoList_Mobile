package com.example.todolist;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class EditTask extends AsyncTask<Task, Integer, Boolean> {
	
	private Context context;
	private String url;
	
	public EditTask(Context context, String url) {
		this.context = context;
		this.url = url;
	}
 
	/** 
	 * This method runs on another thread than the UI thread 
	 * */
	@Override
	protected Boolean doInBackground(Task... tasks) {
		return this.editTask(tasks[0]);
	}
	
	/**
	 * Called after doInBackground() method
	 * This method runs on the UI thread
	 */
	@Override
	protected void onPostExecute(Boolean result) {
		if(result) {
			Toast.makeText(this.context, "Enregistrement termine !", Toast.LENGTH_SHORT).show();			// Notification UI
		}
	}
	
	public boolean editTask(Task task) {
		boolean done = false;
		/**
		 * A COMPLETER
		 */
		return done;
	}

}

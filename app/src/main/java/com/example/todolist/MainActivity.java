package com.example.todolist;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	public ArrayList<Task> list = new ArrayList<Task>();	// Liste de taches (en memoire)
	public ListView listView;								// Liste de taches graphique
	public ArrayAdapter<Task> adapter;						// Adapter qui fait la liaison entre la listView et la liste de tache (en memoire)
	public EditText editText;								// Champs qui permet la modification d'une tache
	public Task task = null;								// Tache selectionne dans la vue
	public String urlAppli = "http://10.0.2.2:9000/api/";	// OU http://10.0.2.2:9000/api/ => adresse local du web service de l'appli web play!"
	
    @Override
    public void onCreate(Bundle savedInstanceState) {		// Execution au lancement de l'application
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // ListView
        adapter = new ArrayAdapter<Task>(this, R.layout.simplerow, R.id.simpleRowTextView, this.list);
        listView = (ListView) findViewById(R.id.listTache);
        listView.setAdapter(adapter);
        
        
        
        // Traitement evenements listView => click => modification tache
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {					// Court click => popup de modification tache
			
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {	// Methode appele quand on click sur un des items de la liste
				task = list.get(position);														// Recupere l'objet tache de la liste (list) a partir de la postion de la tache selectionne 
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(arg1.getContext());	// Construction de la Popup de modification de la tache
				alertDialog.setTitle("Modification tache");										// Ajout un titre a la Popup
				editText = new EditText(arg1.getContext());										// Ajoute le champs edition text pour modifier la tache (1/3)
				editText.setText(task.getTitle());												// Ajoute le champs edition text pour modifier la tache (1/3)
				alertDialog.setView(editText);													// Ajoute le champs edition text pour modifier la tache (1/3)
				
				alertDialog.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {	// Ajoute un bouton 'enregistrer' a la Popup
					public void onClick(DialogInterface dialog, int which) {							// Methode appele quand on click sur le bouton 'enregistrer'
						task.setTitle(editText.getText().toString());									// 
						new EditTask(getApplicationContext(), urlAppli).execute(task);					// Appel asynchrone => requete HTTP qui modifie la tache selectionne sur notre appli via le web servive
					}
				});

				alertDialog.setNegativeButton("Supprimer", new DialogInterface.OnClickListener(){			// Ajoute un bouton annuler
					public void onClick(DialogInterface dialog, int which) {
						new DeleteTask(getApplicationContext(), list, adapter, urlAppli).execute(task);																// Ferme la popup
					}
				});


				
				AlertDialog dialog = alertDialog.create();
				dialog.show();
			}
		});
       
        
        
        // Traitement evenements listView => long click => suppression tache
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {		// Long click => popup de suppression tache
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
				task = list.get(position);
				
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(arg1.getContext());
				alertBuilder.setTitle("Supprimer tache");
				alertBuilder.setMessage("Supprimer la tache : " + task.getTitle() + " ?");
				
				alertBuilder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						new DeleteTask(getApplicationContext(), list, adapter, urlAppli).execute(task);
					}
				});
				
				alertBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
				AlertDialog alertDialog = alertBuilder.create();
				alertDialog.show();
				
				return true;
			}
		});
        
		
        // Lance le telechargement des taches (appel au web service de notre application Play!)
        new DownloadTaskData(getApplicationContext(), this.list, adapter, this.urlAppli).execute();			//Appel asynchrone pour recuperer les taches depuis l'application heberge sur Heroku
    }
    
   
    // Menu =====
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	// Click item menu (actualiser)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Ajouter une tache
			case R.id.menu_add_task:
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);	
				alertDialog.setTitle("Ajouter tache");										
				editText = new EditText(getApplicationContext());										
				alertDialog.setView(editText);			
				alertDialog.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {	
					public void onClick(DialogInterface dialog, int which) {							
						new AddTask(getApplicationContext(), urlAppli, list, adapter).execute(editText.getText().toString());	
					}
				});
				alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				AlertDialog dialog = alertDialog.create();
				dialog.show();
				return true;
			
			// Sychroniser les taches avec l'appli Play!
			case R.id.menu_settings:
				DownloadTaskData downloadTaskData = new DownloadTaskData(getApplicationContext(), this.list, this.adapter, this.urlAppli);	//Appel asynchrone pour recuperer les taches depuis l'application en ligne
		        downloadTaskData.execute();
		        return true;
	
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
    
}

package com.example.todolist;

public class Task {
	
	private long id;
	private boolean isDone;
	private String nom;

	public Task(long id, boolean done, String title) {
		this.id = id;
		this.isDone = done;
		this.nom = title;
	}

	public Task(String title) {
		this.nom = title;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean done) {
		this.isDone = done;
	}

	public String getTitle() {
		return nom;
	}

	public void setTitle(String title) {
		this.nom = title;
	}

	@Override
    public String toString() {
        return this.nom;
    }

}

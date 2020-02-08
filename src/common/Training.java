package common;

import java.util.ArrayList;

public class Training {
	private long id;
	private String name;
	private String description;
	private String keywords;
	ArrayList<Long> questions;
	private ArrayList<TrainingImage> images;
	
	public Training(long id, String name, String description, String keywords, ArrayList<Long> questions, ArrayList<TrainingImage> images){
		this.id = id;
		this.name = name;
		this.description = description;
		this.keywords = keywords;
		this.questions = questions;
		this.images = images;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public ArrayList<Long> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Long> questions) {
		this.questions = questions;
	}

	public ArrayList<TrainingImage> getImages() {
		return images;
	}

	public void setImages(ArrayList<TrainingImage> images) {
		this.images = images;
	}

}

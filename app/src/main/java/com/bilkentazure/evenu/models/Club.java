package com.bilkentazure.evenu.models;

/**
 * Created by Endri Suknaj on 27/04/2018.
 * This class is club model
 * @author Endri Suknaj
 */
public class Club{

	private String id;
	private String name;
	private String description;
	private String image;
	private String thumbImage;

	public Club(){
		this.id = "";
		this.name = "";
		this.description = "";
		this.image = "";
		this.thumbImage = "";
	}

	public Club(String id, String name, String description,String image, String thumbImage){
		this.id = id;
		this.name = name;
		this.description = description;
		this.image = image;
		this.thumbImage = thumbImage;
	}

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getImage(){
		return image;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getThumbImage(){
		return thumbImage;
	}

	public void setThumbImage(String thumbImage){
		this.thumbImage = thumbImage;
	}

	@Override
	public String toString() {
		return "Club{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", image='" + image + '\'' +
				", thumbImage='" + thumbImage + '\'' +
				'}';
	}
}
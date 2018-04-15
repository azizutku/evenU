package com.bilkentazure.evenu.models;

public class User {


	//Variables
	String email;
	String schoolID;
	String name;
	String image;
	String thumbImage;
	String tokenID;

	//Constructors
	public User(){
		email = "";
		name = "";
		schoolID = "";
		image = "";
		thumbImage = "";
		tokenID = "";
	}

	public User(String email, String schoolID, String name, String image,String thumbImage){
		this.email = email;
		this.name = name;
		this.schoolID = schoolID;
		this.image = image;
		this.thumbImage = thumbImage;
	}

	public User(String email, String schoolID, String name, String image,String thumbImage,String tokenID){
		this.email = email;
		this.name = name;
		this.schoolID = schoolID;
		this.image = image;
		this.thumbImage = thumbImage;
		this.tokenID = tokenID;
	}

	//Getter and Setter

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchoolID() {
		return schoolID;
	}

	public void setSchoolID(String schoolID) {
		this.schoolID = schoolID;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getThumbImage() {
		return thumbImage;
	}

	public void setThumbImage(String thumbImage) {
		this.thumbImage = thumbImage;
	}

	public String getTokenID() {
		return tokenID;
	}

	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}

	//Methods

	@Override
	public String toString() {
		return "User{" +
				"email='" + email + '\'' +
				", schoolID='" + schoolID + '\'' +
				", name='" + name + '\'' +
				", image='" + image + '\'' +
				", thumbImage='" + thumbImage + '\'' +
				", tokenID='" + tokenID + '\'' +
				'}';
	}

}
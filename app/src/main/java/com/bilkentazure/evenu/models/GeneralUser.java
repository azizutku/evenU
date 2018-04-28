package com.bilkentazure.evenu.models;

/**
 * Created by Aziz Utku Kağıtcı on 28/04/2018
 * @author Aziz Utku Kağıtcı
 * @version 28/04/2018
 */
public class GeneralUser {

	//Variables
	private String schoolId;
	private String email;
	private String name;
	private String department;
	private String image;
	private String thumbImage;

	//Constructors

	public GeneralUser() {
	}

	public GeneralUser(String schoolId, String email, String name, String department, String image, String thumbImage) {
		this.schoolId = schoolId;
		this.email = email;
		this.name = name;
		this.department = department;
		this.image = image;
		this.thumbImage = thumbImage;
	}

	//Getter and Setter

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
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

	//Methods
	@Override
	public String toString() {
		return "GeneralUser{" +
				"schoolId='" + schoolId + '\'' +
				", email='" + email + '\'' +
				", name='" + name + '\'' +
				", department='" + department + '\'' +
				", image='" + image + '\'' +
				", thumbImage='" + thumbImage + '\'' +
				'}';
	}
}

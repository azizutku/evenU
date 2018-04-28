package com.bilkentazure.evenu.models;


import java.util.ArrayList;

/**
 * Created by Endri Suknaj on 27/04/2018
 * @author Endri Suknaj, Aziz Utku Kağıtcı
 * @version 28/04/2018
 */
public class User {

	//Variables
	private String schoolId;
	private String email;
	private String name;
	private String department;
	private String image;
	private String thumbImage;
	private String tokenID;
	private int geTotal;
	private boolean takeGe250;
	private boolean takeGe251;
	ArrayList<String> attendedEvents;
	ArrayList<String> favoriteEvents;
	ArrayList<String> subscribedDepartments;
	ArrayList<String> subscribeInterests;
	ArrayList<String> subscribedClubs;

	//Constructors

	public User() {
	}


	public User(String schoolId, String email, String name, String department, String image, String thumbImage, String tokenID, int geTotal, boolean takeGe250, boolean takeGe251, ArrayList<String> attendedEvents, ArrayList<String> favoriteEvents, ArrayList<String> subscribedDepartments, ArrayList<String> subscribeInterests, ArrayList<String> subscribedClubs) {
		this.schoolId = schoolId;
		this.email = email;
		this.name = name;
		this.department = department;
		this.image = image;
		this.thumbImage = thumbImage;
		this.tokenID = tokenID;
		this.geTotal = geTotal;
		this.takeGe250 = takeGe250;
		this.takeGe251 = takeGe251;
		this.attendedEvents = attendedEvents;
		this.favoriteEvents = favoriteEvents;
		this.subscribedDepartments = subscribedDepartments;
		this.subscribeInterests = subscribeInterests;
		this.subscribedClubs = subscribedClubs;
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

	public String getTokenID() {
		return tokenID;
	}

	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}

	public int getGeTotal() {
		return geTotal;
	}

	public void setGeTotal(int geTotal) {
		this.geTotal = geTotal;
	}

	public boolean isTakeGe250() {
		return takeGe250;
	}

	public void setTakeGe250(boolean takeGe250) {
		this.takeGe250 = takeGe250;
	}

	public boolean isTakeGe251() {
		return takeGe251;
	}

	public void setTakeGe251(boolean takeGe251) {
		this.takeGe251 = takeGe251;
	}

	public ArrayList<String> getAttendedEvents() {
		return attendedEvents;
	}

	public void setAttendedEvents(ArrayList<String> attendedEvents) {
		this.attendedEvents = attendedEvents;
	}

	public ArrayList<String> getFavoriteEvents() {
		return favoriteEvents;
	}

	public void setFavoriteEvents(ArrayList<String> favoriteEvents) {
		this.favoriteEvents = favoriteEvents;
	}

	public ArrayList<String> getSubscribedDepartments() {
		return subscribedDepartments;
	}

	public void setSubscribedDepartments(ArrayList<String> subscribedDepartments) {
		this.subscribedDepartments = subscribedDepartments;
	}

	public ArrayList<String> getSubscribeInterests() {
		return subscribeInterests;
	}

	public void setSubscribeInterests(ArrayList<String> subscribeInterests) {
		this.subscribeInterests = subscribeInterests;
	}

	public ArrayList<String> getSubscribedClubs() {
		return subscribedClubs;
	}

	public void setSubscribedClubs(ArrayList<String> subscribedClubs) {
		this.subscribedClubs = subscribedClubs;
	}

	//Methods

	@Override
	public String toString() {
		return "User{" +
				"schoolId='" + schoolId + '\'' +
				", email='" + email + '\'' +
				", name='" + name + '\'' +
				", department='" + department + '\'' +
				", image='" + image + '\'' +
				", thumbImage='" + thumbImage + '\'' +
				", tokenID='" + tokenID + '\'' +
				", geTotal=" + geTotal +
				", takeGe250=" + takeGe250 +
				", takeGe251=" + takeGe251 +
				", attendedEvents=" + attendedEvents +
				", favoriteEvents=" + favoriteEvents +
				", subscribedDepartments=" + subscribedDepartments +
				", subscribeInterests=" + subscribeInterests +
				", subscribedClubs=" + subscribedClubs +
				'}';
	}
}
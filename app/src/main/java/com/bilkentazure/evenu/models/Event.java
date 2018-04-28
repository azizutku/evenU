package com.bilkentazure.evenu.models;

import java.sql.Timestamp;
import java.util.Date;


/**
 * Created by Endri Suknaj on 27/04/2018.
 * This class is event model
 * @author Endri Suknaj
 */

public class Event{
	String id;
	String name;
	String clubID;
	String description;
	String location;
	String qrID;
	int gePoint;
	String[] tags;
	String[] keywords;
	Date date;
	Timestamp from;
	Timestamp to;
	public Event(String id, String name, String clubID,int gePoints, String[] tags, String[] keywords, String description, String location, String qrID){
		this.id = id;
		this.name = name;
		date = new Date();
		this.clubID = clubID;
		this.gePoint = gePoint;
		this.tags = tags.clone();
		this.keywords = keywords.clone();
		this.description = description;
		this.location = location;
		this.qrID = qrID;
		from = new Timestamp(System.currentTimeMillis());
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

	public Date getDate(){
		return date;
	}

	public void setDate(Date date){
		this.date = new Date(date.getTime());
	}

	public String getClubID(){
		return clubID;
	}

	public void setClubID(String clubID){
		this.clubID = clubID;
	}

	public int getGEPoints(){
		return gePoint;
	}

	public void setGEPoints(int gePoint){
		this.gePoint = gePoint;
	}

	public String[] getTags(){
		return tags;
	}

	public void setTags(String[] tags){
		this.tags = tags.clone();
	}

	public String[] getKeywords(){
		return keywords;
	}

	public void setKeywords(String[] keywords){
		this.keywords = keywords.clone();
	}
	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getLocation(){
		return location;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getQrID(){
		return qrID;
	}

	public void setQrID(String qrID){
		this.qrID = qrID;
	}

	public Timestamp getTimestampFrom(){
		return from;
	}

	public void setTimestampFrom(Timestamp from){
		this.from = from;
	}

	public Timestamp getTimestampTo(){
		to = new Timestamp(System.currentTimeMillis());
		return to;
	}

	public void setTimestampTo(Timestamp to){
		this.to = to;
	}
}


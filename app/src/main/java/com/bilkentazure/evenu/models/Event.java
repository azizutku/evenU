package com.bilkentazure.evenu.models;

import java.util.Date;
import java.util.Map;


/**
 * Created by Endri Suknaj on 27/04/2018.
 * Edited by Aziz Utku Kağıtcı on 28/04/2018
 * This class is eventmodel
 * @author Endri Suknaj
 */
public class Event{

	private String id;
	private String club_id;
	private String name;
	private String description;
	private String location;
	private Date from;
	private Date to;
	private int ge_point;
	private Map<String,Boolean> tags;
	private Map<String,Boolean> keywords;
	private String qr_id;
	private String spreadsheet;
	private String security_check;


	public Event() {

	}

	public Event(String id, String club_id, String name, String description, String location, Date from, Date to, int ge_point, Map<String, Boolean> tags, Map<String, Boolean> keywords, String qr_id, String spreadsheet, String security_check) {
		this.id = id;
		this.club_id = club_id;
		this.name = name;
		this.description = description;
		this.location = location;
		this.from = from;
		this.to = to;
		this.ge_point = ge_point;
		this.tags = tags;
		this.keywords = keywords;
		this.qr_id = qr_id;
		this.spreadsheet = spreadsheet;
		this.security_check = security_check;
	}


	//Getter and Setter

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClub_id() {
		return club_id;
	}

	public void setClub_id(String club_id) {
		this.club_id = club_id;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public int getGe_point() {
		return ge_point;
	}

	public void setGe_point(int ge_point) {
		this.ge_point = ge_point;
	}

	public Map<String, Boolean> getTags() {
		return tags;
	}

	public void setTags(Map<String, Boolean> tags) {
		this.tags = tags;
	}

	public Map<String, Boolean> getKeywords() {
		return keywords;
	}

	public void setKeywords(Map<String, Boolean> keywords) {
		this.keywords = keywords;
	}

	public String getQr_id() {
		return qr_id;
	}

	public void setQr_id(String qr_id) {
		this.qr_id = qr_id;
	}

	public String getSpreadsheet() {
		return spreadsheet;
	}

	public void setSpreadsheet(String spreadsheet) {
		this.spreadsheet = spreadsheet;
	}

	public String getSecurity_check() {
		return security_check;
	}

	public void setSecurity_check(String security_check) {
		this.security_check = security_check;
	}

	//methods

	@Override
	public String toString() {
		return "Event{" +
				"id='" + id + '\'' +
				", club_id='" + club_id + '\'' +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", location='" + location + '\'' +
				", from=" + from +
				", to=" + to +
				", ge_point=" + ge_point +
				", tags=" + tags +
				", keywords=" + keywords +
				", qr_id='" + qr_id + '\'' +
				", spreadsheet='" + spreadsheet + '\'' +
				", security_check='" + security_check + '\'' +
				'}';
	}
}


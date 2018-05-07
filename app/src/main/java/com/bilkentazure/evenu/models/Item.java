package com.bilkentazure.evenu.models;

/**
 * Created by Aziz Utku Kağıtcı on 06/05/2018
 *
 * @author Aziz Utku Kağıtcı
 * @version 06/05/2018
 */

public class Item {

	public final static String ITEM_DEPARTMENT = "department";
	public final static String ITEM_CLUB = "club";
	public final static String ITEM_INTEREST = "interest";

	private String name;
	private String kind;
	private boolean followed;

	public Item() {
	}

	public Item(String name, String kind, boolean followed) {
		this.name = name;
		this.kind = kind;
		this.followed = followed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public boolean isFollowed() {
		return followed;
	}

	public void setFollowed(boolean followed) {
		this.followed = followed;
	}
}

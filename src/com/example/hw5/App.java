/* FullName: Rohit Pankaj Ruparel*/

package com.example.hw5;

import java.io.Serializable;

import com.parse.ParseObject;

public class App implements Serializable {
	private final String PARSE_TABLE = "Favorites";
	private final String TITLE = "title";
	private final String ID = "id";
	private final String DEVELOPER = "developerName";
	private final String URL = "url";
	private final String SMALL_PHOTO = "smallPhoto";
	private final String LARGE_PHOTO = "largePhoto";
	private final String APP_PRICE = "appPrice";
	private final String RELEASE_DATE = "releaseDate";



	String title, id, url, smallPhoto, largePhoto, developerName, appPrice, releaseDate;

	public App() {

	}

	public App(String title, String id, String url, String smallPhoto,
	String largePhoto, String developerName, String appPrice, String releaseDate) {
		super();
		this.title = title;
		this.id = id;
		this.url = url;
		this.smallPhoto = smallPhoto;
		this.largePhoto = largePhoto;
		this.developerName = developerName;
		this.appPrice = appPrice;
		this.releaseDate = releaseDate;
	}

	public App(ParseObject o) {
		this.title = o.getString(TITLE);
		this.id = o.getString(ID);
		this.url = o.getString(URL);
		this.smallPhoto = o.getString(SMALL_PHOTO);
		this.largePhoto = o.getString(LARGE_PHOTO);
		this.developerName = o.getString(DEVELOPER);
		this.appPrice = o.getString(APP_PRICE);
		this.releaseDate = o.getString(RELEASE_DATE);

	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSmallPhoto() {
		return smallPhoto;
	}

	public void setSmallPhoto(String smallPhoto) {
		this.smallPhoto = smallPhoto;
	}

	public String getLargePhoto() {
		return largePhoto;
	}

	public void setLargePhoto(String largePhoto) {
		this.largePhoto = largePhoto;
	}

	public String getDeveloperName() {
		return developerName;
	}

	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}

	public String getAppPrice() {
		return appPrice;
	}

	public void setAppPrice(String appPrice) {
		this.appPrice = appPrice;
	}



	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public ParseObject getParseObject() {
		ParseObject o = new ParseObject(PARSE_TABLE);
		o.put(ID, this.getId());
		o.put(TITLE, this.getTitle());
		o.put(APP_PRICE, this.getAppPrice());
		o.put(DEVELOPER, this.getDeveloperName());
		o.put(LARGE_PHOTO, this.getLargePhoto());
		o.put(SMALL_PHOTO, this.getSmallPhoto());
		o.put(URL, this.getUrl());
		o.put(RELEASE_DATE, this.getReleaseDate());

		return o;
	}

	@Override
	public String toString() {
		return "App [title=" + title + ", id=" + id + ", url=" + url + ", smallPhoto=" + smallPhoto + ", largePhoto=" + largePhoto + ", developerName=" + developerName + ", appPrice=" + appPrice + "]";
	}


}
package com.uscs.movies.http;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class HttpTheater {

	private int theaterId;
	
	private String street;

	private String theaterName;

	private String city;

	private String state;

	private int zipcode;

	public void setTheaterId(int theaterId) {
		this.theaterId = theaterId;
	}

	public int getTheaterId() {
		return theaterId;
	}

	public void setTheaterName(String theaterName) {
		this.theaterName = theaterName;
	}

	public String getTheaterName() {
		return theaterName;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet() {
		return street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	public int getZipcode() {
		return zipcode;
	}

	@Override
	public String toString() {
		return "HttpTheater [id=" + theaterId + ", theaterName=" + theaterName + ", city=" + city + ", zipcode="
				+ zipcode + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpTheater other = (HttpTheater) obj;
		if (theaterName == null) {
			if (other.theaterName != null)
				return false;
		} else if (!theaterName.equals(other.theaterName))
			return false;
		if (theaterId != other.theaterId)
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
}

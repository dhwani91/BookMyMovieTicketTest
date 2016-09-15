package com.uscs.movies.http;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "theater")
public class HttpTheater {
	@XmlElement
	public int theaterId;
	
	@XmlElement
	public String theaterName;
	
	@XmlElement
	public String city;
	
	@XmlElement
	public String state;
	
	@XmlElement
	public String Street;
	
	@XmlElement
	public int zipcode;
	

	
	@Override
	public String toString() {
		return "HttpTheater [id=" + theaterId + ", theaterName=" + theaterName
				+ ", city=" + city + ", zipcode=" + zipcode + "]";
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
		} else if (! state.equals(other.state))
			return false;
		return true;
	}
}

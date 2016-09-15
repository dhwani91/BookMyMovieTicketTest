package com.uscs.movies.http;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "movie")
public class HttpMovie {
	@XmlElement
	public int movieId;
	
	@XmlElement
	public String movieName;
	
	@XmlElement
	public String movieType;
	
	@XmlElement
	public String movieDesc;
	

	
	@Override
	public String toString() {
		return "HttpMovie [id=" + movieId + ", movieName=" + movieName
				+ ", movieType=" + movieType + ", movieDesc=" + movieDesc + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpMovie other = (HttpMovie) obj;
		if (movieName == null) {
			if (other.movieName != null)
				return false;
		} else if (!movieName.equals(other.movieName))
			return false;
		if (movieId != other.movieId)
			return false;
		if (movieType == null) {
			if (other.movieType != null)
				return false;
		} else if (!movieType.equals(other.movieType))
			return false;
		if (movieDesc == null) {
			if (other.movieDesc != null)
				return false;
		} else if (!movieDesc.equals(other.movieDesc))
			return false;
		return true;
	}
}

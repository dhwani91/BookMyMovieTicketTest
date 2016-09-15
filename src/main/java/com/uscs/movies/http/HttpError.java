package com.uscs.movies.http;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "error")
public class HttpError {
	@XmlElement
	public int status;

	@XmlElement
	public String code;

	@XmlElement
	public String message;

	@XmlElement
	public String debug;

	@Override
	public String toString() {
		return "HttpError [status=" + status + ", code=" + code + ", message=" + message + ", debug=" + debug + "]";
	}

}

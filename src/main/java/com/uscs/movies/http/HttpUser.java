package com.uscs.movies.http;



	import javax.xml.bind.annotation.XmlElement;
	import javax.xml.bind.annotation.XmlRootElement;

	/**
	 * Select fields we want exposed to the REST layer. Separation from business/data layer. 
	 * 
	 * Note "XML" annotation. The resteasy-jettison implementation converts these to JSON depending on
	 * the Accept media type
	 * 
	 * @author rahul
	 *
	 */
	@XmlRootElement(name = "user")
	public class HttpUser {
		
		@XmlElement
		public long id;
		
		@XmlElement
		public String firstName;
		
		@XmlElement
		public String lastName;
		
		@XmlElement
		public String email;
		
		@XmlElement
		public String password;
		
		@Override
		public String toString() {
			return "HttpUser [id=" + id + ", firstName=" + firstName
					+ ", lastName=" + lastName + ", email=" + email + "]";
		}

//		@Override
//		public int hashCode() {
//			final int prime = 31;
//			int result = 1;
//			result = prime * result
//					+ ((firstName == null) ? 0 : firstName.hashCode());
//			result = prime * result + (int) (id ^ (id >>> 32));
//			result = prime * result
//					+ ((lastName == null) ? 0 : lastName.hashCode());
//			result = prime * result + ((pin == null) ? 0 : pin.hashCode());
//			return result;
//		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			HttpUser other = (HttpUser) obj;
			if (firstName == null) {
				if (other.firstName != null)
					return false;
			} else if (!firstName.equals(other.firstName))
				return false;
			if (id != other.id)
				return false;
			if (lastName == null) {
				if (other.lastName != null)
					return false;
			} else if (!lastName.equals(other.lastName))
				return false;
			if (email == null) {
				if (other.email != null)
					return false;
			} else if (!email.equals(other.email))
				return false;
			return true;
		}
		
	}



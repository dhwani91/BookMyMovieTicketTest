package com.uscs.movies.http;

import java.util.List;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestTheaterResource {
	private static final String HTTP_HOST = "http://localhost:8080";
	private static final String URI_PATH = "MovieService/rest/theaters";
	
	private Client client = ClientBuilder.newClient();
	private WebTarget target;
	
	@Before
	public void init(){
		target = client.target(HTTP_HOST).path(URI_PATH);
	}

	@Test
	public void testGetUsersNoParamsJson(){						
		Response response =	target.request().accept(MediaType.APPLICATION_JSON).get();

		//you can use this to print the response
		//System.out.println("HTTP Status=" + response.getStatus());
		//NOTE - you can read the entity ONLY once
		//System.out.println(response.readEntity(String.class));
				
		verifyMissing(response);
	}
	

	private void verifyMissing(Response response) {
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertEquals(409, response.getStatus());
		Assert.assertEquals(409, error.status);
		Assert.assertEquals(500, response.getStatus());
		Assert.assertEquals(500, error.status);
		Assert.assertEquals("MISSING_DATA", error.code);
		Assert.assertEquals("no search parameter provided", error.message);
		Assert.assertEquals("", error.debug);		
	}
	
	@Test
	public void testCreateUsersNoParamsXml(){					
		Response response =	target.request().accept(MediaType.APPLICATION_XML).post(Entity.entity("<theaters/>", MediaType.APPLICATION_XML));
		
		verifyInvalid(response);
	}
	
	@Test
	public void testCreateUsersNoParamsEntityXml(){					
		HttpMovie movieToSend = new HttpMovie();		
		Response response =	target.request().accept(MediaType.APPLICATION_XML).post(Entity.entity(movieToSend, MediaType.APPLICATION_XML));
		
		verifyInvalid(response);
	}
	
	@Test
	public void testCreateUsersNoParamsJson(){					
		Response response =	target.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity("{theaters:{}}", MediaType.APPLICATION_JSON));
		
		verifyInvalid(response);
	}
	
	@Test
	public void testCreateUsersNoParamsEntityJson(){					
		HttpUser userToSend = new HttpUser();		
		Response response =	target.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(userToSend, MediaType.APPLICATION_JSON));
		
		verifyInvalid(response);
	}

	private void verifyInvalid(Response response) {
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertEquals(409, response.getStatus());
		Assert.assertEquals(409, error.status);
		Assert.assertEquals("INVALID_FIELD", error.code);
		Assert.assertEquals("theater name requiered", error.message);
		Assert.assertEquals("", error.debug);		
	}
	
	@Test
	public void testCreateAndGettheater(){					
		HttpTheater theaterToSend = new HttpTheater();
		theaterToSend.theaterName="hello"+new Random().nextInt(99999);
		theaterToSend.Street="Greek"+new Random().nextInt(99999)+"blvd";
		theaterToSend.city="sanjose";
		theaterToSend.state="california";
		theaterToSend.zipcode=new Random().nextInt(99999);
		
		Response response =	target.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(theaterToSend, MediaType.APPLICATION_JSON));
		
		HttpTheater createResponse = response.readEntity(HttpTheater.class);
		//System.err.println(createResponse);
		Assert.assertEquals(201, response.getStatus());
		Assert.assertEquals(createResponse.theaterName, theaterToSend.theaterName);
		Assert.assertEquals(createResponse.city, theaterToSend.city);
		Assert.assertEquals(createResponse.state, theaterToSend.state);
		Assert.assertEquals(createResponse.zipcode, theaterToSend.zipcode);
		Assert.assertNotNull(createResponse.zipcode);
		Assert.assertNotNull(createResponse.theaterName);
		Assert.assertNotNull(createResponse.city);
		Assert.assertNotNull(createResponse.state);
		
		//search for just created user		
//		Response search = target.queryParam("firstName", userToSend.firstName).request().accept(MediaType.APPLICATION_JSON).get();
//		List<HttpUser> searchResponse = search.readEntity(new GenericType<List<HttpUser>>(){});
//		Assert.assertEquals(searchResponse.get(0), createResponse);		
	}
	
}

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

public class TestMovieResource {
	private static final String HTTP_HOST = "http://localhost:8080";
	private static final String URI_PATH = "MovieService/rest/movies";
	
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
	
//	@Test
//	public void testGetUsersNoParamsXml(){						
//		Response response =	target.request().accept(MediaType.APPLICATION_XML).get();
//		
//		verifyMissing(response);
//	}

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
		Response response =	target.request().accept(MediaType.APPLICATION_XML).post(Entity.entity("<movies/>", MediaType.APPLICATION_XML));
		
		verifyInvalid(response);
	}
	
	@Test
	public void testCreateUsersNoParamsEntityXml(){					
		HttpUser userToSend = new HttpUser();		
		Response response =	target.request().accept(MediaType.APPLICATION_XML).post(Entity.entity(userToSend, MediaType.APPLICATION_XML));
		
		verifyInvalid(response);
	}
	
	@Test
	public void testCreateUsersNoParamsJson(){					
		Response response =	target.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity("{movies:{}}", MediaType.APPLICATION_JSON));
		
		verifyInvalid(response);
	}
	
	@Test
	public void testCreateUsersNoParamsEntityJson(){					
		HttpMovie movieToSend = new HttpMovie();		
		Response response =	target.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(movieToSend, MediaType.APPLICATION_JSON));
		
		verifyInvalid(response);
	}

	private void verifyInvalid(Response response) {
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertEquals(409, response.getStatus());
		Assert.assertEquals(409, error.status);
		Assert.assertEquals("INVALID_FIELD", error.code);
		Assert.assertEquals("movie name is required", error.message);
		Assert.assertEquals("", error.debug);		
	}
	
	@Test
	public void testCreateAndGetMovie(){					
		HttpMovie movieToSend = new HttpMovie();
		movieToSend.movieName="spiderman"+new Random().nextInt(99999);
		movieToSend.movieType="comedy";
		movieToSend.movieDesc="action and comedy";
		
		
		Response response =	target.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(movieToSend, MediaType.APPLICATION_JSON));
		
		HttpMovie createResponse = response.readEntity(HttpMovie.class);
		//System.err.println(createResponse);
		Assert.assertEquals(201, response.getStatus());
		Assert.assertEquals(createResponse.movieName, movieToSend.movieName);
		Assert.assertEquals(createResponse.movieDesc, movieToSend.movieDesc);
		Assert.assertEquals(createResponse.movieType, movieToSend.movieType);
		Assert.assertNotNull(createResponse.movieId);
		Assert.assertNull(createResponse.movieName);
		
		//search for just created user		
		Response search = target.queryParam("movieType", movieToSend.movieType).request().accept(MediaType.APPLICATION_JSON).get();
		List<HttpMovie> searchResponse = search.readEntity(new GenericType<List<HttpMovie>>(){});
		Assert.assertEquals(searchResponse.get(0), createResponse);		
	}
	
}

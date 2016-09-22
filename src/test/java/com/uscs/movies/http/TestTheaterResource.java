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
	private static final String URI_PATH = "/MovieService/rest/theaters";
	
	private Client client = ClientBuilder.newClient();
	private WebTarget target;
	
	@Before
	public void init(){
		target = client.target(HTTP_HOST).path(URI_PATH);
	}

	@Test
	public void testTheaterCRUD() {
		// create Theater
		HttpTheater theater = new HttpTheater();
		theater.setTheaterName("citygold" + new Random().nextInt(99999));
		theater.setStreet( new Random().nextInt(99999)+ "blvd");
		theater.setCity("sanjose"+ new Random().nextInt(99999));
		theater.setState("california");
		theater.setZipcode(new Random().nextInt(99999));
		HttpTheater createResponse = target.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(theater, MediaType.APPLICATION_JSON), HttpTheater.class);
		Assert.assertNotNull(createResponse);
		Assert.assertNotNull(createResponse.getTheaterId());
		Assert.assertEquals(createResponse.getTheaterName(), theater.getTheaterName());
		Assert.assertEquals(createResponse.getStreet(), theater.getStreet());
		Assert.assertEquals(createResponse.getCity(), theater.getCity());
		Assert.assertEquals(createResponse.getState(), theater.getState());
		Assert.assertEquals(createResponse.getZipcode(), theater.getZipcode());

		// search for just created movie
		List<HttpTheater> search = target.queryParam("zipcode", createResponse.getZipcode()).request().accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<HttpTheater>>() {
				});
		Assert.assertNotNull(search);
		HttpTheater searchTheater = search.get(0);
		Assert.assertEquals(searchTheater, createResponse);

		// update movie  name
		final String theaterId = Long.toString(searchTheater.getTheaterId());
		final String theaterName = "cityGold" + new Random().nextInt(99999);
		searchTheater.setTheaterName(theaterName);
		Response updateResponse = target.path(theaterId).request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(searchTheater, MediaType.APPLICATION_JSON));
		Assert.assertNotNull(updateResponse);
		Assert.assertEquals(updateResponse.getStatus(), 204);

		// get movie by id
		// search for just created user
		HttpTheater getResponse = target.path(theaterId).request().accept(MediaType.APPLICATION_JSON).get(HttpTheater.class);
		Assert.assertNotNull(getResponse);
		
		Assert.assertNotNull(getResponse.getTheaterId());
//		Assert.assertEquals(getResponse.getTheaterName(),createResponse.getTheaterName());
		Assert.assertEquals(getResponse.getTheaterId(), createResponse.getTheaterId());
		Assert.assertEquals(getResponse.getStreet(), createResponse.getStreet());
		Assert.assertEquals(getResponse.getCity(), createResponse.getCity());
		Assert.assertEquals(getResponse.getState(), createResponse.getState());
		Assert.assertEquals(getResponse.getState(), createResponse.getState());
		

		// delete movie
		Response deleteResponse = target.path(theaterId).request().accept(MediaType.APPLICATION_JSON).delete();
		Assert.assertNotNull(deleteResponse);
		Assert.assertNotEquals(deleteResponse.getStatus(), 200);

		// get movie
		Response response = target.path(theaterId).request().accept(MediaType.APPLICATION_JSON).get();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "Theater not found");

	}
	@Test
	public void testGetUserNotExist() {
		String theaterId = Integer.toString(new Random().nextInt(99999));
		Response response = target.path(theaterId).request().accept(MediaType.APPLICATION_JSON).get();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "Theater not found");

	}

	@Test
	public void testDeleteMovieNotExist() {
		String theaterId = Integer.toString(new Random().nextInt(99999));
		Response response = target.path(theaterId).request().accept(MediaType.APPLICATION_JSON).delete();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "Theater is not Exist");
	}

	@Test
	public void testUpdateMovieNotExist() {
		String theaterId = Integer.toString(new Random().nextInt(99999));
		Response response = target.path(theaterId).request().accept(MediaType.APPLICATION_JSON).get();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "Theater not found");
	}
	
}

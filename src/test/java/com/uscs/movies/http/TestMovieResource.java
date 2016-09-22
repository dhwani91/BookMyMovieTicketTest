package com.uscs.movies.http;

import java.util.List;
import java.util.Random;
import java.util.UUID;

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
	private static final String URI_PATH = "/MovieService/rest/movies";

	private Client client = ClientBuilder.newClient();
	private WebTarget target;

	@Before
	public void init() {
		target = client.target(HTTP_HOST).path(URI_PATH);
	}

	@Test
	public void testUserCRUD() {

		// create movie
		HttpMovie movie = new HttpMovie();
		movie.setMovieName("supeman" + new Random().nextInt(99999));
		movie.setMovieType("drama"+  new Random().nextInt(99999));
		movie.setMovieDesc("It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged");

		HttpMovie createResponse = target.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(movie, MediaType.APPLICATION_JSON), HttpMovie.class);
		Assert.assertNotNull(createResponse);
		Assert.assertNotNull(createResponse.getMovieId());
		Assert.assertEquals(createResponse.getMovieName(), movie.getMovieName());
		Assert.assertEquals(createResponse.getMovieDesc(), movie.getMovieDesc());
		Assert.assertEquals(createResponse.getMovieType(), movie.getMovieType());
		

		// search for just created movie
		List<HttpMovie> search = target.queryParam("movieType", createResponse.getMovieType()).request().accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<HttpMovie>>() {
				});
		Assert.assertNotNull(search);
		HttpMovie searchMovie = search.get(0);
		Assert.assertEquals(searchMovie, createResponse);

		// update movie  name
		final String movieId = Integer.toString(searchMovie.getMovieId());
		final String movieName = "superman" + new Random().nextInt(99999);
		searchMovie.setMovieName(movieName);
		Response updateResponse = target.path(movieId).request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(searchMovie, MediaType.APPLICATION_JSON));
		Assert.assertNotNull(updateResponse);
		Assert.assertEquals(updateResponse.getStatus(), 204);

		// get movie by id
		// search for just created user
		HttpMovie getResponse = target.path(movieId).request().accept(MediaType.APPLICATION_JSON).get(HttpMovie.class);
		Assert.assertNotNull(getResponse);
		
		Assert.assertNotNull(getResponse.getMovieId());
		Assert.assertEquals(getResponse.getMovieId(), createResponse.getMovieId());
		Assert.assertEquals(getResponse.getMovieType(), createResponse.getMovieType());
		Assert.assertEquals(getResponse.getMovieDesc(), createResponse.getMovieDesc());
		Assert.assertNotEquals(getResponse.getMovieName(),createResponse.getMovieName());
		

		// delete movie
		Response deleteResponse = target.path(movieId).request().accept(MediaType.APPLICATION_JSON).delete();
		Assert.assertNotNull(deleteResponse);
		Assert.assertNotEquals(deleteResponse.getStatus(), 200);

		// get movie
		Response response = target.path(movieId).request().accept(MediaType.APPLICATION_JSON).get();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "Movie not found");

	}
	@Test
	public void testGetUserNotExist() {
		String movieId = Integer.toString(new Random().nextInt(99999));
		Response response = target.path(movieId).request().accept(MediaType.APPLICATION_JSON).get();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "Movie not found");

	}

	@Test
	public void testDeleteMovieNotExist() {
		String movieId = Integer.toString(new Random().nextInt(99999));
		Response response = target.path(movieId).request().accept(MediaType.APPLICATION_JSON).delete();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "Movie is not Exist");
	}

	@Test
	public void testUpdateMovieNotExist() {
		String movieId = Integer.toString(new Random().nextInt(99999));
		Response response = target.path(movieId).request().accept(MediaType.APPLICATION_JSON).get();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "Movie not found");
	}
	

}

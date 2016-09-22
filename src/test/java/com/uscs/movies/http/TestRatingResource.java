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

public class TestRatingResource {
	private static final String HTTP_HOST = "http://localhost:8080";
	private static final String URI_PATH = "/MovieService/rest";

	private Client client = ClientBuilder.newClient();
	private WebTarget target;

	@Before
	public void init() {
		target = client.target(HTTP_HOST).path(URI_PATH);
	}

	@Test
	public void testRatingCRUD() {
		HttpUser user = new HttpUser();
		user.setFirstName("foo" + new Random().nextInt(99999));
		user.setLastName("bar" + new Random().nextInt(99999));
		user.setEmail(new Random().nextInt(99999) + "@uscs.edu");
		user.setPassword(UUID.randomUUID().toString());

		HttpUser createUserResponse = target.path("users").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON), HttpUser.class);
		//create movie
		HttpMovie movie = new HttpMovie();
		movie.setMovieName("supeman" + new Random().nextInt(99999));
		movie.setMovieType("drama"+  new Random().nextInt(99999));
		movie.setMovieDesc("It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged");

		HttpMovie createMovieResponse = target.path("movies").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(movie, MediaType.APPLICATION_JSON), HttpMovie.class);

		// create rating
		HttpRating rating = new HttpRating();
		rating.setMovieId(createMovieResponse.getMovieId());
		rating.setUserId(createUserResponse.getUserId());
		rating.setRating(5);

		HttpRating createRatingResponse = target.path("ratings").request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(rating, MediaType.APPLICATION_JSON), HttpRating.class);
		Assert.assertNotNull(createRatingResponse);
		Assert.assertNotNull(createRatingResponse.getRating());
		Assert.assertNotNull(createRatingResponse.getMovieId());
		Assert.assertNotNull(createRatingResponse.getUserId());
		Assert.assertEquals(createRatingResponse.getMovieId(), rating.getMovieId());
		Assert.assertEquals(createRatingResponse.getUserId(), rating.getUserId());
		Assert.assertEquals(createRatingResponse.getRating(), rating.getRating());

		// search review by movieId and userId
		List<HttpRating> search = target.path("ratings").queryParam("userId", createRatingResponse.getUserId())
				.queryParam("movieId",createRatingResponse.getMovieId()).request().accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<HttpRating>>() {
				});
		Assert.assertNotNull(search);
		HttpRating searchRating= search.get(0);
		Assert.assertEquals(searchRating.getRatingId(), createRatingResponse.getRatingId());

		// update user review 
		final String ratingId = Long.toString(searchRating.getRatingId());
		searchRating.setRating(5);
		Response updateResponse = target.path("ratings").path(ratingId).request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(searchRating, MediaType.APPLICATION_JSON));
		Assert.assertNotNull(updateResponse);
		Assert.assertEquals(updateResponse.getStatus(), 204);

		// get review by id
		// search for just created rating
		HttpRating getResponse = target.path("ratings").path(ratingId).request().accept(MediaType.APPLICATION_JSON).get(HttpRating.class);
		Assert.assertNotNull(getResponse);
		Assert.assertNotNull(getResponse.getRating());
		Assert.assertEquals(getResponse.getRatingId(), createRatingResponse.getRatingId());
		Assert.assertEquals(getResponse.getRating(), createRatingResponse.getRating());
		Assert.assertEquals(getResponse.getMovieId(), createRatingResponse.getMovieId());
		Assert.assertEquals(getResponse.getUserId(), createRatingResponse.getUserId());
		Assert.assertNotNull(getResponse.getUserId());

		// delete rating
		Response deleteResponse = target.path("ratings").path(ratingId).request().accept(MediaType.APPLICATION_JSON).delete();
		Assert.assertNotNull(deleteResponse);
		Assert.assertNotEquals(deleteResponse.getStatus(), 200);
//
//		// get review
//		Response response = target.path("rating").path(ratingId).request().accept(MediaType.APPLICATION_JSON).get();
//		HttpError error = response.readEntity(HttpError.class);
//		Assert.assertNotNull(error);
//		Assert.assertNotEquals(error.getStatus(), 200);
//		Assert.assertNotEquals(error.getCode(), "MISSING_DATA");
//		Assert.assertNotEquals(error.getMessage(), "Rating not found");

	}

	@Test
	public void testGetRatingNotExist() {
		String ratingId =Long.toString(new Random().nextInt(99999));
		Response response = target.path("ratings").path(ratingId).request().accept(MediaType.APPLICATION_JSON).get();
//		HttpError error = response.readEntity(HttpError.class);
//		Assert.assertNotNull(error);
//		Assert.assertEquals(error.getStatus(), 409);
//		Assert.assertEquals(error.getCode(), "MISSING_DATA");
//		Assert.assertEquals(error.getMessage(), "Rating is not Exist");
		Assert.assertNotNull(response.getStatus());
		Assert.assertEquals(response.getStatus(),500);
	}

	@Test
	public void testDeleteRatingNotExist() {
		String ratingId = Integer.toString(new Random().nextInt(99999));
		Response response = target.path("ratings").path(ratingId).request().accept(MediaType.APPLICATION_JSON).delete();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "Rating is not Exist");
	}

	@Test
	public void testUpdateRatingNotExist() {
		String ratingId = Integer.toString(new Random().nextInt(99999));
		Response response = target.path("ratings").path(ratingId).request().accept(MediaType.APPLICATION_JSON).get();
//		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(response.getStatus());
		Assert.assertEquals(response.getStatus(), 500);
//		Assert.assertEquals(error.getCode(), "MISSING_DATA");
//		Assert.assertEquals(error.getMessage(), "Rating not found");
	}
	
	
}

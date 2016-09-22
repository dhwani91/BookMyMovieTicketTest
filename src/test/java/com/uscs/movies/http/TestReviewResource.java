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

public class TestReviewResource {
	private static final String HTTP_HOST = "http://localhost:8080";
	private static final String URI_PATH = "/MovieService/rest";

	private Client client = ClientBuilder.newClient();
	private WebTarget target;

	@Before
	public void init() {
		target = client.target(HTTP_HOST).path(URI_PATH);
	}

	@Test
	public void testReviewCRUD() {
		// craete user
		HttpUser user = new HttpUser();
		user.setFirstName("foo" + new Random().nextInt(99999));
		user.setLastName("bar" + new Random().nextInt(99999));
		user.setEmail(new Random().nextInt(99999) + "@uscs.edu");
		user.setPassword(UUID.randomUUID().toString());

		HttpUser createUserResponse = target.path("users").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON), HttpUser.class);

		// create movie
		HttpMovie movie = new HttpMovie();
		movie.setMovieName("supeman" + new Random().nextInt(99999));
		movie.setMovieType("drama" + new Random().nextInt(99999));
		movie.setMovieDesc(
				"It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged");

		HttpMovie createMovieResponse = target.path("movies").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(movie, MediaType.APPLICATION_JSON), HttpMovie.class);

		// create Review
		HttpReview review = new HttpReview();
		review.setMovieId(createMovieResponse.getMovieId());
		review.setUserId(createUserResponse.getUserId());
		review.setReviews("comedy and one time watch movie");
		HttpReview createReviewResponse = target.path("reviews").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(review, MediaType.APPLICATION_JSON), HttpReview.class);
		Assert.assertNotNull(createReviewResponse);
		Assert.assertNotNull(createReviewResponse.getReviewId());
		Assert.assertNotNull(createReviewResponse.getMovieId());
		Assert.assertNotNull(createReviewResponse.getUserId());
		Assert.assertEquals(createReviewResponse.getMovieId(), review.getMovieId());
		Assert.assertEquals(createReviewResponse.getUserId(), review.getUserId());
		Assert.assertEquals(createReviewResponse.getReviews(), review.getReviews());

		// search review by movieId and userId
		List<HttpReview> search = target.path("reviews").queryParam("userId", createReviewResponse.getUserId())
				.queryParam("movieId", createReviewResponse.getMovieId()).request().accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<HttpReview>>() {
				});
		Assert.assertNotNull(search);
		HttpReview searchReview = search.get(0);
		//Assert.assertEquals(searchReview, createReviewResponse);

		// update user review
		final String reviewId = Long.toString(searchReview.getReviewId());
		searchReview.setReviews("pathetic");
		Response updateResponse = target.path("reviews").path(reviewId).request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(searchReview, MediaType.APPLICATION_JSON));
		Assert.assertNotNull(updateResponse);
		Assert.assertEquals(updateResponse.getStatus(), 204);

		// get review by id
		// search for just created review
		HttpReview getResponse = target.path("reviews").path(reviewId).request().accept(MediaType.APPLICATION_JSON)
				.get(HttpReview.class);
		Assert.assertNotNull(getResponse);
		Assert.assertNotNull(getResponse.getReviewId());
		Assert.assertEquals(getResponse.getReviewId(), createReviewResponse.getReviewId());
		Assert.assertNotEquals(getResponse.getReviews(), createReviewResponse.getReviews());
		Assert.assertEquals(getResponse.getMovieId(), createReviewResponse.getMovieId());
		Assert.assertEquals(getResponse.getUserId(), createReviewResponse.getUserId());

		// delete review
		Response deleteResponse = target.path("reviews").path(reviewId).request().accept(MediaType.APPLICATION_JSON).delete();
		Assert.assertNotNull(deleteResponse);
		Assert.assertNotEquals(deleteResponse.getStatus(), 200);

		// get review
		Response response = target.path("reviews").path(reviewId).request().accept(MediaType.APPLICATION_JSON).get();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "Review not found");

	}

	@Test
	public void testGetReviewNotExist() {
		String reviewId = Integer.toString(new Random().nextInt(99999));
		Response response = target.path("reviews").path(reviewId).request().accept(MediaType.APPLICATION_JSON).get();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "Review not found");

	}

	@Test
	public void testDeleteReviewNotExist() {
		String reviewId = Integer.toString(new Random().nextInt(99999));
		Response response = target.path("reviews").path(reviewId).request().accept(MediaType.APPLICATION_JSON).delete();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "Review is not Exist");
	}

	@Test
	public void testUpdateReviewNotExist() {
		String reviewId = Long.toString(new Random().nextInt(99999));
		Response response = target.path("reviews").path(reviewId).request().accept(MediaType.APPLICATION_JSON).get();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "Review not found");
	}

}

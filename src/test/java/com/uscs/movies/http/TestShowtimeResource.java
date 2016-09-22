package com.uscs.movies.http;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestShowtimeResource {
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
		// create movie
		HttpMovie movie = new HttpMovie();
		movie.setMovieName("supeman" + new Random().nextInt(99999));
		movie.setMovieType("drama" + new Random().nextInt(99999));
		movie.setMovieDesc(
				"It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged");

		HttpMovie createMovieResponse = target.path("movies").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(movie, MediaType.APPLICATION_JSON), HttpMovie.class);

		// create Theater
		HttpTheater theater = new HttpTheater();
		theater.setTheaterName("citygold" + new Random().nextInt(99999));
		theater.setStreet(new Random().nextInt(99999) + "blvd");
		theater.setCity("sanjose" + new Random().nextInt(99999));
		theater.setState("california");
		theater.setZipcode(new Random().nextInt(99999));
		HttpTheater createTheaterResponse = target.path("theaters").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(theater, MediaType.APPLICATION_JSON), HttpTheater.class);

		// create showtime
		HttpShowtime showtime = new HttpShowtime();
		showtime.setMovieId(createMovieResponse.getMovieId());
		showtime.setTheaterId(createTheaterResponse.getTheaterId());
		showtime.setShowTime(new Date());
		HttpShowtime createShowtimeResponse = target.path("showtime").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(showtime, MediaType.APPLICATION_JSON), HttpShowtime.class);
		Assert.assertNotNull(createShowtimeResponse);
		Assert.assertNotNull(createShowtimeResponse.getShowtimeId());
		Assert.assertNotNull(createShowtimeResponse.getMovieId());
		Assert.assertNotNull(createShowtimeResponse.getTheaterId());
		Assert.assertEquals(createShowtimeResponse.getMovieId(), showtime.getMovieId());
		Assert.assertEquals(createShowtimeResponse.getTheaterId(), showtime.getTheaterId());
		Assert.assertEquals(createShowtimeResponse.getShowTime(), showtime.getShowTime());
		//search showtime
		List<HttpShowtime> search = target.path("showtime").queryParam("movieId",1).request().accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<HttpShowtime>>() {
				});
		Assert.assertNotNull(search);
		System.out.println(search);
		List<HttpShowtime> searchShowtimeByTheater = target.path("showtime").queryParam("theaterId",2).request().accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<HttpShowtime>>() {
				});
		Assert.assertNotNull(searchShowtimeByTheater);
		System.out.println(search);

	}

}

package com.uscs.movies.http;

public class HttpRating {
	private int ratingId;
	private int rating;
	private int movieId;
	private int userId;


	public void setRatingId(int ratingId) {
		this.ratingId = ratingId;
	}

	public int getRatingId() {
		return ratingId;
	}

	public void setRating(int rating) {
		this.rating= rating;
	}

	public int getRating() {
		return rating;

	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public int getMovieId() {
		return movieId;
	}

}

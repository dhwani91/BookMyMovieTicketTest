package com.uscs.movies.http;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "review")
public class HttpReview {

	private int reviewId;
	private String reviews;
	private int movieId;
	private int userId;

	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public String getReviews() {
		return reviews;
	}

	public void setReviews(String reviews) {
		this.reviews = reviews;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}

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

public class TestUserResource {
	private static final String HTTP_HOST = "http://localhost:8080";
	private static final String URI_PATH = "/MovieService/rest/users";

	private Client client = ClientBuilder.newClient();
	private WebTarget target;

	@Before
	public void init() {
		target = client.target(HTTP_HOST).path(URI_PATH);
	}

	@Test
	public void testUserCRUD() {

		// create user
		HttpUser user = new HttpUser();
		user.setFirstName("foo" + new Random().nextInt(99999));
		user.setLastName("bar" + new Random().nextInt(99999));
		user.setEmail(new Random().nextInt(99999) + "@uscs.edu");
		user.setPassword(UUID.randomUUID().toString());

		HttpUser createResponse = target.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON), HttpUser.class);
		Assert.assertNotNull(createResponse);
		Assert.assertNotNull(createResponse.getUserId());
		Assert.assertEquals(createResponse.getFirstName(), user.getFirstName());
		Assert.assertEquals(createResponse.getLastName(), user.getLastName());
		Assert.assertEquals(createResponse.getEmail(), user.getEmail());

		// search for just created user
		List<HttpUser> search = target.queryParam("firstName", createResponse.getFirstName())
				.queryParam("lastName", createResponse.getLastName()).request().accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<HttpUser>>() {
				});
		Assert.assertNotNull(search);
		HttpUser searchUser = search.get(0);
		Assert.assertEquals(searchUser, createResponse);

		// update user first name
		final String userId = Long.toString(searchUser.getUserId());
		final String firstName = "fooo" + new Random().nextInt(99999);
		searchUser.setFirstName(firstName);
		Response updateResponse = target.path(userId).request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(searchUser, MediaType.APPLICATION_JSON));
		Assert.assertNotNull(updateResponse);
		Assert.assertEquals(updateResponse.getStatus(), 204);

		// get user by id
		// search for just created user
		HttpUser getResponse = target.path(userId).request().accept(MediaType.APPLICATION_JSON).get(HttpUser.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(getResponse.getFirstName(), firstName);
		Assert.assertNotNull(getResponse.getUserId());
		Assert.assertEquals(getResponse.getUserId(), createResponse.getUserId());
		Assert.assertEquals(getResponse.getLastName(), createResponse.getLastName());
		Assert.assertEquals(getResponse.getEmail(), createResponse.getEmail());

		// delete user
		Response deleteResponse = target.path(userId).request().accept(MediaType.APPLICATION_JSON).delete();
		Assert.assertNotNull(deleteResponse);
		Assert.assertNotEquals(deleteResponse.getStatus(), 200);

		// get user
		Response response = target.path(userId).request().accept(MediaType.APPLICATION_JSON).get();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "User not found");

	}

	@Test
	public void testGetUserNotExist() {
		String userId = Integer.toString(new Random().nextInt(99999));
		Response response = target.path(userId).request().accept(MediaType.APPLICATION_JSON).get();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "User not found");

	}

	@Test
	public void testDeleteUserNotExist() {
		String userId = Integer.toString(new Random().nextInt(99999));
		Response response = target.path(userId).request().accept(MediaType.APPLICATION_JSON).delete();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "User is not Exist");
	}

	@Test
	public void testUpdateUserNotExist() {
		String userId = Long.toString(new Random().nextInt(99999));
		Response response = target.path(userId).request().accept(MediaType.APPLICATION_JSON).get();
		HttpError error = response.readEntity(HttpError.class);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), 409);
		Assert.assertEquals(error.getCode(), "MISSING_DATA");
		Assert.assertEquals(error.getMessage(), "User not found");
	}

	@Test
	public void testInvalidUserData() {
		HttpUser user = new HttpUser();
		user.setLastName("bar" + new Random().nextInt(99999));
		user.setEmail(new Random().nextInt(99999) + "@uscs.edu");
		user.setPassword(UUID.randomUUID().toString());
		// HttpUser response =
		// target.request().accept(MediaType.APPLICATION_JSON)
		// .post(Entity.entity(user, MediaType.APPLICATION_JSON),
		// HttpUser.class);
		// response.
		// HttpError error = response.readEntity(HttpError.class);
		// Assert.assertNotNull(error);
		// Assert.assertEquals(error.getStatus(), 409);
		// Assert.assertEquals(error.getCode(), "INVALID_DATA");
		// Assert.assertEquals(error.getMessage(), "User not found");
	}

}

/**
 * 
 */
package com.trainedge.services;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.trainedge.helpers.ApplicationConstants;
import com.trainedge.helpers.AuthHelper;

/**
 * @author adhiraima
 *
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/auth")
public class AuthService {
	
	/*
	 * The method authenticates against a given password with the supplied shopperId.
	 * Will return status 200 OK if the password is non-null and matches up in the DB.
	 * Will return status 200 OK and an errorMessage if the password does not match 
	 * the DB value.
	 * Will return status 400 BAD Request if the password is empty. 
	 * 
	 */
	
	@POST
	@Path("/shopper/{shopperId}")
	public Response authenticateShopper(@PathParam("shopperId") String shopperId, 
			@FormParam("password") String password) throws JSONException {
		JSONObject result = new JSONObject();
		if (null != password && password.length() > 0) {
			if (AuthHelper.authenticateShopper(shopperId, password)) {
				result.put(ApplicationConstants.SUCCESS_MESSAGE, 
						"User authenticated successfully!!");
				return Response.ok(result).build();
			} else {
				result.put(ApplicationConstants.ERROR_MESSAGE, 
						"Username and password do not match!!");
				return Response.ok(result).build();
			}
		} else {
			result.put(ApplicationConstants.ERROR_MESSAGE, 
					"Password cannot be empty!!");
			return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
		}
	}
	
	/*
	 * The method authenticates against a given key with the supplied retailerId.
	 * Will return status 200 OK if the key is non-null and matches up in the DB.
	 * Will return status 200 OK and an errorMessage if the key does not match 
	 * the DB value.
	 * Will return status 400 BAD Request if the key is empty. 
	 * 
	 */
	
	@POST
	@Path("/retailer/{retailerId}")
	public Response authenticateRetailer(@PathParam("retailerId") String retailerId, 
			@FormParam("key") String key) throws JSONException {
		JSONObject result = new JSONObject();
		if (null != key && key.length() > 0) {
			if (AuthHelper.authenticateRetailer(retailerId, key)) {
				result.put(ApplicationConstants.SUCCESS_MESSAGE, 
						"The retailer is authenticated eith the given key!!");
				return Response.ok(result).build();
			} else {
				result.put(ApplicationConstants.ERROR_MESSAGE, 
						"id and key do not match!!");
				return Response.ok(result).build();
			}
		} else {
			result.put(ApplicationConstants.ERROR_MESSAGE, "key cannot be empty!!");
			return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
		}
	}
	
	/* 
	 * The method authenticates against a cookie value with the supplied retailerId.
	 * Will return status 200 OK if the key extracted from the cookie is non-null and 
	 * matches up in the DB.
	 * Will return status 200 OK and an errorMessage if the password does not match 
	 * the DB value.
	 * Will return status 400 BAD Request if the key retrieved from cookie is empty,
	 * or the cookie could not be found in the request. 
	 * 
	 */
	
	@POST
	@Path("/retailer/cookie/{retailerId}")
	public Response authenticateRetailerByCookie(@PathParam("retailerId") 
		String retailerId, @Context HttpServletRequest request) throws JSONException {
		JSONObject result = new JSONObject();
		Cookie authCookie = null;
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equalsIgnoreCase(ApplicationConstants.AUTH_COOKIE)) {
				authCookie = cookie;
				break;
			}
		}
		if (null != authCookie.getValue() && authCookie.getValue().length() > 0) {
			if (AuthHelper.authenticateRetailer(retailerId, authCookie.getValue())) {
				result.put(ApplicationConstants.SUCCESS_MESSAGE, 
						"The retailer is authenticated eith the given key!!");
				return Response.ok(result).build();
			} else {
				result.put(ApplicationConstants.ERROR_MESSAGE, 
						"id and key do not match!!");
				return Response.ok(result).build();
			}
		} else {
			result.put(ApplicationConstants.ERROR_MESSAGE, 
					"key could not be retreived form cookie!!");
			return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
		}
	}
}

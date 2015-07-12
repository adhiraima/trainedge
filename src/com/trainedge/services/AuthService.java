/**
 * 
 */
package com.trainedge.services;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.omg.PortableInterceptor.SUCCESSFUL;

import com.reconext.b2b.exception.B2BDAOException;
import com.trainedge.helpers.ApplicationConstants;
import com.trainedge.helpers.AuthHelper;
import com.trainedge.helpers.ShopperHelper;

/**
 * @author adhiraima
 *
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/auth")
public class AuthService {
	
	/**
	 * 
	 * @param shopperId
	 * @param retailorId
	 * @param shoppertoken
	 * @param retailortoken
	 * @return
	 * @throws JSONException
	 * @throws B2BDAOException
	 * 
	 * The method authenticates with the supplied shopperId and retailerId.
	 * Will return status 200 OK if the retailer and the token are non null and matches  
	 * up in the DB and then the shopper id and the token are non null and mathces up 
	 * in the DB
	 * Will return status 200 OK and an errorMessage if the token does not match 
	 * the DB value.
	 * Will return status 400 BAD Request if any of the params is empty or null. 
	 */
	@GET
	@Path("/shopper/{retailerId}/{shopperId}")
	public Response authenticateShopper(@PathParam("shopperId") String shopperId, 
			@PathParam("retailerId") String retailerId, 
	@HeaderParam(ApplicationConstants.AUTH_HEADER) String shoppertoken,
	@HeaderParam(ApplicationConstants.RETAILER_HEADER) String retailertoken
			) throws JSONException, B2BDAOException {
		JSONObject result = new JSONObject();
		if (AuthHelper.verifyRetailorToken(retailerId, retailertoken)) {
			if (AuthHelper.verifyShopperToken(shoppertoken, shopperId)) {
				//return the shopper as response
				result.put(ApplicationConstants.SUCCESS_MESSAGE, "ShopperFound!");
				result.put("shopper", ShopperHelper.getShopper(shopperId));
				return Response.ok().entity(result).build();
			} else {
				result.put(ApplicationConstants.ERROR_MESSAGE, "Shopper not verified");
				return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
			}
		} else {
			result.put(ApplicationConstants.ERROR_MESSAGE, "Retailor not verified");
			return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
		}
	}
	
	/**
	 * 
	 * @param retailerId
	 * @param retailertoken
	 * @return
	 * @throws JSONException
	 * @throws B2BDAOException
	 * 
	 * The method authenticates the retailer id supplied with the header field 
	 * X-CSRF-Retail-Token value. if a match is found return success 200 status
	 * else will return a bad request 400
	 */
	
	@GET
	@Path("/retailer/{retailerId}")
	public Response authenticateRetailer(@PathParam("retailerId") String retailerId,
			@HeaderParam(ApplicationConstants.RETAILER_HEADER) String retailertoken) 
					throws JSONException, B2BDAOException {
		JSONObject result = new JSONObject();
		if (AuthHelper.verifyRetailorToken(retailerId, retailertoken)) {
			result.put(ApplicationConstants.SUCCESS_MESSAGE, "Retailer verified!");
			return Response.ok().entity(result).build();
		} else {
			result.put(ApplicationConstants.ERROR_MESSAGE, "Shopper not verified!");
			return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
		}
	}
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @throws B2BDAOException
	 * @throws JSONException
	 * 
	 * This method verifies an internal user and checks for username and password, if 
	 * authentication passes returns the DB user
	 */
	
	@POST
	@Path("/reconnext")
	public Response authenticateInternal(@FormParam("username") String userName, 
			@FormParam("password") String password) 
					throws B2BDAOException, JSONException {
		JSONObject result = new JSONObject();
		if (AuthHelper.authenticateInternal(userName, password)) {
			result.put(ApplicationConstants.SUCCESS_MESSAGE, "user verified!");
			result.put("shopper", ShopperHelper.getShopperByUserName(userName));
			return Response.ok().entity(result).build();
		} else {
			result.put(ApplicationConstants.ERROR_MESSAGE, "user not verified");
			return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
		}
	}
	
}

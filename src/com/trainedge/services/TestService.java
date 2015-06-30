/**
 * 
 */
package com.trainedge.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author adhiraima
 *
 */
@Path("/test")
public class TestService {

	@Path("/{param}")
	@GET
	public Response testService(@PathParam("param") String param) {
		System.out.println("coming here!!!");
		String op = "Hello !! "+param;
		return Response.status(200).entity(op).build();
	}
}

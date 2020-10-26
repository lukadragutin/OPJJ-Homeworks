package hr.fer.zemris.galerija.rest;

import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

import com.google.gson.Gson;

import hr.fer.zemris.galerija.model.GalleryLoader;
import hr.fer.zemris.galerija.model.Picture;


/**
 * Class used as a REST API that uses
 * some of its methods as make-do servlets that
 * generate json type of responses to its requests
 * that are being mapped over '/rest/*'. <br>
 * The methods are mainly used to get information 
 * about the image gallery and its images. 
 * @author LukaD
 *
 */
@Path("/pictures")
public class PictureJSON {

	/**
	 * Servlet context
	 */
	@Context
	private ServletContext context;
	
	/**
	 * Relative path to the gallery descriptor
	 */
	private String opisnikPath = "/WEB-INF/opisnik.txt";
	
	
	/**
	 * Loads all the tags and forwards the results
	 * to the webapp frontend in json format
	 * @return Response in json format
	 */
	@GET
	@Produces("application/json")
	public Response getTagsList() {		
		if(!GalleryLoader.isDecriptorSet()) {
			GalleryLoader.setDescriptor(context.getRealPath(opisnikPath));
		}
		
		Set<String> tags = GalleryLoader.getTags();
		Gson result = new Gson();
		
		String[] array = new String[tags.size()];
		String jSon = result.toJson(tags.toArray(array));
		
		return Response.status(Status.OK).entity(jSon).build();
	}
	
	
	/**
	 * Loads all the pictures with the wanted tag 
	 * and forwards the results to the webapp frontend in json format
	 * @return Response in json format
	 */
	@Path("{tag}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPicturesWithTag(@PathParam("tag") String tag) {
		if(!GalleryLoader.isDecriptorSet()) {
			GalleryLoader.setDescriptor(context.getRealPath(opisnikPath));
		}
		
		List<String> pics = GalleryLoader.getPicturesWithTag(tag);
		Gson result = new Gson();
		
		String[] array = new String[pics.size()];
		String jSon = result.toJson(pics.toArray(array));
		
		return Response.status(Status.OK).entity(jSon).build();
	}
	
	
	/**
	 * Loads all the image information about the wanted image
	 * and forwards the results
	 * to the webapp frontend in json format
	 * @return Response in json format
	 */
	@Path("/full/{name}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPictureInfo(@PathParam("name") String name) {
		if(!GalleryLoader.isDecriptorSet()) {
			GalleryLoader.setDescriptor(context.getRealPath(opisnikPath));
		}
		
		Picture pic = GalleryLoader.getPictureInfo(name);
		JSONObject result = new JSONObject();
		
		result.put("path", pic.getName());
		result.put("desc", pic.getDesc());
		result.put("tags", pic.getTag());
	
		
		return Response.status(Status.OK).entity(result.toString()).build();
	}	
}

package com.time.bomb.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.time.bomb.consts.ApplicationConstants;
import com.time.bomb.dto.Album;
import com.time.bomb.dto.Comment;
import com.time.bomb.dto.Photo;
import com.time.bomb.dto.Post;
import com.time.bomb.dto.Todo;
import com.time.bomb.dto.User;

public class FetchResouceCommandManager {
	
	
	public static final Map<String, FetchResouceCommand<Object>> RESOURCES;
	private static final RestTemplate restTemplate;
	private static final ObjectMapper objectMapper;
	
	
	static{
	    
		restTemplate = new RestTemplate();
		objectMapper = new ObjectMapper();
		
		final Map<String, FetchResouceCommand<Object>> resources = new HashMap<>();
	    
	    resources.put(ApplicationConstants.POSTS, (urn)-> {
	    	return getDataFromExternalAPI(urn, new TypeReference<Set<Post>>(){});
	    });
	    
	    resources.put(ApplicationConstants.COMMENTS, (urn)-> {
	    	return getDataFromExternalAPI(urn, new TypeReference<Set<Comment>>(){});
	    });
	    
	    resources.put(ApplicationConstants.ALBUMS, urn->{
	    	return getDataFromExternalAPI(urn, new TypeReference<Set<Album>>(){});
	    });
	    
	    resources.put(ApplicationConstants.PHOTOS, urn->{
	    	return getDataFromExternalAPI(urn, new TypeReference<Set<Photo>>(){});
	    });
	    
	    resources.put(ApplicationConstants.TODOS, urn->{
	    	return getDataFromExternalAPI(urn, new TypeReference<Set<Todo>>(){});
	    });
	    
	    resources.put(ApplicationConstants.USERS, urn->{
	    	return getDataFromExternalAPI(urn, new TypeReference<Set<User>>(){});
	    });
	    
	    
	    RESOURCES = Collections.unmodifiableMap(resources);
	}
	
	private static <T> Object getDataFromExternalAPI(String URN, TypeReference<T> typeRef) {
		
		String URL = "https://jsonplaceholder.typicode.com/" + URN.toLowerCase();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		try {
			return objectMapper.readValue(restTemplate.exchange(URL, HttpMethod.GET, entity, String.class).getBody(), typeRef);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("ERROR in FetchResouceCommandManager : getDataFromExternalAPI");
	}
	
	
	
	
}

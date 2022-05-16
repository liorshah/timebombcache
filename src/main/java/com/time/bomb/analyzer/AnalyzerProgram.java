package com.time.bomb.analyzer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.time.bomb.cache.TimeBombCacheImpl;
import com.time.bomb.consts.ApplicationConstants;
import com.time.bomb.dto.Album;
import com.time.bomb.dto.Comment;
import com.time.bomb.dto.Photo;
import com.time.bomb.dto.Post;
import com.time.bomb.dto.PostCommentetsEmails;
import com.time.bomb.dto.Todo;
import com.time.bomb.dto.UserPostComments;
import com.time.bomb.dto.UserToDo;
import com.time.bomb.exception.AnalyzerResurceNotFouncException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unchecked")
public class AnalyzerProgram {
	
	public Collection<UserToDo> getAllUsersUncompletedTodos(){
		
		log.info("START AnalyzerProgram : getAllUsersTodos");
		
		Set<Todo> todos = ((Set<Todo>)TimeBombCacheImpl.getInstance().getData(ApplicationConstants.TODOS));
		
		if(isCollectionValid(todos)) {
			
			Map<Integer, UserToDo> todosMap = new HashMap<>();
			
			todos.stream()
				.filter(todo -> !todo.isCompleted())
				.forEach(todo -> {
					if(todosMap.containsKey(todo.getUserId())) {
						todosMap.get(todo.getUserId()).getTodos().add(todo);
					}else {
						UserToDo userToDo = UserToDo.builder()
							.userId(todo.getUserId())
							.todos(new HashSet<>(Arrays.asList(todo)))
							.build();
						todosMap.put(todo.getUserId(), userToDo);
					}
				});
			log.info("RESULT AnalyzerProgram : getAllUsersTodos : {}", todosMap.values().toString());
			log.info("END AnalyzerProgram : getAllUsersTodos");
			return todosMap.values();
		}
		
		throw new AnalyzerResurceNotFouncException("ERROR in AnalyzerProgram : getAllUsersTodos");
	}
	
	public Collection<Todo> getUserToDoByUserId(Integer userId) {
		
		log.info("START AnalyzerProgram : getUserToDoByUserId");
		
		Set<Todo> todos = ((Set<Todo>)TimeBombCacheImpl.getInstance().getData(ApplicationConstants.TODOS));
		
		if(isCollectionValid(todos)) {
			
			 Set<Todo> userTodo = todos
					.stream()
					.filter(todo -> !todo.isCompleted() && userId.equals(todo.getUserId()))
					.collect(Collectors.toSet());
			 
			log.info("RESULT AnalyzerProgram : getUserToDoByUserId : {}", userTodo.toString());
			log.info("END AnalyzerProgram : getUserToDoByUserId");
			return userTodo;
			
		}
		
		
		throw new AnalyzerResurceNotFouncException("ERROR in AnalyzerProgram : getUserToDoByUserId");
	}
	
	public Collection<UserPostComments> getUserPostCommentersEmail() {
		
		log.info("START AnalyzerProgram : getUserPostCommentersEmail");
		
		Set<Comment> comments = ((Set<Comment>)TimeBombCacheImpl.getInstance().getData(ApplicationConstants.COMMENTS));
		
		if(isCollectionValid(comments)) {
			
			Map<Integer, PostCommentetsEmails> postComments = new HashMap<>();
			Map<Integer, UserPostComments> userPostComments = new HashMap<>();
			
			// sort all comments by postId and post commenter mails 
			comments
				.stream()
				.forEach(comment->{
					if(postComments.containsKey(comment.getPostId())) {
						postComments.get(comment.getPostId()).getEmails().add(comment.getEmail());
					}else {
						postComments.put(comment.getPostId(), PostCommentetsEmails.builder()
								.postId(comment.getPostId())
								.emails(new HashSet<>(Arrays.asList(comment.getEmail())))
								.build());
					}
			});
			
			// add to user all post and commenter's
			postComments.entrySet().forEach(key->{
				
				Integer userId = getUserIdByPostId(key.getKey());
				
				if(userPostComments.containsKey(userId)) {
					userPostComments.get(userId).getPostCommentersEmail().add(postComments.get(key.getKey()));
				}else {
					userPostComments.put(userId, UserPostComments.builder()
							.userId(userId)
							.postCommentersEmail(new HashSet<>(Arrays.asList(postComments.get(key.getKey()))))
							.build());
				}
			});
			
			log.info("RESULT AnalyzerProgram : getUserPostCommentersEmail : {}", userPostComments.values().toString());
			log.info("END AnalyzerProgram : getUserPostCommentersEmail");
			return userPostComments.values();
		}
		
		throw new AnalyzerResurceNotFouncException("ERROR in AnalyzerProgram : getUserPostCommentersEmail");
	}
	
	
	public Collection<Integer> getUserAlbumsThatPhotosExceedThreshold(Integer userId, Integer threshold) {
		
		log.info("START AnalyzerProgram : getUserAlbumsThatPhotosExceedThreshold");
		
		Map<Integer, Set<Photo>> albumPhotos = new HashMap<>();
		
		Set<Album> albums = ((Set<Album>)TimeBombCacheImpl.getInstance().getData(ApplicationConstants.ALBUMS));
		
		if(isCollectionValid(albums)) {
			
			Set<Integer> userAlbumsId = albums.stream()
					.filter(album-> userId.equals(album.getUserId()))
					.map(album-> album.getId())
					.collect(Collectors.toSet());
				
				Set<Photo> photos = ((Set<Photo>)TimeBombCacheImpl.getInstance().getData(ApplicationConstants.PHOTOS));
				
				if(isCollectionValid(photos)) {
					
					photos.stream()
					.filter(photo-> userAlbumsId.contains(photo.getAlbumId()))
					.forEach(photo->{
						if(albumPhotos.containsKey(photo.getAlbumId())) {
							albumPhotos.get(photo.getAlbumId()).add(photo);
						}else {
							albumPhotos.put(photo.getAlbumId(), new HashSet<>(Arrays.asList(photo)));
						}
				});
				
					albumPhotos.entrySet().removeIf(map-> map.getValue().size() > threshold);
					
					log.info("RESULT AnalyzerProgram : getUserAlbumsThatPhotosExceedThreshold : {}", albumPhotos.keySet());
					log.info("END AnalyzerProgram : getUserAlbumsThatPhotosExceedThreshold");
					return albumPhotos.keySet();
				}
				
		}
		
		throw new AnalyzerResurceNotFouncException("ERROR in AnalyzerProgram : getUserAlbumsThatPhotosExceedThreshold");
		
			
	}

	
	private Integer getUserIdByPostId(Integer postId) {
		
		Set<Post> posts = ((Set<Post>)TimeBombCacheImpl.getInstance().getData(ApplicationConstants.POSTS));
		
		if(posts != null && posts.size() > 0) {
			
			return posts.stream()
					.filter(post-> postId.equals(post.getId()))
					.findFirst()
					.orElseThrow(()-> new RuntimeException("Error in AnalyzerProgram : getUserIdByPostId : User Not Found"))
					.getUserId();
		}
		
		throw new AnalyzerResurceNotFouncException("ERROR in AnalyzerProgram : getUserIdByPostId");
	}
	
	private <T> boolean isCollectionValid(Collection<T> collection) {
		return collection != null && !collection.isEmpty();
	}

	
}

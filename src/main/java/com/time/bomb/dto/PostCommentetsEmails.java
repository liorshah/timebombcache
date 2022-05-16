package com.time.bomb.dto;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class PostCommentetsEmails {
	
	private Integer postId;
	private Set<String> emails;
	
}

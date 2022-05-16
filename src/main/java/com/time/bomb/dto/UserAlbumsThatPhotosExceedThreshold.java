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
public class UserAlbumsThatPhotosExceedThreshold {
	
	private Integer userId;
	private Set<Integer> albumsId;
}

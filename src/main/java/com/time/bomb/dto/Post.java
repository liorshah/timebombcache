package com.time.bomb.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Post{
    private int userId;
    private int id;
    private String title;
    private String body;
}
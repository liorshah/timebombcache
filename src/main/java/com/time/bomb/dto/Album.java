package com.time.bomb.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Album{
    private Integer userId;
    private Integer id;
    private String title;
}
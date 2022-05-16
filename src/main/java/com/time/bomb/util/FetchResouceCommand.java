package com.time.bomb.util;

public interface FetchResouceCommand<T> {

   T fetch(String urn);
   
}
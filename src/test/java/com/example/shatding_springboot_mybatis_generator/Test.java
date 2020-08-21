package com.example.shatding_springboot_mybatis_generator;

import com.example.shatding_springboot_mybatis_generator.util.SnowFlake;

public class Test {
  public static void main(String[] args) {
	SnowFlake snowFlake = new SnowFlake(2, 3);
	for(int i = 0;i < 6;i++){
		long id = snowFlake.nextId();
		System.out.println(id);
		System.out.println( id / 2L % 2L);
	}
}
}

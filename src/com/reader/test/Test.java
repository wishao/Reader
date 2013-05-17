package com.reader.test;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int color = -5826022;
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = (color >> 0) & 0xFF;
		System.out.println(r);
	}

}
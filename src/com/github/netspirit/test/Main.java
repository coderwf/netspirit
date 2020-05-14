package com.github.netspirit.test;

public class Main {
    public static void main(String[] args) {
		long t1 = System.currentTimeMillis();
		
		for(int i=0;i<100000000;i++) {}
			Thread.currentThread().getId();
		
		long t2 = System.currentTimeMillis();
		System.out.println("cost " + (t2 - t1) + " ms");
	}
}

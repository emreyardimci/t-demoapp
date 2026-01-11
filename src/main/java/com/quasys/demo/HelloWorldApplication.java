package com.quasys.demo;

public class HelloWorldApplication {
    public static void main(String[] args) {
        while (true) {
            System.out.println("Hello World from THY!");
			System.out.println("APP_ENV = " + System.getenv("APP_ENV"));
            try {
                Thread.sleep(5_000); // 5 saniye
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
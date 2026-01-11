package com.quasys.demo;

public class HelloWorldApplication {
    public static void main(String[] args) {
        while (true) {
            System.out.println("Hello World from THY!");
			System.out.println("APP_DB_CRED = " + System.getenv("APP_DB_CRED"));
			System.out.println("APP_API_KEY = " + System.getenv("APP_API_KEY"));
            try {
                Thread.sleep(5_000); // 5 saniye
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
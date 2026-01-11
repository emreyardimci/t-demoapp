package com.quasys.demo;

public class HelloWorldApplication {
    public static void main(String[] args) {
                while (true) {
            System.out.println("Hello World from THY!");
            try {
                Thread.sleep(5_000); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

package com.quasys.demo;

public class HelloWorldApplication {
    public static void main(String[] args)  throws Exception {
        while (true) {

        System.out.println("Hello World from THY!");
        System.out.println("APP_POSTGRESQL_PASSWORD: " + System.getenv("APP_POSTGRESQL_PASSWORD"));
        System.out.println("APP_POSTGRESQL_USERNAME: " + System.getenv("APP_POSTGRESQL_USERNAME"));

        System.setProperty("APP_POSTGRESQL_PRIVATEKEY", "ThisIsCertPrivateKey");
        System.out.println("APP_POSTGRESQL_PRIVATEKEY: " + System.getProperty("APP_POSTGRESQL_PRIVATEKEY"));

        Thread.sleep(5_000);
        }
    }
}

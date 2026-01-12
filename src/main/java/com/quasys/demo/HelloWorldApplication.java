package com.quasys.demo;

public class HelloWorldApplication {
    public static void main(String[] args)  throws Exception {
        while (true) {

        System.out.println("Hello World from THY!");
        System.out.println("APP_POSTGRESQL_PASSWORD: " + System.getenv("APP_POSTGRESQL_PASSWORD"));
        System.out.println("APP_POSTGRESQL_USERNAME: " + System.getenv("APP_POSTGRESQL_USERNAME"));

        System.setProperty("APP_POSTGRESQL_PRIVATEKEY", "ThisIsCertPrivateKey");
        System.out.println("APP_POSTGRESQL_PRIVATEKEY: " + System.getProperty("APP_POSTGRESQL_PRIVATEKEY"));

        BeyondTrustClient client = new BeyondTrustClient();
        String secret = client.getSecretText("3eff81ac-5112-4cfc-6361-08de51b90d05");
        System.out.println("APP_POSTGRESQL_BEYONDTRUST_SECRET: " + secret);

        Thread.sleep(5_000);
        }
    }
}

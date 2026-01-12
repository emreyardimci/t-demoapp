package com.quasys.demo;

public class HelloWorldApplication {
    public static void main(String[] args)  throws Exception {
        System.out.println("Hello World from THY!");

        BeyondTrustClient client = new BeyondTrustClient();

        String secret = client.getSecretText(
                "843d6517-0fb6-43f0-809e-08dc4eff0fb3"
        );

        System.out.println("SECRET TEXT:");
        System.out.println(secret);
    }
}

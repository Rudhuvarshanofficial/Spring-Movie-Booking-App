package com.rudhuvarshan.MovieBooking;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.concurrent.CountDownLatch;

public class ConcurrencyTest {

    private static final String URL =
            "http://localhost:8080/book";

    private static final HttpClient client =
            HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {

        // Both users try to book the SAME seat
        String requestBody = """
                {
                    "showId": 1,
                    "seatIds": [3]
                }
                """;

        // Barrier: both threads wait here
        CountDownLatch startSignal = new CountDownLatch(1);

        Thread user1 = new Thread(() ->
                sendBooking(
                        "rudhu@gmail.com",
                        "123456",
                        requestBody,
                        startSignal
                )
        );

        Thread user2 = new Thread(() ->
                sendBooking(
                        "arun@gmail.com",
                        "123456",
                        requestBody,
                        startSignal
                )
        );

        user1.start();
        user2.start();

        // Give both threads time to reach the starting point
        Thread.sleep(1000);

        System.out.println("=================================");
        System.out.println("STARTING CONCURRENT BOOKING TEST");
        System.out.println("=================================");

        // Release both users at almost the same time
        startSignal.countDown();

        // Wait until both requests finish
        user1.join();
        user2.join();

        System.out.println("=================================");
        System.out.println("TEST COMPLETED");
        System.out.println("=================================");
    }


    private static void sendBooking(
            String email,
            String password,
            String requestBody,
            CountDownLatch startSignal) {

        try {

            // Wait until both users are ready
            startSignal.await();

            String credentials =
                    email + ":" + password;

            String encodedCredentials =
                    Base64.getEncoder()
                            .encodeToString(credentials.getBytes());

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(URL))
                            .header(
                                    "Authorization",
                                    "Basic " + encodedCredentials
                            )
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(requestBody)
                            )
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println();
            System.out.println("USER: " + email);
            System.out.println("STATUS: " + response.statusCode());
            System.out.println("RESPONSE: " + response.body());

        } catch (Exception e) {

            System.out.println();
            System.out.println("USER: " + email);
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
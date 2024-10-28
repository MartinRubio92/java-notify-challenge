package com.sondeos.javanotifychallenge.utils;

import org.springframework.web.client.RestClient;

public class RestClientSingleton {
    private static final RestClient INSTANCE = RestClient.create();

    private RestClientSingleton() {
    }

    public static RestClient getInstance() {
        return INSTANCE;
    }
}


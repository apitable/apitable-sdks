package com.apitable.client.api;

import com.apitable.client.api.http.ApiCredential;

public abstract class AbstractBaseTest {

    protected final String DATASHEET_ID = System.getenv("DATASHEET_ID");

    private final String DOMAIN = System.getenv("DOMAIN");

    private final String HOST_URL = "https://"+DOMAIN;

    private static final String API_KEY = System.getenv("TOKEN");

    protected ApitableApiClient apitableApiClient = new ApitableApiClient(HOST_URL, new ApiCredential(API_KEY));
}

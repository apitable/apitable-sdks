package com.apitable.client.api;

import com.apitable.client.api.http.ApiCredential;

public abstract class AbstractBaseTest {

    protected final String DATASHEET_ID = System.getenv("DATASHEET_ID");

    protected final String LINKED_DATASHEET_ID = System.getenv("LINKED_DATASHEET_ID");

    private final String LINKED_VIEW_ID = System.getenv("LINKED_VIEW_ID");

    private final String FOLDER_ID = System.getenv("FOLDER_ID");

    private final String DOMAIN = System.getenv("DOMAIN");

    private final String HOST_URL = "https://"+DOMAIN;

    private static final String API_KEY = System.getenv("TOKEN");

    protected ApitableApiClient apitableApiClient = new ApitableApiClient(HOST_URL, new ApiCredential(API_KEY));
}

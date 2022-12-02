package com.apitable.client.api;

import com.apitable.client.api.exception.ApiException;
import com.apitable.client.api.http.AbstractApi;
import com.apitable.client.api.http.ApiHttpClient;
import com.apitable.client.api.model.CreateDatasheetRequest;
import com.apitable.client.api.model.CreateDatasheetResponse;
import com.apitable.client.api.model.HttpResult;
import com.apitable.core.http.GenericTypeReference;
import com.apitable.core.http.HttpHeader;
import com.apitable.core.utils.StringUtil;

public class DatasheetApi extends AbstractApi {

    private static final String POST_DATASHEET_PATH = "/spaces/%s/datasheets";

    public DatasheetApi(ApiHttpClient apiHttpClient) {
        super(apiHttpClient);
    }

    public CreateDatasheetResponse addDatasheet(String spaceId,
                                                CreateDatasheetRequest datasheet) {

        checkPostDatasheetPathArgs(spaceId);

        final String path = String.format(POST_DATASHEET_PATH, spaceId);

        HttpResult<CreateDatasheetResponse> result = getDefaultHttpClient().post(
                path, new HttpHeader(), datasheet,
                new GenericTypeReference<HttpResult<CreateDatasheetResponse>>() {});
        return result.getData();
    }

    private void checkPostDatasheetPathArgs(String spaceId) {

        if (!StringUtil.hasText(spaceId)) {
            throw new ApiException("space id must be not null");
        }

    }

}

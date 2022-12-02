package com.apitable.client.api;

import com.apitable.client.api.exception.ApiException;
import com.apitable.client.api.http.AbstractApi;
import com.apitable.client.api.http.ApiHttpClient;
import com.apitable.client.api.model.CreateFieldRequest;
import com.apitable.client.api.model.CreateFieldResponse;
import com.apitable.client.api.model.HttpResult;
import com.apitable.client.api.model.field.property.BaseFieldProperty;
import com.apitable.core.http.GenericTypeReference;
import com.apitable.core.http.HttpHeader;
import com.apitable.core.utils.StringUtil;

public class FieldApi extends AbstractApi {

    private static final String POST_FIELD_PATH = "/spaces/%s/datasheets/%s/fields";

    private static final String DELETE_FIELD_PATH = "/spaces/%s/datasheets/%s/fields/%s";


    public FieldApi(ApiHttpClient apiHttpClient) {
        super(apiHttpClient);
    }

    public CreateFieldResponse addField(String spaceId, String datasheetId,
                                        CreateFieldRequest<? extends BaseFieldProperty> field) throws ApiException {

        checkPostFieldPathArgs(spaceId, datasheetId);

        final String path = String.format(POST_FIELD_PATH, spaceId, datasheetId);
        HttpResult<CreateFieldResponse> result = getDefaultHttpClient().post(
                        path, new HttpHeader(), field,
                        new GenericTypeReference<HttpResult<CreateFieldResponse>>() {});
        return result.getData();
    }

    private void checkPostFieldPathArgs(String spaceId, String datasheetId) {

        if (!StringUtil.hasText(spaceId)) {
            throw new ApiException("space id must be not null");
        }

        if (!StringUtil.hasText(datasheetId)) {
            throw new ApiException("datasheet id must be not null");
        }

    }

    public void deleteField(String spaceId, String datasheetId, String fieldId) {

        checkDeleteFieldPathArgs(spaceId, datasheetId, fieldId);

        final String path = String.format(DELETE_FIELD_PATH, spaceId, datasheetId, fieldId);

        getDefaultHttpClient().delete(path, new HttpHeader(), Void.class);

    }

    private void checkDeleteFieldPathArgs(String spaceId, String datasheetId, String fieldId) {
        if (!StringUtil.hasText(spaceId)) {
            throw new ApiException("space id must not be null");
        }

        if (!StringUtil.hasText(datasheetId)) {
            throw new ApiException("datasheet id must not be null");
        }

        if (!StringUtil.hasText(fieldId)) {
            throw new ApiException("fieldId id must not be null");
        }
    }
}

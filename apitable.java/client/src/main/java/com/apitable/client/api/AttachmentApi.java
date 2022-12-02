/*
 * Copyright (c) 2021 apitable, https://apitable.com <support@apitable.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.apitable.client.api;

import java.io.File;

import com.apitable.client.api.exception.ApiException;
import com.apitable.client.api.http.AbstractApi;
import com.apitable.client.api.http.ApiHttpClient;
import com.apitable.client.api.model.Attachment;
import com.apitable.client.api.model.HttpResult;
import com.apitable.core.http.FormDataMap;
import com.apitable.core.http.GenericTypeReference;
import com.apitable.core.http.HttpHeader;
import com.apitable.core.http.HttpMediaType;
import com.apitable.core.http.ResourceLoader;
import com.apitable.core.utils.AssertUtil;

/**
 * api method for datasheet attachment field operation
 */
public class AttachmentApi extends AbstractApi {

    private static final String PATH = "/datasheets/%s/attachments";

    public AttachmentApi(ApiHttpClient apiHttpClient) {
        super(apiHttpClient);
    }

    public Attachment upload(String datasheetId, ResourceLoader loader) throws ApiException {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.setContentType(HttpMediaType.MULTIPART_FORM_DATA);
        FormDataMap formDataMap = new FormDataMap();
        formDataMap.put("file", loader);
        HttpResult<Attachment> result = getDefaultHttpClient().post(String.format(PATH, datasheetId), httpHeader, formDataMap, new GenericTypeReference<HttpResult<Attachment>>() {});
        return result.getData();
    }

    public Attachment upload(String datasheetId, File file) throws ApiException {
        AssertUtil.notNull(file, "file can not be null");
        FormDataMap formDataMap = new FormDataMap();
        formDataMap.put("file", file);
        return upload(datasheetId, formDataMap);
    }

    public Attachment upload(String datasheetId, FormDataMap formData) throws ApiException {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.setContentType(HttpMediaType.MULTIPART_FORM_DATA);
        HttpResult<Attachment> result = getDefaultHttpClient().post(String.format(PATH, datasheetId), httpHeader, formData, new GenericTypeReference<HttpResult<Attachment>>() {});
        return result.getData();
    }
}

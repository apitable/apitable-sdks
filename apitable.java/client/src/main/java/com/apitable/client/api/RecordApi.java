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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.apitable.client.api.exception.ApiException;
import com.apitable.client.api.http.AbstractApi;
import com.apitable.client.api.http.ApiHttpClient;
import com.apitable.client.api.model.ApiQueryParam;
import com.apitable.client.api.model.CreateRecordRequest;
import com.apitable.client.api.model.HttpResult;
import com.apitable.client.api.model.Pager;
import com.apitable.client.api.model.PagerInfo;
import com.apitable.client.api.model.Record;
import com.apitable.client.api.model.Records;
import com.apitable.client.api.model.UpdateRecordRequest;
import com.apitable.core.http.DefaultHttpClient;
import com.apitable.core.http.GenericTypeReference;
import com.apitable.core.http.HttpHeader;
import com.apitable.core.utils.CollectionUtil;
import com.apitable.core.utils.MapUtil;
import com.apitable.core.utils.StringUtil;


/**
 * the api for operate records
 */
public class RecordApi extends AbstractApi {

    private static final String PATH = "/datasheets/%s/records";

    public RecordApi(ApiHttpClient apiHttpClient) {
        super(apiHttpClient);
    }

    public Stream<Record> getRecordsAsStream(String datasheetId) throws ApiException {
        return getRecords(datasheetId, getDefaultPerPage()).stream();
    }

    public List<Record> getRecords(String datasheetId, int page, int itemsPerPage) throws ApiException {
        return getRecords(datasheetId, page, itemsPerPage, false);
    }

    public List<Record> getRecords(String datasheetId, int page, int itemsPerPage, boolean isV3) throws ApiException {
        if (page < 0 || itemsPerPage < 0) {
            throw new ApiException("page or itemsPerPage don't set right");
        }
        ApiQueryParam queryParam = new ApiQueryParam(page, itemsPerPage);
        Map<String, String> uriVariables = queryParam.toMap();
        GenericTypeReference<HttpResult<PagerInfo<Record>>> reference = new GenericTypeReference<HttpResult<PagerInfo<Record>>>() {};
        String uri = String.format(PATH, datasheetId) + MapUtil.extractKeyToVariables(uriVariables);
        DefaultHttpClient client = getDefaultHttpClient();
        if (isV3) {
            client = getHttpClientWithVersion(ApiHttpClient.ApiVersion.V3);
        }
        HttpResult<PagerInfo<Record>> result = client.get(uri, new HttpHeader(), reference, uriVariables);
        return result.getData().getRecords();
    }

    public Pager<Record> getRecords(String datasheetId) throws ApiException {
        return new Pager<>(this, String.format(PATH, datasheetId), getDefaultPerPage(), Record.class);
    }


    public Pager<Record> getRecords(String datasheetId, boolean isV3) throws ApiException {
        if (isV3) {
            return new Pager<>(this, String.format(PATH, datasheetId), getDefaultPerPage(), Record.class, true);
        }
        return getRecords(datasheetId);
    }

    public Pager<Record> getRecords(String datasheetId, int itemsPerPage) throws ApiException {
        return new Pager<>(this, String.format(PATH, datasheetId), itemsPerPage, Record.class);
    }

    public Pager<Record> getRecords(String datasheetId, ApiQueryParam queryParam) throws ApiException {
        return new Pager<>(this, String.format(PATH, datasheetId), queryParam, Record.class);
    }

    public Pager<Record> getRecords(String datasheetId, ApiQueryParam queryParam, boolean isV3) throws ApiException {
        if (isV3) {
            return new Pager<>(this, String.format(PATH, datasheetId), queryParam, Record.class, true);
        }
        return new Pager<>(this, String.format(PATH, datasheetId), queryParam, Record.class);
    }

    public List<Record> addRecords(String datasheetId, CreateRecordRequest record) throws ApiException {
        if (!StringUtil.hasText(datasheetId)) {
            throw new ApiException("datasheet id must be not null");
        }
        if (record == null) {
            return null;
        }
        if (record.getRecords() == null) {
            return null;
        }
        if (record.getRecords().isEmpty()) {
            return null;
        }
        if (record.getRecords().size() > 10) {
            throw new ApiException("record only can add 10 every request");
        }
        HttpResult<Records> result = getDefaultHttpClient().post(String.format(PATH, datasheetId), new HttpHeader(), record, new GenericTypeReference<HttpResult<Records>>() {});
        return result.getData().getRecords();
    }

    public List<Record> updateRecords(String datasheetId, UpdateRecordRequest record) throws ApiException {
        if (!StringUtil.hasText(datasheetId)) {
            throw new ApiException("datasheet id must be not null");
        }
        if (record == null) {
            throw new RuntimeException("Record instance cannot be null.");
        }
        HttpResult<Records> result = getDefaultHttpClient().patch(String.format(PATH, datasheetId), new HttpHeader(), record, new GenericTypeReference<HttpResult<Records>>() {});
        return result.getData().getRecords();
    }

    public void deleteRecord(String datasheetId, String recordId) throws ApiException {
        deleteRecords(datasheetId, Collections.singletonList(recordId));
    }

    public void deleteRecords(String datasheetId, List<String> recordIds) throws ApiException {
        if (!StringUtil.hasText(datasheetId)) {
            throw new ApiException("datasheet id must be not null");
        }
        if (recordIds == null) {
            throw new ApiException("record id array must be not null");
        }
        if (recordIds.isEmpty()) {
            throw new ApiException("record id array must be not empty");
        }
        if (recordIds.size() > 10) {
            List<List<String>> splitList = CollectionUtil.splitListParallel(recordIds, 10);
            splitList.forEach(split -> deleteLimitSize(datasheetId, split));
        }
        else {
            deleteLimitSize(datasheetId, recordIds);
        }
    }

    public void deleteAllRecords(String datasheetId) throws ApiException {
        Stream<Record> recordStream = getRecordsAsStream(datasheetId);
        List<String> recordIds = recordStream.map(Record::getRecordId).collect(Collectors.toList());
        if (!recordIds.isEmpty()) {
            deleteRecords(datasheetId, recordIds);
        }
    }

    private void deleteLimitSize(String datasheetId, List<String> recordIds) {
        Map<String, String> uriVariables = MapUtil.listToUriVariableMap("recordIds", recordIds);
        String uri = String.format(PATH, datasheetId) + MapUtil.extractKeyToVariables(uriVariables);
        getDefaultHttpClient().delete(uri, new HttpHeader(), Void.class, uriVariables);
    }
}

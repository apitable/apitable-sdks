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

package com.apitable.client.api.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.apitable.client.api.Constants;
import com.apitable.client.api.exception.ApiException;
import com.apitable.client.api.http.AbstractApi;
import com.apitable.client.api.http.ApiHttpClient;
import com.apitable.core.http.DefaultHttpClient;
import com.apitable.core.http.GenericTypeReference;
import com.apitable.core.http.HttpHeader;
import com.apitable.core.utils.JacksonConverter;
import com.apitable.core.utils.MapUtil;
import com.fasterxml.jackson.databind.JavaType;

/**
 * <p>This class defines an Iterator implementation that is used as a paging iterator for all API methods that
 * return a List of objects. </p>
 */
public class Pager<T> implements Iterator<List<T>> {

    private int itemsPerPage;

    private int totalPages;

    private int totalItems;

    private int currentPage;

    private List<T> currentItems;

    private Stream<T> pagerStream = null;

    private ApiQueryParam queryParam;

    private AbstractApi api;

    private String url;

    private JavaType javaType;

    public Pager(AbstractApi api, String url, int itemsPerPage, Class<T> type, boolean isV3) throws ApiException {javaType = JacksonConverter.getCollectionJavaType(type);

        this.api = api;
        this.url = url;

        if (itemsPerPage < 1) {
            itemsPerPage = api.getDefaultPerPage();
        }

        this.queryParam = new ApiQueryParam(1, itemsPerPage);
        Map<String, String> uriVariables = this.queryParam.toMap();
        GenericTypeReference<HttpResult<PagerInfo<T>>> reference = new GenericTypeReference<HttpResult<PagerInfo<T>>>() {};
        String uri = url + MapUtil.extractKeyToVariables(uriVariables);
        DefaultHttpClient client = api.getDefaultHttpClient();
        if (isV3) {
            client = api.getHttpClientWithVersion(ApiHttpClient.ApiVersion.V3);
        }
        HttpResult<PagerInfo<T>> result = client.get(uri, new HttpHeader(), reference, uriVariables);
        if (result.getData().getRecords() != null) {
            this.currentItems = JacksonConverter.toGenericBean(result.getData().getRecords(), javaType);
            if (this.currentItems == null) {
                throw new ApiException("Invalid response from server");
            }
        }
        else {
            this.currentItems.clear();
        }
        this.itemsPerPage = result.getData().getPageSize();
        this.totalItems = result.getData().getTotal();
        this.totalPages = this.totalItems == 0 ? 1 : ((this.totalItems - 1) / this.itemsPerPage + 1);
    }

    public Pager(AbstractApi api, String url, int itemsPerPage, Class<T> type) throws ApiException {
        this(api, url, itemsPerPage, type, false);
    }

    public Pager(AbstractApi api, String url, ApiQueryParam queryParam, Class<T> type, boolean isV3) throws ApiException {
        this.api = api;
        this.url = url;
        this.queryParam = queryParam;
        javaType = JacksonConverter.getCollectionJavaType(type);
        GenericTypeReference<HttpResult<PagerInfo<T>>> reference = new GenericTypeReference<HttpResult<PagerInfo<T>>>() {};
        Map<String, String> uriVariables = this.queryParam.toMap();
        String uri = url + MapUtil.extractKeyToVariables(uriVariables);
        DefaultHttpClient client = api.getDefaultHttpClient();
        if (isV3) {
            client = api.getHttpClientWithVersion(ApiHttpClient.ApiVersion.V3);
        }
        HttpResult<PagerInfo<T>> result = client.get(uri, new HttpHeader(), reference, uriVariables);
        if (result.getData().getRecords() != null) {
            this.currentItems = JacksonConverter.toGenericBean(result.getData().getRecords(), javaType);
            if (this.currentItems == null) {
                throw new ApiException("Invalid response from server");
            }
        }
        else {
            this.currentItems.clear();
        }
        this.itemsPerPage = result.getData().getPageSize();
        this.totalItems = result.getData().getTotal();
        this.totalPages = this.totalItems == 0 ? 1 : ((this.totalItems - 1) / this.itemsPerPage + 1);
    }

    public Pager(AbstractApi api, String url, ApiQueryParam queryParam, Class<T> type) throws ApiException {
        this(api, url, queryParam, type, false);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public int getTotalItems() {
        return totalItems;
    }

    @Override
    public boolean hasNext() {
        return this.currentPage < this.totalPages;
    }

    @Override
    public List<T> next() {
        return page(this.currentPage + 1);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public List<T> first() {
        return page(1);
    }

    public List<T> last() {
        return page(totalPages);
    }

    public List<T> page(int pageNumber) {
        if (pageNumber > this.totalPages) {
            throw new NoSuchElementException();
        }
        else if (pageNumber < 1) {
            throw new NoSuchElementException();
        }

        if (this.currentPage == 0 && pageNumber == 1) {
            this.currentPage = 1;
            return this.currentItems;
        }

        if (this.currentPage == pageNumber) {
            return this.currentItems;
        }

        queryParam.withParam(Constants.PAGE_NUM, Integer.toString(pageNumber));
        Map<String, String> uriVariables = queryParam.toMap();
        GenericTypeReference<HttpResult<PagerInfo<T>>> reference = new GenericTypeReference<HttpResult<PagerInfo<T>>>() {};
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException e) {
            // do Nothing
        }
        String uri = url + MapUtil.extractKeyToVariables(uriVariables);
        HttpResult<PagerInfo<T>> result = api.getDefaultHttpClient().get(uri, new HttpHeader(), reference, uriVariables);
        if (result.getData().getRecords() != null) {
            this.currentItems = JacksonConverter.toGenericBean(result.getData().getRecords(), javaType);
        }
        else {
            this.currentItems.clear();
        }
        this.currentPage = pageNumber;
        return this.currentItems;
    }

    /**
     * Gets all the items from each page
     * @return all the items
     */
    public List<T> all() {

        // Make sure that current page is 0, this will ensure the whole list is fetched
        // regardless of what page the instance is currently on.
        currentPage = 0;
        List<T> allItems = new ArrayList<>(Math.max(totalItems, 0));

        // Iterate through the pages and append each page of items to the list
        while (hasNext()) {
            allItems.addAll(next());
        }

        return allItems;
    }

    public Stream<T> stream() throws IllegalStateException {
        if (pagerStream == null) {
            synchronized (this) {
                if (pagerStream == null) {

                    // Make sure that current page is 0, this will ensure the whole list is streamed
                    // regardless of what page the instance is currently on.
                    currentPage = 0;

                    // Create a Stream.Builder to contain all the items. This is more efficient than
                    // getting a List with all() and streaming that List
                    Stream.Builder<T> streamBuilder = Stream.builder();

                    // Iterate through the pages and append each page of items to the stream builder
                    while (hasNext()) {
                        next().forEach(streamBuilder);
                    }

                    pagerStream = streamBuilder.build();
                    return pagerStream;
                }
            }
        }
        throw new IllegalStateException("Stream already issued");
    }
}

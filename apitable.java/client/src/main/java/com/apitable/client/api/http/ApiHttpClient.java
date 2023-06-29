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

package com.apitable.client.api.http;

import com.apitable.core.http.ClientHttpRequestFactory;
import com.apitable.core.http.DefaultHttpClient;
import com.apitable.core.http.HttpHeader;
import com.apitable.core.http.OkHttpClientHttpRequestFactory;

/**
 * api http client
 * defined the host and timeout
 */
public class ApiHttpClient {

    public enum ApiVersion {
        V1,
        V2,
        ;

        public String getApiNamespace() {
            return ("/fusion/" + name().toLowerCase());
        }
    }

    public static final int DEFAULT_PER_PAGE = 100;

    public static final int DEFAULT_CONNECT_TIMEOUT = 60000;

    public static final int DEFAULT_READ_TIMEOUT = 60000;

    private final DefaultHttpClient defaultHttpClient;

    private int defaultPerPage = DEFAULT_PER_PAGE;

    private Integer connectTimeout = DEFAULT_CONNECT_TIMEOUT;

    private Integer readTimeout = DEFAULT_READ_TIMEOUT;

    public ApiHttpClient(ApiVersion apiVersion, String baseUrl, ApiCredential apiCredential) {
        baseUrl += apiVersion.getApiNamespace();
        this.defaultHttpClient = new DefaultHttpClient(baseUrl);
        HttpHeader header = setDefaultHeader(apiCredential);
        this.defaultHttpClient.addGlobalHeader(header);
        this.defaultHttpClient.setResponseBodyHandler(new ApiResponseErrorHandler());
    }

    private HttpHeader setDefaultHeader(ApiCredential apiCredential) {
        HttpHeader header = new HttpHeader();
        header.setUserAgent("apitable-java-client");
        header.setBearerAuth(apiCredential.getToken());
        return header;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setDefaultPerPage(int defaultPerPage) {
        this.defaultPerPage = defaultPerPage;
    }

    public int getDefaultPerPage() {
        return this.defaultPerPage;
    }

    public DefaultHttpClient getDefaultHttpClient() {
        if (connectTimeout != null) {
            // Sets the per request connect timeout.
            ClientHttpRequestFactory requestFactory = this.defaultHttpClient.getRequestFactory();
            if (requestFactory instanceof OkHttpClientHttpRequestFactory) {
                ((OkHttpClientHttpRequestFactory) requestFactory).setConnectTimeout(connectTimeout);
            }
        }

        if (readTimeout != null) {
            // Sets the per request read timeout.
            ClientHttpRequestFactory requestFactory = this.defaultHttpClient.getRequestFactory();
            if (requestFactory instanceof OkHttpClientHttpRequestFactory) {
                ((OkHttpClientHttpRequestFactory) requestFactory).setReadTimeout(readTimeout);
            }
        }
        return this.defaultHttpClient;
    }
}

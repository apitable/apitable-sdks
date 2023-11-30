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

import com.apitable.client.api.http.ApiCredential;
import com.apitable.client.api.http.ApiHttpClient;
import com.apitable.client.api.http.ApiHttpClient.ApiVersion;

/**
 * <p>
 *  This class is provides a simplified interface to a APITable API server,
 *  and divides the API up into a separate API class for each concern.
 * </p>
 */
public class ApitableApiClient {

    private static final String DEFAULT_HOST = "https://api.apitable.com";

    private final ApiVersion apiVersion;

    private final ApiHttpClient apiHttpClient;

    private volatile RecordApi recordApi;

    private volatile AttachmentApi attachmentApi;

    private volatile NodeApi nodeApi;

    private volatile FieldApi fieldApi;

    private volatile DatasheetApi datasheetApi;

    public ApitableApiClient(ApiCredential credential) {
        this(ApiVersion.V1, DEFAULT_HOST, credential);
    }

    public ApitableApiClient(String hostUrl, ApiCredential credential) {
        this(ApiVersion.V1, hostUrl, credential);
    }

    public ApitableApiClient(ApiVersion apiVersion, String hostUrl, ApiCredential credential) {
        this.apiVersion = apiVersion;
        this.apiHttpClient = new ApiHttpClient(apiVersion, hostUrl, credential);
    }

    /**
     * Return the API version that this instance is using.
     *
     * @return the API version that this instance is using
     */
    public ApiVersion getApiVersion() {
        return this.apiVersion;
    }

    /**
     * Get the default number per page for calls that return multiple items.
     *
     * @return the default number per page for calls that return multiple item
     */
    public int getDefaultPerPage() {
        return this.apiHttpClient.getDefaultPerPage();
    }

    /**
     * Set the default number per page for calls that return multiple items.
     *
     * @param defaultPerPage the new default number per page for calls that return multiple item
     */
    public void setDefaultPerPage(int defaultPerPage) {
        this.apiHttpClient.setDefaultPerPage(defaultPerPage);
    }

    /**
     * Sets the per request connect timeout.
     *
     * @param connectTimeout the per request connect timeout in milliseconds, can be null to use default
     */
    public void setRequestTimeout(Integer connectTimeout) {
        this.apiHttpClient.setConnectTimeout(connectTimeout);
    }

    /**
     * Build method that sets the per request connect timeout.
     * @param connectTimeout the per request connect timeout in milliseconds, can be null to use default
     * @return ApitableApiClient instance
     */
    public ApitableApiClient withRequestTimeout(Integer connectTimeout) {
        apiHttpClient.setConnectTimeout(connectTimeout);
        return this;
    }

    /**
     * Sets the per request read timeout.
     *
     * @param readTimeout the per request read timeout in milliseconds, can be null to use default
     */
    public void setReadTimeout(Integer readTimeout) {
        this.apiHttpClient.setReadTimeout(readTimeout);
    }

    /**
     * Build method that sets the per request read timeout.
     * @param readTimeout the per request read timeout in milliseconds, can be null to use default
     * @return ApitableApiClient instance
     */
    public ApitableApiClient withReadTimeout(Integer readTimeout) {
        apiHttpClient.setReadTimeout(readTimeout);
        return this;
    }

    public RecordApi getRecordApi() {
        if (this.recordApi == null) {
            synchronized (this) {
                if (this.recordApi == null) {
                    this.recordApi = new RecordApi(this.apiHttpClient);
                }
            }
        }
        return this.recordApi;
    }

    public AttachmentApi getAttachmentApi() {
        if (this.attachmentApi == null) {
            synchronized (this) {
                if (this.attachmentApi == null) {
                    this.attachmentApi = new AttachmentApi(this.apiHttpClient);
                }
            }
        }
        return this.attachmentApi;
    }

    public NodeApi getNodeApi() {
        if (this.nodeApi == null) {
            synchronized (this) {
                if (this.nodeApi == null) {
                    this.nodeApi = new NodeApi(this.apiHttpClient);
                }
            }
        }
        return this.nodeApi;
    }

    public FieldApi getFieldApi() {
        if (this.fieldApi == null) {
            synchronized (this) {
                if (this.fieldApi == null) {
                    this.fieldApi = new FieldApi(this.apiHttpClient);
                }
            }
        }
        return fieldApi;
    }

    public DatasheetApi getDatasheetApi() {
        if (this.datasheetApi == null) {
            synchronized (this) {
                if (this.datasheetApi == null) {
                    this.datasheetApi = new DatasheetApi(this.apiHttpClient);
                }
            }
        }
        return datasheetApi;
    }


    public boolean isV3(){
        return this.apiVersion == ApiVersion.V3;
    }
}

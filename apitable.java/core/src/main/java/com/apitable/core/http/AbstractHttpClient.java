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

package com.apitable.core.http;

import java.io.IOException;
import java.net.URI;

import com.apitable.core.exception.HttpClientException;
import com.apitable.core.utils.AssertUtil;

/**
 * abstract http client class
 */
public abstract class AbstractHttpClient {

    /**
     * Default implementation OkhttpClient
     */
    private ClientHttpRequestFactory requestFactory = new OkHttpClientHttpRequestFactory();

    /**
     * default response error handler, you can override this handler if deal with self
     */
    protected HttpResponseErrorHandler errorHandler = new DefaultHttpResponseErrorHandler();

    /**
     *
     */
    protected ResponseBodyHandler responseBodyHandler;

    /**
     * Set the request factory that this accessor uses for obtaining client request handles.
     * <p>The default is a {@link OkHttpClientHttpRequestFactory} based on the OKHttp libraries.
     *
     * @param requestFactory http request client factory
     */
    public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
        AssertUtil.notNull(requestFactory, "HttpRequestFactory must not be null");
        this.requestFactory = requestFactory;
    }

    /**
     * Return the request factory that this accessor uses for obtaining client request handles.
     */
    public ClientHttpRequestFactory getRequestFactory() {
        return this.requestFactory;
    }

    /**
     * Create a new {@link ClientHttpRequest} via this template's {@link ClientHttpRequestFactory}.
     *
     * @param uri    the URL to connect to
     * @param method the HTTP method to execute (GET, POST, etc)
     * @return the created request
     * @throws IOException in case of I/O errors
     * @see #getRequestFactory()
     * @see ClientHttpRequestFactory#createRequest(URI, HttpMethod)
     */
    protected ClientHttpRequest createRequest(URI uri, HttpMethod method) throws IOException {
        // create request by factory
        return getRequestFactory().createRequest(uri, method);
    }

    protected <T> T doExecute(URI uri, HttpMethod method, RequestWrapper requestWrapper, ResponseHandler<T> responseHandler) throws HttpClientException {
        // asset param non null
        AssertUtil.notNull(uri, "URI is required");
        AssertUtil.notNull(method, "HttpMethod is required");
        ClientHttpResponse response = null;
        try {
            // create Request instance
            ClientHttpRequest request = createRequest(uri, method);
            // wrapper request like header、requestBody
            if (requestWrapper != null) {
                requestWrapper.wrapper(request);
            }
            // execute request
            response = request.execute();
            // handler error if hasError
            handlerResponse(response);
            // in case of response interceptor
            return (responseHandler != null ? responseHandler.extractData(response, this.responseBodyHandler) : null);
        }
        catch (IOException ex) {
            String resource = uri.toString();
            String query = uri.getRawQuery();
            resource = (query != null ? resource.substring(0, resource.indexOf('?')) : resource);
            throw new RuntimeException("I/O error on " + method.name() + " request for \"" + resource + "\": " + ex.getMessage(), ex);
        }
        finally {
            // close
            if (response != null) {
                response.close();
            }
        }
    }

    private void handlerResponse(ClientHttpResponse response) throws IOException {
        boolean hasError = this.errorHandler.hasError(response);
        if (hasError) {
            this.errorHandler.handlerError(response);
        }
    }
}

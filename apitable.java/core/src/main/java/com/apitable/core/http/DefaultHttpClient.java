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

import java.net.URI;
import java.util.Map;

import com.apitable.core.exception.HttpClientException;
import com.apitable.core.utils.AssertUtil;
import com.apitable.core.utils.StringUtil;

/**
 * Http Client implementation IHttpClient interface
 */
public class DefaultHttpClient extends AbstractHttpClient implements IHttpClient {

    private final UriHandler uriHandler;

    private final HttpHeader defaultHeaders = new HttpHeader();

    public DefaultHttpClient() {
        // init default uri handler
        this.uriHandler = new DefaultUriBuildFactory();
    }

    public DefaultHttpClient(String baseUri) {
        // init default uri handler
        if (baseUri.endsWith(StringUtil.SLASH)) {
            baseUri = baseUri.substring(0, baseUri.length() - 2);
        }
        this.uriHandler = new DefaultUriBuildFactory(baseUri);
    }

    /**
     * construct of custom request factory
     * default is okhttp request
     *
     * @param requestFactory custom request factory
     */
    public DefaultHttpClient(ClientHttpRequestFactory requestFactory) {
        this();
        setRequestFactory(requestFactory);
    }

    public void addGlobalHeader(String headerName, String headerValue) {
        defaultHeaders.add(headerName, headerValue);
    }

    public void addGlobalHeader(HttpHeader headers) {
        defaultHeaders.putAll(headers);
    }

    public void setDefaultUriVariables(Map<String, ?> uriVars) {
        if (this.uriHandler instanceof DefaultUriBuildFactory) {
            ((DefaultUriBuildFactory) this.uriHandler).setDefaultUriVariables(uriVars);
        }
        else {
            throw new IllegalArgumentException("not supported with the configured uriHandler.");
        }
    }

    public UriHandler getUriHandler() {
        return this.uriHandler;
    }

    public HttpHeader getDefaultHeaders() {
        return defaultHeaders;
    }

    public void setErrorHandler(HttpResponseErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public HttpResponseErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    public ResponseBodyHandler getResponseBodyHandler() {
        return responseBodyHandler;
    }

    public void setResponseBodyHandler(ResponseBodyHandler bodyHandler) {
        AssertUtil.notNull(errorHandler, "ResponseBodyHandler must not be null");
        this.responseBodyHandler = bodyHandler;
    }

    // GET Request

    @Override
    public <T> T get(URI uri, HttpHeader header, Class<T> responseType) throws HttpClientException {
        RequestWrapper requestWrapper = new OnlyHeaderWrapper(wrapperDefaultHeader(header));
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(uri, HttpMethod.GET, requestWrapper, responseHandler);
    }

    @Override
    public <T> T get(String urlTemplate, HttpHeader header, Class<T> responseType, Object... uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new OnlyHeaderWrapper(wrapperDefaultHeader(header));
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(urlTemplate, HttpMethod.GET, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T get(String urlTemplate, HttpHeader header, Class<T> responseType, Map<String, ?> uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new OnlyHeaderWrapper(wrapperDefaultHeader(header));
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(urlTemplate, HttpMethod.GET, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T get(String urlTemplate, HttpHeader header, GenericTypeReference<T> responseType, Map<String, ?> uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new OnlyHeaderWrapper(wrapperDefaultHeader(header));
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType.getType());
        return execute(urlTemplate, HttpMethod.GET, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T get(String urlTemplate, HttpHeader header, GenericTypeReference<T> responseType, Object... uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new OnlyHeaderWrapper(wrapperDefaultHeader(header));
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType.getType());
        return execute(urlTemplate, HttpMethod.GET, requestWrapper, responseHandler, uriVariables);
    }

    // POST Request

    @Override
    public <T> T post(URI uri, HttpHeader header, Object requestBody, Class<T> responseType) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(uri, HttpMethod.POST, requestWrapper, responseHandler);
    }

    @Override
    public <T> T post(String urlTemplate, HttpHeader header, Object requestBody, Class<T> responseType, Object... uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(urlTemplate, HttpMethod.POST, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T post(String urlTemplate, HttpHeader header, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(urlTemplate, HttpMethod.POST, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T post(String urlTemplate, HttpHeader header, Object requestBody, GenericTypeReference<T> responseType, Map<String, ?> uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType.getType());
        return execute(urlTemplate, HttpMethod.POST, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T post(String urlTemplate, HttpHeader header, Object requestBody, GenericTypeReference<T> responseType, Object... uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType.getType());
        return execute(urlTemplate, HttpMethod.POST, requestWrapper, responseHandler, uriVariables);
    }

    // PUT Method

    @Override
    public <T> T put(URI uri, HttpHeader header, Object requestBody, Class<T> responseType) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(uri, HttpMethod.PUT, requestWrapper, responseHandler);
    }

    @Override
    public <T> T put(String urlTemplate, HttpHeader header, Object requestBody, Class<T> responseType, Object... uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(urlTemplate, HttpMethod.PUT, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T put(String urlTemplate, HttpHeader header, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(urlTemplate, HttpMethod.PUT, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T put(String urlTemplate, HttpHeader header, Object requestBody, GenericTypeReference<T> responseType, Map<String, ?> uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType.getType());
        return execute(urlTemplate, HttpMethod.PUT, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T put(String urlTemplate, HttpHeader header, Object requestBody, GenericTypeReference<T> responseType, Object... uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType.getType());
        return execute(urlTemplate, HttpMethod.PUT, requestWrapper, responseHandler, uriVariables);
    }

    // PATCH Method

    @Override
    public <T> T patch(URI uri, HttpHeader header, Object requestBody, Class<T> responseType) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(uri, HttpMethod.PATCH, requestWrapper, responseHandler);
    }

    @Override
    public <T> T patch(String urlTemplate, HttpHeader header, Object requestBody, Class<T> responseType, Object... uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(urlTemplate, HttpMethod.PATCH, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T patch(String urlTemplate, HttpHeader header, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(urlTemplate, HttpMethod.PATCH, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T patch(String urlTemplate, HttpHeader header, Object requestBody, GenericTypeReference<T> responseType, Map<String, ?> uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType.getType());
        return execute(urlTemplate, HttpMethod.PATCH, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T patch(String urlTemplate, HttpHeader header, Object requestBody, GenericTypeReference<T> responseType, Object... uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new RequestBodyWrapper(wrapperDefaultHeader(header), requestBody);
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType.getType());
        return execute(urlTemplate, HttpMethod.PATCH, requestWrapper, responseHandler, uriVariables);
    }

    // DELETE Method

    @Override
    public <T> T delete(URI uri, HttpHeader header, Class<T> responseType) throws HttpClientException {
        RequestWrapper requestWrapper = new OnlyHeaderWrapper(wrapperDefaultHeader(header));
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(uri, HttpMethod.GET, requestWrapper, responseHandler);
    }

    @Override
    public <T> T delete(String urlTemplate, HttpHeader header, Class<T> responseType, Object... uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new OnlyHeaderWrapper(wrapperDefaultHeader(header));
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(urlTemplate, HttpMethod.DELETE, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T delete(String urlTemplate, HttpHeader header, Class<T> responseType, Map<String, ?> uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new OnlyHeaderWrapper(wrapperDefaultHeader(header));
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType);
        return execute(urlTemplate, HttpMethod.DELETE, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T delete(String urlTemplate, HttpHeader header, GenericTypeReference<T> responseType, Object... uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new OnlyHeaderWrapper(wrapperDefaultHeader(header));
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType.getType());
        return execute(urlTemplate, HttpMethod.DELETE, requestWrapper, responseHandler, uriVariables);
    }

    @Override
    public <T> T delete(String urlTemplate, HttpHeader header, GenericTypeReference<T> responseType, Map<String, ?> uriVariables) throws HttpClientException {
        RequestWrapper requestWrapper = new OnlyHeaderWrapper(wrapperDefaultHeader(header));
        ResponseHandler<T> responseHandler = new ResponseBodyExtractHandler<>(responseType.getType());
        return execute(urlTemplate, HttpMethod.DELETE, requestWrapper, responseHandler, uriVariables);
    }

    // Core Method

    protected <T> T execute(URI uri, HttpMethod method, RequestWrapper requestWrapper, ResponseHandler<T> responseHandler) throws HttpClientException {
        // do execute
        return doExecute(uri, method, requestWrapper, responseHandler);
    }

    protected <T> T execute(String urlTemplate, HttpMethod method, RequestWrapper requestWrapper, ResponseHandler<T> responseHandler, Object... uriVariables) throws HttpClientException {
        // encode uri with object type variables
        URI expandedUri = getUriHandler().format(urlTemplate, uriVariables);
        // do execute
        return doExecute(expandedUri, method, requestWrapper, responseHandler);
    }

    protected <T> T execute(String urlTemplate, HttpMethod method, RequestWrapper requestWrapper, ResponseHandler<T> responseHandler, Map<String, ?> uriVariables) throws HttpClientException {
        // encode uri with map type variables
        URI expandedUri = getUriHandler().format(urlTemplate, uriVariables);
        // do execute
        return doExecute(expandedUri, method, requestWrapper, responseHandler);
    }

    private HttpHeader wrapperDefaultHeader(HttpHeader httpHeader) {
        if (!this.defaultHeaders.isEmpty()) {
            defaultHeaders.forEach(httpHeader::put);
        }
        return httpHeader;
    }
}

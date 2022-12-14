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


import com.apitable.core.utils.AssertUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Abstract Http Client Object
 */
public abstract class AbstractClientHttpRequest implements ClientHttpRequest {

    private final HttpHeader headers = new HttpHeader();

    private ByteArrayOutputStream bufferedOutput = new ByteArrayOutputStream(1024);

    private boolean executed = false;

    @Override
    public HttpHeader getHeaders() {
        return this.headers;
    }

    @Override
    public OutputStream getBody() throws IOException {
        return this.bufferedOutput;
    }

    @Override
    public ClientHttpResponse execute() throws IOException {
        // asset where request is executing
        assertNotExecuted();
        byte[] bytes = bufferedOutput.toByteArray();
        // letter than 0 mean have no content
        if (headers.getContentLength() < 0) {
            headers.setContentLength(bytes.length);
        }
        ClientHttpResponse result = this.executeInternal(this.headers, bytes);
        this.bufferedOutput = new ByteArrayOutputStream(0);
        this.executed = true;
        return result;
    }

    /**
     * Assert that this request has not been {@linkplain #execute() executed} yet.
     *
     * @throws IllegalStateException if this request has been executed
     */
    protected void assertNotExecuted() {
        AssertUtil.state(!this.executed, "Client already executed");
    }

    /**
     * Abstract template method for the HTTP request.
     *
     * @param headers the HTTP headers
     * @param content request body content
     * @return the response object for the executed request
     * @throws IOException in case of I/O errors
     */
    protected abstract ClientHttpResponse executeInternal(HttpHeader headers, byte[] content) throws IOException;
}

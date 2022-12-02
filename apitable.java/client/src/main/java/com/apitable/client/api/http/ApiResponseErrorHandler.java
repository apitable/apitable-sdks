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

import java.io.IOException;

import com.apitable.client.api.exception.ApiException;
import com.apitable.core.http.ResponseBodyHandler;
import com.apitable.core.utils.JacksonConverter;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * apitable rest api error handler
 */
public class ApiResponseErrorHandler implements ResponseBodyHandler {

    @Override
    public void handleBody(byte[] content) throws IOException {
        JsonNode jsonNode = JacksonConverter.unmarshal(content);
        boolean success = jsonNode.get("success").asBoolean();
        if (!success) {
            throw new ApiException(jsonNode.get("code").asInt(), jsonNode.get("message").toString());
        }
    }
}

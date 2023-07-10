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

import java.util.List;
import java.util.stream.Collectors;

public class NodeSearchParam extends BaseQueryParam {

    private static final String TYPE = "type";
    private static final String PERMISSIONS = "permissions";
    private static final String QUERY = "query";

    public static NodeSearchParam newInstance() {
        return new NodeSearchParam();
    }

    public NodeSearchParam() {
        super();
    }

    public NodeSearchParam(String type) {
        withParam(TYPE, type);
    }

    public NodeSearchParam withType(String type) {
        withParam(TYPE, type);
        return this;
    }

    public NodeSearchParam withPermissions(List<Integer> permissions) {
        List<String> stringList =
            permissions.stream().map(i -> Integer.toString(i)).collect(Collectors.toList());
        withParam(PERMISSIONS, stringList);
        return this;
    }

    public NodeSearchParam withQuery(String query) {
        withParam(QUERY, query);
        return this;
    }
}

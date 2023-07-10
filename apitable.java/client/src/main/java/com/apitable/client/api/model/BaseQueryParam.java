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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseQueryParam extends HashMap<String, List<String>> {
    public BaseQueryParam() {
        super(16);
    }

    public void withParam(String key, String value) {
        if (value != null && !value.isEmpty()) {
            add(key, value);
        }
    }

    public void withParam(String key, List<String> value) {
        if (value != null && !value.isEmpty()) {
            addAll(key, value);
        }
    }

    private void addAll(String key, List<String> values) {
        List<String> currentValues = this.computeIfAbsent(key, k -> new ArrayList<>(1));
        currentValues.addAll(values);
    }

    private void add(String key, String value) {
        List<String> values = this.computeIfAbsent(key, k -> new ArrayList<>(1));
        values.add(value);
    }

    public Map<String, String> toMap() {
        Map<String, String> queryMap = new HashMap<>();
        for (Entry<String, List<String>> entry : this.entrySet()) {
            if (entry.getValue().size() == 1) {
                queryMap.put(entry.getKey(), entry.getValue().get(0));
            }
            else if (entry.getValue().size() > 1) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    queryMap.put(entry.getKey() + "." + i, entry.getValue().get(i));
                }
            }
        }
        return queryMap;
    }
}

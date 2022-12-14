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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.apitable.core.utils.AssertUtil;
import com.apitable.core.utils.StringUtil;

/**
 * A data structure representing HTTP request or response headers,
 * mapping String header names to a list of String values,
 * also offering accessors for common application-level data types.
 */
public class HttpHeader implements Map<String, List<String>>, Serializable {

    private static final long serialVersionUID = -7828980872691946594L;

    /**
     * The HTTP {@code User-Agent} header field name.
     */
    public static final String USER_AGENT = "User-Agent";

    /**
     * The HTTP {@code Content-Type} header field name.
     */
    public static final String CONTENT_TYPE = "Content-Type";

    /**
     * The HTTP {@code Content-Length} header field name.
     */
    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    /**
     * The HTTP {@code Authorization} header field name.
     */
    public static final String AUTHORIZATION = "Authorization";

    public static final String TRANSFER_ENCODING = "Transfer-Encoding";

    final Map<String, List<String>> headers;

    /**
     * default construct method
     */
    public HttpHeader() {
        this(new LinkedHashMap<>(8, 0.75f));
    }

    public HttpHeader(Map<String, List<String>> headers) {
        AssertUtil.notNull(headers, "headers must not be null");
        this.headers = headers;
    }

    public List<String> getOrEmpty(Object headerName) {
        List<String> values = get(headerName);
        return (values != null ? values : Collections.emptyList());
    }

    /**
     * Return the first value for the given key.
     *
     * @param key the key
     * @return the first value for the specified key, or {@code null} if none
     */
    public String getFirstValue(String key) {
        List<String> values = this.headers.get(key);
        return (values != null && !values.isEmpty() ? values.get(0) : null);
    }

    public void setUserAgent(String userAgent) {
        AssertUtil.hasText(userAgent, "userAgent is null");
        set(USER_AGENT, userAgent);
    }

    public void setBearerAuth(String token) {
        AssertUtil.hasText(token, "token is null");
        set(AUTHORIZATION, "Bearer " + token);
    }

    // extend header value, make setter easy

    /**
     * Return the length of the body in bytes, as specified by the {@code Content-Length} header.
     *
     * @return Returns -1 when the content-length is unknown
     */
    public long getContentLength() {
        String value = getFirstValue(CONTENT_LENGTH);
        return (value != null ? Long.parseLong(value) : -1);
    }

    /**
     * Set the length of the body in bytes, as specified by the {@code Content-Length} header.
     */
    public void setContentLength(long contentLength) {
        put(CONTENT_LENGTH, Collections.singletonList(Long.toString(contentLength)));
    }

    public void setContentType(String contentType) {
        put(CONTENT_TYPE, Collections.singletonList(contentType));
    }

    public String getContentType() {
        return getFirstValue(CONTENT_TYPE);
    }

    public void setContentDispositionFormData(String name, String filename) {
        AssertUtil.notNull(name, "Name must not be null");
        if (StringUtil.hasText(filename)) {
            setContentDisposition(buildContentDisposition("form-data", name, filename));
        } else {
            setContentDisposition(buildContentDisposition("form-data", name));
        }
    }

    public void setContentDisposition(String contentDisposition) {
        set(CONTENT_DISPOSITION, contentDisposition);
    }

    public void add(String headerName, String headerValue) {
        List<String> values = this.headers.computeIfAbsent(headerName, k -> new ArrayList<>(1));
        values.add(headerValue);
    }

    public void addAll(String key, List<? extends String> values) {
        List<String> currentValues = this.headers.computeIfAbsent(key, k -> new ArrayList<>(1));
        currentValues.addAll(values);
    }

    public void set(String headerName, String headerValue) {
        List<String> values = new ArrayList<>(1);
        values.add(headerValue);
        this.headers.put(headerName, values);
    }

    public void setAll(Map<String, String> values) {
        values.forEach(this::set);
    }


    @Override
    public int size() {
        return this.headers.size();
    }

    @Override
    public boolean isEmpty() {
        return this.headers.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.headers.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.headers.containsValue(value);
    }

    @Override
    public List<String> get(Object key) {
        return this.headers.get(key);
    }

    @Override
    public List<String> put(String key, List<String> value) {
        return this.headers.put(key, value);
    }

    @Override
    public List<String> remove(Object key) {
        return this.headers.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<String>> map) {
        this.headers.putAll(map);
    }

    @Override
    public void clear() {
        this.headers.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.headers.keySet();
    }

    @Override
    public Collection<List<String>> values() {
        return this.headers.values();
    }

    @Override
    public Set<Entry<String, List<String>>> entrySet() {
        return this.headers.entrySet();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HttpHeader)) {
            return false;
        }
        return unwrap(this).equals(unwrap((HttpHeader) other));
    }

    private static Map<String, List<String>> unwrap(HttpHeader headers) {
        while (headers.headers instanceof HttpHeader) {
            headers = (HttpHeader) headers.headers;
        }
        return headers.headers;
    }

    @Override
    public int hashCode() {
        return this.headers.hashCode();
    }

    @Override
    public String toString() {
        return formatHeaders(this.headers);
    }

    public static String formatHeaders(Map<String, List<String>> headers) {
        return headers.entrySet().stream()
                .map(entry -> {
                    List<String> values = entry.getValue();
                    return entry.getKey() + ":" + (values.size() == 1 ?
                            "\"" + values.get(0) + "\"" :
                            values.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")));
                })
                .collect(Collectors.joining(", ", "[", "]"));
    }

    public static String buildContentDisposition(String type, String name) {
        return type + "; name=\"" + name + '\"';
    }

    public static String buildContentDisposition(String type, String name, String filename) {
        return type
            + "; name=\""
            + name + '\"'
            + "; filename=\""
            + quoteFilename(filename) + '\"';
    }

    private static String quoteFilename(String filename) {
        if (filename.indexOf('"') == -1 && filename.indexOf('\\') == -1) {
            return filename;
        }
        boolean escaped = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filename.length() ; i++) {
            char c = filename.charAt(i);
            if (!escaped && c == '"') {
                sb.append("\\\"");
            }
            else {
                sb.append(c);
            }
            escaped = (!escaped && c == '\\');
        }
        // Remove backslash at the end..
        if (escaped) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}

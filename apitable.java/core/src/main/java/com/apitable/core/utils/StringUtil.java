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

package com.apitable.core.utils;

import java.util.List;
import java.util.Map;

/**
 * this is for {@link String} util method
 */
public class StringUtil {

    public static final char SLASH_CHAR = '/';

    private static final char BACKSLASH_CHAR = '\\';

    public static final String SLASH = "/";

    public static final String EMPTY = "";

    public static final String DOT = ".";

    public static final String DOUBLE_DOT = "..";

    public static final String COLON = ":";

    /**
     * Check whether the given {@code String} contains actual <em>text</em>.
     * <p>More specifically, this method returns {@code true} if the
     * {@code String} is not {@code null}, its length is greater than 0,
     * and it contains at least one non-whitespace character.
     *
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not {@code null}, its
     * length is greater than 0, and it does not contain whitespace only
     * @see Character#isWhitespace
     */
    public static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    public static boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String format(String template, Map<String, ?> values) {
        if (null == template) {
            return null;
        }
        if (null == values || values.isEmpty()) {
            return template;
        }

        String template2 = template;
        String value;
        for (Map.Entry<String, ?> entry : values.entrySet()) {
            value = entry.getValue().toString();
            if (null != value) {
                template2 = replace(template2, 0, "{" + entry.getKey() + "}", value);
            }
        }
        return template2;
    }

    public static String format(String template, Object... params) {
        if (null == template) {
            return null;
        }
        if (params == null || params.length == 0 || isEmpty(template)) {
            return template;
        }
        return String.format(template, params);
    }

    public static String replace(String str, int fromIndex, String searchStr, String replacement) {
        if (isEmpty(str) || isEmpty(searchStr)) {
            return str;
        }
        if (null == replacement) {
            replacement = "";
        }

        final int strLength = str.length();
        final int searchStrLength = searchStr.length();
        if (fromIndex > strLength) {
            return str;
        }
        else if (fromIndex < 0) {
            fromIndex = 0;
        }

        final StringBuilder result = new StringBuilder(strLength + 16);
        if (0 != fromIndex) {
            result.append(str.subSequence(0, fromIndex));
        }

        int preIndex = fromIndex;
        int index;
        while ((index = str.indexOf(searchStr, preIndex)) > -1) {
            result.append(str.subSequence(preIndex, index));
            result.append(replacement);
            preIndex = index + searchStrLength;
        }

        if (preIndex < strLength) {
            result.append(str.subSequence(preIndex, strLength));
        }
        return result.toString();
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String wrap(CharSequence str, CharSequence prefix) {
        return nullToEmpty(prefix).concat(nullToEmpty(str));
    }

    public static String wrap(CharSequence str, CharSequence prefix, CharSequence suffix) {
        return nullToEmpty(prefix).concat(nullToEmpty(str)).concat(nullToEmpty(suffix));
    }

    public static String nullToEmpty(CharSequence str) {
        return nullToDefault(str, EMPTY);
    }

    public static String nullToDefault(CharSequence str, String defaultStr) {
        return (str == null) ? defaultStr : str.toString();
    }

    public static String getName(String filePath) {
        if (null == filePath) {
            return null;
        }
        int len = filePath.length();
        if (0 == len) {
            return filePath;
        }
        if (isFileSeparator(filePath.charAt(len - 1))) {
            // Remove end delimiter ending with delimiter
            len--;
        }

        int begin = 0;
        char c;
        for (int i = len - 1; i > -1; i--) {
            c = filePath.charAt(i);
            if (isFileSeparator(c)) {
                // Find the last path separator (/or\)
                begin = i + 1;
                break;
            }
        }

        return filePath.substring(begin, len);
    }

    public static boolean isFileSeparator(char c) {
        return SLASH_CHAR == c || BACKSLASH_CHAR == c;
    }

    public static String removePrefix(CharSequence str, CharSequence prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return null == str ? null : str.toString();
        }

        final String str2 = str.toString();
        if (str2.startsWith(prefix.toString())) {
            return sub(str2, prefix.length(), str2.length());// Intercept the second half
        }
        return str2;
    }

    public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return null == str ? null : str.toString();
        }

        final String str2 = str.toString();
        if (str2.toLowerCase().startsWith(prefix.toString().toLowerCase())) {
            if (isEmpty(str2)) {
                return null;
            }
            return sub(str2, prefix.length(), str2.length());
        }
        return str2;
    }

    public static String sub(CharSequence str, int fromIndex, int toIndex) {
        if (isEmpty(str)) {
            return null == str ? null : str.toString();
        }
        int len = str.length();

        if (fromIndex < 0) {
            fromIndex = len + fromIndex;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        }
        else if (fromIndex > len) {
            fromIndex = len;
        }

        if (toIndex < 0) {
            toIndex = len + toIndex;
            if (toIndex < 0) {
                toIndex = len;
            }
        }
        else if (toIndex > len) {
            toIndex = len;
        }

        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }

        if (fromIndex == toIndex) {
            return EMPTY;
        }

        return str.toString().substring(fromIndex, toIndex);
    }

    public static String join(List<String> list,  CharSequence conjunction) {
        if (null == list) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(conjunction);
            sb.append(s);
        }
        return sb.toString();
    }

    public static String getFilenameExtension(String fileName) {
        if (fileName == null) {
            return null;
        }

        int extIndex = fileName.lastIndexOf(DOT);
        if (extIndex == -1) {
            return null;
        }

        int folderIndex = fileName.lastIndexOf(SLASH);
        if (folderIndex > extIndex) {
            return null;
        }
        return fileName.substring(extIndex + 1);
    }
}

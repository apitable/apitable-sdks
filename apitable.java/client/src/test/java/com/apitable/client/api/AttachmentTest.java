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

import java.io.File;
import java.net.URL;

import com.apitable.client.api.http.ApiCredential;
import com.apitable.client.api.model.Attachment;
import com.apitable.core.http.ClassPathResourceLoader;
import com.apitable.core.http.FileResourceLoader;
import com.apitable.core.http.UrlResourceLoader;
import com.apitable.core.utils.UrlUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Attachment Api Test
 */
@TestMethodOrder(OrderAnnotation.class)
public class AttachmentTest {

    private final String DATASHEET_ID = System.getenv("DATASHEET_ID");

    private final String LINKED_DATASHEET_ID = System.getenv("LINKED_DATASHEET_ID");

    private final String LINKED_VIEW_ID = System.getenv("LINKED_VIEW_ID");

    private final String DOMAIN = System.getenv("DOMAIN");

    private final String HOST_URL = "https://"+DOMAIN;

    private final String API_KEY = System.getenv("TOKEN");

    private final ApitableApiClient apitableApiClient = new ApitableApiClient(HOST_URL, new ApiCredential(API_KEY));

    @Test
    @Order(1)
    void testUploadWithClassPathResource() throws JsonProcessingException, InterruptedException {
        Attachment attachment = apitableApiClient.getAttachmentApi().upload(DATASHEET_ID, new ClassPathResourceLoader("test.txt"));
        assertThat(attachment).isNotNull();
        System.out.println(JacksonJsonUtil.toJson(attachment, true));
        Thread.sleep(1000);
    }

    @Test
    @Order(2)
    void testUploadWithUrlResource() throws JsonProcessingException, InterruptedException {
        Attachment attachment = apitableApiClient.getAttachmentApi().upload(DATASHEET_ID, new UrlResourceLoader(UrlUtil.url("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3483500324,2196746779&fm=26&gp=0.jpg")));
        assertThat(attachment).isNotNull();
        System.out.println(JacksonJsonUtil.toJson(attachment, true));
        Thread.sleep(1000);
    }

    @Test
    @Order(3)
    void testUploadWithFileResource() throws JsonProcessingException, InterruptedException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("test.txt");
        assertThat(url).isNotNull();
        File file = new File(url.getFile());
        assertThat(file).isNotNull();
        Attachment attachment = apitableApiClient.getAttachmentApi().upload(DATASHEET_ID, new FileResourceLoader(file));
        assertThat(attachment).isNotNull();
        System.out.println(JacksonJsonUtil.toJson(attachment, true));
        Thread.sleep(1000);
    }

    @Test
    @Order(4)
    void testUploadWithFile() throws JsonProcessingException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("test.xlsx");
        assertThat(url).isNotNull();
        File file = new File(url.getFile());
        assertThat(file).isNotNull();
        Attachment attachment = apitableApiClient.getAttachmentApi().upload(DATASHEET_ID, file);
        assertThat(attachment).isNotNull();
        System.out.println(JacksonJsonUtil.toJson(attachment, true));
    }
}

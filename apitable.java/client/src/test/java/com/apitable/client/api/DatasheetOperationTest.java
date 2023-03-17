package com.apitable.client.api;

import java.util.ArrayList;
import java.util.List;

import com.apitable.client.api.http.ApiCredential;
import com.apitable.client.api.model.*;
import com.apitable.client.api.model.builder.CreateFieldRequestBuilder;
import com.apitable.client.api.model.field.FieldTypeEnum;
import com.apitable.client.api.model.field.property.EmptyProperty;
import com.apitable.client.api.model.field.property.SingleTextFieldProperty;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DatasheetOperationTest {

    private final String SPACE_ID = System.getenv("SPACE_ID");

    private final String DOMAIN = System.getenv("DOMAIN");

    private final String HOST_URL = "https://"+DOMAIN;

    private final String API_KEY = System.getenv("TOKEN");

    private final String DATASHEET_ID = System.getenv("DATASHEET_ID");

    private final String LINKED_DATASHEET_ID = System.getenv("LINKED_DATASHEET_ID");

    private final String LINKED_VIEW_ID = System.getenv("LINKED_VIEW_ID");

    private final String VIEW_ID = System.getenv("VIEW_ID");

    private final ApitableApiClient apitableApiClient = new ApitableApiClient(HOST_URL, new ApiCredential(API_KEY));

    @Test
    void testAddDatasheet() {
        CreateDatasheetRequest request = new CreateDatasheetRequest();
        request.setName("datasheet");
        CreateDatasheetResponse response = apitableApiClient.getDatasheetApi().addDatasheet(SPACE_ID, request);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void testAddDatasheetWithOtherInfo() {
        CreateDatasheetRequest request = new CreateDatasheetRequest();
        request.setName("datasheetWithOtherInfo");
        request.setDescription("description");
        request.setFolderId("fodWGD6LirjMM");
        SingleTextFieldProperty property = new SingleTextFieldProperty();
        property.setDefaultValue("default");
        CreateFieldRequest<SingleTextFieldProperty> singleSelectField = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.SingleText)
                .withName("singleSelect")
                .withProperty(property)
                .build();
        CreateFieldRequest<EmptyProperty> textField = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Text)
                .withName("text")
                .withoutProperty()
                .build();
        List<CreateFieldRequest<?>> fields = new ArrayList<>();
        fields.add(singleSelectField);
        fields.add(textField);
        request.setFields(fields);
        CreateDatasheetResponse response = apitableApiClient.getDatasheetApi().addDatasheet(SPACE_ID, request);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void testCreateEmbedLink(){
        CreateEmbedLinkRequest request = new CreateEmbedLinkRequest();
        EmbedLinkPayloadViewControl viewControl = new EmbedLinkPayloadViewControl();
        viewControl.setViewId(VIEW_ID);
        EmbedLinkPayload embedLinkPayload = new EmbedLinkPayload();
        embedLinkPayload.setViewControl(viewControl);
        request.setPayload(embedLinkPayload);
        request.setTheme(EmbedLinkThemeEnum.light);
        CreateEmbedLinkResponse response = apitableApiClient.getDatasheetApi().addEmbedLink(SPACE_ID, DATASHEET_ID, request);
        assertThat(response).isNotNull();
        assertThat(response.getLinkId()).isNotNull();
    }

    @Test
    void testGetEmbedLink(){
        List<GetEmbedLinkResponse> responses = apitableApiClient.getDatasheetApi().getEmbedLink(SPACE_ID, DATASHEET_ID);
        assertThat(responses).isNotNull();
        assertThat(responses.get(0).getLinkId()).isNotNull();
    }

    @Test
    void testDeleteEmbedLink() throws InterruptedException {
        CreateEmbedLinkRequest request = new CreateEmbedLinkRequest();
        EmbedLinkPayloadViewControl viewControl = new EmbedLinkPayloadViewControl();
        viewControl.setViewId(VIEW_ID);
        EmbedLinkPayload embedLinkPayload = new EmbedLinkPayload();
        embedLinkPayload.setViewControl(viewControl);
        request.setPayload(embedLinkPayload);
        request.setTheme(EmbedLinkThemeEnum.light);
        CreateEmbedLinkResponse response = apitableApiClient.getDatasheetApi().addEmbedLink(SPACE_ID, DATASHEET_ID, request);
        assertThat(response).isNotNull();
        assertThat(response.getLinkId()).isNotNull();
        Thread.sleep(1000);
        apitableApiClient.getDatasheetApi().deleteEmbedLink(SPACE_ID, DATASHEET_ID, response.getLinkId());
    }

}

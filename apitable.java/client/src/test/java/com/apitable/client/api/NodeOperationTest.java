package com.apitable.client.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.apitable.client.api.http.ApiCredential;
import com.apitable.client.api.model.Node;
import com.apitable.client.api.model.NodeDetail;
import com.apitable.client.api.model.NodeSearchInfo;
import com.apitable.client.api.model.NodeSearchRequest;
import java.util.List;
import org.junit.jupiter.api.Test;

public class NodeOperationTest {

    private final String SPACE_ID = System.getenv("SPACE_ID");

    private final String DOMAIN = System.getenv("DOMAIN");

    private final String HOST_URL = "https://" + DOMAIN;

    private final String API_KEY = System.getenv("TOKEN");

    private final String DATASHEET_ID = System.getenv("DATASHEET_ID");

    private final ApitableApiClient apitableApiClient =
        new ApitableApiClient(HOST_URL, new ApiCredential(API_KEY));

    @Test
    void testSearchNode() {
        NodeSearchRequest request = new NodeSearchRequest();
        request.setType("Datasheet");
        List<NodeSearchInfo> nodes =
            apitableApiClient.getNodeApi().searNodes(SPACE_ID, request);
        assertThat(nodes).isNotEmpty();
    }

    @Test
    void testGetNodes() {
        List<Node> nodes = apitableApiClient.getNodeApi().getNodes(SPACE_ID);
        assertThat(nodes).isNotEmpty();
    }

    @Test
    void testGetNodeDetail() {
        NodeDetail node = apitableApiClient.getNodeApi().getNode(SPACE_ID, DATASHEET_ID);
        assertThat(node).isNotNull();
        assertThat(node.getId()).isEqualTo(DATASHEET_ID);
    }

}

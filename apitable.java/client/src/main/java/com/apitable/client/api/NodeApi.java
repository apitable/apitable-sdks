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

import com.apitable.client.api.http.ApiHttpClient.ApiVersion;
import com.apitable.client.api.model.NodeSearchInfo;
import com.apitable.client.api.model.NodeSearchParam;
import com.apitable.client.api.model.NodeSearchResult;
import com.apitable.core.utils.MapUtil;
import java.util.List;

import com.apitable.client.api.http.AbstractApi;
import com.apitable.client.api.http.ApiHttpClient;
import com.apitable.client.api.model.GetNodeResult;
import com.apitable.client.api.model.HttpResult;
import com.apitable.client.api.model.Node;
import com.apitable.client.api.model.NodeDetail;
import com.apitable.client.api.model.NodeTree;
import com.apitable.core.http.GenericTypeReference;
import com.apitable.core.http.HttpHeader;
import java.util.Map;


/**
 * the api for operate workbench nodes
 */
public class NodeApi extends AbstractApi {

    private static final String GET_NODES = "/spaces/%s/nodes";

    private static final String GET_NODE = "/spaces/%s/nodes/%s";

    public NodeApi(ApiHttpClient apiHttpClient) {
        super(apiHttpClient);
    }

    public List<NodeSearchInfo> searNodes(String spaceId, NodeSearchParam param) {
        Map<String, String> uriVariables = param.toMap();
        String uri = String.format(GET_NODES, spaceId)
            + MapUtil.extractKeyToVariables(uriVariables);
        GenericTypeReference<HttpResult<NodeSearchResult>> reference =
            new GenericTypeReference<HttpResult<NodeSearchResult>>() {};
        HttpResult<NodeSearchResult> result =
            getHttpClientWithVersion(ApiVersion.V2).get(uri,
                new HttpHeader(), reference, uriVariables);
        return result.getData().getNodes();
    }

    public List<Node> getNodes(String spaceId) {
        GenericTypeReference<HttpResult<GetNodeResult>> reference = new GenericTypeReference<HttpResult<GetNodeResult>>() {};
        HttpResult<GetNodeResult> result = getDefaultHttpClient().get(String.format(GET_NODES, spaceId),
                new HttpHeader(), reference);
        return result.getData().getNodes();
    }

    public NodeDetail getNode(String spaceId, String nodeId) {
        GenericTypeReference<HttpResult<NodeDetail>> reference = new GenericTypeReference<HttpResult<NodeDetail>>() {};
        HttpResult<NodeDetail> result = getDefaultHttpClient().get(String.format(GET_NODE, spaceId, nodeId),
                new HttpHeader(), reference);
        return result.getData();
    }

    public NodeTree getNodeTree(String spaceId, String nodeId) {
        return new NodeTree(this, spaceId, nodeId);
    }

}

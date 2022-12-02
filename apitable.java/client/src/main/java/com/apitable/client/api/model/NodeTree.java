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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.apitable.client.api.NodeApi;
import com.apitable.core.utils.CollectionUtil;

public class NodeTree implements Iterator<List<NodeDetail>> {

    private List<NodeDetail> nodes;

    private List<String> nodeIds;

    private String spaceId;

    private NodeApi api;

    private int totalCount;

    public NodeTree(NodeApi api, String spaceId, String nodeId) {
        this.api = api;
        this.spaceId = spaceId;
        nodes = api.getNode(spaceId, nodeId).getChildren();
        nodeIds = nodes.stream().filter(node -> node.getType().equals("Folder"))
                .map(NodeDetail::getId).collect(Collectors.toList());
    }

    @Override
    public boolean hasNext() {
        return CollectionUtil.isNotEmpty(nodes);
    }

    @Override
    public List<NodeDetail> next() {
        if (totalCount == 0) {
            totalCount = nodes.size();
            return nodes;
        }
        List<List<NodeDetail>> iters = new ArrayList<>();
        nodeIds.forEach(node -> {
            List<NodeDetail> list = api.getNode(spaceId, node).getChildren();
            if (CollectionUtil.isNotEmpty(list)) {
                iters.add(list);
            }
        });
        nodes.clear();
        iters.forEach(iter -> nodes.addAll(iter));
        nodeIds = nodes.stream().filter(node -> node.getType().equals("Folder"))
                .map(NodeDetail::getId).collect(Collectors.toList());
        totalCount = nodes.size();
        return this.nodes;
    }
}

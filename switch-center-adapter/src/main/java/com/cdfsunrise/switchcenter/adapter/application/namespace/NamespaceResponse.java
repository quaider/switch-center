package com.cdfsunrise.switchcenter.adapter.application.namespace;

import lombok.Data;

import java.io.Serializable;

@Data
public class NamespaceResponse implements Serializable {
    private static final long serialVersionUID = -2678284431195501071L;

    private String namespaceId;
    private String namespaceName;
}

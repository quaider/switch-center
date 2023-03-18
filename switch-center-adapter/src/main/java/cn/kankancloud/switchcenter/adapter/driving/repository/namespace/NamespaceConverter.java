package cn.kankancloud.switchcenter.adapter.driving.repository.namespace;

import cn.kankancloud.switchcenter.adapter.domain.namespace.Namespace;
import cn.kankancloud.switchcenter.adapter.driving.repository.namespace.dao.NamespacePo;

public class NamespaceConverter {
    private NamespaceConverter() {
    }

    public static NamespacePo toPo(Namespace ns) {
        NamespacePo nsPo = new NamespacePo();
        nsPo.setId(ns.getId());
        nsPo.setNamespaceId(ns.getNamespaceId());
        nsPo.setNamespaceName(ns.getNamespaceName());

        return nsPo;
    }

    public static Namespace toDomain(NamespacePo namespacePo) {
        Namespace namespace = new Namespace(namespacePo.getNamespaceId(), namespacePo.getNamespaceName());
        namespace.onPersisted(namespacePo.getId());

        return namespace;
    }
}

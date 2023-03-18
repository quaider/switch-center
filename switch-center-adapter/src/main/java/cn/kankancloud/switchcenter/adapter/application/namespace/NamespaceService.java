package cn.kankancloud.switchcenter.adapter.application.namespace;

import cn.kankancloud.switchcenter.adapter.domain.namespace.Namespace;
import cn.kankancloud.switchcenter.adapter.domain.namespace.NamespaceRepository;
import cn.kankancloud.switchcenter.adapter.domain.namespace.NamespaceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NamespaceService {

    private final NamespaceValidator namespaceValidator;
    private final NamespaceRepository repository;

    public void addNamespace(NamespaceCreateCmd cmd) {
        namespaceValidator.namespaceIdShouldUnique(cmd.getNamespaceId());

        Namespace ns = new Namespace(cmd.getNamespaceId(), cmd.getNamespaceName());
        repository.save(ns);
    }

    public void changeName(String namespaceId, String newName) {
        Namespace namespace = namespaceValidator.namespaceMustExist(namespaceId);
        namespace.changeName(newName);

        repository.save(namespace);
    }

    public void delete(String namespaceId) {
        Namespace namespace = namespaceValidator.validateOnDelete(namespaceId);
        namespace.toDelete();

        repository.save(namespace);
    }

}

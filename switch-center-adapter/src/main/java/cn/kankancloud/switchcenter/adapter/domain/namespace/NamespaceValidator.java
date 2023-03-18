package cn.kankancloud.switchcenter.adapter.domain.namespace;

import cn.kankancloud.jbp.core.domain.DomainInjector;
import cn.kankancloud.jbp.core.exception.BizDuplicatedException;
import cn.kankancloud.jbp.core.exception.BizNotFoundException;
import cn.kankancloud.jbp.core.exception.BizValidateException;
import cn.kankancloud.switchcenter.adapter.domain.switchinfo.SwitchInfoRepository;
import lombok.RequiredArgsConstructor;

/**
 * 领域服务
 */
@DomainInjector
@RequiredArgsConstructor
public class NamespaceValidator {

    private final NamespaceRepository namespaceRepository;
    private final SwitchInfoRepository switchInfoRepository;

    public Namespace namespaceMustExist(String namespaceId) {
        return namespaceRepository.findByNamespaceId(namespaceId)
                .orElseThrow(() -> new BizNotFoundException("namespaceId `{}` not found", namespaceId));
    }

    public void namespaceIdShouldUnique(String namespaceId) {
        namespaceRepository.findByNamespaceId(namespaceId)
                .ifPresent(f -> {
                    throw new BizDuplicatedException("namespaceId `{}` is already exists", namespaceId);
                });
    }

    public void namespaceShouldNotHasAnySwitch(String namespaceId) {
        if (switchInfoRepository.containsSwitch(namespaceId)) {
            throw new BizValidateException("namespaceId `{}` is referenced by switches", namespaceId);
        }
    }

    public Namespace validateOnDelete(String namespaceId) {
        Namespace namespace = namespaceMustExist(namespaceId);
        namespaceShouldNotHasAnySwitch(namespaceId);

        return namespace;
    }
}

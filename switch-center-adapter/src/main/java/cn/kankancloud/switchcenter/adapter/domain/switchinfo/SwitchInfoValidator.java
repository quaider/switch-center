package cn.kankancloud.switchcenter.adapter.domain.switchinfo;

import cn.kankancloud.jbp.core.domain.DomainInjector;
import cn.kankancloud.jbp.core.exception.BizDuplicatedException;
import cn.kankancloud.jbp.core.exception.BizNotFoundException;
import lombok.RequiredArgsConstructor;

@DomainInjector
@RequiredArgsConstructor
public class SwitchInfoValidator {

    private final SwitchInfoRepository switchRepository;

    public void switchMustUnique(String namespaceId, String key) {
        if (switchRepository.isSwitchExist(namespaceId, key)) {
            throw new BizDuplicatedException("switch with key `{}` already exist", key);
        }
    }

    public SwitchInfo parentSwitchMustExist(String namespaceId, String parentKey) {
        return switchRepository.findByNamespaceAndKey(namespaceId, parentKey)
                .orElseThrow(() -> new BizNotFoundException("parent switch with key `{}` not found", parentKey));
    }
}

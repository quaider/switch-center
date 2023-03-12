package com.cdfsunrise.switchcenter.adapter.domain.namespace;

import com.cdfsunrise.smart.framework.core.domain.AggregateRoot;
import com.cdfsunrise.smart.framework.core.exception.BizValidateException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Getter
@EqualsAndHashCode(callSuper = true)
@Builder(setterPrefix = "set", toBuilder = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Namespace extends AggregateRoot<Integer, Namespace> {

    @Setter(AccessLevel.PROTECTED)
    private Integer id;

    private String namespaceId;

    private String namespaceName;

    public Namespace(String namespaceId, String namespaceName) {
        if (StringUtils.isEmpty(namespaceId) || StringUtils.isEmpty(namespaceName)) {
            throw new BizValidateException("`namespaceId` or `namespaceName` required");
        }

        if (namespaceId.length() < 4) {
            throw new BizValidateException("the length of `namespaceId` must greater than 4");
        }

        this.namespaceId = namespaceId;
        this.namespaceName = namespaceName;
    }

    public void changeName(String newName) {
        if (!this.namespaceName.equals(newName)) {
            this.namespaceName = newName;
            this.toUpdate();
        }
    }
}

package com.cdfsunrise.switchcenter.adapter.api;

import com.cdfsunrise.smart.framework.core.PagedData;
import com.cdfsunrise.smart.framework.core.Result;
import com.cdfsunrise.smart.framework.core.query.PageQuery;
import com.cdfsunrise.smart.framework.mbp.query.PageQueryBuilder;
import com.cdfsunrise.smart.framework.web.util.BeanUtil;
import com.cdfsunrise.switchcenter.adapter.application.namespace.NamespaceResponse;
import com.cdfsunrise.switchcenter.adapter.driving.repository.namespace.dao.NamespaceMapper;
import com.cdfsunrise.switchcenter.adapter.driving.repository.namespace.dao.NamespacePo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ns")
@RequiredArgsConstructor
public class NamespaceController {

    private final NamespaceMapper namespaceMapper;

    @GetMapping("/query")
    public Result<PagedData<NamespaceResponse>> listNamespaces(@ModelAttribute PageQuery pageQuery) {

        PagedData<NamespaceResponse> pagedData = PageQueryBuilder.builder(NamespacePo.class, NamespaceResponse.class)
                .mapper(namespaceMapper)
                .pageQuery(pageQuery)
                .projectTo(BeanUtil.copyPropertiesSupplier(NamespaceResponse.class))
                .selectPage();

        return Result.success(pagedData);
    }
}

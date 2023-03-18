package cn.kankancloud.switchcenter.adapter.api;

import cn.kankancloud.jbp.core.PagedData;
import cn.kankancloud.jbp.core.Result;
import cn.kankancloud.jbp.core.query.PageQuery;
import cn.kankancloud.jbp.mbp.query.PageQueryBuilder;
import cn.kankancloud.jbp.web.util.BeanUtil;
import cn.kankancloud.switchcenter.adapter.application.namespace.NamespaceResponse;
import cn.kankancloud.switchcenter.adapter.driving.repository.namespace.dao.NamespaceMapper;
import cn.kankancloud.switchcenter.adapter.driving.repository.namespace.dao.NamespacePo;
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

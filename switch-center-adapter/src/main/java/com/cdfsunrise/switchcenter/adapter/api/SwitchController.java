package com.cdfsunrise.switchcenter.adapter.api;

import com.cdfsunrise.smart.framework.core.PagedData;
import com.cdfsunrise.smart.framework.core.Result;
import com.cdfsunrise.smart.framework.core.query.PageQuery;
import com.cdfsunrise.smart.framework.mbp.query.PageQueryBuilder;
import com.cdfsunrise.smart.framework.web.util.BeanUtil;
import com.cdfsunrise.switchcenter.adapter.application.switchinfo.SwitchResponse;
import com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoMapper;
import com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoPo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/switch")
@RequiredArgsConstructor
public class SwitchController {

    private final SwitchInfoMapper switchInfoMapper;

    @GetMapping("/{namespaceId}/query")
    public Result<PagedData<SwitchResponse>> pageSwitches(@PathVariable String namespaceId, @ModelAttribute PageQuery pageQuery) {

        PagedData<SwitchResponse> pagedData = PageQueryBuilder.builder(SwitchInfoPo.class, SwitchResponse.class)
                .mapper(switchInfoMapper)
                .pageQuery(pageQuery)
                .customizeQuery(qw -> qw.lambda().eq(SwitchInfoPo::getNamespaceId, namespaceId))
                .projectTo(BeanUtil.copyPropertiesSupplier(SwitchResponse.class))
                .selectPage();

        return Result.success(pagedData);
    }
}

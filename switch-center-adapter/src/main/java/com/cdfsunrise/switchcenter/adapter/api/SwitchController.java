package com.cdfsunrise.switchcenter.adapter.api;

import com.cdfsunrise.smart.framework.core.PagedData;
import com.cdfsunrise.smart.framework.core.Result;
import com.cdfsunrise.smart.framework.core.query.PageQuery;
import com.cdfsunrise.smart.framework.mbp.query.PageQueryBuilder;
import com.cdfsunrise.smart.framework.web.util.BeanUtil;
import com.cdfsunrise.switchcenter.adapter.application.switchinfo.SwitchInfoResponse;
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
    public Result<PagedData<SwitchInfoResponse>> pageSwitches(@PathVariable String namespaceId, @ModelAttribute PageQuery pageQuery) {

        PagedData<SwitchInfoResponse> pagedData = PageQueryBuilder.builder(SwitchInfoPo.class, SwitchInfoResponse.class)
                .mapper(switchInfoMapper)
                .pageQuery(pageQuery)
                .customizeQuery(qw -> qw.lambda().eq(SwitchInfoPo::getNamespaceId, namespaceId))
                .projectTo(BeanUtil.copyPropertiesSupplier(SwitchInfoResponse.class))
                .selectPage();

        return Result.success(pagedData);
    }
}

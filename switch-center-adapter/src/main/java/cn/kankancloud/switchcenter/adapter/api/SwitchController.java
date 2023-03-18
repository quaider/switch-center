package cn.kankancloud.switchcenter.adapter.api;

import cn.kankancloud.jbp.core.PagedData;
import cn.kankancloud.jbp.core.Result;
import cn.kankancloud.jbp.core.query.PageQuery;
import cn.kankancloud.jbp.mbp.query.PageQueryBuilder;
import cn.kankancloud.jbp.web.util.BeanUtil;
import cn.kankancloud.switchcenter.adapter.application.switchinfo.SwitchInfoResponse;
import cn.kankancloud.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoMapper;
import cn.kankancloud.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoPo;
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

package com.cdfsunrise.switchcenter.adapter.openapi;

import com.cdfsunrise.smart.framework.core.Result;
import com.cdfsunrise.switchcenter.adapter.application.switchinfo.*;
import com.cdfsunrise.switchcenter.adapter.driving.cache.SwitchCacheKey;
import com.cdfsunrise.switchcenter.adapter.driving.cache.SwitchCacheManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "switch")
@RestController
@RequestMapping("/openapi/switch")
@RequiredArgsConstructor
public class OpenApiSwitchInfoController {

    private final SwitchInfoService switchInfoService;

    private final SwitchInfoAssembler switchInfoAssembler;

    private final SwitchCacheManager cacheManager;

    @Operation(summary = "普通body请求")
    @Parameter(name = "namespaceId", description = "命名空间", required = true)
    @Parameter(name = "key", description = "开关键", required = true)
    @GetMapping(value = "/{namespaceId}/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<SwitchInfoValueResponse> getSwitchInfoValue(@PathVariable String namespaceId, @PathVariable String key) {

        return cacheManager.getSwitch(new SwitchCacheKey(namespaceId, key))
                .map(f -> Result.success(switchInfoAssembler.toSwitchInfoValueResponse(f)))
                .orElse(Result.notFound());
    }

    @PutMapping("/{namespaceId}")
    public Result<Void> addParentSwitch(@PathVariable String namespaceId, @Valid @RequestBody SwitchInfoCmd.Add cmd) {
        SwitchInfoCmd switchInfoCmd = new SwitchInfoCmd(cmd, namespaceId);
        switchInfoService.addSwitch(switchInfoCmd);

        return Result.success();
    }

    @PutMapping("/{namespaceId}/{parentKey}")
    public Result<Void> addChildSwitch(@PathVariable String namespaceId, @PathVariable String parentKey, @Valid @RequestBody SwitchInfoCmd.Add cmd) {
        SwitchInfoCmd switchInfoCmd = new SwitchInfoCmd(cmd, namespaceId, parentKey);
        switchInfoService.addSwitch(switchInfoCmd);

        return Result.success();
    }

    @PostMapping("/{namespaceId}/{key}")
    public Result<Void> changeStatus(@PathVariable String namespaceId, @PathVariable String key, @RequestBody @Valid SwitchInfoStatusChangeCmd cmd) {
        switchInfoService.changeStatus(namespaceId, key, cmd);

        return Result.success();
    }

}

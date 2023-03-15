package com.cdfsunrise.switchcenter.adapter.openapi;

import com.cdfsunrise.smart.framework.core.Result;
import com.cdfsunrise.switchcenter.adapter.application.akka.AkkaServerEnvironment;
import com.cdfsunrise.switchcenter.adapter.application.akka.CacheEvictionMessage;
import com.cdfsunrise.switchcenter.adapter.application.switchinfo.*;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "开关配置", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping("/openapi/switch")
@RequiredArgsConstructor
public class OpenApiSwitchInfoController {

    private final SwitchInfoService switchInfoService;
    private final SwitchInfoRepository switchInfoRepository;

    private final SwitchInfoAssembler switchInfoAssembler;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespaceId", value = "命名空间", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "key", value = "开关键", dataTypeClass = String.class, required = true)
    })
    @GetMapping(value = "/{namespaceId}/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<SwitchInfoValueResponse> getSwitchInfoValue(@PathVariable String namespaceId, @PathVariable String key) {
        SwitchInfoValueResponse payload = switchInfoRepository.findByNamespaceAndKey(namespaceId, key)
                .map(switchInfoAssembler::toSwitchInfoValueResponse)
                .orElse(null);

        return Result.success(payload);
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

        AkkaServerEnvironment.getEnv().getPublisher().tell(new CacheEvictionMessage(namespaceId, key), null);

        return Result.success();
    }

}

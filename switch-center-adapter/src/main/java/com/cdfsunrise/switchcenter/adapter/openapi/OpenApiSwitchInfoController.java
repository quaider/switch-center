package com.cdfsunrise.switchcenter.adapter.openapi;

import com.cdfsunrise.smart.framework.core.Result;
import com.cdfsunrise.switchcenter.adapter.application.switchinfo.SwitchInfoCmd;
import com.cdfsunrise.switchcenter.adapter.application.switchinfo.SwitchInfoService;
import com.cdfsunrise.switchcenter.adapter.application.switchinfo.SwitchInfoStatusChangeCmd;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "开关配置", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping("/switch")
@RequiredArgsConstructor
public class OpenApiSwitchInfoController {

    private final SwitchInfoService switchInfoService;

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

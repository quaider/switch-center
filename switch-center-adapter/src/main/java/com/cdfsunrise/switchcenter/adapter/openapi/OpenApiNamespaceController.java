package com.cdfsunrise.switchcenter.adapter.openapi;

import com.cdfsunrise.smart.framework.core.Result;
import com.cdfsunrise.switchcenter.adapter.application.namespace.NamespaceCreateCmd;
import com.cdfsunrise.switchcenter.adapter.application.namespace.NamespaceNameChangeCmd;
import com.cdfsunrise.switchcenter.adapter.application.namespace.NamespaceResponse;
import com.cdfsunrise.switchcenter.adapter.application.namespace.NamespaceService;
import com.cdfsunrise.switchcenter.adapter.domain.namespace.Namespace;
import com.cdfsunrise.switchcenter.adapter.domain.namespace.NamespaceValidator;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "namespace openApi")
@RestController
@RequestMapping("/openapi/ns")
@RequiredArgsConstructor
public class OpenApiNamespaceController {

    private final NamespaceService namespaceService;
    private final NamespaceValidator namespaceValidator;

    @PutMapping("")
    public Result<Void> addNamespace(@Valid NamespaceCreateCmd cmd) {
        namespaceService.addNamespace(cmd);

        return Result.success();
    }

    @PostMapping("/{namespaceId}")
    public Result<Void> changeName(@PathVariable String namespaceId, @RequestBody @Valid NamespaceNameChangeCmd cmd) {
        namespaceService.changeName(namespaceId, cmd.getNewName());

        return Result.success();
    }

    @DeleteMapping("/{namespaceId}")
    public Result<Void> deleteNamespace(@PathVariable String namespaceId) {
        namespaceService.delete(namespaceId);

        return Result.success();
    }

    @GetMapping("/{namespaceId}")
    public Result<NamespaceResponse> getNamespace(@PathVariable String namespaceId) {
        Namespace namespace = namespaceValidator.namespaceMustExist(namespaceId);
        NamespaceResponse response = new NamespaceResponse();
        BeanUtils.copyProperties(namespace, response);

        return Result.success(response);
    }

}

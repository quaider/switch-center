package cn.kankancloud.switchcenter.adapter.openapi;

import cn.kankancloud.jbp.core.Result;
import cn.kankancloud.switchcenter.adapter.application.namespace.NamespaceNameChangeCmd;
import cn.kankancloud.switchcenter.adapter.application.namespace.NamespaceResponse;
import cn.kankancloud.switchcenter.adapter.domain.namespace.Namespace;
import cn.kankancloud.switchcenter.adapter.domain.namespace.NamespaceValidator;
import cn.kankancloud.switchcenter.adapter.application.namespace.NamespaceCreateCmd;
import cn.kankancloud.switchcenter.adapter.application.namespace.NamespaceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "namespace")
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

package cn.kankancloud.switchcenter.adapter.application.namespace;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class NamespaceCreateCmd implements Serializable {
    private static final long serialVersionUID = -1350712004641299577L;

    @NotEmpty
    @Pattern(regexp = "[\\w+.?]+", message = "必须是 字母.字母 的格式")
    private String namespaceId;

    @NotEmpty
    private String namespaceName;
}

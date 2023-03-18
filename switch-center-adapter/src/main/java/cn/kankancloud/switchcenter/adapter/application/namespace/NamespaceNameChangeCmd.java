package cn.kankancloud.switchcenter.adapter.application.namespace;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class NamespaceNameChangeCmd implements Serializable {

    private static final long serialVersionUID = 8858173516515088587L;

    @NotEmpty
    private String newName;
}

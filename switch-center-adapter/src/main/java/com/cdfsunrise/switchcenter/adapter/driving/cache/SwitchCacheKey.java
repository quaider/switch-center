package com.cdfsunrise.switchcenter.adapter.driving.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SwitchCacheKey implements Serializable {
    private static final long serialVersionUID = 933964211066654946L;
    private String namespace;
    private String key;
}

package com.cdfsunrise.switchcenter.adapter.driving.cache;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode
public class SwitchCacheKey {
    String namespace;
    String key;
}

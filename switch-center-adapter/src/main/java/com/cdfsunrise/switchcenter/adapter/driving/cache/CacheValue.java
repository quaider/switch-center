package com.cdfsunrise.switchcenter.adapter.driving.cache;

import lombok.Getter;

@Getter
public class CacheValue<T> {

    private final T value;

    private final boolean fakeValue;

    public CacheValue(T value, boolean fakeValue) {
        this.value = value;
        this.fakeValue = fakeValue;
    }

}

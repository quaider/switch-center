package cn.kankancloud.switchcenter.adapter.application.akka.codec;

import akka.serialization.JSerializer;

public class AkkaSerializer extends JSerializer {
    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {
        return SerializerUtils.deSerialized(bytes);
    }

    @Override
    public int identifier() {
        return 23456;
    }

    @Override
    public byte[] toBinary(Object o) {
        return SerializerUtils.serialize(o);
    }

    @Override
    public boolean includeManifest() {
        return false;
    }
}

package com.cdfsunrise.switchcenter.adapter.application.akka.codec;


import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.serializers.CompatibleFieldSerializer;

/**
 * 序列化器
 */
public class SerializerUtils {

    private SerializerUtils() {
    }

    //每个线程的 Kryo 实例
    private static final ThreadLocal<Kryo> kryoLocal = ThreadLocal.withInitial(() -> {

        Kryo kryo = new Kryo();
        // 支持对象循环引用（否则会栈溢出），会导致性能些许下降
        kryo.setReferences(true);
        // 关闭序列化注册，会导致性能些许下降，但在分布式环境中，注册类生成ID不一致会导致错误
        kryo.setRegistrationRequired(false);
        // 支持删除或者新增字段
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        // 设置类加载器为线程上下文类加载器（如果Processor来源于容器，必须使用容器的类加载器，否则妥妥的CNF）
        kryo.setClassLoader(Thread.currentThread().getContextClassLoader());

        return kryo;
    });

    public static byte[] serialize(Object obj) {

        Kryo kryo = kryoLocal.get();

        // 使用 Output 对象池会导致序列化重复的错误（getBuffer返回了Output对象的buffer引用）
        try (Output opt = new Output(1024, -1)) {
            kryo.writeClassAndObject(opt, obj);
            opt.flush();
            return opt.getBuffer();
        }
    }

    public static Object deSerialized(byte[] buffer) {
        Kryo kryo = kryoLocal.get();
        return kryo.readClassAndObject(new Input(buffer));
    }

}

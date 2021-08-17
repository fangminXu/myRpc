package com.kiro.rpc.serializer;

import com.kiro.rpc.enumeration.SerializerCode;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Xufangmin
 * @create 2021-08-17-21:40
 */
public class ProtobufSerializer implements CommonSerializer{
    private LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    private Map<Class<?>, Schema<?>> schemaMap = new ConcurrentHashMap<>();

    @Override
    public byte[] serialize(Object obj) {
        Class clazz = obj.getClass();
        Schema schema = getSchema(clazz);
        byte[] data;
        try{
            data = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
        return data;
    }

    private Schema getSchema(Class clazz) {
        Schema schema = schemaMap.get(clazz);
        // 这个schema通过RuntimeSchema进行懒创建并缓存
        // 所以可以一直调用RuntimeSchema.getSchema(),这个方法是线程安全的
        if(Objects.isNull(schema)){
            schema = RuntimeSchema.getSchema(clazz);
            if(Objects.nonNull(schema)){
                schemaMap.put(clazz, schema);
            }
        }
        return schema;
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        Schema schema = getSchema(clazz);
        Object obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("PROTOBUF").getCode();
    }
}

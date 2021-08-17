package com.kiro.rpc.serializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiro.rpc.entity.RpcRequest;
import com.kiro.rpc.enumeration.SerializerCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 使用JSON格式的序列化器
 * @author Xufangmin
 * @create 2021-08-16-11:07
 */
public class JsonSerializer implements CommonSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSerializer.class);

    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("序列化时有错误发生:{}", e.getMessage());
        }
        return null;

    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            Object obj = objectMapper.readValue(bytes, clazz);
            if(obj instanceof RpcRequest){
                obj = hanleRequest(obj);
            }
            return obj;
        } catch (IOException e) {
            LOGGER.error("反序列化时有错误发生:{}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }


    private Object hanleRequest(Object obj) throws IOException {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for (int i = 0; i < rpcRequest.getParamsType().length; i++) {
            Class<?> clazz = rpcRequest.getParamsType()[i];
            if(!clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())){
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
                rpcRequest.getParameters()[i] = objectMapper.readValue(bytes, clazz);
            }
        }
        return rpcRequest;
    }
}

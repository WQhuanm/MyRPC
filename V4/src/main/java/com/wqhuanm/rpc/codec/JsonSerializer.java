package com.wqhuanm.rpc.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wqhuanm.rpc.common.Request;
import com.wqhuanm.rpc.common.Response;

public class JsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = JSONObject.toJSONBytes(obj);
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj;
        switch (messageType) {
            case 0:
                Request request = JSON.parseObject(bytes, Request.class);
                if (request.getParams() == null) return request;

                Object[] objects = new Object[request.getParams().length];
                for (int i = 0; i < objects.length; ++i) {
                    Class<?> paramsType = request.getParamTypes()[i];
                    if (!paramsType.isAssignableFrom(request.getParams()[i].getClass())) {
                        objects[i] = JSONObject.toJavaObject((JSONObject) request.getParams()[i], request.getParamTypes()[i]);
                    } else objects[i] = request.getParams()[i];
                }
                request.setParams(objects);
                obj = request;
                break;
            case 1:
                Response response = JSON.parseObject(bytes, Response.class);
                Class<?> type = response.getDataType();
                if (!type.isAssignableFrom(response.getData().getClass())) {
                    response.setData(JSONObject.toJavaObject((JSONObject) response.getData(), type));
                }
                obj = response;
                break;
            default:
                System.out.println("暂时不支持此种消息");
                throw new RuntimeException();
        }
        return obj;
    }

    @Override
    public int getType() {
        return 0;
    }
}

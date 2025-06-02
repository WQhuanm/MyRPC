package com.wqhuanm.rpc.codec;

public interface Serializer {
    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, int messageType);

    int getType();

    static Serializer getSerializerByCode(int code) {
        switch (code) {
            case 0:
                return new JsonSerializer();
            default:
                return null;
        }
    }


}

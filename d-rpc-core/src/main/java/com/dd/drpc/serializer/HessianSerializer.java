package com.dd.drpc.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class HessianSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(outputStream);
        hessianOutput.writeObject(obj);
        return outputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        HessianInput hessianInput = new HessianInput(inputStream);
        Object result = hessianInput.readObject(clazz);
        return (T) result;
    }
}

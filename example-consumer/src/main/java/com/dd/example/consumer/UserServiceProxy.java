// 静态代理不推荐使用，因为要给每一个服务都写一个实现类，不推荐使用

//package com.dd.example.consumer;
//
//import cn.hutool.http.HttpRequest;
//import cn.hutool.http.HttpResponse;
//import com.dd.drpc.model.RpcRequest;
//import com.dd.drpc.model.RpcResponse;
//import com.dd.drpc.serializer.JdkSerializaer;
//import com.dd.drpc.serializer.Serializer;
//import com.dd.example.common.model.User;
//import com.dd.example.common.service.UserService;
//
///**
// * 服务代理（静态代理）
// */
//public class UserServiceProxy implements UserService {
//
//    @Override
//    public User getUser(User user) {
//        Serializer serializer = new JdkSerializaer();
//
//        RpcRequest rpcRequest = RpcRequest.builder()
//                .serviceName(UserService.class.getName())
//                .methodName("getUser")
//                .parameters(new Object[] {user})
//                .parameterTypes(new Class[] {User.class})
//                .build();
//        try {
//            byte[] serialize = serializer.serialize(rpcRequest);
//            HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
//                    .body(serialize)
//                    .execute();
//            byte[] result = httpResponse.bodyBytes();
//            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//            return (User) rpcResponse.getData();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//}

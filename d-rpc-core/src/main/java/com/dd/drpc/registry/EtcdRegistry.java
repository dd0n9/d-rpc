package com.dd.drpc.registry;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Etcd注册中心
 */
public class EtcdRegistry {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建客户端
        System.out.println("creating etcd registry...");
        System.out.println("creating etcd clinet...");
        Client client = Client.builder().endpoints("http://localhost:2379").build();

        System.out.println("creating kvClinet...");
        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.from("test_key".getBytes());
        ByteSequence value = ByteSequence.from("test_value".getBytes());

        System.out.println("putting key-value...");
        kvClient.put(key, value).get();

        CompletableFuture<GetResponse> getFuture = kvClient.get(key);
        System.out.println("getting value...");
        GetResponse getResponse = getFuture.get();
        System.out.println(getResponse.toString());

        System.out.println("deleting key-value...");
        kvClient.delete(key).get();

    }

}

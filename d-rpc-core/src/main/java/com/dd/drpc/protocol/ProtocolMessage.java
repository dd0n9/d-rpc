package com.dd.drpc.protocol;

import cn.hutool.http.Header;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 协议消息结构
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {

    // TODO 自定义协议

    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体
     */
    private T body;

    /**
     * 协议消息体
     */
    @Data
    public static class Header {

        /**
         * 魔数
         */
        private byte magic;

        /**
         * 版本号
         */
        private byte version;

        /**
         * 序列化器
         */
        private byte serializer;

        /**
         * 类型
         */
        private byte type;

        /**
         * 状态
         */
        private byte status;

        /**
         * 请求Id
         */
        private long requestId;

        /**
         * 消息体长度
         */
        private int bodyLength;
    }
}

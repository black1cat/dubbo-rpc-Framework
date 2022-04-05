package com.zzj.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zongzhaojin
 * @date 2022/4/4 17:06
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {
    JSON(1);

    private final int code;
}

package com.zzj.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zongzhaojin
 * @date 2022/4/5 16:15
 */
@AllArgsConstructor
@Getter
public enum  PackageType {
    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;
}

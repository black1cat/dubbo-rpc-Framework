# dubbo-rpc-Framework

根据声哥的教程写的rpc-demo,声哥yyds! dubbo-rpc-Framework 是一款基于 Nacos 实现的 RPC 框架。网络传输实现了基于 Java 原生 Socket 与 Netty 版本，并且实现了多种序列化与负载均衡算法。
![image](https://user-images.githubusercontent.com/45026538/162196399-a0810f14-93cd-477b-b2b8-ce5be6f04376.png)

## 项目模块概览
. roc-api —— 通用接口
. rpc-common —— 实体对象、工具类等公用类
. rpc-core —— 框架的核心实现
. rpc-client —— 测试用消费侧
. rpc-server —— 测试用提供侧

## 传输协议（DRF协议）

```
+---------------+---------------+-----------------+-------------+
|  Magic Number |  Package Type | Serializer Type | Data Length |
|    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
+---------------+---------------+-----------------+-------------+
|                          Data Bytes                           |
|                   Length: ${Data Length}                      |
+---------------------------------------------------------------+
```

![image](https://user-images.githubusercontent.com/45026538/162197199-08c0c411-9506-4be0-b4cc-b3b8292774f1.png)

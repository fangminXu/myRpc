package com.kiro.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @author Xufangmin
 * @create 2021-08-19-17:22
 */
public interface ServiceDiscovery {

    /**
     * 根据服务名称查找服务实体
     */
    InetSocketAddress lookupService(String serviceName);
}

package com.kiro.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kiro.rpc.enumeration.RpcError;
import com.kiro.rpc.exception.RpcException;
import com.kiro.rpc.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Nacos注册服务中心
 * @author Xufangmin
 * @create 2021-08-18-10:04
 */
public class NacosServiceRegistry implements ServiceRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(NacosServiceRegistry.class);
    private final NamingService namingService;

    public NacosServiceRegistry() {
        this.namingService = NacosUtil.getNacosNamingService();
    }

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(namingService, serviceName, inetSocketAddress);
        } catch (NacosException e) {
            LOGGER.error("注册服务时有错误发生：", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }
}

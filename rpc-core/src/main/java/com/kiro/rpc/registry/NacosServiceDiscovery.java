package com.kiro.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kiro.rpc.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Xufangmin
 * @create 2021-08-18-11:48
 */
public class NacosServiceDiscovery implements ServiceDiscovery {
    private static final Logger LOGGER = LoggerFactory.getLogger(NacosServiceDiscovery.class);
    private final NamingService namingService;

    public NacosServiceDiscovery(){
        namingService = NacosUtil.getNacosNamingService();
    }
    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = NacosUtil.getAllInstance(namingService, serviceName);
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            LOGGER.error("获取服务时有错误发生：", e);
        }
        return null;
    }
}

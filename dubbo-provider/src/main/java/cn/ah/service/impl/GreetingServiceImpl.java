package cn.ah.service.impl;

import cn.ah.service.GreetingService;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.rpc.RpcContext;

import java.io.IOException;

/**
 * @author linzehao
 * create on 2020-11-28 20:04
 */
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String sayHello(String name) {
        // 获取隐式参数
        String message = RpcContext.getContext().getAttachment("message");

        System.out.println("=================  Dubbo服务被调用  ==================");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(name);
        return "欢迎光临，" + name + ", " + message;
    }

    public static void main(String[] args) throws IOException {
        // 创建服务提供者配置实例
        ServiceConfig<GreetingService> serviceConfig = new ServiceConfig<GreetingService>();

        // 配置应用
        serviceConfig.setApplication(new ApplicationConfig("greeting-dubbo-provider"));

        // 设置注册中心
        serviceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));

        // 设置接口和实现类
        serviceConfig.setInterface(GreetingService.class);
        serviceConfig.setRef(new GreetingServiceImpl());

        // 设置服务分组和版本
        serviceConfig.setGroup("dubbo");
        serviceConfig.setVersion("1.0.0");

        // 导出服务
        System.out.println("导出服务");
        serviceConfig.export();

        // 阻塞
        System.out.println("server is started");
        System.in.read();
    }

}

package cn.ah.consumer;

import cn.ah.service.GreetingService;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.RpcContext;

/**
 * @author linzehao
 * create on 2020-11-28 20:27
 */
public class GreetingConsumer {

    public static void main(String[] args) {

        // 创建消费者配置实例
        ReferenceConfig<GreetingService> refConfig = new ReferenceConfig<GreetingService>();
        refConfig.setApplication(new ApplicationConfig("greet-dubbo-consumer"));

        // 设置注册中心
        refConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));

        // 设置版本号
        refConfig.setGroup("dubbo");
        refConfig.setVersion("1.0.0");

        // 设置服务
        refConfig.setInterface(GreetingService.class);

        // 设置超时时间
        refConfig.setTimeout(10000);

        // 引用服务
        GreetingService greetingService = refConfig.get();

        // 设置隐式参数
        RpcContext.getContext().setAttachment("message", "下午好");

        // 调用服务
        System.out.println(greetingService.sayHello("张三"));

    }
}

package cn.ah.consumer;

import cn.ah.service.AsyncGreetingService;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.RpcContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 演示异步消费服务
 * @author linzehao
 * create on 2020-11-29 11:28
 */
public class AsyncGreetingConsumer {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ReferenceConfig<AsyncGreetingService> referenceConfig = new ReferenceConfig<AsyncGreetingService>();

        referenceConfig.setApplication(new ApplicationConfig("dubbo-async-consumer"));
        referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        referenceConfig.setGroup("dubbo-async");
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setTimeout(10000);
        referenceConfig.setInterface(AsyncGreetingService.class);

        // 设置异步调用服务
        referenceConfig.setAsync(true);

        AsyncGreetingService asyncGreetingService = referenceConfig.get();

        // 调用服务
        CompletableFuture<String> future = asyncGreetingService.sayHello("李四");

        // 设置回调方法
        future.whenComplete( (v, t) -> {
            if (t != null) {
                t.printStackTrace();
            }else {
                System.out.println(v);
            }
        });

        System.out.println("over");

    }


}

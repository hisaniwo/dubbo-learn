package cn.ah.service.impl;

import cn.ah.service.AsyncGreetingService;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.rpc.RpcContext;
import org.apache.dubbo.common.utils.NamedThreadFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * 演示异步提供服务，服务需要返回CompletableFuture实例
 * @author linzehao
 * create on 2020-11-29 11:22
 */
public class AsyncGreetingServiceImpl implements AsyncGreetingService {

    private final ThreadPoolExecutor bizThreadPool = new ThreadPoolExecutor(8, 16, 1, TimeUnit.MINUTES,
            new SynchronousQueue<>(), new NamedThreadFactory("biz-thread-pool"), new ThreadPoolExecutor.CallerRunsPolicy());


    @Override
    public CompletableFuture<String> sayHello(String name) {
        String message = RpcContext.getContext().getAttachment("message");

        System.out.println("=================  Dubbo服务被调用  ==================");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(name);

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("服务返回");
            return "欢迎光临，" + name + ", " + message;
        }, bizThreadPool);

    }


    public static void main(String[] args) throws IOException {
        ServiceConfig<AsyncGreetingService> serviceConfig = new ServiceConfig<AsyncGreetingService>();

        serviceConfig.setApplication(new ApplicationConfig("dubbo-async-provider"));

        serviceConfig.setVersion("1.0.0");
        serviceConfig.setGroup("dubbo-async");

        serviceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));

        serviceConfig.setInterface(AsyncGreetingService.class);
        serviceConfig.setRef(new AsyncGreetingServiceImpl());

        // 导出服务
        System.out.println("导出服务");
        serviceConfig.export();

        // 阻塞线程
        System.out.println("server is started");
        System.in.read();
    }

}

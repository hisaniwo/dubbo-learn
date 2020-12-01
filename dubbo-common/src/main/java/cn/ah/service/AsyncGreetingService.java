package cn.ah.service;

import java.util.concurrent.CompletableFuture;

/**
 * @author ah
 * create on 2020-11-28 20:03
 * Dubbo异步服务
 */
public interface AsyncGreetingService {

    CompletableFuture<String> sayHello(String name);

}

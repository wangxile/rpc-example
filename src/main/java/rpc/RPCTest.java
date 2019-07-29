package rpc;

import java.net.InetSocketAddress;

/**
 * @Author:wangqi
 * @Description:
 * @Date:Created in 2019/7/29
 * @Modified by:
 */
public class RPCTest {
    public static void main(String[] args) {
        DemoService demoService = (DemoService) RPCClient.rpc(DemoService.class, new InetSocketAddress("localhost", 8080));
        System.out.println(demoService.sayHello("wangxile"));
    }
}

package rpc;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author:wangqi
 * @Description:
 * @Date:Created in 2019/7/29
 * @Modified by:
 */
public class RPCServer implements Registry{

    public static final HashMap<String, Class> SERVICE_REGISTRY_MAP = new HashMap<>();

    //创建线程池
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //服务器运行标识
    private static boolean isRunning = false;

    //对外服务端口
    private int port;

    public RPCServer(int port){
        this.port = port;
    }

    //启动服务器
    public void start() throws Exception{
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        System.out.println("启动服务器。。。");
        try{
            while (true){
                executorService.execute(new ServiceTask(serverSocket.accept()));
            }
        }finally {
            serverSocket.close();
        }
    }

    @Override
    public void register(Class service, Class clazz) {
        System.out.println(service.getName());
        SERVICE_REGISTRY_MAP.put(service.getName(), clazz);
    }

    public static void main(String[] args) throws Exception{
        RPCServer server = new RPCServer(8080);
        server.register(DemoService.class, DemoServiceImpl.class);
        server.start();

    }

}

package rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Author:wangqi
 * @Description:
 * @Date:Created in 2019/7/29
 * @Modified by:
 */
public class RPCClient {
    public static Object rpc(final Class clazz, final InetSocketAddress address){
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //建立连接
                Socket socket = new Socket();
                socket.connect(address);

                String serviceName = clazz.getName();
                String methodName = method.getName();
                Class[] parameterTypes = method.getParameterTypes();

                //将参数写入流中
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeUTF(serviceName);
                objectOutputStream.writeUTF(methodName);
                objectOutputStream.writeObject(parameterTypes);
                objectOutputStream.writeObject(args);
                objectOutputStream.flush();

                //获取调用方的返回值
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Object o = objectInputStream.readObject();

                //释放资源
                objectInputStream.close();
                objectOutputStream.close();
                socket.close();

                return o;
            }
        });
    }
}

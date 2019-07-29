package rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @Author:wangqi
 * @Description:
 * @Date:Created in 2019/7/29
 * @Modified by:
 */
public class ServiceTask implements Runnable{
    Socket client = null;

    public ServiceTask(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try{
            objectInputStream = new ObjectInputStream(client.getInputStream());

            //读取流中的值
            String serviceName = objectInputStream.readUTF();
            String methodName = objectInputStream.readUTF();
            Class[] paramterTypes = (Class[]) objectInputStream.readObject();
            Object[] arguments = (Object[]) objectInputStream.readObject();

            //根据apiClassName 找到对应的实现类  找到对应的方法执行
            Class serviceClass = RPCServer.SERVICE_REGISTRY_MAP.get(serviceName);
            if(serviceClass == null){
                throw new ClassNotFoundException(serviceName + " not found");
            }
            Method method = serviceClass.getMethod(methodName, paramterTypes);
            Object result = method.invoke(serviceClass.newInstance(), arguments);

            //写入方法执行结果
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();

            //释放资源
            objectInputStream.close();
            objectOutputStream.close();
            client.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

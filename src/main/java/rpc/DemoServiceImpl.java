package rpc;

/**
 * @Author:wangqi
 * @Description:
 * @Date:Created in 2019/7/29
 * @Modified by:
 */
public class DemoServiceImpl implements DemoService{
    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}

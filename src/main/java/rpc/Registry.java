package rpc;

/**
 * @Author:wangqi
 * @Description:
 * @Date:Created in 2019/7/29
 * @Modified by:
 */
public interface Registry {

    void register(Class service, Class clazz);
}

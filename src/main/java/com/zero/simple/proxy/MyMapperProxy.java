package com.zero.simple.proxy;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author zero
 * @description MyMapperProxy
 * @date 2022/3/21 13:54
 */
public class MyMapperProxy<T> implements InvocationHandler {
    private Class<T> mapperInterface;
    private SqlSession sqlSession;

    public MyMapperProxy(Class<T> mapperInterface, SqlSession sqlSession) {
        this.mapperInterface = mapperInterface;
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(mapperInterface.getCanonicalName() + "." + method.getName());
        List<T> list = sqlSession.selectList(mapperInterface.getCanonicalName() + "." + method.getName());
        return list;
    }
}

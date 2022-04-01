package com.zero.simple;

import com.zero.simple.plugins.SimpleInterceptor;
import com.zero.simple.mapper.CountryMapper;
import com.zero.simple.model.Country;
import com.zero.simple.proxy.MyMapperProxy;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author zero
 * @description CountryMapperTest
 * @date 2022/3/21 9:28
 */
public class CountryMapperTest {
    private static SqlSessionFactory sqlSessionFactory;

    @BeforeClass
    public static void init() {
        try {
            Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelectAll() {
        // 1.打开session
        SqlSession session= sqlSessionFactory.openSession();
        try {
            List<Country> countryList = session.selectList("selectAll");
            printCountryList(countryList);
        } finally {
            session.close();
        }
    }

    @Test
    public void testSelect() {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        SimpleInterceptor interceptor = new SimpleInterceptor("Zero");
        configuration.addInterceptor(interceptor);
        // 1.打开session
        SqlSession session= sqlSessionFactory.openSession();
        try {
            // 获取mapper接口对应的代理工厂，由于之前解析配置已经将mapper文件和接口类型做了一一对应，
            // 故在configuration对象中的MapperRegistry中就能找到对应关系。
            CountryMapper mapper = session.getMapper(CountryMapper.class);
            List<Country> countryList = mapper.selectAll();
            printCountryList(countryList);
        } finally {
            session.close();
        }
    }

    @Test
    public void testProxy() {
        // 1.打开session
        SqlSession session= sqlSessionFactory.openSession();
        MyMapperProxy countryMapperProxy = new MyMapperProxy(CountryMapper.class, session);
        try {
            CountryMapper countryMapper = (CountryMapper) Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class[]{CountryMapper.class},
                    countryMapperProxy
            );
            List<Country> countryList = countryMapper.selectAll();
            printCountryList(countryList);
        } finally {
            session.close();
        }
    }

    private void printCountryList(List<Country> countryList) {
        for (Country country : countryList) {
            System.out.printf("%-4d%4s%4s\n",country.getId(), country.getCountryName(), country.getCountryCode());
        }
    }
}

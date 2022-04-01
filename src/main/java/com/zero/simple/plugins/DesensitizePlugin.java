package com.zero.simple.plugins;

import com.zero.simple.annotation.Desensitize;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

/**
 * @author zero
 * @description DesensitizePlugin
 * @date 2022/4/1 10:26
 */
@Intercepts(
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = Statement.class)
)
public class DesensitizePlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 1、获取查询结果
        List<Object> records = (List<Object>) invocation.proceed();
        records.forEach(this::desensitize);
        return records;
    }

    private void desensitize(Object source) {
        // 2、获取数据的类型
        Class<?> sourceClass = source.getClass();
        // 3、包装数据
        MetaObject metaObject = SystemMetaObject.forObject(source);
        Arrays.stream(sourceClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Desensitize.class))
                .forEach(field -> doDesensitize(metaObject, field));

    }

    private void doDesensitize(MetaObject metaObject, Field field) {
        // 4、获取属性名
        String name = field.getName();
        // 5、获取属性值
        Object value = metaObject.getValue(name);
        if (String.class == metaObject.getGetterType(name) && value != null) {
            Desensitize desensitize = field.getAnnotation(Desensitize.class);
            DesensitizeStrategy strategy = desensitize.strategy();
            // 6、处理属性值
            String newString = strategy.getDesensitizer().apply((String) value);
            // 7、重新给属性复制
            metaObject.setValue(name, newString);
        }
    }
}

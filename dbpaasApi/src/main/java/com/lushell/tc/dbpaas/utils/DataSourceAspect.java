/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.utils;

import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 *
 * @author tangchao
 */
public class DataSourceAspect {

    public void before(JoinPoint point)
    {
        Object target = point.getTarget();
        String method = point.getSignature().getName();

        Class<?>[] classz = target.getClass().getInterfaces();

        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())
                .getMethod().getParameterTypes();
        try {
            Method m = classz[0].getMethod(method, parameterTypes);
            if (m != null && m.isAnnotationPresent(DataSource.class)) {
                DataSource data = m
                        .getAnnotation(DataSource.class);
                DataSourceContextHolder.setCurrentDataSourceType(data.name());
            }
            
        } catch (NoSuchMethodException e) {
            // TODO: handle exception
            System.out.println("NoSuchMethodException " + e.getMessage());
        } catch (SecurityException e) {
            // TODO: handle exception
            System.out.println("SecurityException " + e.getMessage());
        }
    }
}


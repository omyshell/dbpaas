package com.lushell.tc.dbpaas.utils;
 
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String name() default DataSource.RS;

    public final static String RS	= "resourcePoolDataSource";
    public final static String CM           = "clusterManagerDataSource";
 
}
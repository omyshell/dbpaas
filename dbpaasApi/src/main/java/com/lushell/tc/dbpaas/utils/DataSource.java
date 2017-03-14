package com.lushell.tc.dbpaas.utils;
 
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String name() default DataSource.TEST_CASE;

    public final static String TEST_CASE	= "tcDataSource";
    public final static String DBMETA           = "dbmetaDataSource";
 
}
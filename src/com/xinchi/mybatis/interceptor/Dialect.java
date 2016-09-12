package com.xinchi.mybatis.interceptor;

import com.xinchi.tools.Page;

public abstract class Dialect {  
	  
    public static enum Type{  
        MYSQL,  
        ORACLE,
        SQLSERVER
    }  
      
    public abstract String getLimitString(String sql, Page<?> page);  
      
} 

package com.kts.unity.config.web.utils;

import org.springframework.context.ApplicationContext;

public class AppContext {
    
    private static ApplicationContext context;

    public static ApplicationContext getContext() {
        return context;
    }

    public static void setContext(ApplicationContext context) {
        AppContext.context = context;
    }
    
}

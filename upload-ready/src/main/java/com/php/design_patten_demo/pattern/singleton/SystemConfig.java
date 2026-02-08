package com.php.design_patten_demo.pattern.singleton;

import java.util.HashMap;
import java.util.Map;

public class SystemConfig {
    private static volatile SystemConfig instance;
    private final Map<String, String> config;

    private SystemConfig() {
        config = new HashMap<>();
        config.put("system.name", "图书商城管理系统");
        config.put("system.version", "1.0.0");
        config.put("approval.manager.limit", "500");
    }

    public static SystemConfig getInstance() {
        if (instance == null) {
            synchronized (SystemConfig.class) {
                if (instance == null) {
                    instance = new SystemConfig();
                }
            }
        }
        return instance;
    }

    public String get(String key) {
        return config.get(key);
    }

    public void set(String key, String value) {
        config.put(key, value);
    }
}

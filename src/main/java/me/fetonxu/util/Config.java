package me.fetonxu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private static Properties props = new Properties();

    static {

        try(InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("config.properties")){
            props.load(inputStream);
        }catch (Exception e){
            logger.error(String.format("Config Initialization error: %s", e));
            System.exit(-1);
        }
    }

    public static String getString(String key){
        return props.getProperty(key);
    }

    public static Integer getInt(String key){
        return Integer.parseInt(props.getProperty(key));
    }
    public static Boolean getBool(String key){
        return Boolean.parseBoolean(props.getProperty(key));
    }

}

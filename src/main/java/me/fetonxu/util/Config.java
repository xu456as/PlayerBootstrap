package me.fetonxu.util;

import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static Properties props = new Properties();

    static {

        try(InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("config.properties")){
            props.load(inputStream);
        }catch (Exception e){
            e.printStackTrace();
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

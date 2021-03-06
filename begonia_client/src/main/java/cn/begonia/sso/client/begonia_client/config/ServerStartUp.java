package cn.begonia.sso.client.begonia_client.config;

import groovy.util.logging.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class ServerStartUp implements ApplicationContextAware  {
    private  static  ApplicationContext   applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    public static  ApplicationContext   getApplicationContext(){
        return   applicationContext;
    }

}

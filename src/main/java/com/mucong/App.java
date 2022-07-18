package com.mucong;

import com.mucong.easytodo.EasytodoApp;
import org.springframework.beans.BeansException;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App  {

    public static void main(String[] args) {
        try {
            SpringApplicationBuilder builder = new SpringApplicationBuilder(App.class);
            ApplicationContext context = builder.headless(false).web(WebApplicationType.NONE).run(args);
            EasytodoApp easytodoApp = context.getBean(EasytodoApp.class);
            easytodoApp.createUI();
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }
}

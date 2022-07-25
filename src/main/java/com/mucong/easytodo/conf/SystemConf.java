package com.mucong.easytodo.conf;


import cn.hutool.core.io.FileUtil;
import cn.hutool.setting.Setting;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Component("SystemConf")
public class SystemConf {
    public static final String USER_HOME = System.getProperty("user.home");
    public static final String CONFIG_PATH = USER_HOME + File.separator + ".easytodo";

    public final static String SHOW_TASK = "showtaskboard";
    public final static String SHOW_NOTES = "shownotesboard";

    Setting setting;
    private String settingPath = CONFIG_PATH + File.separator + "config" + File.separator + "system.setting";

    public SystemConf() {
        setting = new Setting(FileUtil.touch(settingPath), StandardCharsets.UTF_8,false);
    }

    public void setConf(String key,String value){setting.put(key,value);}
    public String getConf(String key){
        return setting.get(key);
    }


    public void save(){
        setting.store(settingPath);
    }



}

package com.mucong.easytodo;

import com.mucong.easytodo.conf.SystemConf;
import com.mucong.easytodo.respo.TaskRespository;
import com.mucong.easytodo.ui.MainFrame;
import com.mucong.easytodo.ui.dialog.TaskDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
@Slf4j
public class EasytodoApp {

    @Autowired
    private TaskRespository taskRespository;

    @Autowired
    private MainFrame mainFrame;

    @Autowired
    private TaskDialog taskFrame;

    @Autowired
    private SystemConf systemConf;


    public void createUI(){
        mainFrame.init();
        mainFrame.setVisible(true);
        taskFrame.init();
        if(systemConf.getConf(SystemConf.SHOW_TASK)!=null&&systemConf.getConf(SystemConf.SHOW_TASK).equals("1")){
            taskFrame.showTask();
            mainFrame.getMainPane().taskbordSwitch.setState(1);
        }
        if(systemConf.getConf(SystemConf.SHOW_NOTES)!=null&&systemConf.getConf(SystemConf.SHOW_NOTES).equals("1")){
//            taskDialog.setVisible(true);
            mainFrame.getMainPane().notesBordSwitch.setState(1);
        }
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }


}

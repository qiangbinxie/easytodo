package com.mucong.easytodo;

import com.mucong.easytodo.respo.TaskRespository;
import com.mucong.easytodo.ui.MainFrame;
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


    public void createUI(){
        mainFrame.init();
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }


}

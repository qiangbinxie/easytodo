package com.mucong.easytodo.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class TrayPopMenu extends PopupMenu {

    @Autowired
    private MainFrame mainFrame;

    public TrayPopMenu() {
        //主界面选项
        MenuItem mainFrameItem = new MenuItem("EasyTodo");
        mainFrameItem.addActionListener(e -> mainFrame.setVisible(true));
        this.add(mainFrameItem);

        //退出程序选项
        MenuItem exitItem = new MenuItem("exit");
        exitItem.addActionListener(e -> System.exit(0));
        this.add(exitItem);

        this.setFont(new Font(null,0,14));
    }
}

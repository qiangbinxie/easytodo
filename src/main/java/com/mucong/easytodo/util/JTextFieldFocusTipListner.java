package com.mucong.easytodo.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextFieldFocusTipListner implements FocusListener {
    private String tip;
    private JTextArea textField;

    public JTextFieldFocusTipListner(String tip, JTextArea textField) {
        this.tip = tip;
        this.textField = textField;
        this.textField.setForeground(Color.gray);
        this.textField.setText(tip);
    }

    @Override
    public void focusGained(FocusEvent e) {
        String temp = textField.getText();
        if(temp.equals(tip)) {
            textField.setText("");
            textField.setForeground(Color.WHITE);
        }

    }

    @Override
    public void focusLost(FocusEvent e) {
        String temp = textField.getText();
        if(temp.equals("")) {
            textField.setForeground(Color.GRAY);
            textField.setText(tip);
        }

    }
}

package com.mucong.easytodo.ui.component;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class FootPane extends JPanel {
    public JLabel foot;

    public FootPane() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        foot = new JLabel();
//        foot.setText("说明过程");
        this.add(foot);
        final Spacer spacer1 = new Spacer();
        this.add(spacer1);
        final Spacer spacer2 = new Spacer();
        this.add(spacer2);

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        foot = new JLabel();
        foot.setText("说明过程");
        panel1.add(foot);
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1);
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2);
    }
}

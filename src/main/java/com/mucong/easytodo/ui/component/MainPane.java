package com.mucong.easytodo.ui.component;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.mucong.easytodo.constant.ColorTheme;
import com.mucong.easytodo.ui.dialog.TaskDialog;
import com.mucong.easytodo.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class MainPane extends JPanel {
    public JPanel accoutPanel;
    public JPanel taskBoard;
    public JPanel notesBoard;
    public JPanel histTask;
    public JPanel histNotes;

    public int bodeswitch = 0;
    public int noteswitch = 1;

    @Autowired
    private TaskDialog taskDialog;

    public MainPane() {
        accoutPanel = new noteBar(getLeft("/icon/accout.png", "木聪"), getright(null, null, 0, "")).init();
        taskBoard = new noteBar(getLeft("/icon/task.png", "任务版 "), getright("/icon/radio_off.png", null, bodeswitch, "task")).init();
        notesBoard = new noteBar(getLeft("/icon/notes.png", "便签板 "), getright("/icon/radio_on.png", null, noteswitch, "notes")).init();
        histTask = new noteBar(getLeft("/icon/histask.png", "历史任务"), getright(null, "25", 0, "")).init();
        histNotes = new noteBar(getLeft("/icon/hisnotes.png", "历史便签"), getright(null, "25", 0, "")).init();
        this.add(accoutPanel);
        this.add(createsep(0.9));
        this.add(taskBoard);
        this.add(createsep(0.9));
        this.add(notesBoard);
        this.add(createsep(0.9));
        this.add(histTask);
        this.add(createsep(0.9));
        this.add(histNotes);
        this.add(createsep(0.9));
        this.setBackground(ColorTheme.BLUE);
    }

    /**
     * 开关按钮添加事件
     */
    public class SwitchIcon extends JLabel {
        private int val;
        private String name;

        public SwitchIcon(int val, String name) {
            this.val = val;
            this.name = name;
        }

        public SwitchIcon init() {
            SwitchIcon icon = this;
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    val = (val + 1) % 2;
                    showBoard(name);
                    ImageIcon closeIcon = new ImageIcon(getClass().getResource(ColorTheme.Switch.getByVal(val).getPath()));
                    closeIcon = new ImageIcon(closeIcon.getImage().getScaledInstance(28, 28, Image.SCALE_DEFAULT));
                    icon.setIcon(closeIcon);

                }
            });
            ImageIcon closeIcon = new ImageIcon(getClass().getResource(ColorTheme.Switch.getByVal(val).getPath()));
            closeIcon = new ImageIcon(closeIcon.getImage().getScaledInstance(28, 28, Image.SCALE_DEFAULT));
            icon.setIcon(closeIcon);
            return this;

        }

        private void showBoard(String name) {
            if (name.equals("task")) {
                taskDialog.setVisible((val == 0));
            }
        }
    }


    private JSeparator createsep(double v) {
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension((int) (SystemUtil.width * v), 2));
        separator.setBackground(Color.white);
        return separator;
    }


    public class noteBar extends JPanel {
        public JPanel left;
        public JPanel right;

        public noteBar(JPanel left, JPanel right) {
            this.left = left;
            this.right = right;
        }

        public noteBar init() {
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints constraints = new GridBagConstraints();
            this.setLayout(gridBagLayout);
            constraints.weightx = 0.5;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weighty = 1;
            constraints.gridheight = GridBagConstraints.REMAINDER;

            left.setBackground(ColorTheme.BLUE);
            gridBagLayout.setConstraints(left, constraints);
            this.add(left);
            gridBagLayout.setConstraints(right, constraints);
            this.add(right);
            this.setPreferredSize(new Dimension((int) (SystemUtil.width * 0.91), 38));
            return this;
        }
    }

    private JPanel getright(String path, String text, int xx, String name) {
        JPanel right = new JPanel();
        if (path != null) {
            right.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            right.add(new SwitchIcon(xx, name).init());
        } else if (text != null) {
            right.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            JLabel label = new JLabel(text);
            label.setForeground(ColorTheme.ORANGE);
            label.setFont(new Font("", 1, 18));
            right.add(label);
        }
        right.setBackground(ColorTheme.BLUE);
        return right;
    }

    private JPanel getLeft(String path, String text) {

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel iconLabel = new JLabel();
        ImageIcon closeIcon = new ImageIcon(getClass().getResource(path));
        closeIcon = new ImageIcon(closeIcon.getImage().getScaledInstance(28, 28, Image.SCALE_DEFAULT));
        iconLabel.setIcon(closeIcon);
        panel.add(iconLabel);

        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("", 1, 18));
        panel.add(textLabel);

        return panel;
    }

}
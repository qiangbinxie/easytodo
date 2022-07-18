package com.mucong.easytodo.ui.dialog;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.mucong.easytodo.constant.ColorTheme;
import com.mucong.easytodo.ui.MainFrame;
import com.mucong.easytodo.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@Component
public class TaskDialog extends JDialog {

    @Autowired
    private MainFrame mainFrame;

    private JPanel todo;

    private int x;
    private int y;
    private int width;
    private int height;
    private Color backgroud;
    private float opacity = 0.9f;
    private boolean isVisible = false;


    public TaskDialog() {
        init();

        dragmove();
    }

    private void init() {
        //读取配置文件
        this.setUndecorated(true);
        this.setOpacity(0.3f);
        this.setBounds(200, 200, SystemUtil.width, SystemUtil.height);
        this.setBackground(Color.white);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JPanel title = new JPanel();
        CardLayout cardLayout = new CardLayout();
        title.setLayout(cardLayout);
        title.setBackground(Color.black);
        JPanel titleCard1 = new JPanel();
        JPanel titleCard2 = new JPanel();
        titleCard1.setBackground(Color.black);
        titleCard2.setBackground(Color.black);

        titleCard2.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        title.setPreferredSize(new Dimension(SystemUtil.width, 40));
        titleCard1.setPreferredSize(new Dimension(SystemUtil.width, 40));
        titleCard2.setPreferredSize(new Dimension(SystemUtil.width, 40));
        JLabel lable1 = createIconLable("/icon/nail.png", "固定");
        JLabel lable2 = createIconLable("/icon/addnew.png", "添加任务");
        JLabel lable3 = createIconLable("/icon/Close.png", "关闭任务展示");

        titleCard2.add(lable1);
        titleCard2.add(lable2);
        titleCard2.add(lable3);


        TaskDialog taskDialog = this;
        lable3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                taskDialog.setVisible(false);
            }
        });
        JPanel task = new JPanel();
        task.setPreferredSize(new Dimension(SystemUtil.width, SystemUtil.height - 50));
        task.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JScrollPane taskList = new JScrollPane();
        task.add(taskList);
        lable2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = new JPanel();
                panel.setPreferredSize(new Dimension(SystemUtil.width, 40));
                panel.setBackground(Color.black);
                JTextField textField = new JTextField("请添加任务");
                textField.setPreferredSize(new Dimension(SystemUtil.width, 30));
                textField.setBackground(Color.black);
                panel.add(textField);


                taskList.add(createsep(0.9));
                taskList.add(panel);

            }
        });

        title.add(titleCard1);
        title.add(titleCard2);
        this.getContentPane().add(title);
        this.getContentPane().add(task);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                cardLayout.last(title);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cardLayout.first(title);
            }
        });


    }

    private JSeparator createsep(double v) {
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension((int) (SystemUtil.width * v), 2));
        separator.setBackground(Color.white);
        return separator;
    }

    private JLabel createIconLable(String path, String text) {
        JLabel label = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        icon = new ImageIcon(icon.getImage().getScaledInstance(28, 28, Image.SCALE_DEFAULT));
        label.setIcon(icon);
        label.setToolTipText(text);
        return label;
    }


    int dragx = 0;
    int dragy = 0;

    //添加拖拽移动
    private void dragmove() {
        TaskDialog taskDialog = this;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragx = e.getX();
                dragy = e.getY();
            }

        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point point = taskDialog.getLocation();
                int offsetx = e.getX() - dragx;
                int offsety = e.getY() - dragy;
                taskDialog.setLocation(point.x + offsetx, point.y + offsety);
            }
        });
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
        todo = new JPanel();
        todo.setLayout(new GridLayoutManager(1, 1, new Insets(100, 100, 0, 0), -1, -1));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return todo;
    }

}

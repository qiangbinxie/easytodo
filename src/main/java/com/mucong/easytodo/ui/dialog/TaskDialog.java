package com.mucong.easytodo.ui.dialog;

import com.mucong.easytodo.bean.Task;
import com.mucong.easytodo.constant.TaskStateEnum;
import com.mucong.easytodo.respo.TaskRespository;
import com.mucong.easytodo.ui.MainFrame;
import com.mucong.easytodo.ui.component.MainPane;
import com.mucong.easytodo.ui.component.RoundButton;
import com.mucong.easytodo.util.JTextFieldFocusTipListner;
import com.mucong.easytodo.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.plaf.ScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

@Component
public class TaskDialog extends JDialog {
    @Autowired
    private MainFrame mainFrame;

    @Autowired
    private MainPane mainPane;

    @Autowired
    private TaskRespository taskRespository;


    private JPanel task;

    private JPanel taskList;

    private JScrollPane scrollPane;

    private boolean isVisible = false;


    public TaskDialog() {
        dragmove();
        init();
    }

    private int x = 200;
    private int y = 200;
    private int width=SystemUtil.width;
    private int height=SystemUtil.height;
    private float opacity = 0.6f;

    private void init() {
        //读取配置文件
        this.setUndecorated(true);
        this.setOpacity(opacity);
        this.setBounds(x,y,width,height);
        this.setBackground(Color.white);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JPanel title = new JPanel();
        CardLayout cardLayout = new CardLayout();
        title.setLayout(cardLayout);
        title.setBackground(Color.black);
        JPanel titleCard2 = new JPanel();
        titleCard2.setBackground(Color.black);

        titleCard2.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        title.setPreferredSize(new Dimension(SystemUtil.width, 40));
        titleCard2.setPreferredSize(new Dimension(SystemUtil.width, 40));
        JLabel lable1 = createIconLable("/icon/nail.png", "固定");
        JLabel lable2 = createIconLable("/icon/addnew.png", "添加任务");
        JLabel lable3 = createIconLable("/icon/Close.png", "关闭任务展示");

        titleCard2.add(lable1);
        titleCard2.add(lable2);
        titleCard2.add(lable3);




        //添加隐藏操作按钮，发现内部控件不好用
        /*this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lable1.setVisible(true);
                lable2.setVisible(true);
                lable3.setVisible(true);
                super.mouseEntered(e);
                System.out.println("enter");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lable1.setVisible(false);
                lable2.setVisible(false);
                lable3.setVisible(false);
                super.mouseExited(e);
                System.out.println("exit");
            }
        });*/


        TaskDialog taskFrame = this;
        lable3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                taskFrame.setVisible(false);
                mainPane.changeSwitch(mainPane.taskbordSwitch);
            }

        });

        lable2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                newTask();
            }
        });
        title.add(titleCard2);
        this.add(title);

        //task
        task = new JPanel();
        task.setPreferredSize(new Dimension(SystemUtil.width, SystemUtil.height - 40));
        task.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        task.setBackground(Color.black);
        this.add(task);

    }

    private JTextArea textField = null;
    private JTextField ptextField = new JTextField();

    /**
     * 点击新增按钮后增加一个组件
     */
    private void newTask() {
        if (textField != null) {
            System.out.println("还有任务在创建中");
            return;
        }
        System.out.println("添加新任务");
        Font font = new Font(this.getFont().getFontName(), 0, 20);
        textField = new JTextArea();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    addTask();
                }
            }
        });
        textField.setPreferredSize(new Dimension(SystemUtil.width - 60, 50));
        textField.setBackground(new Color(43, 43, 44));
        textField.setForeground(Color.white);

        textField.addFocusListener(new JTextFieldFocusTipListner("请输入内容，按enter确认", textField));
        textField.setFont(font);
        textField.setBorder(null);
        taskList.remove(ptextField);
        taskList.add(textField);
        taskList.updateUI();
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    public void addTask() {
        String text = textField.getText();
        if (text == null || text.trim().equals("")) {
            return;
        }
        creatTask(text);
        textField = null;
        loadTask();
    }

    private void creatTask(String text) {
        Task t = new Task();
        t.setName(text);
        t.setCreateAt(new Date());
        t.setTaskState(TaskStateEnum.TODO);
        taskRespository.save(t);
        mainPane.updateHis();
    }


    public void loadTask() {

        java.util.List<Task> tasks = taskRespository.findAll(Example.of(new Task().setTaskState(TaskStateEnum.TODO)));
        if (tasks.isEmpty()) {
            createTaskList();
            ptextField.setBackground(Color.black);
            ptextField.setBorder(null);
            ptextField.setEnabled(false);
            taskList.add(ptextField);
            textField = null;
            taskList.updateUI();
            return;
        }
        createTaskList();
        taskList.removeAll();
        for (Task task : tasks) {
            taskList.add(new TaskDialog.TaskItemPane(1, task));
        }
        ptextField.setBackground(Color.black);
        ptextField.setBorder(null);
        ptextField.setEnabled(false);
        taskList.add(ptextField);
        textField = null;
        taskList.updateUI();
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    /**
     * 封装任务列表每一条展示
     */
    Dimension itemDimension = new Dimension(SystemUtil.width, 50);
    Font font;

    public class TaskItemPane extends JPanel {
        public int idx;
        public JLabel taskLabel;
        public RoundButton cplBtn;
        public Task taskInfo;

        public TaskItemPane(int idx, Task task) {
            font = new Font("微软雅黑", 0, 20);
            this.taskInfo = task;
            this.idx = idx;
            this.setPreferredSize(itemDimension);
            this.setBackground(Color.black);
            this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            taskLabel = new JLabel(task.getName());
            taskLabel.setBorder(null);
            taskLabel.setPreferredSize(new Dimension(SystemUtil.width - 70, 50));
            taskLabel.setForeground(Color.white);
            taskLabel.setFont(font);
            this.add(taskLabel);
            TaskDialog.TaskItemPane itemPane = this;
            cplBtn = new RoundButton("xxx");
            cplBtn.setPreferredSize(new Dimension(20, 20));
            cplBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cplTask(itemPane);
                }
            });
            this.add(cplBtn);
        }
    }

    public void cplTask(TaskDialog.TaskItemPane itemPane) {
        taskList.remove(itemPane);
        itemPane.taskInfo.setTaskState(TaskStateEnum.COMPLETE);
        taskRespository.save(itemPane.taskInfo);
        loadTask();
    }


    private void createTaskList() {
        if (taskList == null) {
            taskList = new JPanel();
            BoxLayout boxLayout = new BoxLayout(taskList, BoxLayout.Y_AXIS);
            taskList.setLayout(boxLayout);
            taskList.setBackground(Color.black);
            scrollPane = new JScrollPane(taskList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setPreferredSize(new Dimension(SystemUtil.width - 50, SystemUtil.height - 60));
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(10);
            scrollPane.getVerticalScrollBar().setUI(new ScrollBarUI() {
                @Override
                public void paint(Graphics g, JComponent c) {
                    super.paint(g, c);
                    g.setColor(Color.black);
                    int w = c.getWidth();
                    int h = c.getHeight();
                    g.fillRect(0, 0, w, h);
                }
            });
            task.add(scrollPane);
        }
    }

    public void showTask() {
        this.setVisible(true);
        loadTask();
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
        TaskDialog taskFrame = this;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                dragx = e.getX();
                dragy = e.getY();
            }

        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                Point point = taskFrame.getLocation();
                int offsetx = e.getX() - dragx;
                int offsety = e.getY() - dragy;
                taskFrame.setLocation(point.x + offsetx, point.y + offsety);
            }
        });
    }


}

package com.mucong.easytodo.ui.dialog;

import com.mucong.easytodo.bean.Task;
import com.mucong.easytodo.conf.SystemConf;
import com.mucong.easytodo.constant.ColorTheme;
import com.mucong.easytodo.constant.TaskStateEnum;
import com.mucong.easytodo.respo.TaskRespository;
import com.mucong.easytodo.ui.MainFrame;
import com.mucong.easytodo.ui.component.MainPane;
import com.mucong.easytodo.ui.component.RoundButton;
import com.mucong.easytodo.util.JTextFieldFocusTipListner;
import com.mucong.easytodo.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.plaf.ScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

@Component
@DependsOn("SystemConf")
public class TaskDialog extends JDialog {
    @Autowired
    private MainFrame mainFrame;

    @Autowired
    private MainPane mainPane;

    @Autowired
    private TaskRespository taskRespository;

    @Autowired
    private SystemConf systemConf;


    private JPanel task;

    private JPanel taskList;

    private JScrollPane scrollPane;

    private boolean isVisible = false;


    public TaskDialog() {
        dragmove();

    }

    //设置默认配置
    private int x = 200;
    private int y = 200;
    private int width=SystemUtil.width;
    private int height=SystemUtil.height;
    private float opacity = 0.6f;
    private String nail = "false";



    private final String LOCATIONX = "task.x";
    private final String LOCATIONY = "task.y";
    private final String WIDTHTASK = "task.width";
    private final String HEIGHTTASK = "task.height";
    private final String OPACITYCONF = "task.opacity";
    private final String NAILCONF = "task.nail";

    private void getConfig(){
        String xconf = systemConf.getConf(LOCATIONX);
        if(xconf!=null){
            x = Integer.parseInt(xconf);
        }
        String yconf = systemConf.getConf(LOCATIONY);
        if(yconf!=null){
            y = Integer.parseInt(yconf);
        }
        String wconf = systemConf.getConf(WIDTHTASK);
        if(wconf!=null){
            width = Integer.parseInt(wconf);
        }
        String hconf = systemConf.getConf(HEIGHTTASK);
        if(hconf!=null){
            height = Integer.parseInt(hconf);
        }
        String opconf = systemConf.getConf(OPACITYCONF);
        if(opconf!=null){
            opacity = Float.parseFloat(opconf);
        }
        String nailconf = systemConf.getConf(NAILCONF);
        if(nailconf!=null){
            nail = nailconf;
        }
    }

    private void saveLocation(){
        int xd = this.getX();
        int yd = this.getY();
        systemConf.setConf(LOCATIONX,xd+"");
        systemConf.setConf(LOCATIONY,yd+"");
        systemConf.save();
    }

    private void saveSize(){
        int xd = this.getWidth();
        int yd = this.getHeight();
        systemConf.setConf(WIDTHTASK,xd+"");
        systemConf.setConf(HEIGHTTASK,yd+"");
        systemConf.save();
    }
    private void saveNail(){
        systemConf.setConf(NAILCONF,nail);
        systemConf.save();
    }
    private void saveOpa(){
        systemConf.setConf(OPACITYCONF,opacity+"");
        systemConf.save();
    }

    public void init() {
        //读取配置文件
        getConfig();
        nailSet();
        this.setUndecorated(true);
        this.setOpacity(opacity);
        this.setBounds(x,y,width,height);
        this.setBackground(Color.white);
        this.setLayout(new BorderLayout());
        JPanel title = new JPanel();
        CardLayout cardLayout = new CardLayout();
        title.setLayout(cardLayout);
        title.setBackground(ColorTheme.BLACK);
        JPanel titleCard2 = new JPanel();
        titleCard2.setBackground(ColorTheme.BLACK);

        titleCard2.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        title.setPreferredSize(new Dimension(SystemUtil.width, 40));
        titleCard2.setPreferredSize(new Dimension(SystemUtil.width, 40));
        JLabel lable1 = createIconLable("true".equals(nail)?"/icon/nail_on.png":"/icon/nail.png", "固定");
        JLabel lable2 = createIconLable("/icon/addnew.png", "添加任务");
        JLabel lable3 = createIconLable("/icon/Close.png", "关闭任务展示");

        titleCard2.add(lable1);
        titleCard2.add(lable2);
        titleCard2.add(lable3);

        lable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if("true".equals(nail)){
                    nail = "false";
                    ImageIcon icon = new ImageIcon(getClass().getResource("/icon/nail.png"));
                    icon = new ImageIcon(icon.getImage().getScaledInstance(28, 28, Image.SCALE_DEFAULT));
                    lable1.setIcon(icon);
                }else{
                    nail = "true";
                    ImageIcon icon = new ImageIcon(getClass().getResource("/icon/nail_on.png"));
                    icon = new ImageIcon(icon.getImage().getScaledInstance(28, 28, Image.SCALE_DEFAULT));
                    lable1.setIcon(icon);
                }
                nailSet();
                nailSetScroll();
                nailSetField();
                saveNail();
            }
        });

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
        this.add(title,BorderLayout.NORTH);

        //task
        task = new JPanel();
//        task.setPreferredSize(new Dimension(getDialogWidth(), getDialogHeight() - 40));
        task.setLayout(new BorderLayout());
        task.setBackground(ColorTheme.BLACK);
        this.add(task,BorderLayout.CENTER);

    }

    private void nailSet() {
        if("true".equals(nail)){
            this.removeMouseListener(dragMouseLsr);
            this.removeMouseMotionListener(dragListner);
        }else{
            this.addMouseListener(dragMouseLsr);
            this.addMouseMotionListener(dragListner);
        }
    }
    private void nailSetScroll() {
        if("true".equals(nail)){
            taskList.removeMouseListener(dragMouseLsr);
            taskList.removeMouseMotionListener(scrollMlsr);
        }else{
            taskList.addMouseListener(dragMouseLsr);
            taskList.addMouseMotionListener(scrollMlsr);
        }
    }
    private void nailSetField() {
        if("true".equals(nail)){
            ptextField.removeMouseListener(dragMouseLsr);
            ptextField.removeMouseMotionListener(scrollMlsr);
        }else{
            ptextField.addMouseListener(dragMouseLsr);
            ptextField.addMouseMotionListener(scrollMlsr);
        }
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
            ptextField.setBackground(ColorTheme.BLACK);
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
        ptextField.setBackground(ColorTheme.BLACK);
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

    Font font;

    private int getDialogWidth(){
        return this.getWidth();
    }
    private int getDialogHeight(){
        return this.getHeight();
    }


    public class TaskItemPane extends JPanel {
        public int idx;
        public JLabel taskLabel;
        public RoundButton cplBtn;
        public Task taskInfo;

        public TaskItemPane(int idx, Task task) {

            Dimension itemDimension = new Dimension(getDialogWidth(), 50);
            font = new Font("微软雅黑", Font.BOLD, 20);
            this.taskInfo = task;
            this.idx = idx;
            this.setPreferredSize(itemDimension);
            this.setBackground(ColorTheme.BLACK);
            this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
            taskLabel = new JLabel(task.getName());
            taskLabel.setBorder(null);
            taskLabel.setPreferredSize(new Dimension(getDialogWidth() - 70, 50));
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

        public void updateWidth(){
            System.out.println("更新大小"+taskInfo.getName()+":"+getDialogWidth());
            Dimension dimension = this.getSize();
            dimension.setSize(getDialogWidth(),dimension.height);
            Dimension dimension1 = taskLabel.getSize();
            dimension1.setSize(getDialogWidth()-70,dimension1.height);
            taskLabel.setSize(dimension1);
            this.setSize(dimension);
            this.updateUI();
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
            taskList.setBackground(ColorTheme.BLACK);
            scrollPane = new JScrollPane(taskList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//            scrollPane.setPreferredSize(new Dimension(getDialogWidth() - 50, getDialogHeight() - 60));
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(10);
            scrollPane.getVerticalScrollBar().setUI(new ScrollBarUI() {
                @Override
                public void paint(Graphics g, JComponent c) {
                    super.paint(g, c);
                    g.setColor(ColorTheme.BLACK);
                    int w = c.getWidth();
                    int h = c.getHeight();
                    g.fillRect(0, 0, w, h);
                }
            });
            nailSetScroll();
            nailSetField();
            task.add(scrollPane,BorderLayout.CENTER);
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

    //添加拖拽移动修改窗口大小
    MouseMotionListener dragListner = null;
    private MouseMotionListener scrollMlsr;
    MouseListener dragMouseLsr = null;
    private void dragmove() {
        TaskDialog taskFrame = this;
        dragMouseLsr = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                dragx = e.getX();
                dragy = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                Point point = taskFrame.getLocation();
                if(point.x != x || point.y != y){
                    saveLocation();
                }
                if(width != taskFrame.getWidth()||height != taskFrame.getHeight()){
                    saveSize();
                }
            }
        };
        dragListner = new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                double left = Math.abs(e.getPoint().getX());
                double top = Math.abs(e.getPoint().getY());
                double buttom = Math.abs(taskFrame.getSize().getHeight() - e.getPoint().getY());
                double right = Math.abs(taskFrame.getSize().getWidth() - e.getPoint().getX());
                double blur = 2.0;
                if (left <blur && top < blur){
                    taskFrame.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                }else if(left < blur && buttom < blur){
                    taskFrame.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                }else if(left < blur){
                    taskFrame.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                }else if(right < blur && top < blur){
                    taskFrame.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                }else if(right < blur && buttom < blur){
                    taskFrame.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                }else if(right < blur){
                    taskFrame.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                }else if(top < blur){
                    taskFrame.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                }else if(buttom < blur){
                    taskFrame.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                }else{
                    taskFrame.setCursor(null);
                }


            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if(taskFrame.getCursor().getType() == Cursor.DEFAULT_CURSOR){
                    Point point = taskFrame.getLocation();
                    int offsetx = e.getX() - dragx;
                    int offsety = e.getY() - dragy;
                    taskFrame.setLocation(point.x + offsetx, point.y + offsety);
                    return;
                }
                Dimension dimension = taskFrame.getSize();
                switch (taskFrame.getCursor().getType()) {
                    case Cursor.E_RESIZE_CURSOR:
                        dimension.setSize(e.getX(), dimension.getHeight());
                        taskFrame.setSize(dimension);
                        break;
                    case Cursor.S_RESIZE_CURSOR:
                        dimension.setSize(dimension.getWidth(), e.getY());
                        taskFrame.setSize(dimension);
                        break;
                    case Cursor.SE_RESIZE_CURSOR:
                        dimension.setSize(e.getX(), e.getY());
                        taskFrame.setSize(dimension);
                        break;
                    case Cursor.N_RESIZE_CURSOR:
                        dimension.setSize(dimension.getWidth(), dimension.getHeight() - e.getY());
                        taskFrame.setSize(dimension);
                        taskFrame.setLocation(taskFrame.getLocationOnScreen().x, taskFrame.getLocationOnScreen().y + e.getY());
                        break;
                    case Cursor.W_RESIZE_CURSOR:
                        dimension.setSize(dimension.getWidth() - e.getX(), dimension.getHeight());
                        taskFrame.setSize(dimension);
                        taskFrame.setLocation(taskFrame.getLocationOnScreen().x + e.getX(), taskFrame.getLocationOnScreen().y);
                        break;
                    case Cursor.NE_RESIZE_CURSOR:
                        dimension.setSize(e.getX(), dimension.getHeight() - e.getY());
                        taskFrame.setSize(dimension);
                        taskFrame.setLocation(taskFrame.getLocationOnScreen().x, taskFrame.getLocationOnScreen().y + e.getY());
                        break;
                    case Cursor.NW_RESIZE_CURSOR:
                        dimension.setSize(dimension.getWidth() - e.getX(), dimension.getHeight() - e.getY());
                        taskFrame.setSize(dimension);
                        taskFrame.setLocation(taskFrame.getLocationOnScreen().x + e.getX(), taskFrame.getLocationOnScreen().y + e.getY());
                        break;
                    case Cursor.SW_RESIZE_CURSOR:
                        dimension.setSize(dimension.getWidth() - e.getX(), e.getY());
                        taskFrame.setSize(dimension);
                        taskFrame.setLocation(taskFrame.getLocationOnScreen().x + e.getX(), taskFrame.getLocationOnScreen().y);
                        break;
                }
            }
        };


        scrollMlsr = new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                double left = Math.abs(e.getPoint().getX());
                double buttom = Math.abs(taskFrame.getSize().getHeight() - e.getPoint().getY());
                double right = Math.abs(taskFrame.getSize().getWidth() - e.getPoint().getX());
                double blur = 2.0;
                if(left < blur && buttom < blur){
                    taskFrame.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                }else if(left < blur){
                    taskFrame.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                }else if(right < blur && buttom < blur){
                    taskFrame.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                }else if(right < blur){
                    taskFrame.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                }else if(buttom < blur){
                    taskFrame.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                }else{
                    taskFrame.setCursor(null);
                }


            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if(taskFrame.getCursor().getType() == Cursor.DEFAULT_CURSOR){
                    Point point = taskFrame.getLocation();
                    int offsetx = e.getX() - dragx;
                    int offsety = e.getY() - dragy;
                    taskFrame.setLocation(point.x + offsetx, point.y + offsety);
                    return;
                }
                Dimension dimension = taskFrame.getSize();
                switch (taskFrame.getCursor().getType()) {
                    case Cursor.E_RESIZE_CURSOR:
                        dimension.setSize(e.getX(), dimension.getHeight());
                        taskFrame.setSize(dimension);
                        break;
                    case Cursor.S_RESIZE_CURSOR:
                        dimension.setSize(dimension.getWidth(), e.getY());
                        taskFrame.setSize(dimension);
                        break;
                    case Cursor.SE_RESIZE_CURSOR:
                        dimension.setSize(e.getX(), e.getY());
                        taskFrame.setSize(dimension);
                        break;
                    case Cursor.N_RESIZE_CURSOR:
                        dimension.setSize(dimension.getWidth(), dimension.getHeight() - e.getY());
                        taskFrame.setSize(dimension);
                        taskFrame.setLocation(taskFrame.getLocationOnScreen().x, taskFrame.getLocationOnScreen().y + e.getY());
                        break;
                    case Cursor.W_RESIZE_CURSOR:
                        dimension.setSize(dimension.getWidth() - e.getX(), dimension.getHeight());
                        taskFrame.setSize(dimension);
                        taskFrame.setLocation(taskFrame.getLocationOnScreen().x + e.getX(), taskFrame.getLocationOnScreen().y);
                        break;
                    case Cursor.NE_RESIZE_CURSOR:
                        dimension.setSize(e.getX(), dimension.getHeight() - e.getY());
                        taskFrame.setSize(dimension);
                        taskFrame.setLocation(taskFrame.getLocationOnScreen().x, taskFrame.getLocationOnScreen().y + e.getY());
                        break;
                    case Cursor.NW_RESIZE_CURSOR:
                        dimension.setSize(dimension.getWidth() - e.getX(), dimension.getHeight() - e.getY());
                        taskFrame.setSize(dimension);
                        taskFrame.setLocation(taskFrame.getLocationOnScreen().x + e.getX(), taskFrame.getLocationOnScreen().y + e.getY());
                        break;
                    case Cursor.SW_RESIZE_CURSOR:
                        dimension.setSize(dimension.getWidth() - e.getX(), e.getY());
                        taskFrame.setSize(dimension);
                        taskFrame.setLocation(taskFrame.getLocationOnScreen().x + e.getX(), taskFrame.getLocationOnScreen().y);
                        break;
                }
            }
        };
    }

    private void updateChild() {
        for(java.awt.Component component:taskList.getComponents()){
            if(component instanceof TaskItemPane){
                TaskItemPane ip = (TaskItemPane) component;
                ip.updateWidth();
            }
        }
    }


}

package com.mucong.easytodo.ui;

import com.formdev.flatlaf.extras.FlatSVGUtils;
import com.mucong.easytodo.constant.ColorTheme;
import com.mucong.easytodo.ui.component.FootPane;
import com.mucong.easytodo.ui.component.MainPane;
import com.mucong.easytodo.ui.component.TitlePane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


@Component
public class MainFrame extends JFrame {
    private int x;
    private int y;
    public int width;
    public int heght;
    public int dpi;

    @Autowired
    private TrayPopMenu trayPopMenu;


    public MainFrame() throws HeadlessException {
        //计算窗口起始大小和位置，中间位置，
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        width = 3*dpi;
        heght = 2*width;
        if(heght>screen.getHeight()){
            heght =(int)( 0.8 * screen.getHeight());
            width = heght/2;
        }
        x = (int)screen.getWidth()/2 - width/2;
        y = (int)screen.getHeight()/2 - heght/2;
        this.setBounds(x,y,width,heght);
        this.setUndecorated(true);
        this.setOpacity(0.8f);
//        this.setBackground(Color.getHSBColor(216, 48, 90));
        this.setIconImages(FlatSVGUtils.createWindowIconImages("/icon/EasyTodo.svg"));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                x = e.getX();
                y = e.getY();
            }
        });
        JFrame mainframe = this;
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                Point point = mainframe.getLocation();
                int offsetx = e.getX()-x;
                int offsety = e.getY()-y;
                mainframe.setLocation(point.x+offsetx,point.y+offsety);
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                tray();
            }
        });
        if(SystemTray.isSupported()){
            // 创建弹出菜单
            SystemTray systemTray = SystemTray.getSystemTray();
            try {
                Image image2 = FlatSVGUtils.svg2image("/icon/EasyTodo.svg",1f);
                TrayIcon trayIcon = new TrayIcon(image2,"EasyTodo");
                trayIcon.setImageAutoSize(true);
                trayIcon.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(MouseEvent.BUTTON1 == e.getButton()){
                            mainframe.setVisible(!mainframe.isVisible());
                        }else if(MouseEvent.BUTTON3 == e.getButton()){
                            trayIcon.setPopupMenu(trayPopMenu);
                        }
                    }
                });
                systemTray.add(trayIcon);
                this.setVisible(false);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    @Autowired
    private TitlePane titlePane;
    @Autowired
    private MainPane mainPane;
    @Autowired
    private FootPane footPane;

    public void init(){

        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weighty = 0.11;
        titlePane.setBackground(ColorTheme.BLUE);
        gridBagLayout.setConstraints(titlePane,constraints);
        constraints.weighty = 0.4;
        mainPane.setBackground(ColorTheme.BLUE);
        gridBagLayout.setConstraints(mainPane,constraints);
        constraints.weighty = 0.51;
        footPane.setBackground(ColorTheme.BLUE);
        gridBagLayout.setConstraints(footPane,constraints);

        this.add(titlePane);
        this.add(mainPane);
        this.add(footPane);

    }

    private void tray() {
        this.setVisible(false);
    }


}

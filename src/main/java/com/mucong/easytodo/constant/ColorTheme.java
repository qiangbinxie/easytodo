package com.mucong.easytodo.constant;

import java.awt.*;

public class ColorTheme {
    public static final Color BLUE = new Color(119,163,229);
    public static final Color ORANGE = new Color(249, 171, 98);

    public enum Switch{
        ON(0,"/icon/radio_on.png"),
        OFF(1,"/icon/radio_off.png");

        private int val;
        private String path;

        Switch(int val, String path) {
            this.val = val;
            this.path = path;
        }
        public static Switch getByVal(int val){
            for(Switch vl :Switch.values()){
                if(vl.val == val){
                    return vl;
                }
            }
            return null;
        }

        public int getVal() {
            return val;
        }

        public String getPath() {
            return path;
        }
    }
}

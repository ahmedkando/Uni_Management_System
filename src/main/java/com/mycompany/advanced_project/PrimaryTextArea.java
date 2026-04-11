package com.mycompany.advanced_project;

import javax.swing.*;
//import java.util.List;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.border.LineBorder;

public class PrimaryTextArea extends JTextArea{
    public PrimaryTextArea(boolean editable){
        if (editable==true){
    setCursor(new Cursor(Cursor.TEXT_CURSOR));
    setBorder(new LineBorder(Color.GRAY, 1));
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(new LineBorder(new Color(77, 163, 255), 1));
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBorder(new LineBorder(Color.GRAY, 1));
            }
        });
        }
        else
        {
            setBorder(new LineBorder(Color.GRAY, 1));
            setEditable(false);
            setFocusable(false);
//            setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
            setForeground(new Color(0, 0, 0));
            
        }
    }
}

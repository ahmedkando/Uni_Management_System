package com.mycompany.advanced_project;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PrimaryField extends JTextField{
    public PrimaryField(){
        setCursor(new Cursor(Cursor.TEXT_CURSOR));
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
}

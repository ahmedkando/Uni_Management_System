package com.mycompany.advanced_project;
import javax.swing.*;
import java.awt.*;

public class PrimaryButton extends JButton{
    public PrimaryButton(String text)
    {
        super(text);
        setFocusPainted(false);
        setFont(new Font("Arial", Font.BOLD, 15));
        setForeground(Color.WHITE);
        setBackground(new Color(77, 163, 255));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        // ============================hover effect
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(new Color(0, 0, 0));
                
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(new Color(77, 163, 255));
                
            }
        });
    }
}

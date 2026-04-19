package com.mycompany.advanced_project.UI;
import javax.swing.*;
import java.awt.*;


public class TemplatePanel extends JPanel{
    public TemplatePanel(LayoutManager layout) {
        setLayout(layout);
        setOpaque(true);
        setBackground(new Color(255, 255, 255));
        setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
    }
    
}

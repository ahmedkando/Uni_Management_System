package com.mycompany.advanced_project.Panels_Fields;
import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel{
    
    public HeaderPanel(LayoutManager layout){
        setLayout(layout);
        setOpaque(true);
        setBackground(new Color(0, 51, 102));
        setPreferredSize(new Dimension(850, 50));
        setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));
    }
    
}

package com.mycompany.advanced_project.UI;
import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {

    public GradientPanel(LayoutManager layout) {
        setLayout(layout);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
  

        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(
                0, 0, new Color(128, 90, 213), // purple
                w, h, new Color(255, 140, 0) // orange
        );

        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, w, h, 25, 25);

        super.paintComponent(g);
    }
}

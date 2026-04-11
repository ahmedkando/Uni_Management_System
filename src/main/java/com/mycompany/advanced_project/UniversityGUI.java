package com.mycompany.advanced_project;

import javax.swing.*;
import java.util.List;
import java.awt.*;

public class UniversityGUI extends JFrame {

    private final System_controller system;

    public UniversityGUI(System_controller system) {
        this.system = system;

        setTitle("University Management System");
        setSize(850, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel menu = new GradientPanel(new GridLayout(0, 1, 5, 5));
        menu.setPreferredSize(new Dimension(180, 0));

        JPanel content = new JPanel(new CardLayout());

        content.add(addStudentPanel(), "ADD_STUDENT");
        content.add(addInstructorPanel(), "ADD_INSTRUCTOR");
        content.add(addCoursePanel(), "ADD_COURSE");
        content.add(enrollPanel(), "ENROLL");
        content.add(removeStudentPanel(), "REMOVE");
        content.add(announcementPanel(), "ANNOUNCE");
        content.add(feedPanel(), "FEED");
        content.add(messagePanel(), "MESSAGE");
        content.add(listPanel(), "LIST");

        addMenuButton(menu, content, "Add Student", "ADD_STUDENT");
        addMenuButton(menu, content, "Add Instructor", "ADD_INSTRUCTOR");
        addMenuButton(menu, content, "Add Course", "ADD_COURSE");
        addMenuButton(menu, content, "Enroll Student", "ENROLL");
        addMenuButton(menu, content, "Remove Student", "REMOVE");
        addMenuButton(menu, content, "Announcement", "ANNOUNCE");
        addMenuButton(menu, content, "Student Feed", "FEED");
        addMenuButton(menu, content, "Send Message", "MESSAGE");
        addMenuButton(menu, content, "List All", "LIST");

        add(menu, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        setVisible(true);
    }

    private void addMenuButton(JPanel menu, JPanel content, String text, String card) {
        JButton btn = new JButton(text);

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 14f));
        btn.setForeground(Color.WHITE);

        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // ============================shadow effect
                g2.setColor(new Color(0, 0, 0, 60));
                g2.fillRoundRect(6, 6, getWidth() - 6, getHeight() - 6, 20, 20);

                // ============================butoon background color
                g2.setColor(new Color(255, 255, 255, 180));
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 20, 20);

                super.paintComponent(g);
            }
        };

        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        wrapper.add(btn, BorderLayout.CENTER);

        // ============================hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                wrapper.setBorder(BorderFactory.createEmptyBorder(4, 4, 12, 12));
                wrapper.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                wrapper.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                wrapper.repaint();
            }
        });

        btn.addActionListener(e -> {
            CardLayout cl = (CardLayout) content.getLayout();
            cl.show(content, card);
        });

        menu.add(wrapper);
    }

    private JPanel addStudentPanel() {
        JPanel p = new GradientPanel(null);

        JTextField u = new JTextField();
        JTextField e = new JTextField();
        JButton b = new JButton("Add Student");

        addLabel(p, "Username:", 30, 30);
        addLabel(p, "Email:", 30, 70);

        u.setBounds(140, 30, 200, 25);
        e.setBounds(140, 70, 200, 25);
        b.setBounds(140, 110, 150, 30);

        b.addActionListener(a -> {
            Student s = system.addStudent(u.getText(), e.getText());
            JOptionPane.showMessageDialog(this, "Student ID: " + s.getId());
        });

        p.add(u);
        p.add(e);
        p.add(b);
        return p;
    }

    private JPanel addInstructorPanel() {
        JPanel p = new GradientPanel(null);

        JTextField u = new JTextField();
        JTextField e = new JTextField();
        JButton b = new JButton("Add Instructor");

        addLabel(p, "Username:", 30, 30);
        addLabel(p, "Email:", 30, 70);

        u.setBounds(140, 30, 200, 25);
        e.setBounds(140, 70, 200, 25);
        b.setBounds(140, 110, 150, 30);

        b.addActionListener(a -> {
            Instructor i = system.addInstructor(u.getText(), e.getText());
            JOptionPane.showMessageDialog(this, "Instructor ID: " + i.getId());
        });

        p.add(u);
        p.add(e);
        p.add(b);
        return p;
    }

    private JPanel addCoursePanel() {
        JPanel p = new GradientPanel(null);

        JTextField name = new JTextField();
        JTextField credits = new JTextField();
        JTextField type = new JTextField();
        JTextField detail = new JTextField();
        JButton b = new JButton("Add Course");

        addLabel(p, "Name:", 30, 30);
        addLabel(p, "Credits:", 30, 70);
        addLabel(p, "Type:", 30, 110);
        addLabel(p, "Platform / Room:", 30, 150);

        name.setBounds(160, 30, 200, 25);
        credits.setBounds(160, 70, 200, 25);
        type.setBounds(160, 110, 200, 25);
        detail.setBounds(160, 150, 200, 25);
        b.setBounds(160, 190, 150, 30);

        b.addActionListener(a -> {
            Course c = system.addCourse(
                    name.getText(),
                    Integer.parseInt(credits.getText()),
                    type.getText(),
                    detail.getText());
            JOptionPane.showMessageDialog(this, "Course ID: " + c.getId());
        });

        p.add(name);
        p.add(credits);
        p.add(type);
        p.add(detail);
        p.add(b);
        return p;
    }

    private JPanel enrollPanel() {
        JPanel p = new GradientPanel(null);

        JTextField s = new JTextField();
        JTextField c = new JTextField();
        JButton b = new JButton("Enroll");

        addLabel(p, "Student ID:", 30, 40);
        addLabel(p, "Course ID:", 30, 80);

        s.setBounds(140, 40, 200, 25);
        c.setBounds(140, 80, 200, 25);
        b.setBounds(140, 120, 150, 30);

        b.addActionListener(a -> JOptionPane.showMessageDialog(this,
                system.enrollStudent(s.getText(), c.getText())
                        ? "Enrolled!"
                        : "Failed!"));

        p.add(s);
        p.add(c);
        p.add(b);
        return p;
    }

    private JPanel removeStudentPanel() {
        JPanel p = new GradientPanel(null);

        JTextField id = new JTextField();
        JButton b = new JButton("Remove");

        addLabel(p, "Student ID:", 30, 40);

        id.setBounds(140, 40, 200, 25);
        b.setBounds(140, 80, 150, 30);

        b.addActionListener(a -> JOptionPane.showMessageDialog(this,
                system.removeStudent(id.getText())
                        ? "Student removed!"
                        : "Not found!"));

        p.add(id);
        p.add(b);
        return p;
    }

    private JPanel announcementPanel() {
        JPanel p = new GradientPanel(null);

        JTextField cid = new JTextField();
        JTextArea text = new JTextArea();
        JButton b = new JButton("Post");

        addLabel(p, "Course ID:", 30, 30);

        cid.setBounds(140, 30, 200, 25);
        text.setBounds(30, 70, 330, 100);
        b.setBounds(140, 190, 150, 30);

        b.addActionListener(a -> JOptionPane.showMessageDialog(this,
                system.postAnnouncement(cid.getText(), text.getText())
                        ? "Posted!"
                        : "Course not found!"));

        p.add(cid);
        p.add(text);
        p.add(b);
        return p;
    }

    private JPanel feedPanel() {
        JPanel p = new GradientPanel(null);

        JTextField sid = new JTextField();
        JTextArea area = new JTextArea();
        JButton b = new JButton("View");

        addLabel(p, "Student ID:", 30, 30);

        sid.setBounds(140, 30, 200, 25);
        area.setBounds(30, 70, 400, 150);
        b.setBounds(140, 230, 150, 30);

        b.addActionListener(a -> {
            List<String> feed = system.getStudentFeed(sid.getText());
            area.setText(String.join("\n", feed));
        });

        p.add(sid);
        p.add(area);
        p.add(b);
        return p;
    }

    private JPanel messagePanel() {
        JPanel p = new GradientPanel(null);

        JTextField from = new JTextField();
        JTextField to = new JTextField();
        JTextArea msg = new JTextArea();
        JButton b = new JButton("Send");

        addLabel(p, "From ID:", 30, 30);
        addLabel(p, "To ID:", 30, 70);

        from.setBounds(140, 30, 200, 25);
        to.setBounds(140, 70, 200, 25);
        msg.setBounds(30, 110, 330, 100);
        b.setBounds(140, 230, 150, 30);

        b.addActionListener(a -> JOptionPane.showMessageDialog(this,
                system.sendMessage(from.getText(), to.getText(), msg.getText())
                        ? "Sent!"
                        : "Failed!"));

        p.add(from);
        p.add(to);
        p.add(msg);
        p.add(b);
        return p;
    }

    private JPanel listPanel() {
        JPanel p = new GradientPanel(null);

        JTextArea area = new JTextArea();
        JButton b = new JButton("Refresh");

        area.setBounds(20, 20, 520, 300);
        b.setBounds(200, 330, 150, 30);

        b.addActionListener(a -> {
            StringBuilder sb = new StringBuilder();
            system.getAllUsers().forEach(u -> sb.append(u.getInfo()).append("\n"));
            sb.append("\n");
            system.getAllCourses().forEach(c -> sb.append(c.getInfo()).append("\n"));
            area.setText(sb.toString());
        });

        p.add(area);
        p.add(b);
        return p;
    }

    private void addLabel(JPanel p, String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 120, 25);
        p.add(l);
    }
}

package com.mycompany.advanced_project.Panels_Fields;

import javax.swing.*;

import com.mycompany.advanced_project.*;
import com.mycompany.advanced_project.Classes.Course;
import com.mycompany.advanced_project.Classes.Instructor;
import com.mycompany.advanced_project.Classes.Student;

import java.util.List;
import java.awt.*;


public class UniversityGUI extends JFrame {

    private final System_controller system;
    private JPanel currentWrapper;
    private JButton currentBtn;
    private String LoggedUserID="0"; //will be used for authorizing this person is admin or student or instructor
    private String currentPage="Login";//Login Signup Inside
    private String UserName="Sondos";
    JButton Headerbtn;
    JLabel HeaderLabel;
    JPanel menu;
    public UniversityGUI(System_controller system) {
        this.system = system;

        setTitle("University Management System");
        setSize(850, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
// ============================HEADER
        HeaderPanel header=new HeaderPanel(new BorderLayout());
        
        addHeaderLabel(header);
        
        
        
// ============================MainContent
        JPanel content = new TemplatePanel(new CardLayout());
        
        addHeaderButton(content, header);
        
        content.add(addLogInPanel(content), "LOG_IN");
        content.add(addSignUpPanel(content), "SIGN_UP");
        content.add(addStudentPanel(), "ADD_STUDENT");
        content.add(addInstructorPanel(), "ADD_INSTRUCTOR");
        content.add(addCoursePanel(), "ADD_COURSE");
        content.add(enrollPanel(), "ENROLL");
        content.add(removeStudentPanel(), "REMOVE");
        content.add(announcementPanel(), "ANNOUNCE");
        content.add(feedPanel(), "FEED");
        content.add(messagePanel(), "MESSAGE");
        content.add(listPanel(), "LIST");
        
// ============================SideMenu     
        JPanel sideMenu= new SidePanel(new BorderLayout());
        menu = new SidePanel(new GridLayout(0, 1, 5, 5));
        menu.setPreferredSize(new Dimension(180, 0));
        
        addMenuButton(menu, content, "Add Student", "ADD_STUDENT");
        addMenuButton(menu, content, "Add Instructor", "ADD_INSTRUCTOR");
        addMenuButton(menu, content, "Add Course", "ADD_COURSE");
        addMenuButton(menu, content, "Enroll Student", "ENROLL");
        addMenuButton(menu, content, "Remove Student", "REMOVE");
        addMenuButton(menu, content, "Announcement", "ANNOUNCE");
        addMenuButton(menu, content, "Student Feed", "FEED");
        addMenuButton(menu, content, "Send Message", "MESSAGE");
        addMenuButton(menu, content, "List All", "LIST");
        
        sideMenu.add(menu, BorderLayout.WEST);
        menu.setVisible(false);
        sideMenu.setPreferredSize(new Dimension(182,500));
        add(header, BorderLayout.NORTH);
        add(sideMenu, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private void addHeaderLabel(HeaderPanel header)
    {
        HeaderLabel = new JLabel("");
        HeaderLabel.setFont(new Font("Arial", Font.BOLD, 20));
        HeaderLabel.setForeground(Color.WHITE);
        header.add(HeaderLabel, BorderLayout.WEST);
        HeaderLabel.setVisible(false);
    }
    
    private void addHeaderButton(JPanel content, HeaderPanel header)
    {
        Headerbtn = new PrimaryButton("SignUp");
        header.add(Headerbtn, BorderLayout.EAST);
        
        Headerbtn.addActionListener(e -> {
            
            CardLayout cl = (CardLayout) content.getLayout();
            switch (currentPage) {
                case "Inside":
                    showInside(content,false);
                    break;
                case "Login":
                    cl.show(content, "SIGN_UP");
                    currentPage="Signup";
                    Headerbtn.setText("LogIn");
                    break;
                default:
                    showInside(content,false);
                    break;
            }
        });
    }
    
    private void addMenuButton(JPanel menu, JPanel content, String text, String card) {
        JButton btn = new JButton(text);
        
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 14f));
        btn.setForeground(Color.WHITE);

        JPanel wrapper = new JPanel(new BorderLayout());
       
        wrapper.setOpaque(true);
        wrapper.setBackground(new Color(0, 51, 102));
        if (card.equals("ADD_STUDENT")) {
            currentWrapper=wrapper; currentBtn=btn;
           currentWrapper.setBackground(new Color(255,255,255));
            currentBtn.setForeground(Color.BLACK);
        }
        wrapper.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        wrapper.add(btn, BorderLayout.CENTER);
        wrapper.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        
        // ============================hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (wrapper!=currentWrapper){
                wrapper.setBackground(new Color(0,0,0));
                wrapper.repaint();}
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (wrapper!=currentWrapper){
                wrapper.setBackground(new Color(0, 51, 102));
                wrapper.repaint();
                }
            }
        });

        btn.addActionListener(e -> {
            wrapper.setBackground(new Color(0,0,0));
            CardLayout cl = (CardLayout) content.getLayout();
            currentBtn.setForeground(Color.WHITE);
            currentWrapper.setBackground(new Color(0, 51, 102));
            currentWrapper.repaint();
            currentWrapper=wrapper;
            currentBtn=btn;
            currentWrapper.setBackground(new Color(255,255,255));
            currentBtn.setForeground(Color.BLACK);
            currentWrapper.repaint();
            cl.show(content, card);
        });

        menu.add(wrapper);
    }
    private void showInside(JPanel content, boolean flag)
    {
        CardLayout cl = (CardLayout) content.getLayout();
        if (flag==true){
            //i will be inside
            HeaderLabel.setText("Welcome, "+UserName);
            HeaderLabel.setVisible(true);
            cl.show(content, "FEED");
            menu.setVisible(true);
            Headerbtn.setText("LogOut");
            currentPage="Inside";}
        else
        {
            //i will be in login
            HeaderLabel.setVisible(false);
            cl.show(content, "LOG_IN");
            menu.setVisible(false);
            Headerbtn.setText("SignUp");
            currentPage="Login";
        }
    }
    private JPanel addLogInPanel(JPanel content){
        JPanel p = new TemplatePanel(null);

        JTextField u = new PrimaryField();
        JPasswordField e = new PrimaryPasswordField();
        JButton b = new PrimaryButton("Log In");
  
        addTitle(p,"Enter Credentials: ",110,80);
        addLabel(p, "User ID:", 110, 150);
        addLabel(p, "Password:", 110, 190);

        u.setBounds(180, 152, 200, 25);
        e.setBounds(180, 192, 200, 25);
        b.setBounds(180, 250, 150, 30);

        b.addActionListener(a -> {
            //!check that the credentials are correct
//            Student s = system.addStudent(u.getText(), e.getText());
//            JOptionPane.showMessageDialog(this, "Logged in Successfuly!");
            showInside(content,true);
            u.setText("");
            e.setText("");
        });

        p.add(u);
        p.add(e);
        p.add(b);
        return p;
    }
        private JPanel addSignUpPanel(JPanel content){
        JPanel p = new TemplatePanel(null);

        JTextField u = new PrimaryField();
        JPasswordField e = new PrimaryPasswordField();
        JButton b = new PrimaryButton("Sign Up");
  
        addTitle(p,"Create Account: ",110,80);
        addLabel(p, "User ID:", 110, 150);
        addLabel(p, "Password:", 110, 190);

        u.setBounds(180, 152, 200, 25);
        e.setBounds(180, 192, 200, 25);
        b.setBounds(180, 250, 150, 30);

        b.addActionListener(a -> {
            //!create user
            Student s = system.addStudent(u.getText(), e.getText());
            JOptionPane.showMessageDialog(this, "Signed Up Successfuly!");
            showInside(content,true);
            u.setText("");
            e.setText("");
        });

        p.add(u);
        p.add(e);
        p.add(b);
        return p;
    }
    private JPanel addStudentPanel(){
        JPanel p = new TemplatePanel(null);

        JTextField u = new PrimaryField();
        JTextField e = new PrimaryField();
        JButton b = new PrimaryButton("Add Student");
  
        addTitle(p,"Enter Student Data: ",110,80);
        addLabel(p, "Username:", 110, 150);
        addLabel(p, "Email:", 110, 190);

        u.setBounds(180, 152, 200, 25);
        e.setBounds(180, 192, 200, 25);
        b.setBounds(180, 250, 150, 30);

        b.addActionListener(a -> {
            //!throw an exception if either of them is empty
            Student s = system.addStudent(u.getText(), e.getText());
            JOptionPane.showMessageDialog(this, "Student ID: " + s.getId());
            u.setText("");
            e.setText("");
        });

        p.add(u);
        p.add(e);
        p.add(b);
        return p;
    }

    private JPanel addInstructorPanel() {
        JPanel p = new TemplatePanel(null);

        JTextField u = new PrimaryField();
        JTextField e = new PrimaryField();
        JButton b = new PrimaryButton("Add Instructor");
        
        addTitle(p,"Enter Instructor Data: ",110,80);
        addLabel(p, "Username:", 110, 150);
        addLabel(p, "Email:", 110, 190);

        u.setBounds(180, 152, 200, 25);
        e.setBounds(180, 192, 200, 25);
        b.setBounds(180, 250, 150, 30);


        b.addActionListener(a -> {
            Instructor i = system.addInstructor(u.getText(), e.getText());
            JOptionPane.showMessageDialog(this, "Instructor ID: " + i.getId());
            u.setText("");
            e.setText("");
        });

        p.add(u);
        p.add(e);
        p.add(b);
        return p;
    }

    private JPanel addCoursePanel() {
        JPanel p = new TemplatePanel(null);

        JTextField name = new PrimaryField();
        JTextField credits = new PrimaryField();
        JTextField type = new PrimaryField();
        JTextField detail = new PrimaryField();
        JButton b = new PrimaryButton("Add Course");
        
        addTitle(p,"Enter Course Data: ",110,70);
        addLabel(p, "Name:", 110, 140);
        addLabel(p, "Credits:", 110, 180);
        addLabel(p, "Type:", 110, 220);
        addLabel(p, "Platform / Room:", 110, 260);

        name.setBounds(225, 142, 200, 25);
        credits.setBounds(225, 182, 200, 25);
        type.setBounds(225, 222, 200, 25);
        detail.setBounds(225, 262, 200, 25);
        b.setBounds(180, 320, 150, 30);

        b.addActionListener(a -> {
            Course c = system.addCourse(
                    name.getText(),
                    Integer.parseInt(credits.getText()),
                    type.getText(),
                    detail.getText());
            JOptionPane.showMessageDialog(this, "Course ID: " + c.getId());
            name.setText("");
            credits.setText("");
            type.setText("");
            detail.setText("");
        });

        p.add(name);
        p.add(credits);
        p.add(type);
        p.add(detail);
        p.add(b);
        return p;
    }

    private JPanel enrollPanel() {
        JPanel p = new TemplatePanel(null);

        JTextField s = new PrimaryField();
        JTextField c = new PrimaryField();
        JButton b = new PrimaryButton("Enroll");
        addTitle(p,"Enter Enrollment Data: ",110,80);
        addLabel(p, "Student ID:", 110, 150);
        addLabel(p, "Course ID:", 110, 190);

        s.setBounds(185, 152, 200, 25);
        c.setBounds(185, 192, 200, 25);
        b.setBounds(185, 250, 150, 30);

        b.addActionListener(a -> JOptionPane.showMessageDialog(this,
                system.enrollStudent(s.getText(), c.getText())
                        ? "Enrolled!"
                        : "Failed!"));
        s.setText("");
            c.setText("");

        p.add(s);
        p.add(c);
        p.add(b);
        return p;
    }

    private JPanel removeStudentPanel() {
        JPanel p = new TemplatePanel(null);

        JTextField id = new PrimaryField();
        JButton b = new PrimaryButton("Remove");
        
        addTitle(p,"Enter Student Data: ",110,100);
        addLabel(p, "Student ID:", 110, 170);

        id.setBounds(185, 172, 200, 25);
        b.setBounds(185, 220, 150, 30);

        b.addActionListener(a -> JOptionPane.showMessageDialog(this,
                system.removeStudent(id.getText())
                        ? "Student removed!"
                        : "Not found!"));
        p.add(id);
        p.add(b);
        return p;
    }

    private JPanel announcementPanel() {
        JPanel p = new TemplatePanel(null);

        JTextField cid = new PrimaryField();
        JTextArea text = new PrimaryTextArea(true);
        JButton b = new PrimaryButton("Post");
        
        addTitle(p,"Enter your Course: ",110,70);
        addLabel(p, "Course ID:", 110, 122);
        addLabel(p, "Announcement:", 110, 150);

        cid.setBounds(180, 124, 200, 25);
        text.setBounds(110, 180, 270, 100);
        b.setBounds(180, 300, 150, 30);

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
        JPanel p = new TemplatePanel(null);

//        JTextField sid = new JTextField();
        addTitle(p,"Student Feed:",110,70);
        JTextArea area = new PrimaryTextArea(false);
        JButton b = new PrimaryButton("View");

//        addLabel(p, "Student ID:", 30, 30);

//        sid.setBounds(140, 30, 200, 25);
        area.setBounds(110, 122, 400, 150);
        b.setBounds(230, 300, 150, 30);

        b.addActionListener(a -> {
            List<String> feed = system.getStudentFeed(LoggedUserID);
            area.setText(String.join("\n", feed));
        });

//        p.add(sid);
        p.add(area);
        p.add(b);
        return p;
    }

    private JPanel messagePanel() {
        JPanel p = new TemplatePanel(null);

//        JTextField from = new PrimaryField();
        JTextField to = new PrimaryField();
        JTextArea msg = new PrimaryTextArea(true);
        JButton b = new PrimaryButton("Send");

//        addLabel(p, "From ID:", 30, 30);
//i removed this 3shan since en el user hykon signed in fa msh mhtagin from who
// okie dokie
        addTitle(p,"Enter your Recipient: ",110,70);
        addLabel(p, "Recipient ID:", 110, 122);
        addLabel(p, "Announcement:", 110, 150);
        
//        from.setBounds(140, 30, 200, 25);
        to.setBounds(195, 124, 200, 25);
        msg.setBounds(110, 180, 285, 100);
        b.setBounds(180, 300, 150, 30);

        b.addActionListener(a -> JOptionPane.showMessageDialog(this,
                system.sendMessage(LoggedUserID, to.getText(), msg.getText())
                        ? "Sent!"
                        : "Failed!"));

//        p.add(from);
        p.add(to);
        p.add(msg);
        p.add(b);
        return p;
    }

    private JPanel listPanel() {
        JPanel p = new TemplatePanel(null);
        addTitle(p,"List:",110,70);
        JTextArea area = new PrimaryTextArea(false);
        JButton b = new PrimaryButton("Refresh");

        area.setBounds(110, 122, 400, 150);
        b.setBounds(230, 300, 150, 30);

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
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        p.add(l);
    }
    private void addTitle(JPanel p, String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y,300, 50);
        l.setFont(new Font("Segoe UI", Font.BOLD, 19));
        p.add(l);
    }
}

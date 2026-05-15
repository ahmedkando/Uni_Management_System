package com.mycompany.advanced_project.UI;

import com.mycompany.advanced_project.Classes.Course;
import com.mycompany.advanced_project.Classes.Instructor;
import com.mycompany.advanced_project.Classes.Student;
import com.mycompany.advanced_project.service.System_controller;
import javafx.animation.TranslateTransition;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;


public class UniversityGUI extends Application {

    private static System_controller system;
    public static void setSystem(System_controller sys) {
        system = sys;
    }
    private Pane currentWrapper;
    private Button currentBtn;
    private String LoggedUserID="0"; //will be used for authorizing this person is admin or student or instructor
    private String currentPage="Login";//Login Signup Inside
    private String UserName="Sondos";
    Button Headerbtn;
    Label HeaderLabel;
    private ExecutorService  rainExecutor;
    private volatile boolean isRaining = false;
    private VBox menu = new VBox(9);
    TranslateTransition slide = new TranslateTransition();
    private Pane bubbles=new Pane();
    private void startBinaryRain() {
    if (isRaining) return; 
    isRaining = true;
    
    rainExecutor = Executors.newSingleThreadExecutor();
    rainExecutor.submit(() -> {
        while (isRaining) {
            try {
                String bit = Math.random() > 0.5 ? "1" : "0";
                double xPos = Math.random() * 170;
                
                Color[] techColors = {Color.CYAN, Color.LIME, Color.WHITE};
                Color selected = techColors[(int)(Math.random() * techColors.length)];
                Color finalColor = selected.deriveColor(0, 1, 1, 0.15);

                Platform.runLater(() -> {
                    spawnSingleCharacter(bit, xPos, finalColor);
                });
                
                Thread.sleep(100); 

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    });
}
    public void stopRainAnimation() {
    isRaining = false; // This breaks the 'while' loop in the background thread
    
    if (rainExecutor != null) {
        rainExecutor.shutdownNow(); // Immediately kills the background worker
    }
    
    // UI Cleanup
    Platform.runLater(() -> {
        bubbles.getChildren().clear(); // Instantly removes all 1s and 0s from the screen
    });
}
private void spawnSingleCharacter(String content, double x, Color color) {
    Text character = new Text(content);
    character.setFont(Font.font("Monospaced", 15));
    character.setFill(color);
    character.setX(x);
    character.setY(-50);
    bubbles.getChildren().add(character);

    TranslateTransition fall = new TranslateTransition(Duration.seconds(6 + Math.random() * 10), character);
    fall.setFromY(-50);
    fall.setToY(520);
    fall.setOnFinished(e -> bubbles.getChildren().remove(character));
    fall.play();
}

    public void start(Stage primaryStage) {
        BorderPane mainLayout = new BorderPane();
        
        primaryStage.setTitle("University Management System");
        primaryStage.centerOnScreen();

// ============================HEADER
        HeaderPanel header = new HeaderPanel(); 
        addHeaderLabel(header);
        mainLayout.setTop(header);
    
          
// ============================MainContent
        StackPane content = new TemplatePanel();
        
        content.setAlignment(Pos.CENTER);
        addHeaderButton(content, header);
        
        content.getChildren().addAll(
        addSignUpPanel(content),
        addStudentPanel(),
        addInstructorPanel(),
        addCoursePanel(),
        enrollPanel(),
        removeStudentPanel(),
        announcementPanel(),
        feedPanel(),
        messagePanel(),
        listPanel(),
        addLogInPanel(content)        
    );
        
        content.getChildren().forEach(node -> node.setVisible("LOG_IN".equals(node.getId())));
        
        mainLayout.setCenter(content); 

        
// ============================SideMenu     
        BorderPane sideMenu = new SidePanel();
        Pane temp = new StackPane();
        menu.setPrefWidth(180);
    
        
        addMenuButton(content, "Add Student", "ADD_STUDENT");
        addMenuButton(content, "Add Instructor", "ADD_INSTRUCTOR");
        addMenuButton(content, "Add Course", "ADD_COURSE");
        addMenuButton(content, "Enroll Student", "ENROLL");
        addMenuButton(content, "Remove Student", "REMOVE");
        addMenuButton(content, "Announcement", "ANNOUNCE");
        addMenuButton(content, "Student Feed", "FEED");
        addMenuButton(content, "Send Message", "MESSAGE");
        addMenuButton(content, "List All", "LIST");
        
//        Pane leftanimation= new Pane();
    startBinaryRain();
    temp.getChildren().addAll(bubbles,menu);
    sideMenu.setLeft(temp);
    menu.setVisible(false); 
    sideMenu.setPrefWidth(182);
    mainLayout.setLeft(sideMenu);

Scene scene = new Scene(mainLayout, 850, 500);
    primaryStage.setScene(scene);
    primaryStage.show();

}

    private void addHeaderLabel(HeaderPanel header)
    {
        HeaderLabel = new Label("SONDOS");
        HeaderLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        HeaderLabel.setTextFill(Color.WHITE);
        header.setLeft(HeaderLabel); 
        HeaderLabel.setVisible(false);
    }
    
    private void addHeaderButton(StackPane content, HeaderPanel header)
    {
        Headerbtn = new PrimaryButton("SignUp");
        Headerbtn.setPrefSize(100, 60);
        header.setRight(Headerbtn);
        Headerbtn.setOnAction(e -> {
    switch (currentPage) {
        case "Inside":
            showInside(content, false);
            break;
            
        case "Login":
            content.getChildren().forEach(node -> node.setVisible("SIGN_UP".equals(node.getId())));
            currentPage = "Signup";
            Headerbtn.setText("LogIn");
            break;
            
        case "Signup":
            content.getChildren().forEach(node -> node.setVisible("LOG_IN".equals(node.getId())));
            currentPage = "Login";
            Headerbtn.setText("SignUp");
            break;

        default:
            showInside(content, false);
            break;
    }
});
    }
    
    private void addMenuButton(StackPane content, String text, String card) {
        Button btn = new Button(text);
        
        btn.setBackground(Background.EMPTY); 
        btn.setFocusTraversable(false);      
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btn.setTextFill(Color.WHITE);

        StackPane wrapper = new StackPane();
       
        wrapper.setBackground(new Background(new BackgroundFill(Color.rgb(0, 51, 102), null, null)));
        
        if (card.equals("FEED")) {
        currentWrapper = wrapper; 
        currentBtn = btn;
        currentWrapper.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        currentBtn.setTextFill(Color.BLACK); 
    }
        wrapper.setPadding(new Insets(8));
        wrapper.getChildren().add(btn);
        wrapper.setCursor(Cursor.HAND); 
        
        
        // ============================hover effect
        wrapper.setOnMouseEntered(e -> {
        if (wrapper != currentWrapper) {
            wrapper.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        }
    });

    wrapper.setOnMouseExited(e -> {
        if (wrapper != currentWrapper) {
            wrapper.setBackground(new Background(new BackgroundFill(Color.rgb(0, 51, 102), null, null)));
        }
    });

        btn.setOnAction(e -> {
            currentWrapper.setBackground(new Background(new BackgroundFill(Color.rgb(0, 51, 102), null, null)));
            currentBtn.setTextFill(Color.WHITE);
            
            currentWrapper=wrapper;
            currentBtn=btn;
            
            currentWrapper.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            currentBtn.setTextFill(Color.BLACK);
            
            content.getChildren().forEach(node -> {
        if (card.equals(node.getId())) {
            node.setVisible(true);
            node.toFront();
        } else {
            node.setVisible(false);
        }
    });
        });

        menu.getChildren().add(wrapper);
    }
private ExecutorService labelExecutor;
private volatile boolean isLabelAnimating = false;

public void startLabelAnimation(Label headerLabel) {
    if (isLabelAnimating) return;
    isLabelAnimating = true;

    labelExecutor = Executors.newSingleThreadExecutor();
    
    labelExecutor.submit(() -> {
        double startX = headerLabel.getTranslateX();
        double currentX = startX;
        double targetX = startX + 40; 
        double step = 0.8; 
        boolean movingRight = true;

        while (isLabelAnimating) {
            try {
                if (movingRight) {
                    currentX += step;
                    if (currentX >= targetX) movingRight = false;
                } else {
                    currentX -= step;
                    if (currentX <= startX - 10) movingRight = true;
                }
                final double finalX = currentX;
                Platform.runLater(() -> headerLabel.setTranslateX(finalX));

                Thread.sleep(16); 

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    });
}

public void stopLabelAnimation() {
    isLabelAnimating = false; 
    
    if (labelExecutor != null) {
        labelExecutor.shutdownNow(); 
    }
}
    private void showInside(StackPane content, boolean flag)
    {
        if (flag==true){
            //i will be inside
            HeaderLabel.setPadding(new Insets(13, 0, 0, 0));
            HeaderLabel.setText("Welcome, "+UserName);
            HeaderLabel.setVisible(true);
            startLabelAnimation(HeaderLabel);
            slide.play();
            stopRainAnimation();
            menu.setVisible(true);
            Headerbtn.setText("LogOut");
            currentPage="Inside";
            content.getChildren().forEach(node -> {
            if ("FEED".equals(node.getId())) {
                node.setVisible(true);
                node.toFront();
            } else {
                node.setVisible(false);
            }
        });
           
        }
        else
        {
            //i will be in login
            menu.setVisible(false);
            startBinaryRain();
            Headerbtn.setText("SignUp");
            currentPage="Login";
            slide.stop();
            HeaderLabel.setVisible(false);
            content.getChildren().forEach(node -> {
            if ("LOG_IN".equals(node.getId())) {
                node.setVisible(true);
                node.toFront();
            } else {
                node.setVisible(false);
            }
        });
            
        }
    }
    private VBox createFormContainer() {
        VBox form = new VBox(18);
        form.setAlignment(Pos.TOP_CENTER);
        form.setPadding(new Insets(30));
        return form;
    }
    private Pane addLogInPanel(StackPane content){
        StackPane p = new TemplatePanel();
        p.setPadding(new Insets(100, 0, 0, 50));
        p.setId("LOG_IN");

        TextField u = new PrimaryField();
        PasswordField e = new PrimaryPasswordField();
        Button b = new PrimaryButton("Log In");

         VBox form = createFormContainer();
        addTitle(form,"Enter Credentials:");
        addLabel(form, "User ID:", 0, 0, u);
        addLabel(form, "Password:", 0, 0, e);

        HBox buttonContainer = new HBox(b);
        buttonContainer.setAlignment(Pos.CENTER);
        form.getChildren().add(buttonContainer);

        b.setOnAction(a -> {
            showInside(content,true);
            u.setText("");
            e.setText("");
        });

        p.getChildren().add(form);
        return p;
    }
        private StackPane addSignUpPanel(StackPane content){
        StackPane p = new TemplatePanel();
        p.setPadding(new Insets(100, 0, 0, 50));
        p.setId("SIGN_UP");

        TextField u = new PrimaryField();
        PasswordField e = new PrimaryPasswordField();
        Button b = new PrimaryButton("Sign Up");

        VBox form = createFormContainer();
        addTitle(form,"Create Account:");
        addLabel(form, "User ID:", 0, 0, u);
        addLabel(form, "Password:", 0, 0, e);

        HBox buttonContainer = new HBox(b);
        buttonContainer.setAlignment(Pos.CENTER);
        form.getChildren().add(buttonContainer);

        b.setOnAction(a -> {
            Student s = system.addStudent(u.getText(), e.getText());
            showAlert( "Signed Up Successfully!");
            showInside(content,true);
            u.setText("");
            e.setText("");
        });

        p.getChildren().add(form);
        return p;
    }
    private StackPane addStudentPanel(){
        StackPane p = new TemplatePanel();
        p.setPadding(new Insets(100, 0, 0, 50));
        p.setId("ADD_STUDENT");

        TextField u = new PrimaryField();
        TextField e = new PrimaryField();
        Button b = new PrimaryButton("Add Student");

        VBox form = createFormContainer();
        addTitle(form,"Enter Student Data:");
        addLabel(form, "Username:", 0, 0, u);
        addLabel(form, "Email:", 0, 0, e);

        HBox buttonContainer = new HBox(b);
        buttonContainer.setAlignment(Pos.CENTER);
        form.getChildren().add(buttonContainer);

        b.setOnAction(a -> {
            Student s = system.addStudent(u.getText(), e.getText());
            showAlert( "Student ID: " + s.getId());
            u.setText("");
            e.setText("");
        });
        p.getChildren().add(form);
        return p;
    }

    private StackPane addInstructorPanel() {
        StackPane p = new TemplatePanel();
        p.setId("ADD_INSTRUCTOR");
        p.setPadding(new Insets(100, 0, 0, 50));

        TextField u = new PrimaryField();
        TextField e = new PrimaryField();
        Button b = new PrimaryButton("Add Instructor");

        VBox form = createFormContainer();
        addTitle(form,"Enter Instructor Data:");
        addLabel(form, "Username:", 0, 0, u);
        addLabel(form, "Email:", 0, 0, e);

        HBox buttonContainer = new HBox(b);
        buttonContainer.setAlignment(Pos.CENTER);
        form.getChildren().add(buttonContainer);

        b.setOnAction(a -> {
            Instructor i = system.addInstructor(u.getText(), e.getText(),200.0);
            showAlert( "Instructor ID: " + i.getId());
            u.setText("");
            e.setText("");
        });

        p.getChildren().add(form);
        return p;
    }

    private StackPane addCoursePanel() {
        StackPane p = new TemplatePanel();
        p.setId("ADD_COURSE");
        p.setPadding(new Insets(65, 0, 0, 50));

        TextField name = new PrimaryField();
        TextField credits = new PrimaryField();
        TextField type = new PrimaryField();
        TextField detail = new PrimaryField();
        Button b = new PrimaryButton("Add Course");

        VBox form = createFormContainer();
        addTitle(form,"Enter Course Data:");
        addLabel(form, "Name:", 0, 0, name);
        addLabel(form, "Credits:", 0, 0, credits);
        addLabel(form, "Type:", 0, 0, type);
        addLabel(form, "Platform / Room:", 0, 0, detail);

        HBox buttonContainer = new HBox(b);
        buttonContainer.setAlignment(Pos.CENTER);
        form.getChildren().add(buttonContainer);

        b.setOnAction(a -> {
            Course c = system.addCourse(
            name.getText(),
            Integer.parseInt(credits.getText()),
            type.getText(),
            detail.getText());
            showAlert( "Course ID: " + c.getId());
            name.setText("");
            credits.setText("");
            type.setText("");
            detail.setText("");
        });

        p.getChildren().add(form);
        return p;
    }

    private StackPane enrollPanel() {
        
        StackPane p = new TemplatePanel();
        p.setId("ENROLL");
        p.setPadding(new Insets(100, 0, 0, 50));

        TextField s = new PrimaryField();
        TextField c = new PrimaryField();
        Button b = new PrimaryButton("Enroll");

        VBox form = createFormContainer();
        addTitle(form,"Enter Enrollment Data:");
        addLabel(form, "Student ID:", 0, 0, s);
        addLabel(form, "Course ID:", 0, 0, c);

        HBox buttonContainer = new HBox(b);
        buttonContainer.setAlignment(Pos.CENTER);
        form.getChildren().add(buttonContainer);

        b.setOnAction(a -> showAlert(
                system.enrollStudent(Integer.parseInt(s.getText()), Integer.parseInt(c.getText()))
                        ? "Enrolled!"
                        : "Failed!"));

        p.getChildren().add(form);
        return p;
    }

    private StackPane removeStudentPanel() {
        StackPane p = new TemplatePanel();
        p.setId("REMOVE");
        p.setPadding(new Insets(125, 0, 0, 50));

        TextField id = new PrimaryField();
        Button b = new PrimaryButton("Remove");

        VBox form = createFormContainer();
        addTitle(form,"Enter Student Data:");
        addLabel(form, "Student ID:", 0, 0, id);

        HBox buttonContainer = new HBox(b);
        buttonContainer.setAlignment(Pos.CENTER);
        form.getChildren().add(buttonContainer);

        b.setOnAction(a -> showAlert(
                system.removeStudent(Integer.parseInt(id.getText()))
                        ? "Student removed!"
                        : "Not found!"));

        p.getChildren().add(form);
        return p;
    }

    private StackPane announcementPanel() {
        StackPane p = new TemplatePanel();
        p.setId("ANNOUNCE");
        p.setPadding(new Insets(50, 0, 0, 50));

        TextField cid = new PrimaryField();
        TextArea text = new PrimaryTextArea(true);
        Button b = new PrimaryButton("Post");

        VBox form = createFormContainer();
        addTitle(form,"Enter your Course:");
        addLabel(form, "Course ID:", 0, 0, cid);
        addLabel(form, "Announcement:", 0, 0, text);

        HBox buttonContainer = new HBox(b);
        buttonContainer.setAlignment(Pos.CENTER);
        form.getChildren().add(buttonContainer);

        b.setOnAction(a -> showAlert(
                system.postAnnouncement(Integer.parseInt(cid.getText()), text.getText())
                        ? "Posted!"
                        : "Course not found!"));

        p.getChildren().add(form);
        return p;
    }

    private StackPane feedPanel() {
        StackPane p = new TemplatePanel();
        p.setId("FEED");
        p.setPadding(new Insets(75, 0, 0, 0));

        TextArea area = new PrimaryTextArea(false);
        Button b = new PrimaryButton("View");

        VBox form = createFormContainer();
        addTitle(form,"Student Feed:");
        area.setPrefSize(400, 150);
        area.setWrapText(true);
        HBox areaContainer = new HBox(area);
        areaContainer.setAlignment(Pos.CENTER);
        form.getChildren().add(areaContainer);

        HBox buttonContainer = new HBox(b);
        buttonContainer.setAlignment(Pos.CENTER);
        form.getChildren().add(buttonContainer);

        b.setOnAction(a -> {
            List<String> feed = system.getStudentFeed(Integer.parseInt(LoggedUserID));
            area.setText(String.join("\n", feed));
        });

        p.getChildren().add(form);
        return p;
    }

    private StackPane messagePanel() {
        StackPane p = new TemplatePanel();
        p.setId("MESSAGE");
        p.setPadding(new Insets(50, 0, 0, 50));

        TextField to = new PrimaryField();
        TextArea msg = new PrimaryTextArea(true);
        Button b = new PrimaryButton("Send");

        VBox form = createFormContainer();
        addTitle(form,"Enter your Recipient:");
        addLabel(form, "Recipient ID:", 0, 0, to);
        addLabel(form, "Announcement:", 0, 0, msg);

        HBox buttonContainer = new HBox(b);
        buttonContainer.setAlignment(Pos.CENTER);
        form.getChildren().add(buttonContainer);

        b.setOnAction(a -> showAlert(
                system.sendMessage(Integer.parseInt(LoggedUserID), Integer.parseInt(to.getText()), msg.getText())
                        ? "Sent!"
                        : "Failed!"));

        p.getChildren().add(form);
        return p;
    }

    private StackPane listPanel() {
        
        StackPane p = new TemplatePanel();
        p.setId("LIST");
        p.setPadding(new Insets(75, 0, 0, 0));

        TextArea area = new PrimaryTextArea(false);
        Button b = new PrimaryButton("Refresh");

        VBox form = createFormContainer();
        addTitle(form,"List:");
        area.setPrefSize(400, 150);
        area.setWrapText(true);
        HBox areaContainer = new HBox(area);
        areaContainer.setAlignment(Pos.CENTER);
        form.getChildren().add(areaContainer);

        HBox buttonContainer = new HBox(b);
        buttonContainer.setAlignment(Pos.CENTER);
        form.getChildren().add(buttonContainer);

        b.setOnAction(a -> {
            StringBuilder sb = new StringBuilder();
            system.getAllUsers().forEach(u -> sb.append(u.getInfo()).append("\n"));
            sb.append("\n");
            system.getAllCourses().forEach(c -> sb.append(c.getInfo()).append("\n"));
            area.setText(sb.toString());
        });

        p.getChildren().add(form);
        return p;
    }

    private void addLabel(Pane p, String text, int x, int y, TextInputControl w) {
        Label l = new Label(text);
        l.setPrefWidth(140);
        l.setFont(Font.font("Segoe UI", 15));
        w.setPrefWidth(240);
        HBox temp = new HBox(10, l, w);
        temp.setAlignment(Pos.CENTER_LEFT);
        p.getChildren().add(temp);
    }
    private void addTitle(Pane p, String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 19));
        HBox titleRow = new HBox(l);
        titleRow.setAlignment(Pos.CENTER);
        p.getChildren().add(titleRow);
    }
    private void showAlert(String s)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
}

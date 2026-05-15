package com.mycompany.advanced_project.UI;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.geometry.*;

public class TemplatePanel extends StackPane {
    public TemplatePanel() {
        setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        setPadding(new Insets(0, 0, 0, 50));
    }
    
}

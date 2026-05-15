package com.mycompany.advanced_project.UI;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

public class HeaderPanel extends BorderPane {
    
    public HeaderPanel() {
        setBackground(new Background(new BackgroundFill(Color.rgb(0, 51, 102), null, null)));
        setPrefHeight(50);
        setPadding(new Insets(0, 28, 0, 28));
    }
    
}

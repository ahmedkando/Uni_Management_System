package com.mycompany.advanced_project.UI;
           
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

public class PrimaryButton extends Button{
    public PrimaryButton(String text)
    {
        super(text);
        setFont(Font.font("Arial", FontWeight.BOLD, 15));
        setTextFill(Color.WHITE);
        setBackground(new Background(new BackgroundFill(Color.rgb(77, 163, 255), null, null)));
        setCursor(Cursor.HAND);
        // ============================hover effect
        setOnMouseEntered(e -> {
                setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0), null, null)));
                
            });

            setOnMouseExited(e -> {
                setBackground(new Background(new BackgroundFill(Color.rgb(77, 163, 255), null, null)));
        });
    }
}

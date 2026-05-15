package com.mycompany.advanced_project.UI;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

public class PrimaryField extends TextField{
    private final Border focusBorder = new Border(new BorderStroke(
            Color.rgb(77, 163, 255), 
            BorderStrokeStyle.SOLID, 
            CornerRadii.EMPTY, 
            new BorderWidths(1)
    ));

    private final Border defaultBorder = new Border(new BorderStroke(
            Color.GRAY, 
            BorderStrokeStyle.SOLID, 
            CornerRadii.EMPTY, 
            new BorderWidths(1)
    ));

    public PrimaryField(){
        
        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null)));
        setBorder(defaultBorder);

        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setBorder(focusBorder);
            } else {
                setBorder(defaultBorder);
            }
        });
    }
}

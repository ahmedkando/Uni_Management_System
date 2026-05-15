package com.mycompany.advanced_project.UI;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

public class PrimaryTextArea extends TextArea{
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

    public PrimaryTextArea(boolean editable) {
        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null)));
        setBorder(defaultBorder);

        if (editable) {
            focusedProperty().addListener((observable, oldValue, newValue) -> {
                setBorder(newValue ? focusBorder : defaultBorder);
            });
        } else {
            setEditable(false);
            setFocusTraversable(false);
            setMouseTransparent(true);
        }
    }
}

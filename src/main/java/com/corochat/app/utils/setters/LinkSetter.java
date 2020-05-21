package com.corochat.app.utils.setters;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

public class LinkSetter {
    private static final String COLOR_LINK = "#000000";
    private static final String COLOR_BASIS = "#948e8e";

    public static void setAsLink(final Label label) {
        label.setCursor(Cursor.HAND);
        label.setTextFill(Paint.valueOf(COLOR_LINK));
    }

    public static void unsetAsLink(final Label label) {
        label.setTextFill(Paint.valueOf(COLOR_BASIS));
    }
}

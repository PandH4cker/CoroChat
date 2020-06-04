package com.corochat.app.client.utils.setters;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

/**
 * <h1>The LinkSetter object</h1>
 * <p>
 *     This class helps to set as a link a label
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Raphael Dray
 * @author Thierry Khamphousone
 * @version 0.0.4
 * @since 0.0.2
 */
public class LinkSetter {
    /**
     * Black color for the link hover
     */
    private static final String COLOR_LINK = "#000000";
    /**
     * Shady gray for the basis color of the link
     */
    private static final String COLOR_BASIS = "#948e8e";

    /**
     * Set as a link a label
     * @param label The label to be set
     * @see Label
     * @see Paint
     */
    public static void setAsLink(final Label label) {
        label.setCursor(Cursor.HAND);
        label.setTextFill(Paint.valueOf(COLOR_LINK));
    }

    /**
     * Unset as a link a label
     * @param label The label to be unset
     * @see Paint
     */
    public static void unsetAsLink(final Label label) {
        label.setTextFill(Paint.valueOf(COLOR_BASIS));
    }
}

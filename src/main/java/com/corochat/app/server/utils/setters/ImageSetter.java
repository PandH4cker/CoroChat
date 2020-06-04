package com.corochat.app.server.utils.setters;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Paths;

/**
 * <h1>The ImageSetter object</h1>
 * <p>
 *     This class helps to set an image from resources into an ImageView
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Raphael Dray
 * @version 0.0.4
 * @since 0.0.1
 */
public class ImageSetter {
    /**
     * The path to the resources
     */
    private static final String PATH_TO_RESOURCES = "src/main/resources/images/";

    /**
     * Helps to set an image into an ImageView
     * @param image The ImageView that the image has to be set
     * @param imageName The image name
     * @see ImageView
     * @see Paths
     * @see Image
     */
    public static void set(final ImageView image, final String imageName) {
        image.setImage(new Image(Paths.get(PATH_TO_RESOURCES + imageName).toUri().toString()));
    }
}

package com.corochat.app.utils.setters;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Paths;

public class ImageSetter {
    private static final String PATH_TO_RESOURCES = "src/main/resources/images/";

    public static void set(final ImageView image, final String imageName) {
        image.setImage(new Image(Paths.get(PATH_TO_RESOURCES + imageName).toUri().toString()));
    }
}

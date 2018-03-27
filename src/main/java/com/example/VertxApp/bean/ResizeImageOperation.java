package com.example.VertxApp.bean;

import io.vertx.core.eventbus.impl.MessageImpl;

import java.awt.image.BufferedImage;

public class ResizeImageOperation {

    public String filename;
    public BufferedImage image;

    public String getFilename() {
        return filename;
    }

    public BufferedImage getImage() {
        return image;
    }

    public ResizeImageOperation(String filename, BufferedImage image) {
        this.filename = filename;
        this.image = image;
    }
}

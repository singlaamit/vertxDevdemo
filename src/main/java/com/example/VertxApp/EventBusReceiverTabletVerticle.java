package com.example.VertxApp;

import com.example.VertxApp.bean.ResizeImageOperation;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.eventbus.impl.MessageImpl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static com.example.VertxApp.MainVerticle.resize;

public class EventBusReceiverTabletVerticle extends AbstractVerticle {

    public void start(Future<Void> startFuture) {

        MessageConsumer<String> consumer=vertx.eventBus().consumer("tablet", message -> {
            ResizeImageOperation test = ((ResizeImageOperation) ((MessageImpl)message).body());
            try{
                BufferedImage resized = resize(test.getImage(), 500, 500);
                final String renditionDestination = System.getenv("RENDITION_HOME");
                File output = new File(renditionDestination +"/tablet/"+test.getFilename());
                ImageIO.write(resized, "png", output);
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
        });
    }
}

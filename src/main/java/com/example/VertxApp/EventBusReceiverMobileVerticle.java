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
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static com.example.VertxApp.MainVerticle.resize;

public class EventBusReceiverMobileVerticle extends AbstractVerticle {

    public void start(Future<Void> startFuture) {

            MessageConsumer<String> consumer=vertx.eventBus().consumer("mobile", message -> {
            ResizeImageOperation test = ((ResizeImageOperation) ((MessageImpl)message).body());
            try{

                ResizeImageWithSize(test.getImage(),500,500,test,"fast");
                ResizeImageWithSize(test.getImage(),500,500,test,"medium");
                ResizeImageWithSize(test.getImage(),500,500,test,"slow");
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
        });
    }

     public void ResizeImageWithSize(BufferedImage image, int Height, int Width,ResizeImageOperation resizeImageOperation, String path) throws IOException {
         BufferedImage resized = resize(image, Height, Width);
         final String renditionDestination = System.getenv("RENDITION_HOME");

         File output = new File(renditionDestination +"/mobile/"+path+"/"+resizeImageOperation.getFilename());
         ImageIO.write(resized, "png", output);
     }

}

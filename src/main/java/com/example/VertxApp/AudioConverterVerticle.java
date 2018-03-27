package com.example.VertxApp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Files;


public class AudioConverterVerticle extends AbstractVerticle {

    public void start() throws Exception{
        MessageConsumer<String> consumer=vertx.eventBus().consumer("audio", handler -> {
            JsonObject jb=new JsonObject(handler.body()) ;
            String input=jb.getString("upload");
            String output=jb.getString("output");
            try{
                String path=System.getenv("RENDITION_HOME")+"/audio/"+output;
                byte[] bFile = Files.readAllBytes(new File(input).toPath());
                FileOutputStream f=new FileOutputStream(new File(path));
                f.write(bFile);

            }catch (Exception e){

            }
            handler.reply("done");
        });
    }


}



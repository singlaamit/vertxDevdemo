package com.example.VertxApp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

public class VideoConverterVerticle extends AbstractVerticle {

    public void start() throws Exception{
        MessageConsumer<String> consumer=vertx.eventBus().consumer("video", handler -> {
            JsonObject jb=new JsonObject(handler.body()) ;
            String input=jb.getString("upload");
            String output=jb.getString("output");
            try{
                java.lang.Runtime.getRuntime().exec("ffmpeg -i "+input+" "+ System.getenv("RENDITION_HOME")+"/video/"+output);
            }catch (Exception e){

            }
            handler.reply("done");
        });
    }


}



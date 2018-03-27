package com.example.VertxApp;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class MyImage
{
  public static void main(String args[])throws IOException {
    int width = 963;    //width of the image
    int height = 640;   //height of the image
    BufferedImage image = null;
    File f = null;

    //read image
    try{
      f = new File("D:\\Image\\Taj.jpg"); //image file path
      image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      image = ImageIO.read(f);
      System.out.println("Reading complete.");
    }catch(IOException e){
      System.out.println("Error: "+e);
    }

    //write image
    try{
      f = new File("D:\\Image\\Output.jpg");  //output file path
      ImageIO.write(image, "jpg", f);
      System.out.println("Writing complete.");
    }catch(IOException e){
      System.out.println("Error: "+e);
    }
  }//main() ends here
}//class ends here

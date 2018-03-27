package com.example.VertxApp;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.example.VertxApp.MyVideoListener;
import sun.java2d.pipe.OutlineTextRenderer;

import java.io.PrintStream;

public class MediaConvertor {
    private static final Integer WIDTH = 640;
    private static final Integer HEIGHT = 360;

    public  String INPUT_FILE = "";
    public  String OUTPUT_FILE = "";

    MediaConvertor(String INPUT_FILE,String OUTPUT_FILE){
        this.INPUT_FILE=INPUT_FILE;
        this.OUTPUT_FILE=OUTPUT_FILE;
    }

    public void mediaconverterfunction() {
        PrintStream ps=System.out;
        // create custom listeners
        MyVideoListener myVideoListener = new MyVideoListener(WIDTH, HEIGHT);
        Resizer resizer = new Resizer(WIDTH, HEIGHT);

        // reader
        IMediaReader reader = ToolFactory.makeReader(INPUT_FILE);
        ps.println("helloooo"+INPUT_FILE);
        ps.println("helloooo"+OUTPUT_FILE);
        reader.addListener(resizer);

        // writer
        IMediaWriter writer = ToolFactory.makeWriter(OUTPUT_FILE, reader);
        resizer.addListener(writer);
        writer.addListener(myVideoListener);

        // show video when encoding
        reader.addListener(ToolFactory.makeViewer(true));

        while (reader.readPacket() == null) {
            // continue coding
        }
    }

}

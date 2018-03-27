package com.example.VertxApp.codec;

import com.example.VertxApp.bean.ResizeImageOperation;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ResizeImageOperationCodec implements MessageCodec<ResizeImageOperation, ResizeImageOperation> {
    @Override
    public void encodeToWire(Buffer buffer, ResizeImageOperation resizeImageOperation) {
        // Easiest ways is using JSON object
        JsonObject jsonToEncode = new JsonObject();
        jsonToEncode.put("filename", resizeImageOperation.filename);
        jsonToEncode.put("image", resizeImageOperation.getImage());

        // Encode object to string
        String jsonToStr = jsonToEncode.encode();

        // Length of JSON: is NOT characters count
        int length = jsonToStr.getBytes().length;

        // Write data into given buffer
        buffer.appendInt(length);
        buffer.appendString(jsonToStr);
    }

    @Override
    public ResizeImageOperation decodeFromWire(int position, Buffer buffer) {
        // My custom message starting from this *position* of buffer
        int _pos = position;

        // Length of JSON
        int length = buffer.getInt(_pos);

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        String jsonStr = buffer.getString(_pos+=4, _pos+=length);
        JsonObject contentJson = new JsonObject(jsonStr);
        BufferedImage image = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(contentJson.getBinary("image")));
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        // We can finally create custom message object
        return new ResizeImageOperation(contentJson.getString("filename"), image);
    }

    @Override
    public ResizeImageOperation transform(ResizeImageOperation resizeImageOperation) {
        return resizeImageOperation;
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}

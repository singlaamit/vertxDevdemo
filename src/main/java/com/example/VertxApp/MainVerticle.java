package com.example.VertxApp;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.nio.file.Files;

public class MainVerticle extends AbstractVerticle {

    HttpServer httpServer;
    Router router;
    HttpClient httpClient;
    HandlebarsTemplateEngine engine;
    SQLConnection connection;
    String encodedImage = "";

    @Override
    public void start() throws Exception {

        httpServer = vertx.createHttpServer();
        router = Router.router(vertx);
        httpClient = vertx.createHttpClient();
        engine = HandlebarsTemplateEngine.create();

        router.route().handler(BodyHandler.create().setUploadsDirectory("uploads"));

        router.route("/gagan/garg").handler(routingContext -> {

            HttpServerResponse serverResponse = routingContext.response();

            final String url = "****************";
            httpClient.getAbs(url, response -> {

                if (response.statusCode() != 200) {
                    System.err.println("fail");
                } else {

                    response.bodyHandler(res1 -> {
                        serverResponse.putHeader("content-type", "application/json").end(res1);
                    });

                    engine.render(routingContext, "templates/index.hbs", res -> {
                        if (res.succeeded()) {
                            serverResponse.setChunked(true);
                            serverResponse.write(res.result());
                            routingContext.put("body", response.bodyHandler(res1 -> {
                                serverResponse.putHeader("content-type", "application/json").end(res1);
                            }));
                        } else {
                            routingContext.fail(res.cause());
                        }
                    });
                }

            }).end();

        });


        router.get("/template/handler").handler(ctx -> {

            engine.render(ctx, "templates/index.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });

        });

        router.get("/template/submit_qaform").handler(ctx -> {

            engine.render(ctx, "templates/submit_qa.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });

        });


        router.post("/abc").handler(ctx -> {

            HttpServerRequest hr = ctx.request();
            String name = hr.params().get("firstname");
            String phone = hr.params().get("phone");
            String email = hr.params().get("email");
            String question = hr.params().get("question");

            for (FileUpload fu : ctx.fileUploads()) {

                try {
                    File f = new File(fu.uploadedFileName());
                    BufferedImage image = ImageIO.read(f);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(image, "png", baos);
                    byte[] res = baos.toByteArray();
                    encodedImage = Base64.encode(baos.toByteArray());

                    // ctx.response().end(encodedImage);

                    // String image = hr.params().get("fileToUpload");
                    if (!encodedImage.equalsIgnoreCase("")) {

                        saveInDatabase(ctx.response(), name, phone, email, question, encodedImage);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        httpServer.requestHandler(router::accept).listen(5200);

    }

    private void fileUpload(RoutingContext rc, HttpServerResponse response) {

        response.putHeader("Content-Type", "text/plain");
        response.setChunked(true);

        for (FileUpload f : rc.fileUploads()) {
            //  System.out.println("f");
            rc.response().write("Filename: " + f.fileName());
            rc.response().write("\n");
            rc.response().write("Size: " + f.size());
        }

        response.end();

    }

    private void saveInDatabase(HttpServerResponse response, String name, String phone, String email, String
        question, String imageFile) {

        JsonObject mySQLClientConfig = new JsonObject()
            .put("host", "@@@@@@@@@@@")
            .put("username", "@@@@@@@@@")
            .put("password", "@@@@@@@@@")
            .put("database", "@@@@@@@@@")
            .put("charset", "@@@@@@@@@");
        SQLClient mySQLClient = MySQLClient.createShared(vertx, mySQLClientConfig);

        mySQLClient.getConnection(res -> {
            if (res.succeeded()) {

                connection = res.result();

                connection.execute("insert into tbl_vertx_demo (name,email,phone,question,image) values ('" + name + "', '" + email + "','" + phone + "','" + question + "','" + imageFile + "')", r -> {
                    if (r.succeeded()) {
                        connection.query("select name,email,phone,question,image from tbl_vertx_demo", show -> {

                            ResultSet rs = new ResultSet();
                            rs = show.result();
                            response.putHeader("content-type", "application/json").end(rs.getResults().toString());

                        });
                    } else {
                        response.end(String.valueOf(r.result()));
                    }
                });

            } else {

            }
        });

    }

}

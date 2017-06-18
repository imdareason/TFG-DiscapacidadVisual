package com.gettingstarted.erik.tfg_appdiscvisual;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import java.io.IOException;

/**
 * Created by Erik on 18/06/2017.
 */

public class RequestManager extends Thread {




    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    private String colorName;

    public String getColorToGet() {
        return colorToGet;
    }

    public void setColorToGet(String colorToGet) {
        this.colorToGet = colorToGet;
    }

    private String colorToGet;

    static final String baseURL = "http://www.colorhexa.com/";

    public void run() {
        Document doc;
        try {
            String totalURL = baseURL+colorToGet;
            doc = Jsoup.connect(totalURL).get();
            Node child = doc.childNode(doc.childNodeSize()-1);
            child = child.childNode(3);
            child = child.childNode(5);
            child = child.childNode(5);
            child = child.childNode(1);
            child = child.childNode(1);
            child = child.childNode(11);
            child = child.childNode(1);
            child = child.childNode(2);
            colorName = Jsoup.parse(child.toString()).text();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


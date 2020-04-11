/**
 * @author Peter Ho
 * @date   04/09/2020
 */
package com.peter;

import java.awt.Graphics;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Viewer {
    
    // Not good OO design, but it's 4 in the morning I don't really care
    final static int size = 14;
    static HashMap<String, Button> buttons = new HashMap<>();
    static int[] values = new int[size];
    static boolean lock = false;
    static int xC, yC, xA, yA; // calibration
    
    Img bkg = null;
    String prefix = null;
    final static boolean DEBUG = false;
    
    void populateDigital(Document document)
    {
        NodeList nList = document.getElementsByTagName("button");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                System.out.println("name : " + eElement.getAttribute("name"));
                System.out.println("image : " + eElement.getAttribute("image"));
                System.out.println("x : " + eElement.getAttribute("x"));
                System.out.println("y : " + eElement.getAttribute("y"));
                System.out.println("width : " + eElement.getAttribute("width"));
                System.out
                        .println("height : " + eElement.getAttribute("height"));
                String name = eElement.getAttribute("name");
                String image = eElement.getAttribute("image");
                int x = Integer.parseInt(eElement.getAttribute("x"));
                int y = Integer.parseInt(eElement.getAttribute("y"));
                int width = Integer.parseInt(eElement.getAttribute("width"));
                int height = Integer.parseInt(eElement.getAttribute("height"));
                Button b = new Button(name, prefix + image, x, y, width,
                        height);
                buttons.put(name, b);
            }
        }
        
        nList = document.getElementsByTagName("analog");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                String name = eElement.getAttribute("name");
                String image = eElement.getAttribute("image");
                int x = Integer.parseInt(eElement.getAttribute("x"));
                int y = Integer.parseInt(eElement.getAttribute("y"));
                int width = Integer.parseInt(eElement.getAttribute("width"));
                int height = Integer.parseInt(eElement.getAttribute("height"));
                int direction = eElement.getAttribute("direction")
                        .equals("left") ? -1 : 1;
                int reverse = eElement.getAttribute("reverse").equals("true")
                        ? -1
                        : 1;
                Button b = new Trigger(name, prefix + image, x, y, width,
                        height, direction, reverse);
                buttons.put(name, b);
            }
        }
        
        nList = document.getElementsByTagName("stick");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                String name = eElement.getAttribute("xname");
                String image = eElement.getAttribute("image");
                int x = Integer.parseInt(eElement.getAttribute("x"));
                int y = Integer.parseInt(eElement.getAttribute("y"));
                int width = Integer.parseInt(eElement.getAttribute("width"));
                int height = Integer.parseInt(eElement.getAttribute("height"));
                int xrange = Integer.parseInt(eElement.getAttribute("xrange"));
                int yrange = Integer.parseInt(eElement.getAttribute("yrange"));
                Button b = new Analog(name, prefix + image, x, y, width, height,
                        xrange, yrange);
                buttons.put(name, b);
            }
        }
        
    }
    
    public Viewer(String filepath)
    {
        try {
            getSkin(filepath);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void getSkin(String p)
            throws ParserConfigurationException, SAXException, IOException
    {
        prefix = "/" + p;
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document document = builder.parse(getClass().getResourceAsStream(prefix + "skin.xml"));
        
        document.getDocumentElement().normalize();
        
        populateDigital(document);
        
        String fBackground = ((Element) document
                .getElementsByTagName("background").item(0))
                        .getAttribute("image");
        System.out.println(prefix + fBackground);
        
        bkg = new Img(0, 0, prefix + fBackground);
    }
    
    public void draw(Graphics g)
    {
        bkg.draw(g);
        for (Entry<String, Button> b : buttons.entrySet()) {
            b.getValue().draw(g);
        }
    }
    
    public static int calibrate = 0;
    
    public void physics()
    {
        if (Viewer.lock)
            return;
        Viewer.lock = true;
        
        if (calibrate == 1) {
            ((Analog) buttons.get("lstick_x")).centerX = xA;
            ((Analog) buttons.get("lstick_x")).centerY = yA;
            ((Analog) buttons.get("cstick_x")).centerX = xC;
            ((Analog) buttons.get("cstick_x")).centerY = yC;
            
            ((Analog) buttons.get("lstick_x")).maxX= 226;
            ((Analog) buttons.get("lstick_x")).minX = 24;
            ((Analog) buttons.get("lstick_x")).maxY= 233;
            ((Analog) buttons.get("lstick_x")).minY = 28;
            
            ((Analog) buttons.get("cstick_x")).maxX= 214;
            ((Analog) buttons.get("cstick_x")).minX = 35;
            ((Analog) buttons.get("cstick_x")).maxY= 218;
            ((Analog) buttons.get("cstick_x")).minY = 38;
            
            
            calibrate = 2;
        }
        
        buttons.get("a").value = values[0];
        buttons.get("b").value = values[1];
        buttons.get("x").value = values[2];
        buttons.get("y").value = values[3];
        buttons.get("start").value = values[4];
        buttons.get("z").value = values[5];
        buttons.get("r").value = values[6];
        buttons.get("l").value = values[7];
        
        ((Analog) buttons.get("lstick_x")).val_x = values[8];
        ((Analog) buttons.get("lstick_x")).val_y = values[9];
        ((Analog) buttons.get("cstick_x")).val_x = values[10];
        ((Analog) buttons.get("cstick_x")).val_y = values[11];
        
        buttons.get("trig_l").value = values[12];
        buttons.get("trig_r").value = values[13];
        Viewer.lock = false;
    }
}

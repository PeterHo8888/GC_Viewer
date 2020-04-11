/**
 * @author Peter Ho
 * @date   04/09/2020
 */
package com.peter;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Img {
    int x, y;
    Image img = null;
    
    public Img(int x, int y, String filename)
    {
        this.x = x;
        this.y = y;
        try {
            System.out.println(filename + " is the resource");
            img = new ImageIcon(getClass().getResource(filename)).getImage();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    public void draw(Graphics g)
    {
        g.drawImage(img, x, y, null);
    }
    
    public void draw(Graphics g, Button b)
    {
        if (b instanceof Trigger) {
            Trigger t = (Trigger) b;
            if (t.direction == 1) { // L-trigger
                if (Viewer.buttons.get("l").value != 1)
                    g.drawImage(img, x, y, (int) (t.value / 255.0 * t.width),
                            t.height, null);
            } else {
                if (Viewer.buttons.get("r").value != 1)
                    g.drawImage(img, x + t.width, y,
                            (int) (-t.value / 255.0 * t.width), t.height, null);
            }
        } else if (b instanceof Analog) {
            Analog a = (Analog) b;
            a.val_x = Math.min(a.val_x, a.maxX);
            a.val_x = Math.max(a.val_x, a.minX);
            a.val_y = Math.min(a.val_y, a.maxY);
            a.val_y = Math.max(a.val_y, a.minY);
            
            if (Math.abs(a.val_x - a.centerX) < 5)
                a.val_x = a.centerX;
            if (Math.abs(a.val_y - a.centerY) < 5)
                a.val_y = a.centerY;
            
            int x, y;
            
            if (a.val_x == a.centerX) {
                x = a.x;
            } else if (a.val_x > a.centerX) {
                double multiplier = a.xrange / (double) (a.maxX - a.centerX);
                x = a.x + (int) ((a.val_x - a.centerX) * multiplier);
            } else {
                double multiplier = a.xrange / (double) (a.centerX - a.minX);
                x = a.x + (int) ((a.val_x - a.centerX) * multiplier);
            }
            
            if (a.val_y == a.centerY) {
                y = a.y;
            } else if (a.val_y > a.centerX) {
                double multiplier = a.yrange / (double) (a.maxY - a.centerY);
                y = a.y - (int) ((a.val_y - a.centerY) * multiplier);
            } else {
                double multiplier = a.yrange / (double) (a.centerY - a.minY);
                y = a.y - (int) ((a.val_y - a.centerY) * multiplier);
            }
            
            g.drawImage(img, x, y, null);
        }
    }
}

/**
 * @author Peter Ho
 * @date   04/09/2020
 */
package com.peter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
    int x, y;
    BufferedImage img = null;
    
    public Image(int x, int y, String filename)
    {
        this.x = x;
        this.y = y;
        try {
            //img = ImageIO.read(getClass().getResource(filename));
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
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
            if (a.x > a.max)
                a.x = a.max;
            if (a.y > a.max)
                a.y = a.max;
            if (a.x < a.min)
                a.x = a.min;
            if (a.y < a.min)
                a.y = a.min;
            int x = (int) (a.x + (a.val_x - a.centerX)
                    * (a.xrange / (double) (a.max - a.centerX)));
            int y = (int) (a.y - (a.val_y - a.centerY)
                    * (a.yrange / (double) (a.max - a.centerY)));
            g.drawImage(img, x, y, null);
        }
    }
}

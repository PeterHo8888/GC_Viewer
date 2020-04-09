/**
 * @author Peter Ho
 * @date   04/09/2020
 */
package com.peter;

import java.awt.Graphics;

public class Button {
    String name;
    String filename;
    int x, y, width, height;
    Image i;
    int value;
    
    public Button(String n, String f, int x, int y, int w, int h)
    {
        name = n;
        filename = f;
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        i = new Image(x, y, filename);
        value = 0;
    }
    
    public void draw(Graphics g)
    {
        if (value == 1)
            i.draw(g);
    }
}

class Trigger extends Button {
    int direction;
    int reverse;
    public Trigger(String n, String f, int x, int y, int w, int h, int d, int r)
    {
        super(n, f, x, y, w, h);
        direction = d;
        reverse = r;
    }
    
    public void draw(Graphics g)
    {
        i.draw(g, this);
    }
}

class Analog extends Button {
    int xrange, yrange;
    int val_x, val_y;
    int centerX, centerY;
    int max, min;
    public Analog(String n, String f, int x, int y, int w, int h, int xr, int yr)
    {
        super(n, f, x, y, w, h);
        xrange = xr;
        yrange = yr;
        val_x = val_y = 128;
        max = 247;
        min = 10;
    }
    
    public void draw(Graphics g)
    {
        i.draw(g, this);
    }
}

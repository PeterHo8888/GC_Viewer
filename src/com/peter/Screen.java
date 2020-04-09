/**
 * @author Peter Ho
 * @date   04/09/2020
 */
package com.peter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Screen extends JPanel implements Runnable, ActionListener {
    private Thread thread = new Thread(this);
    Viewer viewer = null;

    boolean defined = false;
    
    private Timer timer = new Timer(10, this);

    public Screen() {
        thread.start();
    }

    public void define() {
        timer.start();
        viewer = new Viewer("Tron-GC-Style/");
        defined = true;
    }

    public void paintComponent(Graphics g) {
        if (!defined)
            return;
        g.setColor(new Color(245, 245, 245));
        g.fillRect(0, 0, 800, 600);

        viewer.draw(g);
    }

    public void run() {
        define();
        while (true) {
            viewer.physics();
            try {
                Thread.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource()==timer) {
            repaint();
        }
    }
}
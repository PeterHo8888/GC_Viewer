/**
 * @author Peter Ho
 * @date   04/09/2020
 */
package com.peter;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Frame extends JFrame {
    public static final String title = "GC Viewer";
    //public static final Dimension size = new Dimension(351, 120);
    public static final Dimension size = new Dimension(341, 110); // corrected
    
    public static int width, height;
    
    public Frame()
    {
        setTitle(title);
        getContentPane().setPreferredSize(size);
        pack();
        width = getWidth();
        height = getHeight();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 1, 0, 0));
        Screen screen = new Screen();
        add(screen);
        setAlwaysOnTop(true);
        setVisible(true);
    }
    
    public static void main(String[] args)
    {
        new Frame();
        SerialComm main = new SerialComm();
        main.initialize();
        Thread t = new Thread() {
            public void run()
            {
                while (true) {
                    try {
                        Thread.sleep(1000000);
                    } catch (InterruptedException ie) {
                    }
                }
            }
        };
        t.start();
        System.out.println("Started");
        
    }
}
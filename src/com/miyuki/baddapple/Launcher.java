package com.miyuki.baddapple;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

/**
 * This view is just here to show that bad apple hasn't crashed in the background.
 * yet.
 * */
public class Launcher extends JFrame {
	private static final long serialVersionUID = 1L;

	private Thread thread;

	public Canvas canvas;

	public Image background;
	public Image rotatingIcon;

	public float logoRotation = 0;

	public Launcher() {
		super("BadApple Launcher");

		canvas = new Canvas();

		setSize(430, 300);
		setUndecorated(true);
		setResizable(false);
		
		//Resource.GetImage() sometimes doesn't works with this function
		//i'm not sure why, so i'll just do it in the "legacy" way
		setIconImage(Toolkit.getDefaultToolkit().getImage(Launcher.class.getResource("/assets/badapple/icons/icon.png")));
		
		setLocationRelativeTo(null);
		add(canvas, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.background = Resource.GetImage("internal://launcher/background.png").getImage();
		this.rotatingIcon = Resource.GetImage("internal://icon.png").getImage();

		setVisible(true);
		
		String titleStr = Language.GetKey("launcher-title");
		Font title = Resource.DeriveMainFont(Font.PLAIN, 16);
		Font desc = Resource.DeriveMainFont(Font.PLAIN, 5);
		
		thread = new Thread(new Runnable() {
			Graphics2D g;

			@Override
			public void run() {
				while (true) {
					BufferStrategy bs = canvas.getBufferStrategy();
					if (bs == null) {
						canvas.createBufferStrategy(3);
						continue;
					}
					g = (Graphics2D) bs.getDrawGraphics();
					g.drawImage(background, 0, 0, null);
/*
					 AffineTransform tr = new AffineTransform();
					 tr.translate(10, 236);
					    tr.rotate(
					            Math.toRadians(logoRotation),
					            rotatingIcon.getWidth(null) / 2,
					            rotatingIcon.getHeight(null) / 2
					    );

					//    tr.translate(10, 236);
					
					g.drawImage(rotatingIcon, tr, null);*/
					
			        logoRotation -= 0.5;
			        if (logoRotation < -360)
			        	logoRotation = 0;
			        
			        g.setRenderingHint(
			        	    RenderingHints.KEY_ANTIALIASING,
			        	    RenderingHints.VALUE_ANTIALIAS_ON);
			        	g.setRenderingHint(
			        	    RenderingHints.KEY_TEXT_ANTIALIASING,
			        	    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			        
			        g.setColor(Color.white);
					g.setFont(title);
					g.drawString(titleStr, 10, 20);
					g.setFont(desc);
					
					String[] lines = StandardOut.GetCapturedBuffer().split("\n");
					int ln = 0;
					for (String l : lines) {
						g.drawString(l, 10, 30 + (5* ln));
						ln++;
					}
					
			        bs.show();
					
					
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
					}
				}
			}
		});
		thread.start();
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);

		if (!b) {
			Resource.DeleteCache("internal://launcher/background.png");
			Resource.DeleteCache("internal://launcher/spinnusblend.png");
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

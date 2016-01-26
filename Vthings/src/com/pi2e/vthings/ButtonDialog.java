package com.pi2e.vthings;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JLabel;

public class ButtonDialog extends Dialog  {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3848399156037482805L;
	JLabel label = new JLabel();
	JButton ok = new JButton("确定");
	
	public ButtonDialog(Frame parent, String titleString, String labelString) {
		super(parent, true);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(titleString);
		setSize(460, 300);
		System.out.println("0----------" + parent.getLocation().x + "     "
				+ parent.getLocation().y);
		setLocation(new Point(230+100 + parent.getLocation().x,
				150 + parent.getLocation().y+100));
		setResizable(false);
		setLayout(null);

		Dimension preferred = new Dimension(400, 100);// 设置尺寸
		label.setPreferredSize(preferred);
		add(label);
		Font font = new Font("宋体", Font.BOLD, 25);
		label.setFont(font);
		label.setText(labelString);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBounds(30, 75, 400, 100);
		
		
		
	 
		 
	}

 
	
	 
}

package com.pi2e.vthings;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class SelectDialog extends Dialog implements ActionListener,KeyListener{
	
	
	public interface SelectDialogDelegate{
		public void yesButtonClickAction();
//		public void noButtonClickAction();
	}
	
	SelectDialogDelegate selectDialogDelegate=null;
	
	public void setSelectDialogAction(SelectDialogDelegate selectDialogDelegate){
		
		this.selectDialogDelegate=selectDialogDelegate;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2116728563327043538L;
	
	
	
	JButton yes = new JButton("继续(键盘Space键)");
//	JButton no = new JButton("NO(键盘N键)");
	JLabel label = new JLabel();
	public SelectDialog(Frame parent, String titleString, String labelString,String buttonName){
		super(parent, true); 

		setTitle(titleString);
		setSize(460, 300);
		System.out.println("0----------" + parent.getLocation().x + "     "
				+ parent.getLocation().y);
		setLocation(new Point(230 + parent.getLocation().x,
				150 + parent.getLocation().y));
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
		
		addKeyListener(this);

		Dimension preferredSize = new Dimension(400, 50);// 设置尺寸
		yes.setPreferredSize(preferredSize);
		add(yes);
		yes.setBounds(30, 200, 400, 50);
//		yes.setForeground(Color.green);
		yes.addActionListener(this);
		yes.setText(buttonName);
		/*
		no.setPreferredSize(preferredSize);
		add(no);
		no.setBounds(260, 200, 150, 50);
		no.setForeground(Color.red);
		no.addActionListener(this);
		*/
		
		setNOfucous();
		
	}
	
	public SelectDialog(Frame parent, String titleString, String labelString){
		super(parent, true);
		
		setTitle(titleString);
		setSize(460, 300);
		System.out.println("0----------" + parent.getLocation().x + "     "
				+ parent.getLocation().y);
		setLocation(new Point(230+100  + parent.getLocation().x,
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
		
		addKeyListener(this);

		Dimension preferredSize = new Dimension(400, 50);// 设置尺寸
		yes.setPreferredSize(preferredSize);
		add(yes);
		yes.setBounds(30, 200, 400, 50);
//		yes.setForeground(Color.green);
		yes.addActionListener(this);
		
		/*
		no.setPreferredSize(preferredSize);
		add(no);
		no.setBounds(260, 200, 150, 50);
		no.setForeground(Color.red);
		no.addActionListener(this);
		*/
		
		setNOfucous();
	}
	
	private void setNOfucous(){
 
		yes.setFocusable(false);
//		no.setFocusable(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == yes) {
			if (selectDialogDelegate != null) {
				selectDialogDelegate.yesButtonClickAction();
			}

		}
		/*
		else if (e.getSource() == no) {
			if (selectDialogDelegate != null) {
				selectDialogDelegate.noButtonClickAction();
			}
		}
		*/
		
		
		setNOfucous();
		dispose();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("-----       WART----" + e.getKeyChar() + "        "
				+ e.getKeyCode());
		// N键 89
		
		if (e.getKeyCode()==32) {
			 
				if (selectDialogDelegate != null) {
					selectDialogDelegate.yesButtonClickAction();
				}

			 
			
			setNOfucous();
			dispose();
		}
		 
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}

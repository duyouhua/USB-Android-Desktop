package com.pi2e.vthings;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class WarnDialog extends Dialog implements ActionListener {
	/**
	 * 
	 */

	public interface WarnDialogDelagete {
		public void okButtonClickAction();
	}

	WarnDialogDelagete warnDialogDelagete = null;

	public void setWarnDialogAction(WarnDialogDelagete warnDialogDelagete) {
		this.warnDialogDelagete = warnDialogDelagete;
	}

	private static final long serialVersionUID = -5155360380207444348L;
	JButton ok = new JButton("确定");
	JLabel label = new JLabel();

	public WarnDialog(Frame parent, String titleString, String labelString) {
		super(parent, true);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

		Dimension preferredSize = new Dimension(400, 50);// 设置尺寸
		ok.setPreferredSize(preferredSize);
		add(ok);
		ok.setBounds(30, 200, 400, 50);
		ok.addActionListener(this);
		
		new Thread(){
			public void run() {
				try {
					Thread.sleep(3000);
					dispose();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			};
		}.start();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			if (warnDialogDelagete != null) {
				warnDialogDelagete.okButtonClickAction();
			}

		}
		dispose();
	}

}

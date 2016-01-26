package com.pi2e.vthings;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ProperitesDialog extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6293023341653334873L;

	private static final String[] NUMBER = { "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "0", "-" };

	public interface ProperitesDelegate {
		public void okButtonClickAction();

	}

	ProperitesDelegate properitesDelegate = null;

	public void setDelegate(ProperitesDelegate properitesDelegate) {
		this.properitesDelegate = properitesDelegate;
	}

	JLabel label1 = new JLabel("请输入WIFI名称");

	JLabel label2 = new JLabel("请输入WIFI密码");

	JLabel label22 = new JLabel("请输入WIFI信号阀值");

	JLabel label3 = new JLabel("请输入固件版本号");

	JLabel label4 = new JLabel("请输入说客版本号");
	JLabel label5 = new JLabel("请选择adb的路径");

	JButton adbPathButton;

	JTextField rows = new JTextField();

	JTextField maxWifi = new JTextField();
	JTextField minWifi = new JTextField();

	JTextField columns = new JTextField();

	JTextField imgrows = new JTextField();

	JTextField androidcolumns = new JTextField();

	JButton ok = new JButton("确定");

	JButton cancel = new JButton("取消");

	String adbPath;

	ProperitesDialog(Frame parent, String titleString)

	{

		super(parent, true);

		setTitle(titleString);
		setSize(460, 400 + 50);
		System.out.println("0----------" + parent.getLocation().x + "     "
				+ parent.getLocation().y);
		setLocation(new Point(230 + parent.getLocation().x,
				150 + parent.getLocation().y));
		setResizable(false);
		setLayout(null);

		add(label1);

		add(label2);
		add(label22);
		add(label3);

		add(label4);

		add(label5);
		Font font = new Font("Lucida Grande", Font.PLAIN, 16);

		label1.setBounds(50, 30, 150, 40);
		label1.setFont(font);
		label2.setBounds(50, 80, 150, 40);
		label2.setFont(font);

		label22.setBounds(50, 80 + 50, 150, 40);
		label22.setFont(font);

		maxWifi.setBounds(210, 80 + 50, 80, 40);
		add(maxWifi);
		minWifi.setBounds(320, 80 + 50, 80, 40);
		add(minWifi);

		JLabel label221 = new JLabel("到");
		label221.setBounds(300, 80 + 50, 20, 40);
		label221.setFont(font);
		add(label221);

		label3.setBounds(50, 130 + 50, 150, 40);
		label3.setFont(font);
		label4.setBounds(50, 180 + 50, 150, 40);
		label4.setFont(font);

		label5.setBounds(50, 230 + 50, 150, 40);
		label5.setFont(font);

		add(rows);

		add(columns);

		add(imgrows);

		add(androidcolumns);

		rows.setBounds(210, 30, 190, 40);
		columns.setBounds(210, 80, 190, 40);

		imgrows.setBounds(210, 130 + 50, 190, 40);
		androidcolumns.setBounds(210, 180 + 50, 190, 40);

		add(ok);

		add(cancel);

		Dimension preferredSize = new Dimension(150, 50);// 设置尺寸
		ok.setPreferredSize(preferredSize);
		ok.setBounds(60, 300 + 50, 150, 50);

		cancel.setBounds(140, 100, 60, 25);
		cancel.setPreferredSize(preferredSize);
		cancel.setBounds(260, 300 + 50, 150, 50);

		adbPathButton = new JButton("点击浏览adb所在的位置");
		adbPathButton.addActionListener(this);
		adbPathButton.setBounds(210, 230 + 50, 190, 40);
		add(adbPathButton);

		PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
		rows.setText(propertiesUtil.getValue(PropertiesUtil.WIFI_NAME));
		columns.setText(propertiesUtil.getValue(PropertiesUtil.WIFI_PASSWORD));
		imgrows.setText(propertiesUtil.getValue(PropertiesUtil.IMG));
		androidcolumns.setText(propertiesUtil.getValue(PropertiesUtil.APK));
		adbPath = propertiesUtil.getValue(PropertiesUtil.ADBPATH);
		maxWifi.setText(propertiesUtil.getValue(PropertiesUtil.WIFI_MAX));
		minWifi.setText(propertiesUtil.getValue(PropertiesUtil.WIFI_MIN));
		/*
		 * 
		 * 	propertiesUtil.setValue(PropertiesUtil.WIFI_MAX, maxWifi.getText());

			propertiesUtil.setValue(PropertiesUtil.WIFI_MIN, minWifi.getText());
		 * 
		 * 
		 * */
		
		if (adbPath.length() > 0 && checkAndroidAdbPath(adbPath)) {
			adbPathButton.setText("adb的路径:" + adbPath);
			adbPathButton.setForeground(Color.green);
		}

		ok.addActionListener(this);

		cancel.addActionListener(this);

	}

	public void actionPerformed(ActionEvent e)

	{

		if (e.getSource() == adbPathButton) {

			Frame f = new Frame("adb目标路径");
			f.setSize(120, 90);
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.showOpenDialog(f);
			File dir = chooser.getSelectedFile();
			if (dir != null) {
				adbPath = dir.getAbsolutePath();
				if (checkAndroidAdbPath(adbPath)) {
					adbPathButton.setText("adb的路径:" + adbPath);
					adbPathButton.setForeground(Color.green);

					Preferences preferences = Preferences.userRoot();
					preferences.put("adbPath", adbPath);

				} else {
					adbPathButton.setText("未能识别的adb的路径:" + adbPath);
					adbPathButton.setForeground(Color.red);
				}

				System.out.println("---------" + dir.getAbsolutePath());
			} else {
				System.out.println("没有选取文件");
			}

		} else if (e.getSource() == ok) {
			PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
			propertiesUtil.setValue(PropertiesUtil.WIFI_NAME, rows.getText());
			propertiesUtil.setValue(PropertiesUtil.WIFI_PASSWORD,
					columns.getText());
			propertiesUtil.setValue(PropertiesUtil.IMG, imgrows.getText());
			propertiesUtil.setValue(PropertiesUtil.APK,
					androidcolumns.getText());
			propertiesUtil.setValue(PropertiesUtil.ADBPATH, adbPath);

			propertiesUtil.setValue(PropertiesUtil.WIFI_MAX, maxWifi.getText());

			propertiesUtil.setValue(PropertiesUtil.WIFI_MIN, minWifi.getText());

		}

		if (e.getSource() != adbPathButton) {

			dispose();
		}

		if (e.getSource() == ok && properitesDelegate != null) {
			properitesDelegate.okButtonClickAction();
		}

	}

	private boolean checkAndroidAdbPath(String path) {

		if (path.length() > 0 && path.contains("adb")) {
			return true;
		}

		return false;
	}

	private boolean checkNum(String n) {
		// return true;

		if (n.length() == 0) {
			return false;
		}

		boolean b = true;

		for (int i = 0; i < n.length(); i++) {
			char c = n.charAt(i);
			b = b && isContain(String.valueOf(c));
		}

		return b;
	}

	private boolean isContain(String c) {
		for (String s : NUMBER) {
			if (!c.equals(s)) {
				return false;
			}
		}

		return true;

	}
}

package com.pi2e.vthings;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.pi2e.vthings.ProperitesDialog.ProperitesDelegate;
import com.pi2e.vthings.SelectDialog.SelectDialogDelegate;
import com.pi2e.vthings.ToastDialog.ToastDialogDelegate;

// 喇叭按钮一次就行  
// 左右声道 xiaopai  shuoke
// 录音5 秒
// 高低音 

public class VthingPCClient extends JFrame implements KeyListener {

	/**
	 * VERSION NUMBER
	 */
	public static final String VERSION_NUM = "V 2.0 2016-01-06";

	/**
	 * 
	 */
	private static final long serialVersionUID = 3028251150240414784L;

	private JPanel contentPane;

	/**
	 * 是否测试过
	 */
	private JLabel result_status_1, result_status_2, result_status_3,
			result_status_4, result_status_5, result_status_6, result_status_7,
			result_status_8, result_status_9, result_status_a;

	private List<JLabel> resultStatusList;

	/**
	 * 测试后的返回值
	 */
	private JLabel result_response_1, result_response_2, result_response_3,
			result_response_4, result_response_5, result_response_6,
			result_response_7, result_response_9_1, result_response_9_2,
			result_response_a;

	// private List<JLabel> resultResponseList;

	private JButton adbPathButton;
	private JButton startTestButton;

	private SelectDialog buttonDialog1;

	private ToastDialog toastDialog = new ToastDialog(VthingPCClient.this,
			"USB测试");;

	/*
	 * private JButton recordRadioButton; private JButton playRadioButton;
	 */

	private String adbPath;

	private String order = "  shell am start -n com.ijustyce.usb/com.ijustyce.usb.MainActivity";

	private BufferedOutputStream out = null;
	private BufferedInputStream in = null;
	private Socket socket = null;

	private boolean isConnected;

	private boolean isRecordEnding;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VthingPCClient frame = new VthingPCClient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VthingPCClient() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 770);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setFocusable(true);
		contentPane.addKeyListener(this);

		resultStatusList = new ArrayList<JLabel>();
		// resultResponseList = new ArrayList<JLabel>();

		JLabel version = new JLabel(VERSION_NUM);
		version.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		version.setBounds(20, 10, 171, 20);
		contentPane.add(version);

		JLabel lblNewLabel = new JLabel("请选择先配置参数");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblNewLabel.setBounds(39, 28, 182, 42);
		contentPane.add(lblNewLabel);

		adbPathButton = new JButton("点击( 或按下键盘 F键 )配置信息");
		adbPathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				showPropiesDlalog();

			}
		});
		adbPathButton.setBounds(233, 28, 647, 42);
		contentPane.add(adbPathButton);

		if (checkPropites()) {
			adbPathButton.setText("配置完成");
			adbPathButton.setForeground(Color.green);
		}

		// String rootDir =
		// this.getClass().getResource(File.separator).getPath();
		// System.out.println("------------"+rootDir);
		// rootDir=;
		startTestButton = new JButton("点击( 或按下键盘 Enter键 )开始测试");
		// startTestButton = new JButton(rootDir);
		startTestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				boolean b = checkPropites();

				if (!b) {
					showPropertiesWarn();

				} else {
					startTestButtonAction();
				}
				setPaneFocusable();
			}
		});
		startTestButton.setBounds(39, 116, 841, 42);
		contentPane.add(startTestButton);

		/**
		 * USB 0---0
		 */

		JLabel lblUsb = new JLabel("1.USB连接");
		lblUsb.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblUsb.setBounds(39, 196, 171, 42);
		contentPane.add(lblUsb);

		result_status_1 = new JLabel("未测试1");
		result_status_1.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_status_1.setBounds(215, 196, 84, 42);
		contentPane.add(result_status_1);

		result_response_1 = new JLabel("连接状态未知");
		result_response_1.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_response_1.setBounds(347, 196, 522, 42);
		contentPane.add(result_response_1);

		/**
		 * 2.软件系统版本
		 */

		JLabel label_6 = new JLabel("2.软件系统版本");
		label_6.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		label_6.setBounds(39, 250, 171, 42);
		contentPane.add(label_6);

		result_status_2 = new JLabel("未测试2");
		result_status_2.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_status_2.setBounds(215, 250, 84, 42);
		contentPane.add(result_status_2);

		result_response_2 = new JLabel("4.4.4");
		result_response_2.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_response_2.setBounds(347, 250, 533, 42);
		contentPane.add(result_response_2);

		/**
		 * 喇叭高低音
		 */

		JLabel label_11 = new JLabel("3.喇叭高低音");
		label_11.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		label_11.setBounds(39, 304, 160, 42);
		contentPane.add(label_11);

		result_status_3 = new JLabel("未测试3");
		result_status_3.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_status_3.setBounds(215, 304, 84, 42);
		contentPane.add(result_status_3);

		result_response_3 = new JLabel("系统会播放两段音频，一段高音，一段低音，请注意听");
		result_response_3.setForeground(Color.BLUE);
		result_response_3.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_response_3.setBounds(347, 304, 398, 42);
		contentPane.add(result_response_3);

		/**
		 * FM
		 */
		JLabel lblFm = new JLabel("4.FM调频");
		lblFm.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblFm.setBounds(39, 358, 160, 42);
		contentPane.add(lblFm);

		result_status_4 = new JLabel("未测试4");
		result_status_4.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_status_4.setBounds(215, 358, 84, 42);
		contentPane.add(result_status_4);

		result_response_4 = new JLabel("听一听是否能收到电台");
		result_response_4.setForeground(Color.BLACK);
		result_response_4.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_response_4.setBounds(347, 358, 398, 42);
		contentPane.add(result_response_4);

		/**
		 * 网络搜索
		 */
		JLabel label_5 = new JLabel("6.网络搜索");
		label_5.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		label_5.setBounds(39, 408 + 54, 160, 42);
		contentPane.add(label_5);

		result_status_5 = new JLabel("未测试5");
		result_status_5.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_status_5.setBounds(215, 408 + 54, 84, 42);
		contentPane.add(result_status_5);

		result_response_5 = new JLabel("搜到如下网络信号");
		result_response_5.setForeground(Color.BLACK);
		result_response_5.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_response_5.setBounds(347, 408 + 54, 398, 42);
		contentPane.add(result_response_5);

		/**
		 * 网络连接
		 */
		JLabel label_7 = new JLabel("7.网络连接");
		label_7.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		label_7.setBounds(39, 462 + 54, 160, 42);
		contentPane.add(label_7);

		result_status_6 = new JLabel("未测试6");
		result_status_6.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_status_6.setBounds(215, 462 + 54, 84, 42);
		contentPane.add(result_status_6);

		result_response_6 = new JLabel("网络连接成功");
		result_response_6.setForeground(Color.BLACK);
		result_response_6.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_response_6.setBounds(347, 462 + 54, 398, 42);
		contentPane.add(result_response_6);

		/**
		 * 网络信号
		 */
		JLabel label_9 = new JLabel("8.网络信号");
		label_9.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		label_9.setBounds(39, 516 + 54, 182, 42);
		contentPane.add(label_9);

		result_status_7 = new JLabel("未测试7");
		result_status_7.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_status_7.setBounds(215, 516 + 54, 84, 42);
		contentPane.add(result_status_7);

		result_response_7 = new JLabel("-55.5");
		result_response_7.setForeground(Color.BLACK);
		result_response_7.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_response_7.setBounds(347, 516 + 54, 398, 42);
		contentPane.add(result_response_7);

		/**
		 * 麦克风"
		 */

		JLabel label_4 = new JLabel("5.麦克风");
		label_4.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		label_4.setBounds(39, 570 - 54 * 3, 160, 42);
		contentPane.add(label_4);

		JLabel jtLb = new JLabel("请按照提示测试麦克风");
		jtLb.setForeground(Color.BLACK);
		jtLb.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		jtLb.setBounds(347, 570 - 54 * 3, 398, 42);
		contentPane.add(jtLb);

		result_status_8 = new JLabel("未测试8");
		result_status_8.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_status_8.setBounds(215, 570 - 54 * 3, 84, 42);
		contentPane.add(result_status_8);

		/*
		 * playRadioButton = new JButton("点击(P键)播放录音");
		 * playRadioButton.setBounds(552, 570, 126, 42);
		 * contentPane.add(playRadioButton);
		 * playRadioButton.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { // TODO
		 * Auto-generated method stub playRadioButtonAction();
		 * setPaneFocusable(); } }); playRadioButton.setVisible(false);
		 */

		JLabel label_21 = new JLabel("9.胸前按钮");
		label_21.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		label_21.setBounds(39, 624, 160, 42);
		contentPane.add(label_21);

		result_status_9 = new JLabel("未测试9");
		result_status_9.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_status_9.setBounds(215, 624, 84, 42);
		contentPane.add(result_status_9);

		JLabel butlab = new JLabel("请按照提示胸前按钮");
		butlab.setForeground(Color.BLACK);
		butlab.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		butlab.setBounds(347, 624, 398, 42);
		contentPane.add(butlab);

		result_response_9_1 = new JLabel("按下胸前按钮");
		result_response_9_1.setHorizontalAlignment(SwingConstants.CENTER);
		result_response_9_1.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_response_9_1.setBounds(347, 624, 196, 42);
		// contentPane.add(result_response_9_1);

		result_response_9_2 = new JLabel("长按胸前按钮");
		result_response_9_2.setHorizontalAlignment(SwingConstants.CENTER);
		result_response_9_2.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_response_9_2.setBounds(549, 624, 196, 42);
		// contentPane.add(result_response_9_2);

		JLabel label_25 = new JLabel("10.复位");
		label_25.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		label_25.setBounds(39, 678, 160, 42);
//		contentPane.add(label_25);

		result_status_a = new JLabel("未测试a");
		result_status_a.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_status_a.setBounds(215, 678, 84, 42);
//		contentPane.add(result_status_a);

		result_response_a = new JLabel("请按照复位提示操作");
		result_response_a.setForeground(Color.BLACK);
		result_response_a.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		result_response_a.setBounds(347, 678, 398, 42);
//		contentPane.add(result_response_a);

		resultStatusList.add(result_status_1);
		resultStatusList.add(result_status_2);
		resultStatusList.add(result_status_3);
		resultStatusList.add(result_status_4);
		resultStatusList.add(result_status_5);
		resultStatusList.add(result_status_6);
		resultStatusList.add(result_status_7);
		resultStatusList.add(result_status_8);
		resultStatusList.add(result_status_9);
		resultStatusList.add(result_status_a);
		/*
		 * resultResponseList.add(result_response_1);
		 * resultResponseList.add(result_response_2);
		 * resultResponseList.add(result_response_3);
		 * resultResponseList.add(result_response_4);
		 * resultResponseList.add(result_response_5);
		 * resultResponseList.add(result_response_6);
		 * resultResponseList.add(result_response_7);
		 * resultResponseList.add(result_response_9_1);
		 * resultResponseList.add(result_response_9_1);
		 * resultResponseList.add(result_response_a);
		 */

		allocStatusJLabels();
		allocResponseJLabels();
		
		
		toastDialog.setToastDialogDelegate(new ToastDialogDelegate() {
			
			@Override
			public void yesButtonClickAction() {
				// TODO Auto-generated method stub
//				reconnect();
				hiddenStartTestWarn();
				
				endSocket();
				
			}
		});

	}

	private void showPropiesDlalog() {
		ProperitesDialog properitesDialog = new ProperitesDialog(
				VthingPCClient.this, "配置信息");
		properitesDialog.setDelegate(new ProperitesDelegate() {

			@Override
			public void okButtonClickAction() {
				// TODO Auto-generated method stub

				setPaneFocusable();

				if (checkPropites()) {
					adbPathButton.setText("配置完成");
					adbPathButton.setForeground(Color.green);
				}

			}
		});
		properitesDialog.show();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("------@@@-------" + e.getKeyCode());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("-------$$$------" + e.getKeyChar() + "        "
				+ e.getKeyCode());
		// F键
		if (e.getKeyCode() == 70) {

			// adbPathButtonAction();

			showPropiesDlalog();

			// enter 键
		} else if (e.getKeyCode() == 10) {
			// startTestButtonAction();
			// if (checkPropites()) {
			// showPropertiesWarn();
			//
			// } else {
			// startTestButtonAction();
			// }

			boolean b = checkPropites();

			if (!b) {
				showPropertiesWarn();

			} else {
				startTestButtonAction();
			}
			setPaneFocusable();

			// R键
		} else if (e.getKeyCode() == 82) {
			// recordRadioButtonAction();

			// P键
		} else if (e.getKeyCode() == 80) {
			// playRadioButtonAction();

			// DEL 键
		} else if (e.getKeyCode() == 8) {
			 
			
			
			endSocket();
			allocResponseJLabels();
			allocStatusJLabels();

		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("----!!!---------");
	}

	private void allocResponseJLabels() {
		result_response_1.setText("没有连接到设备");
		result_response_2.setText("暂无版本信息");
		result_response_5.setText("未搜索到任何网络");
		result_response_6.setText("暂无网络连接");
		result_response_7.setText("0");
		// result_response_9_1.setText("按下胸前按钮");
		// result_response_9_2.setText("长按胸前按钮");
		// result_response_9_1.setForeground(Color.black);
		// result_response_9_2.setForeground(Color.black);
		// result_response_a.setText("复位结果");
		// playRadioButton.setVisible(false);
	}

	private void allocStatusJLabels() {
		for (JLabel j : resultStatusList) {
			setStatusJLabelNormal(j);
		}
	}

	private void setStatusJLabelNormal(JLabel l) {
		l.setText("未测试");
		l.setForeground(Color.black);
		l.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	}

	private void setStatusJLabelFail(JLabel l) {

		l.setText("NG");
		l.setFont(new Font("Lucida Grande", Font.PLAIN, 26));
		l.setForeground(Color.red);
	}

	private void setStatusJLabelOK(JLabel l) {
		l.setText("pass");
		l.setForeground(Color.green);
	}

	private void set1USBResult() {
		setStatusJLabelOK(result_status_1);
		result_response_1.setText("成功连接到设备");
	}

	private void get2VersionAction() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
					sendMsgString(UsbMessage.MESSAGETYPE_VERSION,
							UsbMessage.MESSAGEACTION, null, null);
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		}.start();

	}

	private void set2VersionResult(String imgVersion, String appVersion) {
		PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
		if (imgVersion.equals(propertiesUtil.getValue(PropertiesUtil.IMG))
				&& appVersion.equals(propertiesUtil
						.getValue(PropertiesUtil.APK))) {
			setStatusJLabelOK(result_status_2);
			result_response_2.setText("固件版本和软件版本都匹配");
		} else if (!imgVersion.equals(propertiesUtil
				.getValue(PropertiesUtil.IMG))
				&& appVersion.equals(propertiesUtil
						.getValue(PropertiesUtil.APK))) {
			setStatusJLabelFail(result_status_2);
			result_response_2.setText("固件版本不匹配");
		} else if (imgVersion.equals(propertiesUtil
				.getValue(PropertiesUtil.IMG))
				&& !appVersion.equals(propertiesUtil
						.getValue(PropertiesUtil.APK))) {
			setStatusJLabelFail(result_status_2);
			result_response_2.setText("软件版本不匹配");
		} else if (!imgVersion.equals(propertiesUtil
				.getValue(PropertiesUtil.IMG))
				&& !appVersion.equals(propertiesUtil
						.getValue(PropertiesUtil.APK))) {
			setStatusJLabelFail(result_status_2);
			result_response_2.setText("固件版本和软件版本都不匹配");
		}

		/*
		 * if (version.length() > 0) { result_response_2.setText("当前系统版本为: " +
		 * version); } else { result_response_2.setText("未获取到系统版本"); }
		 */

	}

	private void showPropertiesWarn() {

		new WarnDialog(VthingPCClient.this, "配置Properties", "请先配置基本信息").show();
	}

	private void showSpeakerWarn() {
		StringBuilder builder = new StringBuilder("<html>");
		// builder.append("点击开始按钮开始录音").append("<br/>");
		builder.append("喇叭正在播放音乐").append("<br/>");
		builder.append("高音10秒，低音10秒").append("<br/>");
		builder.append("<span style=\"color:#FF0000;\">");
		builder.append("20秒后自动结束播放");
		builder.append("</span>");
		builder.append("</html>");
		new WarnDialog(VthingPCClient.this, "喇叭测试中", builder.toString()).show();
	}

	private void get3SpeakerResulrt() {
		new Thread() {
			public void run() {
				try {

					sendMsgString(UsbMessage.MESSAGETYPE_SPEAKER,
							UsbMessage.MESSAGEACTION_SPEAKER_HIGH, null, null);
					Thread.sleep(1000 * 10);

					sendMsgString(UsbMessage.MESSAGETYPE_SPEAKER,
							UsbMessage.MESSAGEACTION_SPEAKER_LOW, null, null);
					Thread.sleep(1000 * 10);

					sendMsgString(UsbMessage.MESSAGETYPE_SPEAKER,
							UsbMessage.MESSAGEACTION_SPEAKER_STOP, null, null);
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		}.start();
	}

	private void set3SpeakerResulrt() {
		setStatusJLabelOK(result_status_3);
	}

	private void showFMWarn() {
		StringBuilder builder = new StringBuilder("<html>");
		// builder.append("点击开始按钮开始录音").append("<br/>");
		builder.append("正在播放FM").append("<br/>");
		builder.append("请注意是否能收到电台").append("<br/>");
		builder.append("<span style=\"color:#FF0000;\">");
		builder.append("8秒后自动结束播放");
		builder.append("</span>");
		builder.append("</html>");
		new WarnDialog(VthingPCClient.this, "喇叭测试中", builder.toString()).show();
	}

	private void get4FMResulrt() {
		new Thread() {
			public void run() {
				try {

					sendMsgString(UsbMessage.MESSAGETYPE_FM,
							UsbMessage.MESSAGEACTION_FM_STARTORCHANGE, null,
							null);
					Thread.sleep(1000 * 8);
					sendMsgString(UsbMessage.MESSAGETYPE_FM,
							UsbMessage.MESSAGEACTION_FM_STOP, null, null);
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		}.start();

	}

	private void set4FMResulrt() {
		setStatusJLabelOK(result_status_4);
	}

	private void showNOTresultWifiWarn() {
		StringBuilder builder = new StringBuilder("<html>");
		// builder.append("点击开始按钮开始录音").append("<br/>");
		builder.append("无法搜索到网络").append("<br/>");
		builder.append("请检查设备").append("<br/>");
		builder.append("<span style=\"color:#FF0000;\">");
		builder.append("或者按DEL键停止后重新测试");
		builder.append("</span>");
		builder.append("</html>");
		new WarnDialog(VthingPCClient.this, "WIFI测试中", builder.toString())
				.show();
	}

	private void showTestLeftMirphoneWarn() {
		StringBuilder builder = new StringBuilder("<html>");
		// builder.append("点击开始按钮开始录音").append("<br/>");
		builder.append("测试左侧麦克风").append("<br/>");
		builder.append("请堵住右侧麦克风").append("<br/>");
		builder.append("<span style=\"color:#FF0000;\">");
		builder.append("对左侧麦克风说:测试,测试");
		builder.append("</span>");
		builder.append("</html>");

		SelectDialog selectDialog = new SelectDialog(VthingPCClient.this,
				"左侧麦克风测试", builder.toString());
		selectDialog.setSelectDialogAction(new SelectDialogDelegate() {

			@Override
			public void yesButtonClickAction() {
				// TODO Auto-generated method stub
				testLeftMirphone();
			}

		});
		selectDialog.show();
	}

	private void testLeftMirphone() {
		new Thread() {
			public void run() {
				try {

					sendMsgString(UsbMessage.MESSAGETYPE_MICROPHONE,
							UsbMessage.MESSAGEACTION_MICROPHONE_STARTRECORDING,
							UsbMessage.MICROPHONE_LEFT, null);
					Thread.sleep(1000 * 5);
					sendMsgString(UsbMessage.MESSAGETYPE_MICROPHONE,
							UsbMessage.MESSAGEACTION_MICROPHONE_STOPRECORDING,
							UsbMessage.MICROPHONE_LEFT, null);

					// new Thread(){
					// public void run() {
					showRecordEndAction();
					// };
					// }.start();

					Thread.sleep(1000 * 1);
					sendMsgString(UsbMessage.MESSAGETYPE_MICROPHONE,
							UsbMessage.MESSAGEACTION_MICROPHONE_PLAYRECORDING,
							UsbMessage.MICROPHONE_LEFT, null);
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		}.start();
	}

	private void showButtonAction() {
		StringBuilder builder = new StringBuilder("<html>");
		// builder.append("点击开始按钮开始录音").append("<br/>");
		builder.append("请在提示网络配置成功后").append("<br/>");
		builder.append("按下胸前按钮").append("<br/>");
		builder.append("<span style=\"color:#FF0000;\">");
		builder.append("若无反应请重试！");
		builder.append("</span>");
		builder.append("</html>");
		new WarnDialog(VthingPCClient.this, "喇叭测试中", builder.toString()).show();
	}

	private void showTestRightMirphoneWarn() {
		StringBuilder builder = new StringBuilder("<html>");
		// builder.append("点击开始按钮开始录音").append("<br/>");
		builder.append("测试右侧麦克风").append("<br/>");
		builder.append("请堵住左侧麦克风").append("<br/>");
		builder.append("<span style=\"color:#FF0000;\">");
		builder.append("对左侧麦克风说:说客,说客");
		builder.append("</span>");
		builder.append("</html>");

		SelectDialog selectDialog = new SelectDialog(VthingPCClient.this,
				"左侧麦克风测试", builder.toString());
		selectDialog.setSelectDialogAction(new SelectDialogDelegate() {

			@Override
			public void yesButtonClickAction() {
				// TODO Auto-generated method stub
				testRighttMirphone();
			}

		});
		selectDialog.show();
	}

	private void testRighttMirphone() {
		new Thread() {
			public void run() {
				try {

					sendMsgString(UsbMessage.MESSAGETYPE_MICROPHONE,
							UsbMessage.MESSAGEACTION_MICROPHONE_STARTRECORDING,
							UsbMessage.MICROPHONE_RIGHT, null);
					Thread.sleep(1000 * 5);
					sendMsgString(UsbMessage.MESSAGETYPE_MICROPHONE,
							UsbMessage.MESSAGEACTION_MICROPHONE_STOPRECORDING,
							UsbMessage.MICROPHONE_RIGHT, null);

					showRecordEndAction();

					Thread.sleep(1000 * 1);
					sendMsgString(UsbMessage.MESSAGETYPE_MICROPHONE,
							UsbMessage.MESSAGEACTION_MICROPHONE_PLAYRECORDING,
							UsbMessage.MICROPHONE_RIGHT, null);
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		}.start();
	}

	private void get5WIFIResulrt() {
		sendMsgString(UsbMessage.MESSAGETYPE_WIFI,
				UsbMessage.MESSAGEACTION_WIFI_SEARCH, null, null);
	}

	private void set5NetResulrt(String nets) {
		setStatusJLabelOK(result_status_5);
		result_response_5.setText("找到可用网络  " + nets);
	}

	private void set6NetResulrt() {
		setStatusJLabelOK(result_status_6);
		result_response_6.setText("网络连接成功");
	}

	private void set7NetRSSIResulrt(String rssi) {

		int rs = Integer.parseInt(rssi);

		System.out
				.println("------------------------------------------!!!!!!!!!!!!!!!!!"
						+ rssi);
		if (rs >= minWifi() && rs <= maxWifi()) {
			setStatusJLabelOK(result_status_7);
			result_response_7.setText("网络信号正常,信号值:   " + rs);
		} else {
			setStatusJLabelFail(result_status_7);
			result_response_7.setText("网络信号异常,信号值:   " + rs);
		}

	}

	private int minWifi() {

		try {
			int m1 = Integer.parseInt(PropertiesUtil.getInstance().getValue(
					PropertiesUtil.WIFI_MIN));
			int m2 = Integer.parseInt(PropertiesUtil.getInstance().getValue(
					PropertiesUtil.WIFI_MAX));

			return m1 < m2 ? m1 : m2;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	private int maxWifi() {
		try {
			int m1 = Integer.parseInt(PropertiesUtil.getInstance().getValue(
					PropertiesUtil.WIFI_MIN));
			int m2 = Integer.parseInt(PropertiesUtil.getInstance().getValue(
					PropertiesUtil.WIFI_MAX));
			return m1 > m2 ? m1 : m2;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	private void set8MoIResulrt() {
		setStatusJLabelOK(result_status_8);
	}

	private void set91IResulrt() {
		result_response_9_1.setText("收到按钮信号");
	}

	private void set92IResulrt() {
		setStatusJLabelOK(result_status_9);
		result_response_9_1.setText("收到长按信号");
	}

	private void setaResulrt() {
		setStatusJLabelOK(result_status_a);
		result_response_a.setText("复位成功");
	}

	private void setPaneFocusable() {

		adbPathButton.setFocusable(false);
		startTestButton.setFocusable(false);
		// recordRadioButton.setFocusable(false);
		// playRadioButton.setFocusable(false);

		contentPane.setFocusable(true);
	}

	private void adbPathButtonAction() {

		/*
		 * MyDialog dlg = new MyDialog(USPPcClient.this,true); dlg.show();
		 */
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

	}

	private void startTestButtonAction() {

		if (!checkAndroidAdbPath(adbPath)) {

			new WarnDialog(VthingPCClient.this, "配置文件", "请先配置基本信息").show();

			return;
		}

		if (!isConnected) {

			new Thread() {
				public void run() {
					showStartTestWarn();

				};
			}.start();

			try {
				Thread.sleep(500);
			} catch (Exception e) {
				// TODO: handle exception
			}
			new Thread() {
				public void run() {
					reconnect();

				};
			}.start();

		}
	}

	private void recordRadioButtonAction() {
		new Thread() {
			public void run() {
				try {
					showMicrophoneTestWarn();
					sendMsgString(UsbMessage.MESSAGETYPE_MICROPHONE,
							UsbMessage.MESSAGEACTION_MICROPHONE_STARTRECORDING,
							null, null);
					Thread.sleep(1000 * 15);
					sendMsgString(UsbMessage.MESSAGETYPE_MICROPHONE,
							UsbMessage.MESSAGEACTION_MICROPHONE_STOPRECORDING,
							null, null);
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		}.start();
	}

	private void playRadioButtonAction() {
		sendMsgString(UsbMessage.MESSAGETYPE_MICROPHONE,
				UsbMessage.MESSAGEACTION_MICROPHONE_PLAYRECORDING, null, null);
	}

	private void showMicrophoneTestWarn() {
		StringBuilder builder = new StringBuilder("<html>");
		// builder.append("点击开始按钮开始录音").append("<br/>");
		builder.append("请对准麦克风说话").append("<br/>");
		builder.append("8秒后系统停止录音").append("<br/>");
		builder.append("<span style=\"color:#FF0000;\">");
		builder.append("请在录音完毕后播放");
		builder.append("</span>");
		builder.append("</html>");
		new WarnDialog(VthingPCClient.this, "喇叭测试中", builder.toString()).show();
	}

	//

	private void showStartTestWarn() {
		if (toastDialog != null) {
			toastDialog.show();

		}

	}

	private void hiddenStartTestWarn() {
		if (toastDialog != null) {
			toastDialog.dispose();
		}
	}

	private void connectErrorShow() {

		StringBuilder builder = new StringBuilder("<html>");
		// builder.append("点击开始按钮开始录音").append("<br/>");
		builder.append("未连接到设备").append("<br/>");
		builder.append("请保证USB正常连接").append("<br/>");
		builder.append("<span style=\"color:#FF0000;\">");
		builder.append("请重试");
		builder.append("</span>");
		builder.append("</html>");
		new WarnDialog(VthingPCClient.this, "未连接到设备", builder.toString())
				.show();
	}

	private void showRecordEndAction() {
		StringBuilder builder = new StringBuilder("<html>");
		// builder.append("点击开始按钮开始录音").append("<br/>");
		builder.append("录音已经完成").append("<br/>");
		builder.append("请听声音").append("<br/>");
		new WarnDialog(VthingPCClient.this, "麦克风", builder.toString()).show();
	}

	private class RecvMsg implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				String currCMD = readFromSocket(in);
				if ("error".equalsIgnoreCase(currCMD)) {
					endSocket();
					break;
				}

				recMsgString(currCMD);

			}

		}

	}

	private void socketEndAction() {
		StringBuilder builder = new StringBuilder("<html>");
		// builder.append("点击开始按钮开始录音").append("<br/>");是
		builder.append("连接已经断开").append("<br/>");
		builder.append("1.请记录下如何操作导致断开").append("<br/>");
		;
		builder.append("2.复位设备会引起断开").append("<br/>");
		;
		builder.append("</html>");
		SelectDialog selectDialog = new SelectDialog(VthingPCClient.this,
				"断开连接", builder.toString(), "按Space键关闭");
		selectDialog.setSelectDialogAction(new SelectDialogDelegate() {

			@Override
			public void yesButtonClickAction() {
				// TODO Auto-generated method stub
				allocResponseJLabels();
				allocStatusJLabels();
			}
		});
		selectDialog.show();
	}

	private void recMsgString(String s) {

		System.out.println("############################ " + s);

		if (s == null || s.length() == 0 || "error".equals(s)) {
			endSocket();
			socketEndAction();
			
			
			reconnect();
			
			return;
		}

		CAnyValue caAnyValue = new CAnyValue();
		caAnyValue.decodeJSON(s);
		String backType = caAnyValue.get(UsbMessage.MESSAGETYPE).asString();
		String backAction = caAnyValue.get(UsbMessage.MESSAGEACTION).asString();

		if (UsbMessage.MESSAGETYPE_USB.equals(backType)) {

			hiddenStartTestWarn();

			if (UsbMessage.MESSAGESTATUS_OK.equals(caAnyValue.get(
					UsbMessage.MESSAGESTATUS).asString())) {
				isConnected = true;
				set1USBResult();

				get2VersionAction();

			}
		} else if (UsbMessage.MESSAGETYPE_VERSION.equals(backType)) {

			// showTestLeftMirphoneWarn();

			set2VersionResult(caAnyValue.get(UsbMessage.ARG0).asString(),
					caAnyValue.get(UsbMessage.ARG1).asString());

			showSpeakerWarn();
			get3SpeakerResulrt();

		} else if (UsbMessage.MESSAGETYPE_SPEAKER.equals(backType)) {
			set3SpeakerResulrt();

			StringBuilder builder = new StringBuilder("<html>");
			// builder.append("点击开始按钮开始录音").append("<br/>");
			builder.append("是否能听到高低音？").append("<br/>");
			builder.append("高音：有点刺耳的声音").append("<br/>");
			builder.append("低音：一直嘟嘟嘟的声音");
			builder.append("</span>");
			builder.append("</html>");

			SelectDialog selectDialog = new SelectDialog(VthingPCClient.this,
					"喇叭测试", builder.toString());
			selectDialog.setSelectDialogAction(new SelectDialogDelegate() {

				@Override
				public void yesButtonClickAction() {
					// TODO Auto-generated method stub
					showFMWarn();
					get4FMResulrt();
				}

			});
			selectDialog.show();

		} else if (UsbMessage.MESSAGETYPE_FM.equals(backType)) {
			set4FMResulrt();
			// 固定频道 130.7
			SelectDialog selectDialog = new SelectDialog(VthingPCClient.this,
					"FM测试", "FM是否能收到电台？");
			selectDialog.setSelectDialogAction(new SelectDialogDelegate() {

				@Override
				public void yesButtonClickAction() {
					// TODO Auto-generated method stub
					// 调频之后原来是测试网络了 ，现在改为测试麦克风后测试网络
					// get5WIFIResulrt();

					showTestLeftMirphoneWarn();
				}

			});
			selectDialog.show();

		} else if (UsbMessage.MESSAGETYPE_WIFI.equals(backType)) {
			if (UsbMessage.MESSAGEACTION_WIFI_SEARCH.equals(backAction)) {
				String arg0 = caAnyValue.get(UsbMessage.ARG0).asString();
				String items[] = arg0.split(UsbMessage.SEPARATOR);
				if (arg0.length() > 0 && items.length > 0) {
					StringBuffer sb = new StringBuffer();
					for (String string : items) {
						sb.append(string).append(";");
					}
					setStatusJLabelOK(result_status_5);
					result_response_5.setText("搜索到" + items.length + "个网络:"
							+ sb.toString());

					sendMsgString(UsbMessage.MESSAGETYPE_WIFI,
							UsbMessage.MESSAGEACTION_WIFI_CONNECTED,
							getDefaultWifiName(), getDefaultWifiPassword());

				} else {
					setStatusJLabelFail(result_status_5);
					showNOTresultWifiWarn();
				}

			} else if (UsbMessage.MESSAGEACTION_WIFI_CONNECTED
					.equals(backAction)) {
				String arg0 = caAnyValue.get(UsbMessage.ARG0).asString();
				String arg1 = caAnyValue.get(UsbMessage.ARG1).asString();
				setStatusJLabelOK(result_status_6);
				setStatusJLabelOK(result_status_7);

				if (UsbMessage.MESSAGESTATUS_OK.equals(arg0)) {
					result_response_6.setText("连接成功");
				} else if (UsbMessage.MESSAGESTATUS_FAIL.equals(arg0)) {
					result_response_6.setText("连接失败");
					setStatusJLabelFail(result_status_6);
					setStatusJLabelFail(result_status_7);
				}
				// result_response_7.setText(arg1);
				set7NetRSSIResulrt(arg1);

				showButtonAction();

			}

		} else if (UsbMessage.MESSAGETYPE_MICROPHONE.equals(backType)) {
			//

			if (UsbMessage.MESSAGEACTION_MICROPHONE_PLAYRECORDINGEND
					.equals(backAction)) {
				// recordRadioButton.setVisible(false);
				// playRadioButton.setVisible(true);

				String arg0 = caAnyValue.get(UsbMessage.ARG0).asString();

				if (UsbMessage.MICROPHONE_LEFT.equals(arg0)) {
					SelectDialog selectDialog = new SelectDialog(
							VthingPCClient.this, "麦克风测试", "是否能够听到录音？");
					selectDialog
							.setSelectDialogAction(new SelectDialogDelegate() {

								@Override
								public void yesButtonClickAction() {
									// TODO Auto-generated method stub
									// testLeftMirphone();
									// System.out.println("---------------------  ");

									new Thread() {
										@Override
										public void run() {
											// TODO Auto-generated method stub
											// super.run();
											// testRighttMirphone();

											showTestRightMirphoneWarn();
										}
									}.start();

								}

							});
					selectDialog.show();

				} else if (UsbMessage.MICROPHONE_RIGHT.equals(arg0)) {
					setStatusJLabelOK(result_status_8);
					SelectDialog selectDialog = new SelectDialog(
							VthingPCClient.this, "麦克风测试", "是否能够听到录音？");
					selectDialog
							.setSelectDialogAction(new SelectDialogDelegate() {

								@Override
								public void yesButtonClickAction() {
									// TODO Auto-generated method stub
									// testRighttMirphone();

									// showTestRightMirphoneWarn();
									/*
									 * System.out .println(
									 * "------   yesButtonClickAction  --------  "
									 * );
									 * 
									 * buttonDialog1 = null; buttonDialog1 = new
									 * SelectDialog( VthingPCClient.this,
									 * "按钮测试", "请按下胸前按钮"); buttonDialog1
									 * .setSelectDialogAction(new
									 * SelectDialogDelegate() {
									 * 
									 * @Override public void
									 * yesButtonClickAction() { // TODO
									 * Auto-generated // method stub
									 * buttonDialog2ShowAction(); } });
									 * buttonDialog1.show();
									 */

									// / 测试WIFI完成之后测试胸前按钮
									// showButtonAction();

									get5WIFIResulrt();

								}

							});
					selectDialog.show();

				}

			}

		} else if (UsbMessage.MESSAGETYPE_BUTTON.equals(backType)) {
			if (UsbMessage.MESSAGEACTION_BUTTON_CLICK.equals(backAction)) {
				// result_response_9_1.setText("按下完成");
				// result_response_9_1.setForeground(Color.green);

				setStatusJLabelOK(result_status_9);
				buttonDialog2ShowAction();
				sendMsgString(UsbMessage.MESSAGETYPE_BUTTON,
						UsbMessage.MESSAGEACTION_BUTTON_CLICK,
						"", "");

			} else if (UsbMessage.MESSAGEACTION_BUTTON_LONGCLICK
					.equals(backAction)) {
				// result_response_9_2.setText("长按完成");
				// result_response_9_2.setForeground(Color.green);
				// buttonDialog2DismissAction();

			}

		}

		/*
		 * if (UsbMessage.MESSAGETYPE_USB.equals(backType)) { if
		 * (UsbMessage.MESSAGESTATUS_OK.equals(caAnyValue.get(
		 * UsbMessage.MESSAGESTATUS).asString())) { isConnected = true;
		 * 
		 * connectedSuccessShow(); }
		 * 
		 * setTestPassStatus(usbTestResultLabel, isConnected);
		 * 
		 * } else if (UsbMessage.MESSAGETYPE_SPEAKER.equals(backType)) {
		 * 
		 * } else if (UsbMessage.MESSAGETYPE_FM.equals(backType)) {
		 * 
		 * } else if (UsbMessage.MESSAGETYPE_MICROPHONE.equals(backType)) { if
		 * (UsbMessage.MESSAGEACTION_MICROPHONE_STOPRECORDING
		 * .equals(backAction)) {
		 * 
		 * if (!isRecordEnding) { recordingEndShow(); }
		 * 
		 * isRecordEnding = true; }
		 * 
		 * } else if (UsbMessage.MESSAGETYPE_WIFI.equals(backType)) {
		 * 
		 * if (UsbMessage.MESSAGEACTION_WIFI_SEARCH.equals(backAction)) { String
		 * arg0 = caAnyValue.get(UsbMessage.ARG0).asString(); String items[] =
		 * arg0.split(UsbMessage.SEPARATOR); if (items.length > 0) { wifiName =
		 * items[0]; allocCombox(items); }
		 * 
		 * } else if (UsbMessage.MESSAGEACTION_WIFI_CONNECTED
		 * .equals(backAction)) {
		 * 
		 * if (UsbMessage.MESSAGESTATUS_OK.equals(caAnyValue.get(
		 * UsbMessage.MESSAGESTATUS).asString())) { connectNewOK(); }else if
		 * (UsbMessage.MESSAGESTATUS_FAIL.equals(caAnyValue.get(
		 * UsbMessage.MESSAGESTATUS).asString())){ connectNewFail(); }
		 * 
		 * connectNetMessage(); }
		 * 
		 * } else if (UsbMessage.MESSAGETYPE_BUTTON.equals(backType)) {
		 * 
		 * if (UsbMessage.MESSAGEACTION_BUTTON_LONGCLICK.equals(backAction)) {
		 * 
		 * setTestPassStatus( longClickButtonLabel,
		 * UsbMessage.MESSAGESTATUS_OK.equals(caAnyValue.get(
		 * UsbMessage.MESSAGESTATUS).asString()));
		 * 
		 * } else if (UsbMessage.MESSAGEACTION_BUTTON_CLICK.equals(backAction))
		 * { setTestPassStatus( clickButtonLabel,
		 * UsbMessage.MESSAGESTATUS_OK.equals(caAnyValue.get(
		 * UsbMessage.MESSAGESTATUS).asString())); } }
		 */

	}

	private void buttonDialog2ShowAction() {
		// buttonDialog1.dispose();

		buttonDialog1 = null;
		buttonDialog1 = new SelectDialog(VthingPCClient.this, "按钮测试", "胸前按钮正常",
				"完成(键盘Space键)");
		buttonDialog1.setSelectDialogAction(new SelectDialogDelegate() {

			@Override
			public void yesButtonClickAction() {
				// TODO Auto-generated method stub
				buttonDialog2DismissAction();
			}
		});
		buttonDialog1.show();

	}

	private void buttonDialog2DismissAction() {
		setStatusJLabelOK(result_status_a);
		StringBuilder builder = new StringBuilder("<html>");
		// builder.append("点击开始按钮开始录音").append("<br/>");
		builder.append("测试完成").append("<br/>");
		builder.append("请换下一台测试").append("<br/>");
		builder.append("</html>");

		SelectDialog selectDialog = new SelectDialog(VthingPCClient.this,
				"测试完成", builder.toString(), "完成(键盘Space键)");
		selectDialog.setSelectDialogAction(new SelectDialogDelegate() {

			@Override
			public void yesButtonClickAction() {
				// TODO Auto-generated method stub
				endSocket();
				allocResponseJLabels();
				allocStatusJLabels();
				
				
				
				
				
				 
			}
		});
		selectDialog.show();

	}

	private boolean checkPropites() {
		PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
		String s1 = propertiesUtil.getValue(PropertiesUtil.WIFI_NAME);
		String s2 = propertiesUtil.getValue(PropertiesUtil.WIFI_PASSWORD);
		String s3 = propertiesUtil.getValue(PropertiesUtil.IMG);
		String s4 = propertiesUtil.getValue(PropertiesUtil.APK);
		adbPath = propertiesUtil.getValue(PropertiesUtil.ADBPATH);

		if (s1.length() > 0 && s2.length() > 0 && checkAndroidAdbPath(adbPath)) {
			return true;
		}

		return false;

	}

	private boolean checkAndroidAdbPath(String path) {

		if (path.length() > 0 && path.contains("adb")) {
			return true;
		}

		return false;
	}

	private void sendMsgString(String msgType, String msgAction, String arg0,
			String arg1) {
		CAnyValue caAnyValue = new CAnyValue();
		caAnyValue.put(UsbMessage.MESSAGETYPE, msgType);
		caAnyValue.put(UsbMessage.MESSAGEACTION, msgAction);
		caAnyValue.put(UsbMessage.ARG0, arg0 != null ? arg0 : "");
		caAnyValue.put(UsbMessage.ARG1, arg1 != null ? arg1 : "");
		String sBuf = caAnyValue.encodeJSON();
		sendMsgString(sBuf);
	}

	private void sendMsgString(String s) {
		try {
			out.write(s.getBytes());
			out.flush();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e + "-------------------");
		}

	}

	private void connectAndroid() {
		try {

			Runtime rt = Runtime.getRuntime();
			System.out.println(adbPath + order);
			Process proc = rt.exec(adbPath + order);
			InputStream stderr = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}

			Thread.sleep(1000*5);

			Runtime.getRuntime().exec(adbPath + " forward tcp:12580 tcp:10086"); // 端口转换
			// Runtime.getRuntime().exec(adbPath +
			// " forward tcp:5555 tcp:10086");
		} catch (Throwable t) {
			// t.printStackTrace();

			System.out.println("======@@@@@@@@@@@@@@@@" + t);
		}

	}

	private void startSocket() {
		try {
			InetAddress serveraddr = null;
			serveraddr = InetAddress.getByName("127.0.0.1");
			System.out.println("TCP 1111" + "C: Connecting...");
			socket = new Socket(serveraddr, 12580);
			socket.setKeepAlive(true);
			socket.setSoTimeout(1000 * 3600);
			System.out.println("TCP 221122" + "C:RECEIVE");
			out = new BufferedOutputStream(socket.getOutputStream());
			in = new BufferedInputStream(socket.getInputStream());
			sendMsgString(UsbMessage.MESSAGETYPE_USB,
					UsbMessage.MESSAGEACTION_USB_CONNECTEDUSB, null, null);

		} catch (Exception e) {
			// TODO: handle exception

			System.out.println("-----------------------------" + e);

			if (e instanceof java.net.ConnectException) {
				
				reconnect();
				
				/*

				hiddenStartTestWarn();
				try {
					Thread.sleep(500);
				} catch (Exception e2) {
					// TODO: handle exception
				}
				connectErrorShow();
				*/
				
			}
		}

	}
	
	
	private void reconnect(){
		endSocket();
		
		
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		connectAndroid();
		startSocket();
		new Thread(new RecvMsg()).start();
	}
	

	private void endSocket() {
		isConnected = false;

		try {
			
			
			if (in!=null) {
				in.close();
			}
			if (out!=null) {
				out.close();
			}
			
			if (socket!=null) {
				socket.close();
			}
			
			

			if (in != null) {
				in = null;
			}
			if (out != null) {
				out = null;
			}
			if (socket != null) {
				socket = null;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 
	 * @param in
	 * @return
	 * 
	 *         read data from InputStream
	 */

	public static String readFromSocket(InputStream in) {
		System.out.println("readFromSocket    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		int MAX_BUFFER_BYTES = 4000;
		String msg = null;
		byte[] tempbuffer = new byte[MAX_BUFFER_BYTES];
		try {

			int numReadedBytes = in.read(tempbuffer, 0, tempbuffer.length);
			if (numReadedBytes != -1) {
				msg = new String(tempbuffer, 0, numReadedBytes, "utf-8");
			}

		} catch (Exception e) {

			System.out.println(e + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

			msg = "error";
			
//			new VthingPCClient().reconnect();
			
		} finally {
			tempbuffer = null;
		}

		return msg;

	}

	private static String getDefaultWifiName() {
		return PropertiesUtil.getInstance().getValue(PropertiesUtil.WIFI_NAME);
	}

	private static String getDefaultWifiPassword() {
		return PropertiesUtil.getInstance().getValue(
				PropertiesUtil.WIFI_PASSWORD);
	}
}

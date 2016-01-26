package com.pi2e.vthings;

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


public class ToastDialog extends Dialog implements ActionListener{
	
	
	
	public interface ToastDialogDelegate{
		public void yesButtonClickAction();
		
	}
	ToastDialogDelegate toastDialogDelegate=null;
	
	public void setToastDialogDelegate(ToastDialogDelegate toastDialogDelegate){
		
		this.toastDialogDelegate=toastDialogDelegate;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -6509297267808916752L;
	
	JButton yes = new JButton("停止(键盘Space键)");
	public ToastDialog(Frame parent, String titleString) {
		super(parent, true);
		
		
		JLabel label = new JLabel();
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(titleString);
		setSize(460, 300);
		System.out.println("0----------" + parent.getLocation().x + "     "
				+ parent.getLocation().y);
		setLocation(new Point(230 +100 + parent.getLocation().x,
				150 + parent.getLocation().y+100));
		setResizable(false);
		setLayout(null);

		Dimension preferred = new Dimension(400, 100);// 设置尺寸
		label.setPreferredSize(preferred);
		
		
		Font font = new Font("宋体", Font.BOLD, 25);
		label.setFont(font);
		StringBuilder builder = new StringBuilder("<html>");
		// builder.append("点击开始按钮开始录音").append("<br/>");
		builder.append("测试正在进行中").append("<br/>");
		builder.append("请不要断开USB数据线").append("<br/>");
		 
		builder.append("</html>");
		
		label.setText(builder.toString());
		
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBounds(30, 75, 400, 100);
		
		add(label);
	
		
		Dimension preferredSize = new Dimension(400, 50);// 设置尺寸
		yes.setPreferredSize(preferredSize);
		add(yes);
		yes.setBounds(30, 200, 400, 50);
//		yes.setForeground(Color.green);
		yes.addActionListener(this);
		yes.setText("停止");
		
//		setNOfucous();
		
		
		
	}

	/*
	 

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
			 
				if (toastDialogDelegate != null) {
					toastDialogDelegate.yesButtonClickAction();
				}

			 
			
			setNOfucous();
			dispose();
		}
		 
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
*/
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == yes) {
			if (toastDialogDelegate != null) {
				toastDialogDelegate.yesButtonClickAction();
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
	
	

	private void setNOfucous(){
 
		yes.setFocusable(false);
//		no.setFocusable(false);
	}
	 

}

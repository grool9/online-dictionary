package src.Interface.listener;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.*;

import Server.Picture;
import src.Interface.panel.ChoosePanel;
import src.Interface.panel.LoginPanel;
import src.Interface.panel.SearchPanel;
import src.Interface.panel.TextPanel;
import src.Interface.share.SendPicture;
import src.Translate.BaiduTranslate;
import src.Translate.BingTranslate;
import src.Translate.History;
import src.Translate.JinshanTranslate;
import src.Translate.YoudaoTranslate;
import src.userLogin.Login;
import src.userLogin.UserState;

public class ButtonListener implements ActionListener{
	Object []obj;
	private int type;
	private UserState user=new UserState();
	private Socket socket;
	private DataOutputStream toServer;
	Login login;
	
	private Lock lock=new ReentrantLock();

	
	public ButtonListener(int type,UserState user, Socket soct,Object []obj){
		this.obj=obj;
		this.type=type;
		this.user=user;
		this.socket=soct;
	/*	try{
			//create an output stream to send data to the server
			toServer=new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException ex){
			System.err.println(ex);
			System.err.println("Fail!");
		}*/
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch (type){
		case 1:handleSearch();break;//search button
		case 2:handlePrev();break;//prev button
		case 3:handleNext();break;//next button
		case 5:handleLogin();break;//login button
		case 7:handleMessage();break;//message button
		case 8:handleShare();break;//share button
		case 10:case 11:case 12:case 13:case 14:case 15:handleColor();break;
		}
	}

	public void handleSearch(){
		SearchPanel searchpanel=(SearchPanel)obj[0];
		TextPanel textpanel=(TextPanel)obj[1];
		History his  = (History)obj[2];
	//	String key = searchpanel.input.getSelectedItem().toString();
		String key=searchpanel.input.getText();
		
	/*	lock.lock();
		try{
			//send
			toServer.writeInt(3);
			toServer.writeUTF(key);
			
			toServer.writeInt(5);
			toServer.writeInt(6);
		}
		catch(IOException ex){
			System.err.println(ex);
		}
		finally{
			lock.unlock();
		}*/
		
		if(textpanel.bing.isSelected()){
			BingTranslate B = new BingTranslate();
			String text = B.Translation(key);
			textpanel.Out.setText(text);
		}
		if(textpanel.youdao.isSelected()){
			YoudaoTranslate Y = new YoudaoTranslate();
			String text = Y.Translation(key);
			textpanel.Out.setText(text);
		}
		if(textpanel.jinshan.isSelected()){
			JinshanTranslate J = new JinshanTranslate();
			String text = J.Translate(key);
			textpanel.Out.setText(text);
		}	
		
		Vector<String>H=his.Read();
		H.addElement(key);
		his.Write(H,1);
	}
	
	public void handlePrev(){
		SearchPanel searchpanel = (SearchPanel)obj[0];
		TextPanel textpanel = (TextPanel)obj[1];
		History his = (History)obj[2];
		
		Vector<String>H=his.Read();
		his.prevpointer();
		String key = H.elementAt(his.getpointer());
		his.Write(H, 0);
		
		searchpanel.input.setText(key);
		if(textpanel.bing.isSelected()){
			BingTranslate B = new BingTranslate();
			String text = B.Translation(key);
			textpanel.Out.setText(text);
		}
		if(textpanel.youdao.isSelected()){
			YoudaoTranslate Y = new YoudaoTranslate();
			String text = Y.Translation(key);
			textpanel.Out.setText(text);
		}
		if(textpanel.jinshan.isSelected()){
			JinshanTranslate J = new JinshanTranslate();
			String text = J.Translate(key);
			textpanel.Out.setText(text);
		}	
	}
	
	public void handleNext(){
		SearchPanel searchpanel = (SearchPanel)obj[0];
		TextPanel textpanel = (TextPanel)obj[1];
		History his = (History)obj[2];
		
		Vector<String>H=his.Read();
		his.nextpointer();
		String key = H.elementAt(his.getpointer());
		his.Write(H, 0);
		
		searchpanel.input.setText(key);
		if(textpanel.bing.isSelected()){
			BingTranslate B = new BingTranslate();
			String text = B.Translation(key);
			textpanel.Out.setText(text);
		}
		if(textpanel.youdao.isSelected()){
			YoudaoTranslate Y = new YoudaoTranslate();
			String text = Y.Translation(key);
			textpanel.Out.setText(text);
		}
		if(textpanel.jinshan.isSelected()){
			JinshanTranslate J = new JinshanTranslate();
			String text = J.Translate(key);
			textpanel.Out.setText(text);
		}	
	}
	
	public void handleLogin(){
		LoginPanel loginpanel = (LoginPanel)obj[0];
		TextPanel textpanel = (TextPanel)obj[1];
		if(!user.Logged()){	
			login = new Login(socket);		
			login.setLocation(200,100);	
			login.setVisible(true);
		}
		login.addWindowListener(new WindowAdapter(){
			public void windowClosing(java.awt.event.WindowEvent e){
				super.windowClosing(e);
				System.out.println("closed");
				//user=login.getUser();
				if(user.Logged()){	
					loginpanel.Right.add(loginpanel.message);
					loginpanel.Right.add(loginpanel.Logout);
					loginpanel.Right.revalidate();
					loginpanel.Right.repaint();
					textpanel.Above.add(textpanel.like);
					textpanel.Above.add(textpanel.share);
					textpanel.Above.revalidate();
					textpanel.Above.repaint();
					
				}
			}
		});
	}
	
	
	public void handleMessage(){
		LoginPanel loginpanel = (LoginPanel)obj[0];
		String messagefile="image/message/2.png";
		ImageIcon icon = new ImageIcon(messagefile);  
        icon.setImage(icon.getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT));
		loginpanel.message.setIcon(icon);
	}
	
	public void handleShare(){
		SearchPanel searchpanel = (SearchPanel)obj[0];
		TextPanel textpanel = (TextPanel)obj[1];
		String[] userlist = (String[])obj[2];
		String[] onlineuserlist = (String [])obj[3]; 
		String key = searchpanel.input.getText();
		if(textpanel.bing.isSelected()){
			BingTranslate B = new BingTranslate();
			String text = B.Translation(key);
			SendPicture pic=new SendPicture(text,socket,userlist,onlineuserlist);
		}
		if(textpanel.youdao.isSelected()){
			YoudaoTranslate Y = new YoudaoTranslate();
			String text = Y.Translation(key);
			SendPicture pic=new SendPicture(text,socket,userlist,onlineuserlist);
		//	textpanel.Out.setText(text);
		}
		if(textpanel.jinshan.isSelected()){
			JinshanTranslate J = new JinshanTranslate();
			String text = J.Translate(key);
			SendPicture pic=new SendPicture(text,socket,userlist,onlineuserlist);
		//	textpanel.Out.setText(text);
		}	
	}
	
	public void handleColor(){
		String color="";
		switch(type){
		case 10:color="green";break;
		case 11:color="yellow";break;
		case 12:color="blue";break;
		case 13:color="darkblue";break;
		case 14:color="pink";break;
		case 15:color="black";break;
		}
		LoginPanel loginpanel = (LoginPanel)obj[0];
		SearchPanel searchpanel = (SearchPanel)obj[1];
		ChoosePanel choosepanel= (ChoosePanel)obj[2];
		TextPanel textpanel = (TextPanel)obj[3];
		
		loginpanel.setColor(color);
		searchpanel.setColor(color);
		choosepanel.setColor(color);
		textpanel.setColor(color);
	}
}

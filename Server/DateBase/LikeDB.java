package Server.DateBase;

import java.sql.*;
import java.util.Vector;

public class LikeDB {
	private Connection connection=null;
	private PreparedStatement pstmt;
    private PreparedStatement pstmt1;
    private PreparedStatement pstmt2;
	
    public LikeDB(Connection con){
    	connection=con;
    }
    
    //用户点赞处理
	public void changeLikes(String username, String word,String aDict){
		String sql1,sql2;

		try{
			sql1="update personal_likes set "+aDict + "=0-"+aDict + 
					" where pusername=? and pword=?;";
			sql2="update likes set "+aDict+"="+aDict+
					"+(select "+aDict+
					" from personal_likes where pusername = ? and pword=?) where word=?;";
			
			//personal_likes
			pstmt1=(PreparedStatement)connection.prepareStatement(sql1);
			pstmt1.setString(1, username);
			pstmt1.setString(2, word);
			
			//likes
			pstmt2=(PreparedStatement)connection.prepareStatement(sql2);
			pstmt2.setString(1, username);
			pstmt2.setString(2, word);
			pstmt2.setString(3, word);
			
			pstmt1.executeUpdate();
			pstmt2.executeUpdate();
			
			pstmt1.close();
			pstmt2.close();
		}
		catch(SQLException ex){
			ex.printStackTrace();
		}
	}
	
	public void add(String username, String word){
		String queryString1="select * from likes where word=?";
		String queryString2="select * from personal_likes where pusername=? and pword=?";
		
		String addString1="insert into likes (word) values (?)";
		String addString2="insert into personal_likes (pusername, pword) values (?,?)";
		
		try{
			pstmt1=(PreparedStatement)connection.prepareStatement(queryString1);
			pstmt1.setString(1, word);
			
			pstmt2=(PreparedStatement)connection.prepareStatement(queryString2);
			pstmt2.setString(1, username);
			pstmt2.setString(2, word);
			
			ResultSet rset1=pstmt1.executeQuery();
			ResultSet rset2=pstmt2.executeQuery();
			
			if(rset1.next()){
			}
			else{
				pstmt1.close();
				
				pstmt1=(PreparedStatement)connection.prepareStatement(addString1);
				pstmt1.setString(1, word);
				
				//System.out.println(pstmt1);
				pstmt1.executeUpdate();
			}
			
			if(rset2.next()){	
			}
			else{
				pstmt2=(PreparedStatement)connection.prepareStatement(addString2);
				pstmt2.setString(1, username);
				pstmt2.setString(2, word);
				
				//System.out.println(pstmt2);
				pstmt2.executeUpdate();
			}
			
			pstmt1.close();
			pstmt2.close();
		}
		catch(SQLException ex){
			ex.printStackTrace();
		}
	}
	
	//获得所有点赞数
	public Vector<Integer> getLikes(String word){
		String queryString="select baidu,youdao,jinshan from likes where word=?";
		Vector<Integer> vec=new Vector<Integer>();

		try{
			pstmt=(PreparedStatement)connection.prepareStatement(queryString);
			pstmt.setString(1, word);
			
			ResultSet rset=pstmt.executeQuery();
			
			if(rset.next()){				
				vec.add(rset.getInt(1));
				vec.add(rset.getInt(2));
				vec.add(rset.getInt(3));
				
				pstmt.close();
			}
			else{
				vec.add(0);
				vec.add(0);
				vec.add(0);
				
				pstmt.close();
			}
		}
		catch(SQLException ex){
			ex.printStackTrace();
		}
		return vec;
	}
	
	//获得个人是否点赞
	public Vector<Integer> getPersonalLikes(String username,String word){
		String queryString="select baidu,youdao,jinshan from personal_likes where pusername=? and pword=?";
		Vector<Integer> vec=new Vector<Integer>();

		try{
			pstmt=(PreparedStatement)connection.prepareStatement(queryString);
			pstmt.setString(1, username);
			pstmt.setString(2, word);
			
			ResultSet rset=pstmt.executeQuery();
			
			if(rset.next()){				
				vec.add(rset.getInt(1));
				vec.add(rset.getInt(2));
				vec.add(rset.getInt(3));
				
				pstmt.close();
			}
			else{
				vec.add(-1);
				vec.add(-1);
				vec.add(-1);
				
				pstmt.close();
			}
		}
		catch(SQLException ex){
			ex.printStackTrace();
		}
		return vec;
	}

}

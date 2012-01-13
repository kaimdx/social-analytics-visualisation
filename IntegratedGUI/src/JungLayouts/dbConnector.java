
/********************************************************************************
 *   File Name:		dbConnector.java
 *   File Version:	3
 *   Created by:	Nakul Sharma
 *   Date Created:	Tue Jan 13, 2012
 *********************************************************************************/

/*********************************************************************************
--| Module Body:
--|     dbConnector
--|
--| Implementation Notes:
--|     This Class is used to connect to the database and fetch the stored data
--|		from the database.
--|
--| Portability Issues:
--|		None
--|
 *********************************************************************************/
package JungLayouts;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class dbConnector {
	
	//Lists to collect Image URLs
	public static List<String> returnImageLinks = new ArrayList<String>();
	public static List<String> allreturnImageLinks = new ArrayList<String>();
	
	//Fetched Images paths to return
	public static String[] returnString;
	
	//Parameters used for database Connection
	static String mySqlDB = "jdbc:mysql://localhost/twitter?user=root&password=password";
	static String jdbsDriver = "com.mysql.jdbc.Driver";
	static String UserName = "root";
	static String Password = "password";
    
	//Local Variables Used
	static int countUser, countImages, countTweets;
	static String UserImage, UserNames, toUserNames,userTweets;
	
	/*******************
	*   Constructor   *
	*******************/
	public dbConnector(){
		 
                //Clearing all the Lists
                returnImageLinks.clear();
                allreturnImageLinks.clear();
                
                
		Connection myconnection = null;
			
		try{				   
			Class.forName(jdbsDriver).newInstance();
			myconnection = DriverManager.getConnection (mySqlDB);
			System.out.println("Connected to the Database: " + mySqlDB);
		        
			/*-----------------------------------------
		      Creating a Statement to fetch User Names,
		      Profile Images and Tweet Text
		    -----------------------------------------*/		        
		    Statement User = myconnection.createStatement ();
		    User.executeQuery ("SELECT username,touser,profile_image_source,tweettext FROM tweetalldata");
		    ResultSet resultUser = User.getResultSet();
		        
		    //Resetting the Counter
		    countUser = 0;
		    
                    GraphPlotter.LUserList.clear();
                    GraphPlotter.LtoUserList.clear();
                    
		    //Fetching the User Names
		    while (resultUser.next () & (getNodeCount(GraphPlotter.LUserList, GraphPlotter.LtoUserList) <= SocialNetworkVisualizer.userInputTweetCount-1))
		    {
		    	UserNames = resultUser.getString ("username");
		    	toUserNames = resultUser.getString ("touser");
		    	UserImage = resultUser.getString("profile_image_source");
		    	userTweets = resultUser.getString ("tweettext");
		    	
		    	if(SocialNetworkVisualizer.searchFlag == true){
		    		
		    		if(userTweets.lastIndexOf(SocialNetworkVisualizer.searchKey) != -1){
		    			GraphPlotter.LUserList.add(UserNames);
		    			GraphPlotter.LtoUserList.add(toUserNames);
		    			allreturnImageLinks.add(UserImage);
		    			GraphPlotter.UserTweets.add(userTweets);	
		    			
		    			System.out.println(UserNames);
		    		}		    		
		    	}
		    	else{
	    			GraphPlotter.LUserList.add(UserNames);
	    			GraphPlotter.LtoUserList.add(toUserNames);
	    			allreturnImageLinks.add(UserImage);
	    			GraphPlotter.UserTweets.add(userTweets);	
		    	}
		        
		        ++countUser;
		        
		    }
		        
	        for(int i = 0; i < GraphPlotter.UserTweets.size(); i++){
	        	
	        	System.out.println(GraphPlotter.UserTweets.get(i));
	        }
	        
		    //Closing the Statement & Result Set
		    User.close ();
		    resultUser.close ();
		      
		    System.out.println (SocialNetworkVisualizer.userInputTweetCount + " Users were retrieved.");
		    
		    //Removing the Duplicates
		    returnImageLinks = ProcessData.newUniquify(allreturnImageLinks);
		    
		    //Creating an array of Strings from the List
		    returnString = new String[returnImageLinks.size()];
			for (int n = 0; n <returnImageLinks.size(); n++){				
				returnString[n]= returnImageLinks.get(n);
			}
		}   
		catch(Exception e){
			System.out.println("Problem: can not connect to the Database");
		}
	}
	
	int getNodeCount( List<String> LUserList,  List<String> LtoUserList){
	
		int totalCount = 0;
		List<String> tempLUserList, tempLtoUserList;
		
		//tempLUserList = ProcessData.newUniquify(LUserList);
		//tempLtoUserList = ProcessData.newUniquify(LtoUserList);
		
		totalCount = LUserList.size() + LtoUserList.size();
	
		return totalCount;
	}


}
























import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Mapper {
	
	public static class MapperStructure {
		
		public class userObject {
			@SerializedName("id")
			private long id;
			
			public long getUserId() {
				return id;
			}
		}
		@SerializedName("id")
	    private long tweet_id;  
		@SerializedName("user")
	    private userObject user;
	    //getter and setter methods  
	  
	   
	    public long getTweetId() {  
	        return tweet_id;  
	    }  
	    
	    public long getUserId() {
	    	return user.getUserId();
	    }

	}
	


	public static void main(String[] args){
		BufferedReader reader = null;
		PrintStream writer = null;
		try {

			reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
			writer = new PrintStream(System.out, true, "UTF-8");
			String line = "";
			while ((line = reader.readLine()) != null) {
				Gson gson = new Gson(); 
				MapperStructure mapJson = gson.fromJson(line, MapperStructure.class);
				writer.println(mapJson.getTweetId() + "\t" + mapJson.getUserId());					
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

		} finally {
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		
	}
	
}


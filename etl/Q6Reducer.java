

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import com.google.gson.Gson;

public class Reducer {
public static class OutputStructure {
		
		private long tweet_id;
  
	    private String text;

	    
	    public OutputStructure (long t_id, String t) {
	    	this.tweet_id = t_id;

	    	this.text = t;

	    }


	    public void setTweetId(long t_id) {
	    	tweet_id = t_id;
	    }
	    
	    public long getTweetId() {
	    	return tweet_id;
	    }

	    public void setText(String t) {
	    	text = t;
	    }
	    
	    public String getText() {
	    	return text;
	    }
	    

	}

	public class Result{
		public String text;
		public int score;
	}
	
	/*
	 * Three main data structure in the reducer
	 *  1. scoreMap: ScoreMap stores all the information extracted from afinn.txt
	 *               the key is the text and the value is the score it has;
	 *  2. banMap:   BanMap stores all the information extracted from banned.txt
	 *               the key is the banned string and the value is the string of
	 *               '*' used to combine a new word when it comes to the banned word
	 *  3. rotMap:   Used to generate the banMap
	 */

	private static HashMap<String, String> banMap = new HashMap<>();
	private HashMap<Character, Character> rotMap = new HashMap<>();
	
	private final static String SECRET_CODE = "\u50ea\u87f6\u908d";
	
	
	public void importBan() {
		BufferedReader br = new BufferedReader(new FileReader("banned.properties"));
		try {
		    String line = br.readLine();
		    
		    while (line != null){
		    	String result = "";
		    	for (int i = 0; i < line.length(); i++) {
					char tmp = line.charAt(i);
					if(rotMap.containsKey(tmp))
						tmp = rotMap.get(line.charAt(i));
					result += tmp;
				}
		    	String censorValue = "";
		    	for(int i = 0; i < result.length() - 2; i++){
		    		censorValue += "*";
		    	}
		    	banMap.put(result, censorValue);
		    	line = br.readLine();
		    }

		} finally {
		    br.close();
		}
		
		
	}
	
	public void initRot13(){
		int []start = {65, 97};
		for( int j = 0; j < 2; j ++){
			int base = start[j];
			for( int i = 0; i < 26; i++){
				char key = (char)(base + i);
				char value = '-';
				if(i < 13){
					value = (char)(base + i + 13);
				} else {
					value = (char)(base + i - 13);
				}
				rotMap.put(key, value);	
			}
		}			
	}

	// censore the text
	public String reduce(String text){
		String []words = text.split("[^a-zA-Z0-9]+");
		String resultText = "";
		for (int i = 0; i < words.length; i++) {
			String currentWord = words[i];			
			if(banMap.containsKey(currentWord.toLowerCase())){
				String newWord = currentWord.charAt(0) 
						+ banMap.get(currentWord.toLowerCase()) 
						+ currentWord.charAt(currentWord.length() - 1);
				resultText = resultText + text.substring(0, text.indexOf(currentWord)) + newWord;
				text = text.substring(text.indexOf(currentWord) + currentWord.length());
			
			} else {
				resultText = resultText + text.substring(0, text.indexOf(currentWord)) + currentWord;
				text = text.substring(text.indexOf(currentWord) + currentWord.length());
			}
		}
		
		resultText = resultText + text;
		return resultText;		
	}
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Reducer r = new Reducer();
		r.initRot13();
		r.importBan();


		BufferedReader reader = null;
		PrintStream writer = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
			writer = new PrintStream(System.out, true, "UTF-8");

			String line = "";
			long currentTweetId = 0;
			long previousTweetId = 0;
			while ((line = reader.readLine()) != null) {
				Gson gson = new Gson(); 

				line = line.substring(line.indexOf(" ")+1);
				OutputStructure mapJson = gson.fromJson(line, OutputStructure.class);
				currentTweetId = mapJson.getTweetId();
				// filter the duplicate tweet
				if(currentTweetId == previousTweetId)
					continue;
				String text = mapJson.getText();
				text = text.replace("\n", SECRET_CODE);
				String reduce = r.reduce(text);
				// censor the text
				String reduceText = reduce.replace(SECRET_CODE, "\n");

				// just output the tweetid and text
				OutputStructure output = new OutputStructure(mapJson.getTweetId(), reduceText);				

				writer.println(gson.toJson(output));
				previousTweetId = currentTweetId;
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}

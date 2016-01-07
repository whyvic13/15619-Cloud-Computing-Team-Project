

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import com.google.gson.Gson;

public class Reducer {
		

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Reducer r = new Reducer();
		r.importAfinnScore();
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

				String[] parts = line.split("\t");
				currentTweetId = Long.parseLong(parts[0]);

				if(currentTweetId == previousTweetId)
					continue;
				writer.println(line);
				previousTweetId = currentTweetId;
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			reader.close();
			writer.close();
		}
		
		
	}

}

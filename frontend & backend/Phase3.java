import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import java.io.BufferedReader;
import java.io.FileReader;



public class Phase3 extends Verticle {

		public static class MysqlResult {
			public int score;
			public long tweet_id;
			public String text;
			public String time;

			public MysqlResult(int s, long t, String te, String ti) {
				score = s;
				tweet_id = t;
				text = te;
				time = ti;
			}

			public int getScore() {
				return score;
			}
			public long getTweetId() {
				return tweet_id;
			}
			public String getText() {
				return text;
			}
			public String getTime() {
				return time;
			}
		}

		/// JDBC driver name and database URL
		static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		static final String DB_URL_Q2 = "jdbc:mysql://localhost:3306/q2?useUnicode=yes&characterEncoding=UTF-8";
		//static final String DB_URL_Q3 = "jdbc:mysql://localhost:3306/q3?useUnicode=yes&characterEncoding=UTF-8";
		static final String DB_URL_Q4 = "jdbc:mysql://localhost:3306/q4?useUnicode=yes&characterEncoding=UTF-8";
		// static final String SECRET_CODE = "u908du87f6u50ea";
		//  Database credentials
		static final String USER = "root";
		static final String PASS = "1234";
		static Connection conn_q2 = null;
		//static Connection conn_q3 = null;
		static Connection conn_q4 = null;
		// static Statement stmt = null;


		// Q1
		private static char[][] map = new char[26][91];
		private static int[][] find = new int[50][2500];
		// Q5
		static int[] counts = new int[53767998];
		static HashMap<Long, Integer> userid = new HashMap<>();
		
		public static void init() {
			//Q5
			try {
				BufferedReader reader = new BufferedReader(new FileReader("processedResult"));
				String line = "";
				int i = 0;
				while((line = reader.readLine()) != null) {
					String[] split = line.replace("\n","").split(",");
					counts[i] = Integer.parseInt(split[2]);
					userid.put(Long.parseLong(split[0]), i);
					i++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			//Q1
			for( int i = 0; i < 26; i++ ){
				for( int j = 0; j < 65; j++ ){
					map[i][j] = 'x';
				}
				for( int j = 65; j < 91; j++){
					int sum = j - i;
					if (sum < 65)
						map[i][j] = (char)(sum + 26);
					else
						map[i][j] = (char) sum;
				}
			}
			
			for(int i = 0; i < 50; i++){
				for (int j =0; j < 2500; j++){
					find[i][j] = 0;
				}
			}
			
			for( int cnt = 0; cnt < 50; cnt++){	
				int N = cnt + 1;
				int yCnt = 0;
				for(int i = 0; i < N; i++){
		  			for(int j = 0; j <= i; j++){
		  				find[cnt][yCnt] = j*N+i-j;
		  				yCnt++;
		  			}
		  		}
		  		
		  		for(int i = N; i <= 2*N-2; i++){
		  			for(int j = N-1; i-j<N; j--){
		  				find[cnt][yCnt] = (i-j)*N+j;
		  				yCnt++;
		  			}
		  		}
		  		
			}
			
		}
		
		static {
			try {
				 //STEP 2: Register JDBC driver
				 Class.forName("com.mysql.jdbc.Driver");

				 //STEP 3: Open a connection
				 System.out.println("Connecting to database...");
				
				 conn_q2 = DriverManager.getConnection(DB_URL_Q2,USER,PASS);
				 //conn_q3 = DriverManager.getConnection(DB_URL_Q3,USER,PASS);
				 conn_q4 = DriverManager.getConnection(DB_URL_Q4,USER,PASS);				
				
			}catch (Exception e) {
				e.printStackTrace();
			}

			init();
			
		}

		//compute Y and Z
	  	public String getResponse(BigInteger dividend, String ciphertext)  {		
	  		String response = "";
	  		BigInteger X = new BigInteger("8271997208960872478735181815578166723519929177896558845922250595511921395049126920528021164569045773");		
	  		BigInteger Y = dividend.divide(X);

	  		BigInteger result[] = Y.divideAndRemainder(new BigInteger("25"));
	  		
	  		int Z = 1 + result[1].intValue();		
	  		response = retrieveMessage(ciphertext, Z);
	  		return response;
	  	}
	  	
	  	//retrieve message before Diagonalize step and Caesarify step
	  	public String retrieveMessage(String message, int Z) {
	  		
	  		ArrayList<Character> output = new ArrayList<>();
	  		int num = message.length();
	  		int length = (int)Math.sqrt(num);
	  		
	  		for(int i = 0; i < num; i++)
	  			output.add(message.charAt(find[length-1][i]));
	  		

	  		String result = "";
	  		for(int k = 0; k < output.size(); k++){
	  			result += map[Z][(int)output.get(k)];
	  		}		
	  		
	  		return result;
	  	}

		
		@Override
		public void start() {
		
			final RouteMatcher routeMatcher = new RouteMatcher();
			final HttpServer server = vertx.createHttpServer();
	
			routeMatcher.get("/", new Handler<HttpServerRequest>() {
				@Override
				public void handle(final HttpServerRequest req) {
					
					req.response().end("hello world"); //Do not remove this
				}
			});
			
			routeMatcher.get("/q1", new Handler<HttpServerRequest>() {
				@Override
				public void handle(final HttpServerRequest req) {
					MultiMap map = req.params();
					String key = map.get("key");
					String message = map.get("message");

					String res = "Lucky Star,3398-9470-0419\n";
					SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date now = new Date();
					String strDate = sdfDate.format(now);	      
					res += strDate + "\n";
					if(key != null){
					  String result = getResponse(new BigInteger(key), message);
					  res += result + "\n";
					}
					
					req.response().putHeader("content-type", "application/json; charset=utf-8");
					req.response().end(res);
					// req.response.end(result+text);	
				}
			});
			
			routeMatcher.get("/q2", new Handler<HttpServerRequest>() {
				@Override
				public void handle(final HttpServerRequest req) {
					MultiMap map = req.params();
					final String id = map.get("userid");
					final String raw_time = map.get("tweet_time");
					// final int index = (int)(Long.parseLong(id) % 10);
					// char c = id.charAt(id.length()-1);
					// final int index = c - '0';
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
					java.util.Date parsedDate = null;
					try {
						parsedDate = format.parse(raw_time);						
					}catch (Exception e) {
						e.printStackTrace();
					}
					final long time = parsedDate.getTime()/1000;

					Statement stmt = null;
					String result = "LuckyStar,3398-9470-0419\n";
					//retrieve message from database
					try{							
						//STEP 4: Execute a query
						
						stmt = conn_q2.createStatement();
						String sql = "SELECT text FROM tweet WHERE user_id = " + id + " AND created_at = " + time + ";";
						
						ResultSet rs = stmt.executeQuery(sql);
						//STEP 5: Extract data from result set
						while(rs.next()){
							//Retrieve by column name
							String text = rs.getString("text");
							result += text + "\n";
						}
						rs.close();
							
					}catch(SQLException se){
						//Handle errors for JDBC
						se.printStackTrace();
					}catch(Exception e){
						//Handle errors for Class.forName
						e.printStackTrace();
					}finally {
						try {
							stmt.close();
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
					
					req.response().putHeader("content-type", "application/json; charset=utf-8");
					req.response().end(result);
					// req.response.end(result+text);	
				}
			});


			routeMatcher.get("/q3", new Handler<HttpServerRequest>() {
				@Override
				public void handle(final HttpServerRequest req) {
					MultiMap map = req.params();
					final String start_date = map.get("start_date");
					final String end_date = map.get("end_date");
					final String user_id = map.get("userid");
					final String n = map.get("n");


					// ArrayList<MysqlResult> mysqlPos = new ArrayList<MysqlResult>();
					// ArrayList<MysqlResult> mysqlNeg = new ArrayList<MysqlResult>();
					//StringBuilder result = new StringBuilder("LuckyStar,3398-9470-0419\n");
					String result = "LuckyStar,3398-9470-0419\nPositive Tweets\n\nNegative Tweets\n";
					/*
					//retrieve message from database
					Statement stmt = null;	
					try{
												
						//STEP 4: Execute a query
						stmt = conn_q3.createStatement();
						
						//Query all the data that between the date and related to the target user
						String sql_pos = "SELECT created_at, tweet_id, score, text FROM tweet0 WHERE user_id = " +
						 user_id + " AND created_at BETWEEN '" + start_date + "' AND '" + end_date + "';";

						String sql_neg = "SELECT created_at, tweet_id, score, text FROM tweet1 WHERE user_id = " +
						 user_id + " AND created_at BETWEEN '" + start_date + "' AND '" + end_date + "';";

						// Positive record
						ResultSet rs_pos = stmt.executeQuery(sql_pos);
						

						//STEP 5: Extract data from result set
						while(rs_pos.next()){
							//Retrieve by column name
							int score  = rs_pos.getInt("score");
							long tweet_id = rs_pos.getLong("tweet_id");
							String text = rs_pos.getString("text");
							String time = rs_pos.getString("created_at");
							// store all the data into queue
							// PosElement posE = new PosElement(score,tweet_id,text,time);
							// pos.offer(posE);
							// System.out.println(score + tweet_id + text + time);
							MysqlResult posE = new MysqlResult(score, tweet_id, text, time);
							mysqlPos.add(posE);
						}

						Collections.sort(mysqlPos, new Comparator<MysqlResult>(){
							public int compare(MysqlResult a, MysqlResult b) {
								if (a.score < b.score) {
									return 1;
								}
								else if (a.score > b.score) {
									return -1;
								}
								else {
									return a.tweet_id < b.tweet_id ? -1 : 1;
								}							
							}
						});

						// Negative record
						ResultSet rs_neg = stmt.executeQuery(sql_neg);

						while(rs_neg.next()){
							//Retrieve by column name
							int score  = rs_neg.getInt("score");
							long tweet_id = rs_neg.getLong("tweet_id");
							String text = rs_neg.getString("text");
							String time = rs_neg.getString("created_at");

							// NegElement negE = new NegElement(score, tweet_id, text, time);
							// neg.offer(negE);

							MysqlResult negE = new MysqlResult(score, tweet_id, text, time);
							mysqlNeg.add(negE);

						}

						Collections.sort(mysqlNeg, new Comparator<MysqlResult>(){
							public int compare(MysqlResult a, MysqlResult b) {
								if (a.score < b.score) {
									return -1;
								}
								else if (a.score > b.score) {
									return 1;
								}
								else {
									return a.tweet_id < b.tweet_id ? -1 : 1;
								}							
							}
						});



						rs_pos.close();
						rs_neg.close();

						// retrieve data from two priority queue
						int num = Integer.parseInt(n);
						result.append("Positive Tweets\n");
						int i = 0;
						int posLength = mysqlPos.size();
						while(i < num && i < posLength) {
							MysqlResult tmp = mysqlPos.get(i);
							i++;
							result.append(tmp.getTime()).append(",").append(tmp.getScore()).append(",").append(tmp.getTweetId()).append(",").append(tmp.getText()).append("\n");
						}
						result.append("\nNegative Tweets\n");
						i = 0;
						int negLength = mysqlNeg.size();
						while(i < num && i < negLength) {
							MysqlResult tmp = mysqlNeg.get(i);
							i++;
							result.append(tmp.getTime()).append(",").append(tmp.getScore()).append(",").append(tmp.getTweetId()).append(",").append(tmp.getText()).append("\n");
						}
						
					}catch(SQLException se){
						//Handle errors for JDBC
						se.printStackTrace();
					}catch(Exception e){
						//Handle errors for Class.forName
						e.printStackTrace();
					}finally {
						try{
							stmt.close();
						}catch(Exception e){
							e.printStackTrace();
						}							
					}
					*/
					req.response().putHeader("content-type", "application/json; charset=utf-8");
					req.response().end(result);	
						
				}
			});


			routeMatcher.get("/q4", new Handler<HttpServerRequest>() {
				@Override
				public void handle(final HttpServerRequest req) {
					MultiMap map = req.params();
					final String hashtag = map.get("hashtag");
					final String num = map.get("n");
					Statement stmt = null;
					String result = "LuckyStar,3398-9470-0419\n";
					//retrieve message from database
					try{					
						
						//STEP 4: Execute a query
						stmt = conn_q4.createStatement();
					
						String sql = "SELECT text FROM q4 WHERE hashTag = '" + hashtag + "' LIMIT " + num + ";";
						ResultSet rs = stmt.executeQuery(sql);
						
						while(rs.next()){
							//Retrieve by column name	
							String text = rs.getString("text");						
							result += text + "\n";
						}					
						rs.close();
						
					}catch(SQLException se){
						//Handle errors for JDBC
						se.printStackTrace();
					}catch(Exception e){
						//Handle errors for Class.forName
						e.printStackTrace();
					}finally {
						try{						
							stmt.close();
						}catch(Exception e){
							e.printStackTrace();
						}							
					}
					
					req.response().putHeader("content-type", "application/json; charset=utf-8");
					req.response().end(result);	
						
				}
			});
			
			routeMatcher.get("/q5", new Handler<HttpServerRequest>() {
				@Override
				public void handle(final HttpServerRequest req) {
					MultiMap map = req.params();
					final String userid_min = map.get("userid_min");
					final String userid_max = map.get("userid_max");
					final Long uid_min = Long.parseLong(userid_min);
					final Long uid_max = Long.parseLong(userid_max);

					
					String result = "LuckyStar,3398-9470-0419\n";


					int index_min = userid.get(uid_min);
					int index_max = userid.get(uid_max);


					if(index_min == 0) {
						result += counts[index_max] + "\n";
					}
					else {
						int res = counts[index_max] - counts[index_min - 1];
						result += res + "\n";
					}
					
					req.response().putHeader("content-type", "application/json; charset=utf-8");
					req.response().end(result);
						
				}
			});
		
			server.requestHandler(routeMatcher);
			server.listen(80);
		}
}

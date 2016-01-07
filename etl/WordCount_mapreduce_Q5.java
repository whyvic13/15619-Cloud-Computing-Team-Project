import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class WordCount {

	
	public static class Map extends Mapper<Object, Text, Text, IntWritable>{

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

    
    
	    public void map(Object key, Text value, Context context
	                    ) throws IOException, InterruptedException {
	    	String line = value.toString();
	    	String[] words = line.split("\\t");
		    word.set(words[1]);
		    context.write(word, one);
	    	
	
	    }
	}

	public static class Reduce extends Reducer<Text,IntWritable,Text,IntWritable> {
	  	private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}	
			result.set(sum);
			context.write(key, result);
			
		}
	}
	
	
	public static void main(String[] args) throws Exception {
	  Configuration conf = new Configuration();
	  Job job = Job.getInstance(conf, "word count");
	  job.setJarByClass(WordCount.class);
	  
	  
	  job.setMapperClass(Map.class);

	  job.setReducerClass(Reduce.class);


	  job.setOutputKeyClass(Text.class);
	  job.setOutputValueClass(IntWritable.class);
	  
	  FileInputFormat.addInputPath(job, new Path(args[0]));
	  FileOutputFormat.setOutputPath(job, new Path(args[1]));
	  job.setInputFormatClass(TextInputFormat.class);
	  job.setOutputFormatClass(TextOutputFormat.class);
	  
	  job.waitForCompletion(true);

	}
}

package org.myorg;

import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;


public class FifaPlayers extends Configured implements Tool {


 public static void main(String[] args) throws Exception {
 int res = ToolRunner.run(new FifaPlayers(), args);
 System.exit(res);
 }

 public int run(String[] args) throws Exception {
 Job job = Job.getInstance(getConf(), "wordcount");
 job.setJarByClass(this.getClass());
 // Use TextInputFormat, the default unless job.setInputFormatClass is used
 FileInputFormat.addInputPath(job, new Path(args[0]));
 FileOutputFormat.setOutputPath(job, new Path(args[1]));
 job.setMapperClass(Map.class);
 job.setReducerClass(Reduce.class);
 job.setOutputKeyClass(Text.class);
 job.setOutputValueClass(IntWritable.class);
 return job.waitForCompletion(true) ? 0 : 1;
 }

 public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {

 public void map(LongWritable offset, Text lineText, Context context)
 throws IOException, InterruptedException {
 String line = lineText.toString();
 String [] keyvalue = line.split(",");
 context.write(new Text(keyvalue[5]),new IntWritable (1));
 }
 }

 public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
 @Override
 public void reduce(Text word, Iterable<IntWritable> counts, Context context)
 throws IOException, InterruptedException {
 int sum = 0;
      for (IntWritable count : counts) {
        sum += count.get();
      }
      context.write(word, new IntWritable(sum));
 }
 }
}

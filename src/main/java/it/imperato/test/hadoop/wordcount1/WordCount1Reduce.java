package it.imperato.test.hadoop.wordcount1;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class WordCount1Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

    private LongWritable result = new LongWritable();

    /*public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {

        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        context.write(key, new IntWritable(sum));

    }*/

    public void reduce(Text key, Iterable<LongWritable> values, Reducer<Text, LongWritable, Text, LongWritable>.Context context)
            throws IOException, InterruptedException {
        long sum = 0L;

        LongWritable val;
        for(Iterator var6 = values.iterator(); var6.hasNext(); sum += val.get()) {
            val = (LongWritable)var6.next();
        }

        this.result.set(sum);
        context.write(key, this.result);
    }
}

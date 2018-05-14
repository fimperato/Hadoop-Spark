package it.imperato.test.hadoop.wordcount1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCount1Mapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    public void map(LongWritable lineOffSet, Text record, Context context)
            throws IOException, InterruptedException {

        for (String str : record.toString().split(" ")) {
            context.write(new Text(str), new LongWritable(1));
        }

    }

}

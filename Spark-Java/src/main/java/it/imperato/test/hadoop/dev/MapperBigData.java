package it.imperato.test.hadoop.dev;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Mapper
 */
class MapperBigData extends Mapper<
        LongWritable, // Tipo Key di Input
        Text, // Tipo Value di Input
        Text, // Tipo Key di Output
        IntWritable> {// Tipo Value di Output

    protected void map(
            LongWritable key,
            Text value,
            Context context) throws IOException, InterruptedException {
        // Suddivide le frasi in parole
        String[] words = value.toString().split("\\s+");

        // Per ogni parola
        for(String word : words) {
            // Calcola il minuscolo
            String cleanedWord = word.toLowerCase();

            // Scrive l'accoppiata (word, 1)
            context.write(new Text(cleanedWord), new IntWritable(1));
        }
    }

}

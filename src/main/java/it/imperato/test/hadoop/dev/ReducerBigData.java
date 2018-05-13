package it.imperato.test.hadoop.dev;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reducer
 */
class ReducerBigData extends Reducer<
        Text, // Tipo Key di Input
        IntWritable, // Tipo Value di Input
        Text, // Tipo Key di Output
        IntWritable> { // Tipo Value di Output

    @Override
    protected void reduce(
            Text key,
            Iterable<IntWritable> values,
            Context context) throws IOException, InterruptedException {
        int occurrances = 0;
        // Per ogni parola uguale => somma
        for (IntWritable value : values) {
            occurrances = occurrances + value.get();
        }
        // Scrive il totale delle occorrenze
        context.write(key, new IntWritable(occurrances));
    }
}


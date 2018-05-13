package it.imperato.test.spark.dev;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.apache.spark.mllib.util.MLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.HashMap;

public class JavaRandomForestClassificationExample {

    private static final Logger log = LoggerFactory.getLogger(JavaRandomForestClassificationExample.class);

    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf().setAppName("JavaRandomForestClassificationExample")
                .setMaster("local[2]").set("spark.executor.memory","2g");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        // Load and parse the data file.
        String datapath = "data/mllib/sample_multiclass_classification_data.txt";
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(jsc.sc(), datapath).toJavaRDD();
        // Split the data into training and test sets (30% held out for testing)
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3});
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];

        // Train a RandomForest model.
        // Empty categoricalFeaturesInfo indicates all features are continuous.
        Integer numClasses = 3;
        HashMap<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
        Integer numTrees = 55; // Use more in practice.
        String featureSubsetStrategy = "auto"; // Let the algorithm choose.
        String impurity = "gini";
        Integer maxDepth = 5;
        Integer maxBins = 32;
        Integer seed = 12345;

        final RandomForestModel model = RandomForest.trainClassifier(trainingData, numClasses,
                categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins,
                seed);

        // Evaluate model on test instances and compute test error
        JavaPairRDD<Double, Double> predictionAndLabel =
                testData.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
                    @Override
                    public Tuple2<Double, Double> call(LabeledPoint p) {
                        return new Tuple2<>(model.predict(p.features()), p.label());
                    }
                });
        Double testErr =
                1.0 * predictionAndLabel.filter(new Function<Tuple2<Double, Double>, Boolean>() {
                    @Override
                    public Boolean call(Tuple2<Double, Double> pl) {
                        return !pl._1().equals(pl._2());
                    }
                }).count() / testData.count();
        log.info("Test Error: " + testErr);
        log.info("Learned classification forest model:\n" + model.toDebugString());

        // Save and load model
        model.save(jsc.sc(), "target/tmp/myRandomForestClassificationModel");
        RandomForestModel sameModel = RandomForestModel.load(jsc.sc(),
                "target/tmp/myRandomForestClassificationModel");
        // $example off$

        jsc.stop();
    }
}

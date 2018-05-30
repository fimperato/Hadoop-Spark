package it.imperato.test.spark.dev.dataanalysis;

import it.imperato.test.utils.FileUtils;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

public class SparkAnalysis01 {

    private static final Logger log = Logger.getLogger(SparkAnalysis01.class);

    public static void main(String[] args) {

        String inputPath1 = null, inputPath2 = null, outputPath1 = null, outputPath2 = null;
        Double prezzoThr = null;
        int etaThr;

        if(args==null || args.length<6) {
            inputPath1 = "data/spark/testdata/acquisti.csv";
            inputPath2 = "data/spark/testdata/team.csv";
            outputPath1 = "data/spark/testoutput/analysis01_1";
            // outputPath2 = "data/spark/testoutput/analysis01_2";
            etaThr = 25;
            prezzoThr = new Double(30.5);
        }
        else {
            inputPath1 = args[0];
            inputPath2 = args[1];
            outputPath1 = args[2];
            outputPath2 = args[3];
            prezzoThr = Double.parseDouble(args[4]);
            etaThr = Integer.valueOf(args[4]);
            prezzoThr = Double.parseDouble(args[5]);
        }

        FileUtils.deleteDirectoryWithFiles(outputPath1);

        final Double prezzoThreshold = prezzoThr;
        String sparkMaster = "local[1]";
        SparkConf conf = new SparkConf().setAppName("sparkanalysis01").setMaster(sparkMaster);
        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        JavaRDD<String> acquistiRDD = sparkContext.textFile(inputPath1).cache();
        JavaRDD<String> teamRDD = sparkContext.textFile(inputPath2);

        // PRIMA FASE analisi: quali squadre hanno una eta media superiore alla soglia stabilita:
        JavaRDD<String> acquistiMappedRDD = acquistiRDD.map(new Function<String, String>() {
            @Override
            public String call(String arg0) throws Exception {
                String[] split = arg0.split(",");
                return split[1];
            }
        }).distinct();

        JavaRDD<String> etaMediaMaggioreTeamRDD = teamRDD.filter(new Function<String, Boolean>() {

            @Override
            public Boolean call(String arg0) throws Exception {
                String[] split = arg0.split(",");
                try {
                    if (Integer.valueOf(split[3]) >= etaThr) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    log.warn("Titolo elaborato.");
                }
                return false;
            }

        }).map(new Function<String, String>() {
            @Override
            public String call(String arg0) throws Exception {
                String[] split = arg0.split(",");
                return split[0];
            }
        });

        etaMediaMaggioreTeamRDD.saveAsTextFile(outputPath1);

        JavaRDD<String> loaded = sparkContext.textFile(outputPath1);
        // Collect RDD per la stampa risultati
        log.info("###### FASE - 01 ###### Verifica risultato RDD salvato ...");
        for(String line : loaded.collect()){
            log.info("###### FASE - 01 ###### Read RDD line: " + line);
        }


        // SECONDA FASE analisi: Selezione delle squadre con propensione ad acquisti che svecchiano la rosa
        // Elimino la riga dell'header
        JavaRDD<String> acquistiFilteredRDD = acquistiRDD
                .filter(new Function<String, Boolean>() {
                    @Override
                    public Boolean call(String line) throws Exception {
                        String[] split = line.split(",");
                        try {
                            Double value = Double.parseDouble(split[4]);
                            return true;
                        } catch (NumberFormatException e) {
                            log.warn("Titolo filtrato."); // caso split[4]="EtaPlayer"
                        }
                        return false;
                    }
                });

        // Rimappo id e valore-object sulla tupla spark
        JavaPairRDD<String, TipoAcquistiConteggio> mapConteggioAcquistiGiovani = acquistiFilteredRDD
                .mapToPair(new PairFunction<String, String, TipoAcquistiConteggio>() {
            @Override
            public Tuple2<String, TipoAcquistiConteggio> call(String arg0) throws Exception {
                String[] split = arg0.split(",");
                try {
                    String etaPlayerPerRigaAcquisto = split[4];
                    Double value = Double.parseDouble(etaPlayerPerRigaAcquisto);
                    TipoAcquistiConteggio tipoAcquistiConteggio;
                    if (value <= etaThr) {
                        tipoAcquistiConteggio = new TipoAcquistiConteggio(1, 1);
                    } else {
                        tipoAcquistiConteggio = new TipoAcquistiConteggio(1, 0);
                    }
                    String squadraPerRigaAcquisto = split[1];
                    return new Tuple2<String, TipoAcquistiConteggio>(squadraPerRigaAcquisto, tipoAcquistiConteggio);
                } catch (NumberFormatException e) {
                    log.warn("Titolo elaborato."); // dovrebbe essere gia stato filtrato nel precedente step
                }
                return null;
            }
        });

        log.info("###### FASE - 02.001 ###### Verifica risultato RDD elaborato ...");
        for(Tuple2<String, TipoAcquistiConteggio> line : mapConteggioAcquistiGiovani.collect()){
            log.info("###### FASE - 02.001 ###### Read RDD tupla. ID: "+line._1+" value: "+line._2.acquistiGiovani);
        }

        // Somma del totale e del secondo indicatore per ogni tupla (ha come result un TipoAcquistiConteggio con le somme per ogni tupla)
        JavaPairRDD<String, TipoAcquistiConteggio> reducedSommeIndicatori = mapConteggioAcquistiGiovani
            .reduceByKey(new Function2<TipoAcquistiConteggio, TipoAcquistiConteggio, TipoAcquistiConteggio>() {
                @Override
                public TipoAcquistiConteggio call(TipoAcquistiConteggio vIterate1, TipoAcquistiConteggio vIterate2) throws Exception {
                    return new TipoAcquistiConteggio(vIterate1.acquistiTotali + vIterate2.acquistiTotali,
                            vIterate1.acquistiGiovani + vIterate2.acquistiGiovani);
                }
            });

        // Per ogni tupla considero per l'output: l'id della tupla e l'indicatore percentuale/frazione
        // (valore secondo indicatore calcolato su valore indicatore totale)
        JavaPairRDD<String, Double> tupleConIndicatoreFrazione = reducedSommeIndicatori
                .mapToPair(new PairFunction<Tuple2<String, TipoAcquistiConteggio>, String, Double>() {
            @Override
            public Tuple2<String, Double> call(Tuple2<String, TipoAcquistiConteggio> tupla) throws Exception {
                Double frazioneGiovaniSuTotale = new Double(tupla._2.acquistiGiovani) / new Double(tupla._2.acquistiTotali);
                return new Tuple2<String, Double>(tupla._1, frazioneGiovaniSuTotale);
            }
        });
        log.info("###### FASE - 02.003 ###### Verifica risultato RDD elaborato ...");
        for(Tuple2<String, Double> line : tupleConIndicatoreFrazione.collect()){
            log.info("###### FASE - 02.003 ###### Read RDD tupla. ID: "+line._1+" value: "+line._2);
        }

        /*
        .filter(new Function<Tuple2<String, Double>, Boolean>() {
            @Override
            public Boolean call(Tuple2<String, Double> v) throws Exception {
                return v._2 > threshold ? true : false;
            }
        }).sortByKey().map(new Function<Tuple2<String, Double>, String> () {
            @Override
            public String call(Tuple2<String, Double> arg0) throws Exception {
                return arg0._1;
            }
        });

        mapConteggioAcquistiGiovani.saveAsTextFile(outputPath2);
        */

        sparkContext.close();
    }

}

package it.imperato.test.spark.dev.join;

import it.imperato.test.hadoop.hdfs.HdfsReaderMain;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;

public class AnalysisWithDataJoin {

    private static final Logger log = Logger.getLogger(AnalysisWithDataJoin.class);

    public static void main(String [] args)
    {
        SparkSession session = SparkSession.builder()
                .appName("test_join_data").master("local[*]").getOrCreate();

        Dataset<Row> squadre = session.read().option("header",true).csv("data/spark/testdata/team.csv");
        Dataset<Row> acquisti = session.read().option("header",true).csv("data/spark/testdata/acquisti.csv");

        ArrayList<String> listaJoinColumn = new ArrayList<String>();
        listaJoinColumn.add("SquadraId");

        // Left outer join tra i due set di dati:
        Dataset<Row> acquistiConSquadre = squadre.join(acquisti,
                scala.collection.JavaConversions.asScalaBuffer(listaJoinColumn),"leftouter");

        // Verifica data join:
        acquistiConSquadre.show();

        /* Result:
        +---------+-----------+-------------------+----------+--------------+------+
        |SquadraId|       Nome|         Campionato|AcquistoId|        Player|Prezzo|
        +---------+-----------+-------------------+----------+--------------+------+
        |        1|Real Madrid|      Liga Espanola|      null|          null|  null|
        |        2|    Chelsea|     Premier League|         3|Olivier Giroud|    17|
        |        2|    Chelsea|     Premier League|         2| Alvaro Morata|    66|
        |        3|   Juventus|Campionato Italiano|         4| Douglas Costa|    40|
        |        4|      Inter|Campionato Italiano|         1|Stefan de Vrij|    35|
        +---------+-----------+-------------------+----------+--------------+------+
         */
    }

}

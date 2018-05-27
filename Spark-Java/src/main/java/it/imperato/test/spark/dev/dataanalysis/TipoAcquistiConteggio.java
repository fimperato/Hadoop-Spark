package it.imperato.test.spark.dev.dataanalysis;

import java.io.Serializable;

public class TipoAcquistiConteggio implements Serializable {

    public Integer acquistiTotali = 0;
    public Integer acquistiGiovani = 0;
    public Integer acquistiNazioneSquadra = 0;

    public TipoAcquistiConteggio(Integer acquistiTotali, Integer acquistiGiovani) {
        this.acquistiTotali = acquistiTotali;
        this.acquistiGiovani = acquistiGiovani;
    }

    public TipoAcquistiConteggio(Integer acquistiTotali, Integer acquistiGiovani, Integer acquistiNazioneSquadra) {
        this.acquistiTotali = acquistiTotali;
        this.acquistiGiovani = acquistiGiovani;
        this.acquistiNazioneSquadra = acquistiNazioneSquadra;
    }

}
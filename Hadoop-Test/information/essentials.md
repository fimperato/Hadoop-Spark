## comandi linux 

### Esplorazione file system:

    ls -l
-l     use a long listing format

    ls -lh
-h, --human-readable with -l and/or -s, print human readable sizes (e.g., 1K 234M 2G)

Sort output and print sizes in human readable format (e.g., 1K 234M 2G): 
    ls -lSh
-S     sort by file size

    ls -s
-s, --size print the allocated size of each file, in blocks

    ls Links
list directory 'Links'
    
    pwd
print whole path name current directory

    du
stima l'uso dello spazio occupato da ogni elemento specificato (intero filesystem, cartella, file specificato)
Le opzioni più utilizzate per df sono: 
* -a , che dice al comando du di restituire la dimensione dei singoli file e directory 
* -s mostra solo il totale per ogni argomento
* -h che rende l’ouput del comando du (15M, invece che in byte senza arrotondamenti)
* -c che restituisce la dimensione totale di tutti i file analizzati



    df
riporta lo spazio disco usato dal file system (intero filesystem, cartella, file specificato)
Le opzioni più utilizzate per df sono: 
* -k che mostra i valori di uscita in kilobyte 
(questo di solito è l’impostazione predefinita)
* -h che stampa le dimensioni in formato leggibile (per esempio, 1K 234M 2G) 
* -i che da informazioni sugli inode.


    man {nome-comando}
    
apre l'interface per il refernce manual sul comando da verificare; q per uscire

### Gestione file system:

    mkdir
crea directory 

    cp 
    cp file_sorgente destinazione1
copia 1 file nella directory 'destinazione1'

    cp file1 file2 ... fileN destinazione1
copia più file-i nella dir finale destinazione1 (ad es. '/tempdir'):
Se nella dir destinazione esiste già un file chiamato file1 o file2, sarà sovrascritto.

    mv 
    mv sorgente destinazione1 (rinomina il file da 'sorgente' a 'destinazione1')
    mv sorgente destinazione1  (move il file 'sorgente' nella dir 'destinazione1')
    mv sorgente1 .. sorgenteN destinazione1 (move i file 'sorgente-i' nella dir 'destinazione1')
Sposta o rinomina il/i file

    rm file-1 (elimino il file-1)
    rm file-1 file-2 (elimino il file-1 e il file-2)
    rm -rf directory (elimina una directory non vuota)
    rmdir directory (elimino una directory vuota)
rm, rmdir, per eliminare file e directory

    rm -rf ./*.txt
    (del *.txt, in windows)
elimina tutti i file di tipo txt nella directory corrente

    find . -name '*.txt' -delete
    (del /s *.txt, in windows)
elimina tutti i file di tipo txt nella directory corrente ricorsivamente
    
    touch file-1
crea il file vuoto 'file-1'
modifica la data di ultima update nel caso di file-1 già esistente


### Esplorazione contenuto file e directory

    cat file-1
Mostra il contenuto del file-1
    
    cat file-1 file-2
Concantena i file-1 e file-2

    cat file-1 file-2 > file-3
Concatena e copia di file-1 e -2 nel file-3 risultante

    head file-1
Printa in console la prima parte del file-1

    head -5 file-1
Printa in console le prime 5 righe del file-1

    tail file-1
Printa in console l'ultima parte del file-1

    tail -5 file-1
Printa in console le ultime 5 righe del file-1

    more file-1
more riporta in uscita il file-1, una schermata alla volta. 
La pressione del tasto ENTER permette di avanzare una riga alla volta, 
la pressione della barra spaziatrice invece fa avanzare di una schermata alla volta. 
Per uscire prima della fine deggli ingressi è sufficiente la pressione del tasto Q o di CTRL-C.

    less file-1
less mostra in console il file-1, una schermata alla volta. Operazioni dopo aver attivato less:
                                                            
    ENTER -> avanza di una riga 
    BARRA SPAZIATRICE -> avanza di una schermata 
    B -> torna indietro di una schermata 
    Q -> esci 
    / -> chiede una parola o una espressione regolare su cui fare una ricerca
E' simile al comando more, ma ha più possibilità: oltre a poter avanzare, si può anche tornare indietro. 
Viene generalmente utilizzato quando si vuole velocemente prendere visione di un lungo file di testo 
oppure in abbinamento con altri comandi, quando essi forniscono in uscita informazioni 
che non hanno spazio su di una singola schermata. 

    wc file-1
Mostra in console il count delle linee, parole e byte per il file-1. 
Opzioni disponibili per il singolo conteggio su linee, caratteri, byte:
    
    -c, --bytes            print the byte counts
    -m, --chars            print the character counts
    -l, --lines            print the newline counts
    -w, --words            print the word counts


    grep str-1 file-1
Permette la ricerca della string str-1 nel file-1

    grep -i str-1 file-1
Permette la ricerca della string str-1 nel file-1, indipendentemente da upper/lower case

    ls -la | grep -i str-1 
Permette la ricerca della string str-1 nell'output del primo comando: il grep è in pipe al primo comando


### Editing e tool per file

    vim file-1 (apre l'editor di vim per la modifica del file-1)
    :i (edit del file-1)
    :wq (save e quit)
    :!q :q! (quit ma senza save)
Uso di vim per la modifica file

    cut -d ':' -f3 vimtest.txt
Permette di prelevare i byte della terza posizione dell'array ottenuto dalla riga dopo lo split per il carattere ':'

    cat vimtest.txt | tr ':' '#'
Permette di modificare o cancellare caratteri dallo stream di input di vimtest.txt. 
E' possibile ad esempio sostituire i caratteri ":" con il carattere "#"

    cat vimtest.txt | tr -d ':'
Con l'opzione "-d" elimino e non sostituisco le occorrenze trovate

    sort vimtest.txt
Ordina le linee del vimtest.txt

    sort -k2 -r - u vimtest.txt 
Ordina le linee del vimtest.txt in base al campo N. N è la posizione della colonna del campo nel record.
-r. Visualizza i record in ordine inverso. 
-u. Rimuove i record duplicati


Operatori bash per scrivere i risultati delle operazioni, e non solo visualizzarli su console:
    
    > prompt operator, sovrascrive il file se esiste altrimenti lo crea e scrive l'output della operazione del comando
    >> double prompt operator, concatena alla fine del file se esiste altrimenti lo crea e scrive l'output della operazione del comando
    | pipe operator, invia al secondo comando in pipe, l'output del primo comando
    
    & esegue il comando in background
    || esegue il secondo comando se il primo comando fallisce
    && esegue il secondo comando solo se il primo viene eseguito con successo

### Operatori di processo

    free -m
Mostra il totale della memoria, libera e usata (-m per la leggibilità)

    top
Vista real time dei processi in running 

    ps a
Vista snapshot dei processi in running 

    kill {PID}
Per arrestare un processo in esecuzione (il cui pid è dato dal comando ps o top). Di tipo SIGTERM.
SIGTERM: si tratta di una richiesta piuttosto soft di terminazione: il processo avvia alcune procedure prima di terminare

    kill -9 {PID}
Per arrestare un processo in esecuzione (il cui pid è dato dal comando ps o top). Di tipo SIGKILL.
SIGKILL: è usato per uccidere un processo: è un comando kill di tipo brutale

    nice
Per modificare la priorità


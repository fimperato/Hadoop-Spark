## Permessi file - folder linux

### - chmod:

Per assegnare i permessi di esecuzione all'owner di file-1:

    chmod u+x nomefile
    
Per assegnare i permessi di lettura, scrittura ed esecuzione all'owner di file-1:

    chmod u+rwx nomefile
    
| Simbolo | Permesso | Azione per file | Azione per folder |
| :---:  | :---: | --- | --- |
| `r` | read | lettura: consente di aprire un file per visualizzarne il contenuto | consente la visualizzazione del contenuto della directory, con il comando ls |
| `w` | write | scrittura: consente di sovrascrivere o aggiungere dati a un file | consente la creazione o l'eliminazione di file all'interno della directory | 
| `x` | execute | esecuzione: consente di eseguire un file (nel caso si tratti di un file eseguibile) | consente di accedere alla directory (per esempio con il comando cd) anche nel caso non se ne possa visualizzarne il contenuto | 
| `-` |  | assenza del permesso | assenza del permesso |

Assegnazione con sistema ottale: composto da tre cifre, definisce i permessi per owner, gruppo e utenti.
Questi tre numeri vengono calcolati sommando i seguenti valori ottali:

| Valore ottale | Permesso aggregato | 
| :---:  | :--- |
| 0 | Nessun permesso |
| 1 | Esecuzione |
| 2 | Scrittura |
| 4 | Lettura |

Ad esempio:
    
    > chmod 777 file-1 (permessi completti per owner, group e users)
    > chmod 644 nomefile (permessi di lettura e scrittura per owner, solo lettura per altri)

Con opzione -R, il comando agisce ricorsivamente all'interno della directory/sottodirectory

### - chown:

    chown ownerName:groupName file-1

Ad esempio:

    chown Francesco:mygr dataset.csv (Francesco proprietario e mygr gruppo per il file 'dataset.csv')

Con opzione -R, il comando agisce ricorsivamente all'interno della directory/sottodirectory

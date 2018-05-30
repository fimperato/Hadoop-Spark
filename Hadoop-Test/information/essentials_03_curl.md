## curl

download sull'output di una pagina:

    curl https://www.wikipedia.org
    
#### -o 

download di una pagina e salvataggio in page.html

    curl https://www.wikipedia.org -o page.html
    
download di più pagine e salvataggio in page0{n}.html

    curl https://www.wikipedia.org -o page01.html https://www.google.com/ -o page02.html
    
#### -i

download sull'output di una pagina con incluso l'http response header (server name, cookie, data del documento, ecc.)

    curl -i http://francesco-imperato-ng.appspot.com
   
#### -L  

esegue il comando curl sulla pagina con nuova posizione http se il server verifica che quella chiamata è moved: 
se il server verifica che la pagina è stata spostata la server response include nell'header la Location. Ad es.
    
    HTTP/1.1 301 Moved Permanently
    Date: Tue, 27 May 2018 13:14:52 GMT
    Connection: close
    Content-Type: text/html
    Location: http://sitenuovaposizione... 

    curl -L -i http://francesco-imperato-ng.appspot.com



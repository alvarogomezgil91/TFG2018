#!/bin/bash
## Script to download Yahoo historical stocks using the script get-yahoo-quotes.
##
##
## Usage: insertar_datos.sh
##
##
## Author: Alvaro Gabriel Gomez Gil algomez@correo.ugr.es
##
## Copyright (c) 2018 Alvaro Gabriel Gomez Gil - All Rights Reserved
##
##
## History
##
## 02-03-2018 : Created script
##
## ----------------------------------------------------------------------------------------------------
## Comprobamos si viene se pasa el parámetro
## ----------------------------------------------------------------------------------------------------


if [ $# -eq 0 ] ; then
    echo "No se han introducido parámetros"
    exit 1
fi

fichero_query="query_$1.sql"
fichero_error="error_query_$1.sql"
fichero_tecnico_query="tecnico_query_$1.sql"
fichero_tecnico_error="error_tecnico_query_$1.sql"
fichero_tecnico="tecnico.csv"

k1=$(awk 'BEGIN {print ("'2'" / "'27'")}')
k2=$(awk 'BEGIN {print ("'2'" / "'13'")}')
k3=$(awk 'BEGIN {print ("'2'" / "'10'")}')

precio26="0.00"
EMAayer26="0.00"
EMA26="0.00"

precio12="0.00"
EMAayer12="0.00"
EMA12="0.00"

MACD="0.00"
MACDK="0.00"
SENAL="0.00"
SENALayer="0.00"

path_csv="csv_stocks/"
path="queries/"
path_tecnico="csv_tecnico/"

rm -f ${path_csv}${fichero_query}
rm -f ${path_csv}${fichero_error}


path_tecnico="csv_tecnico/"

rm -f ${path_tecnico}${fichero_tecnico_query}
rm -f ${path_tecnico}${fichero_tecnico_error}

nombre_fichero=$1.csv



if [ ! -f ${path_csv}${nombre_fichero} ] ; then
	echo "No existe el fichero para ese símbolo"
	exit 1
fi



echo "DELETE FROM stocks WHERE simbolo='$1'; " > ${path}${fichero_query}
echo "DELETE FROM tecnicos_stocks WHERE simbolo='$1'; " > ${path_tecnico}${fichero_tecnico_query}

while read line
do
	
	#Date,Open,High,Low,Close,Adj Close,Volume
    fecha=`echo $line | cut -d "," -f 1`
	apertura=`echo $line | cut -d "," -f 2`
	maximo=`echo $line | cut -d "," -f 3`
	minimo=`echo $line | cut -d "," -f 4`
	cierre=`echo $line | cut -d "," -f 5`
	adj_cierre=`echo $line | cut -d "," -f 6`
	volume=`echo $line | cut -d "," -f 7`
	
	if (( $(awk 'BEGIN {print ("'$apertura'" > "'$cierre'")}') )); then
		tendencia=1001
	elif (( $(awk 'BEGIN {print ("'$apertura'" < "'$cierre'")}') )); then
		tendencia=1002
	else
		tendencia=1003
	fi
	
	statement="INSERT INTO stocks (simbolo, fecha, apertura, maximo, minimo, cierre, adj_cierre, volume, tendencia) VALUES ('$1', '$fecha', $apertura, $maximo, $minimo, $cierre, $adj_cierre, $volume, $tendencia);"
	
	if [[ ! $statement == *"null"* ]] ; then
		echo $statement >> ${path}/${fichero_query}
	else
		echo $statement >> ${path}/${fichero_error}
	fi
	
	cierre1=`echo $cierre | cut -d "." -f 1`
	cierre1=`echo $cierre | cut -d "." -f 2`
	#cierre="${cierre1},${cierre2}"
	
	precio26=$(awk 'BEGIN {print ("'$cierre'" * "'$k1'")}')
	EMA26aux=$(awk 'BEGIN {print ("'1'" - "'$k1'")}')
	EMAayer26=$(awk 'BEGIN {print ("'$EMA26'" * "'$EMA26aux'")}')
	EMA26=$(awk 'BEGIN {print ("'$precio26'" + "'$EMAayer26'")}')
	
	precio12=$(awk 'BEGIN {print ("'$cierre'" * "'$k2'")}')
	EMA12aux=$(awk 'BEGIN {print ("'1'" - "'$k2'")}')
	EMAayer12=$(awk 'BEGIN {print ("'$EMA12'" * "'$EMA12aux'")}')
	EMA12=$(awk 'BEGIN {print ("'$precio12'" + "'$EMAayer12'")}')
	
	MACD=$(awk 'BEGIN {print ("'$EMA12'" - "'$EMA26'")}')
	MACDK=$(awk 'BEGIN {print ("'$MACD'" * "'$k3'")}')
	
	SENALaux=$(awk 'BEGIN {print ("'1'" - "'$k3'")}')
	SENALayer=$(awk 'BEGIN {print ("'$SENAL'" * "'$SENALaux'")}')
	SENAL=$(awk 'BEGIN {print ("'$MACDK'" + "'$SENALayer'")}')
	HISTOGRAMA=$(awk 'BEGIN {print ("'$MACD'" - "'$SENAL'")}')
	
	tecnico="${1};${fecha};${cierre};${EMA26};${EMA12};${MACD};${SENAL}"
	
	statement_tecnico="INSERT INTO tecnicos_stocks (simbolo, fecha, EMA26, EMA12, MACD, SENAL, HISTOGRAMA)"
	statement_tecnico=${statement_tecnico}" VALUES ('$1', '${fecha}', ${EMA26}, ${EMA12}, ${MACD}, ${SENAL}, ${HISTOGRAMA});"
	
	echo ${tecnico} >> ${path_tecnico}${fichero_tecnico}
	echo ${statement_tecnico} >> ${path_tecnico}${fichero_tecnico_query}
		
done < ${path_csv}${nombre_fichero}

exit 0

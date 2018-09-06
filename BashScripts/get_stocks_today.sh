#!/bin/bash
##
## Script to download Yahoo historical stocks using the script get-yahoo-quotes.
##
## Usage: get-stocks
##
##
## Author: Alvaro Gabriel Gomez Gil algomez@correo.ugr.es
##
## Copyright (c) 2018 Alvaro Gabriel Gomez Gil - All Rights Reserved
##
##
## History
##
## 16-02-2018 : Created script
##
## ----------------------------------------------------------------------------------------------------
## Variables
## ----------------------------------------------------------------------------------------------------
file="stocks_file.txt"

path_csv_today="csv_today/"
path_queries_today="queries_today/"

today=`date +%Y%m%d`
startTime=`date +%H:%M:%S`
errorFile="err_stocks_ibex_$today.txt"
numlines=`wc -l ${file}`
cont=0
salida=0
msg="PROCESO TERMINADO CORRECTAMENTE"
rm -f errorFile

clear

echo ""
echo ""
echo ""
echo "Empezamos la ejecucion a las $startTime"

if [ ! -e ${path_csv_today}${today} ] ; then
	mkdir ${path_csv_today}${today}
	chmod 777 ${path_csv_today}${today}
fi

if [ ! -e ${path_queries_today}${today} ] ; then
	mkdir ${path_queries_today}${today}
	chmod 777 ${path_queries_today}${today}
fi

rm -f "${path_csv_today}${today}/error_${today}.csv"
rm -f "${path_csv_today}${today}/error_aux_${today}.csv"
rm -f "${path_queries_today}${today}/query_${today}.txt"
rm -f "${path_queries_today}${today}/query_tecnico_${today}.txt"

while IFS='' read -r line || [[ -n "$line" ]]; do

	echo "./get-yahoo-quotes-today.sh $line"
    ./get-yahoo-quotes-today.sh $line > /dev/null
	if [ $? ]; then
		echo $line >> $errorFile
	fi

	
	echo "./genera_query_today.sh $line"
	./genera_query_today.sh $line
	
	cont=$[${cont}+1]
	
	echo "Vamos por la línea ${cont} de ${numlines}"
	
	
done < "$file"



cat ${path_queries_today}${today}/query_* >> ${path_queries_today}${today}/query_${today}.txt
cat ${path_queries_today}${today}/error_query_* >> ${path_queries_today}${today}/error_query_${today}.txt

cat ${path_queries_today}${today}/tecnico_query_* >> ${path_queries_today}${today}/query_tecnico_${today}.txt
cat ${path_queries_today}${today}/error_tecnico_query_* >> ${path_queries_today}${today}/error_query_tecnico_${today}.txt

endTime=`date +%H:%M:%S`

if [ ! -s "${path_queries_today}${today}/query_${today}.txt" ] ; then
	echo ""
	echo ""
	echo "No se encuentra el fichero ${path_queries_today}${today}/query_${today}.txt"
	msg="PROCESO TERMINADO CON ERROR!!"
	salida=1
fi

if [ ! -s "${path_queries_today}${today}/query_tecnico_${today}.txt" ] ; then
	echo ""
	echo ""
	echo "No se encuentra el fichero ${path_queries_today}${today}/query_tecnico_${today}.txt"
	msg="PROCESO TERMINADO CON ERROR!!"
	salida=1
fi


echo ""
echo ""
echo $msg
echo "Hemos empezado a las  --> ${startTime}"
echo "Hemos terminado a las --> ${endTime}"

exit $salida




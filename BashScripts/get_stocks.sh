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
today=`date +%Y%m%d`
startTime=`date +%H:%M:%S`
errorFile="err_stocks_$today.txt"
numlines=`wc -l ${file}`
cont=0
rm -f errorFile

clear

echo ""
echo ""
echo ""
echo "Empezamos la ejecucion a las $startTime"

while IFS='' read -r line || [[ -n "$line" ]]; do

	echo "./get-yahoo-quotes.sh $line"
    ./get-yahoo-quotes.sh $line > /dev/null
	if [ $? ]; then
		echo $line >> $errorFile
	fi

	
	#echo "./genera_query.sh $line"
	#./genera_query.sh $line
	
	## Aquí vamos a poner nuestro pedazo de Python
	echo "python /C/workspaceGIT/TFG2018/BashScripts/PythonScripts/genera_query.py -f=$line"
	python /C/workspaceGIT/TFG2018/BashScripts/PythonScripts/genera_query.py -f=$line
	
	cont=$[${cont}+1]
	
	echo "Vamos por la línea ${cont} de ${numlines}"
	
	
done < "$file"

cat queries/query_* >> queries/query.txt
cat queries/error_query_* >> queries/query.txt

cat csv_tecnico/tecnico_query_* >> csv_tecnico/query_tecnico.txt
cat csv_tecnico/error_tecnico_query_* >> csv_tecnico/error_query_tecnico.txt

endTime=`date +%H:%M:%S`

echo ""
echo ""
echo ""
echo "Hemos empezado a las  --> ${startTime}"
echo "Hemos terminado a las --> ${endTime}"

exit 0




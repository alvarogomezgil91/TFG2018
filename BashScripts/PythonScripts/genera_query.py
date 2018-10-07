import argparse
import datetime

def main():

    if __name__ == '__main__':
        # Initialize the parser
        parser = argparse.ArgumentParser(
            description="Genera_query"
        )

        # Add the parameters positional/optional

        parser.add_argument('-f', '--fichero', help="Nombre de fichero", type=str)

        # Parse the arguments
        args = parser.parse_args()

        param1 = args.fichero

        if param1 == "":
            print("No se ha introducido ningún parámetro correcto")
        else:
            path = "C:\\workspaceGIT\\BashScripts\\"
            path_csv = path + "csv_stocks\\"
            path_queries = path + "queries\\"
            path_tecnico = path + "csv_tecnico\\"

            nombre_fichero = path_csv + param1 + ".csv"
            fichero_query = path_queries + "query_" + param1 + ".sql"
            fichero_error = path_queries + "error_query_" + param1 + ".sql"
            fichero_tecnico_query = path_tecnico + "tecnico_query_" + param1 + ".sql"
            fichero_tecnico_error = path_tecnico + "error_tecnico_" + param1 + ".sql"
            fichero_tecnico = path_tecnico + "tecnico.csv"

            k1 = 2 / 27
            k2 = 2 /13
            k3 = 2 /10

            precio26 = float(0)
            EMAayer26 = float(0)
            EMA26 = float(0)

            precio12 = float(0)
            EMAayer12 = float(0)
            EMA12 = float(0)

            MACD = float(0)
            MACDK = float(0)
            SENAL = float(0)
            SENALayer = float(0)

            with open(nombre_fichero, 'r') as readh1, \
                    open(fichero_query, 'w') as writeh1, \
                    open(fichero_tecnico_query, 'w') as writeh2, \
                    open(fichero_tecnico, 'w') as writeh3, \
                    open(fichero_error, 'w') as writeh4:

                stock = param1

                writeh1.write("DELETE FROM stocks WHERE simbolo='" + stock + "'; ")
                writeh2.write("DELETE FROM tecnicos_stocks WHERE simbolo='" + stock + "'; ")

                for line in readh1:

                    if line.count('null')>1:
                        continue

                    fecha = line.split(',')[0]
                    apertura = float(line.split(',')[1])
                    maximo = float(line.split(',')[2])
                    minimo = float(line.split(',')[3])
                    cierre = float(line.split(',')[4])
                    adj_cierre = float(line.split(',')[5])
                    volume = float(line.split(',')[6])

                    if (apertura > cierre):
                        tendencia = 1001
                    elif (apertura < cierre):
                        tendencia = 1002
                    else:
                        tendencia = 1003

                    query_stock = "INSERT INTO stocks (simbolo, fecha, apertura, maximo, minimo, cierre, adj_cierre, volume, tendencia) VALUES"
                    query_stock = query_stock + "('" + str(stock) + "', '" + str(fecha) + "', " + str(apertura) + ", " + str(maximo) \
                                  + ", " + str(minimo) + ", " + str(cierre) + ", " + str(adj_cierre) + ", " + str(volume) + ", " + str(tendencia) + ");\n"

                    if query_stock.count("null") > 0:
                        writeh4.write(query_stock)
                    else:
                        writeh1.write(query_stock)

                    precio26 = cierre * k1
                    EMA26aux = 1 - k1
                    EMAayer26 = EMA26 * EMA26aux
                    EMA26 = precio26 + EMAayer26

                    precio12 = cierre * k2
                    EMA12aux = 1 - k2
                    EMAayer12 = EMA12 * EMA12aux
                    EMA12 = precio12 + EMAayer12

                    MACD = EMA12 - EMA26
                    MACDK = MACD * k3

                    SENALaux = 1 - k3
                    SENALayer = SENAL * SENALaux
                    SENAL = MACDK + SENALayer
                    HISTOGRAMA = MACD - SENAL

                    tecnico = stock + ";" + fecha + ";" + str(cierre) + ";" + str(EMA26) + ";" + str(EMA12) + ";" + str(MACD) + ";" + str(SENAL) + "\n"

                    writeh3.write(tecnico)

                    query_tecnico = "INSERT INTO tecnicos_stocks (simbolo, fecha, EMA26, EMA12, MACD, SENAL, HISTOGRAMA)"
                    query_tecnico = query_tecnico + " VALUES ('" + stock + "', '" + fecha + "', " + str(EMA26) + ", " + str(EMA12) + ", " + str(MACD) + ", " + str(SENAL) + ", " + str(HISTOGRAMA) + ");\n"

                    writeh2.write(query_tecnico)

    return

print("Iniciamos el proceso: " + str(datetime.datetime.now()))

main()

print("Finalizamos el proceso: " + str(datetime.datetime.now()))

<?php
/**
 * Obtiene el detalle de un alumno especificado por
 * su identificador "user_name"
 */

require 'Users.php';

if ($_SERVER['REQUEST_METHOD'] == 'GET') {

    if (isset($_GET['user_name'])) {

        // Obtener parámetro user_name
        $parametro = $_GET['user_name'];

        // Tratar retorno
        $retorno = Users::getByUserName($parametro);


        if ($retorno) {

            $user["estado"] = 1;		// cambio "1" a 1 porque no coge bien la cadena.
            $user["user"] = $retorno;
            // Enviar objeto json del alumno
            print json_encode($user);
        } else {
            // Enviar respuesta de error general
            print json_encode(
                array(
                    'estado' => '2',
                    'mensaje' => 'No se obtuvo el registro'
                )
            );
        }

    } else {
        // Enviar respuesta de error
        print json_encode(
            array(
                'estado' => '3',
                'mensaje' => 'Se necesita un identificador'
            )
        );
    }
}


<?php
/**
 * Obtiene todas las alumnos de la base de datos
 */

require 'Users.php';

if ($_SERVER['REQUEST_METHOD'] == 'GET') {

    // Manejar petici�n GET
    $users = Users::getAll();

    if ($users) {

        $datos["estado"] = 1;
        $datos["users"] = $users;

        print json_encode($datos);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error"
        ));
    }
}


<?php
/**
 * Select from remote_user_credentials by user_name
 */

require 'Users.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    // Decodificando formato Json
	$body = json_decode(file_get_contents("php://input"), true);

    // Insertar Alumno
    $retorno = Users::getRemoteCredentials(
        $body['user_name'],
		$body['password']
		);

    
	if ($retorno) {
		header('Content-Type: application/json');

        $datos["estado"] = 1;
        $datos["mensaje"] = "Bieeeeen";

        print json_encode($datos);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error"
        ));
    }
}

?>
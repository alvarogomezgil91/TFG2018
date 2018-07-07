<?php
/**
 * Select from remote_user_credentials by user_name
 */

require 'GraphicData.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    // Decodificando formato Json
	$body = json_decode(file_get_contents("php://input"), true);

    // Insertar Alumno
	$retorno1 = GraphicData::getSizeData(
		$body['simbolo']
		);
    $retorno2 = GraphicData::getGeneralData(
		$body['simbolo']
		);
		


    
	if ($retorno1) {
		header('Content-Type: application/json');

        $datos["estado"] = 1;
		$datos["tamano"] = $retorno1;
        $datos["mensaje"] = $retorno2;

        print json_encode($datos);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error"
        ));
    }
}

?>
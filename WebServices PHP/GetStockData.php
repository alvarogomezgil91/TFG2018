<?php
/**
 * Select from remote_user_credentials by user_name
 */

require 'GraphicData.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    // Decodificando formato Json
	$body = json_decode(file_get_contents("php://input"), true);

    $retorno = GraphicData::getStocksData(
		$body['simbolo']
		);
		


    
	if ($retorno) {
		header('Content-Type: application/json');

        $datos["estado"] = 1;
		$datos["mensaje"] = $retorno;

        print json_encode($datos);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error"
        ));
    }
}

?>
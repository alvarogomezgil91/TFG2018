<?php
/**
 * Select from remote_user_credentials by user_name
 */

require 'StocksData.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    // Decodificando formato Json
	$body = json_decode(file_get_contents("php://input"), true);

    $retorno1 = StocksData::getPreviousStocksData(
		$body['simbolo'],
		$body['fecha']
		);
	
	$retorno2 = StocksData::getPredictionStocksData(
		$body['simbolo'],
		$body['fecha']
		);	
    
	if ($retorno1) {
		header('Content-Type: application/json');

        $datos["estado"] = 1;
		$datos["previos"] = $retorno1;
		$datos["prediccion"] = $retorno2;

        print json_encode($datos);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error"
        ));
    }
}

?>
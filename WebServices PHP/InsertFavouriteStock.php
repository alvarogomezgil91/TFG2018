<?php
/**
 * Select from remote_user_credentials by user_name
 */

require 'StocksData.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    // Decodificando formato Json
	$body = json_decode(file_get_contents("php://input"), true);

    $retorno = StocksData::insertFavouriteStock(
		$body['user_name'],
		$body['stock']
		);
		
	if ($retorno) {
		header('Content-Type: application/json');

        $datos["estado"] = 1;
		$datos["mensaje"] = "Insercion correcta";
		print json_encode($datos);
        
    } else {
        header('Content-Type: application/json');

        $datos["estado"] = 2;
		$datos["mensaje"] = $retorno;
		print json_encode($datos);
    }
}

?>
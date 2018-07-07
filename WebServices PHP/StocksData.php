<?php

/**
 * Representa el la estructura de las Alumnoss
 * almacenadas en la base de datos
 */
require 'Database.php';

class StocksData
{
    function __construct()
    {
    }

    /**
     * Retorna en la fila especificada de la tabla 'Alumnos'
     *
     * @param $idAlumno Identificador del registro
     * @return array Datos del registro
     */
	
	public static function getIndexStocksData()
    {
        $consulta = "SELECT simbolo, cierre
							FROM stocks
							WHERE fecha in (select max(fecha) from stocks)
							ORDER BY cierre desc";
							
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(
				array(
					$simbolo
					)
				);

            return $comando->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            return false;
        }
		
    }
	
	public static function getPredictionStocksData()
    {
        $consulta = "SELECT simbolo, cierre
							FROM stocks
							WHERE fecha in (select max(fecha) from stocks)
							ORDER BY cierre desc";
							
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(
				array(
					$simbolo
					)
				);

            return $comando->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            return false;
        }
		
    }
	
	
	public static function getSizeData($simbolo)
    {
        $consulta = "SELECT count(*) as num_rows
							FROM stocks
							WHERE simbolo = ?";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(
				array(
					$simbolo
					)
				);
			
			$row = $comando->fetch(PDO::FETCH_ASSOC);

			$retorno = $row['num_rows'];


            return $retorno;

        } catch (PDOException $e) {
            return false;
        }
		
    }
	public static function getFavouriteStocksByUser(
	$user_name
	)
    {
        $consulta = "SELECT simbolo
							FROM favourites_stocks_by_user
							WHERE user_name = ?";
							
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(
				array(
					$user_name
					)
				);

            return $comando->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            return false;
        }
		
    }

	public static function getFavouriteStocksData(
	$simbolos
	)
    {
        $consulta = "SELECT simbolo, cierre
							FROM stocks
							WHERE fecha in (select max(fecha) from stocks)
								AND simbolo in ($simbolos)
							ORDER BY cierre desc";
							
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(
				array(
					$simbolos
					)
				);

            return $comando->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            return false;
        }
		
    }    
	
	
	
}

?>
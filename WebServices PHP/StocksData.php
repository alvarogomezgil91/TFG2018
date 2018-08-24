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
	 
	 public static function getMarketsData()
    {
        $consulta = "SELECT a.simbolo, c.nombre_stock, a.cierre, a.tendencia, (
    									select count(*)
    									from stocks_favoritos f
    									where f.user_name='Alvaro1' and f.stock=a.simbolo
										) as favorito, 
										c.es_mercado 
					FROM stocks a, cod_stocks c
					WHERE a.fecha in (select max(a.fecha) from stocks)
						AND c.es_mercado = 1001
						AND a.simbolo=c.simbolo
					GROUP BY a.simbolo
					ORDER BY a.cierre desc";
							
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute();

            return $comando->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            return false;
        }
		
    }
	
	
	public static function getMarketStocksData(
	$simbolo
	)
    {
        $consulta = "SELECT a.simbolo, c.nombre_stock, c.descripcion_stock, a.fecha, a.apertura, a.cierre, a.tendencia, (
    									select count(*)
    									from stocks_favoritos f
    									where f.user_name='Alvaro1' and f.stock=a.simbolo
										) as favorito, 
										c.es_mercado 
					FROM stocks a, cod_stocks c
					WHERE a.fecha in (select max(a.fecha) from stocks)
						AND a.simbolo=c.simbolo
                        AND a.simbolo in (select simbolo from relacion_stocks where simbolo_mercado = ? )
					GROUP BY a.simbolo
					ORDER BY a.cierre desc";
							
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
	
	 
	 
	
	public static function getIndexStocksData()
    {
        $consulta = "SELECT a.simbolo, c.nombre_stock, c.descripcion_stock,
							a.fecha, a.apertura, a.cierre, a.tendencia, c.es_mercado, (
    									select count(*)
    									from stocks_favoritos f
    									where f.user_name='Alvaro1' and f.stock=a.simbolo
										) as favorito
					FROM stocks a, cod_stocks c
					WHERE a.fecha in (select max(a.fecha) from stocks)
                    	AND a.simbolo=c.simbolo
					GROUP BY a.simbolo
					ORDER BY a.cierre desc";
							
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
	
	public static function getPredictionsData(
	$fecha
	)
    {
        $consulta = "SELECT a.simbolo, c.nombre_stock, c.descripcion_stock, a.fecha, a.apertura, a.cierre_predecido, c.es_mercado
					FROM stocks_prediccion a, cod_stocks c
					WHERE a.fecha in (select min(fecha) from stocks_prediccion where fecha > ? )
                    	AND a.simbolo=c.simbolo
					GROUP BY a.simbolo
					ORDER BY a.cierre_predecido desc";
							
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(
				array(
					$fecha
					)
				);

            return $comando->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            return false;
        }
		
    }
	
	public static function getPreviousStocksData(
	$simbolo,
	$fecha
	)
    {
        $consulta = "SELECT simbolo, fecha, apertura, cierre
							FROM stocks
							WHERE simbolo = ?
                            	AND fecha < ?
							ORDER BY fecha asc";
							
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(
				array(
					$simbolo,
					$fecha
					)
				);

            return $comando->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            return false;
        }
		
    }
	
	
	public static function getPredictionStocksData(
	$simbolo,
	$fecha
	)
    {
        $consulta = "SELECT simbolo, fecha, apertura, cierre_predecido
							FROM stocks_prediccion
							WHERE simbolo = ?
                            	AND NOT fecha < ?
							ORDER BY fecha asc";
							
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(
				array(
					$simbolo,
					$fecha
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
	public static function getFavouriteStocks(
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
	$user_name
	)
    {
        $consulta = "SELECT a.simbolo, c.nombre_stock, c.descripcion_stock, a.fecha, a.apertura, a.cierre, a.tendencia, c.es_mercado
					FROM stocks a, stocks_favoritos f, cod_stocks c
					WHERE a.fecha in (select max(a.fecha) from stocks)
						AND a.simbolo=f.stock
                        AND a.simbolo=c.simbolo
						AND f.user_name = 'Alvaro1'
                    GROUP BY a.simbolo
					ORDER BY a.cierre desc";
							
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

	public static function insertFavouriteStock(
	$user_name,
	$simbolo
	)
    {
        $insercion = "INSERT INTO `stocks_favoritos`(`user_name`, `stock`)
						VALUES ( ? , ? )";
		
		$consulta = "SELECT 1 FROM `stocks_favoritos`
					WHERE user_name = ? AND stock = ?";
					
							
        try {
            // Preparar sentencia
            $comando1 = Database::getInstance()->getDb()->prepare($insercion);
            // Ejecutar sentencia preparada
            $comando1->execute(
				array(
					$user_name,
					$simbolo
					)
				);
			$comando2 = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando2->execute(
				array(
					$user_name,
					$simbolo
					)
				);

            return $comando2->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            return false;
        }
		
    }
	
	
	public static function deleteFavouriteStock(
	$user_name,
	$simbolo
	)
    {
        $insercion = "DELETE FROM `stocks_favoritos` WHERE user_name = ? AND stock = ?";
		
		$consulta = "SELECT 1 FROM `stocks_favoritos`
					WHERE user_name = ? AND stock = ?";
					
							
        try {
            // Preparar sentencia
            $comando1 = Database::getInstance()->getDb()->prepare($insercion);
            // Ejecutar sentencia preparada
            $comando1->execute(
				array(
					$user_name,
					$simbolo
					)
				);
			$comando2 = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando2->execute(
				array(
					$user_name,
					$simbolo
					)
				);

            return $comando2->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            return false;
        }
		
    } 
	
	
	
}

?>
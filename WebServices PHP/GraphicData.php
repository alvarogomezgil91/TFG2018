<?php

/**
 * Representa el la estructura de las Alumnoss
 * almacenadas en la base de datos
 */
require 'Database.php';

class GraphicData
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
    public static function getGeneralData($simbolo)
    {
        $consulta = "SELECT *
							FROM stocks
							WHERE simbolo = ?
							ORDER BY fecha asc";
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

    
	
	
	
}

?>
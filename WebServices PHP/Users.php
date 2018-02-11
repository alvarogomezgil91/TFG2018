<?php

/**
 * Representa el la estructura de las Alumnoss
 * almacenadas en la base de datos
 */
require 'Database.php';

class Users
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
    public static function getAll()
    {
        $consulta = "SELECT * FROM remote_user_credentials";
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

    /**
     * Obtiene los campos de un Alumno con un identificador
     * determinado
     *
     * @param $idAlumno Identificador del alumno
     * @return mixed
     */
    public static function getByUserName($user_name)
    {
        // Consulta de la tabla Alumnos
        $consulta = "SELECT *
                             FROM remote_user_credentials
                             WHERE user_name = ?";

        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(array($user_name));
            // Capturar primera fila del resultado
            $row = $comando->fetch(PDO::FETCH_ASSOC);
            return $row;

        } catch (PDOException $e) {
            // Aquí puedes clasificar el error dependiendo de la excepción
            // para presentarlo en la respuesta Json
            return -1;
        }
    }

    /**
     * Actualiza un registro de la bases de datos basado
     * en los nuevos valores relacionados con un identificador
     *
     * @param $idAlumno      identificador
     * @param $nombre      nuevo nombre
     * @param $direccion nueva direccion
     
     */
    public static function update(
        $idAlumno,
        $nombre,
        $direccion
    )
    {
        // Creando consulta UPDATE
        $consulta = "UPDATE Alumnos" .
            " SET nombre=?, direccion=? " .
            "WHERE idAlumno=?";

        // Preparar la sentencia
        $cmd = Database::getInstance()->getDb()->prepare($consulta);

        // Relacionar y ejecutar la sentencia
        $cmd->execute(array($nombre, $direccion, $idAlumno));

        return $cmd;
    }

    /**
     * Insertar un nuevo Alumno
     *
     * @param $nombre      nombre del nuevo registro
     * @param $direccion dirección del nuevo registro
     * @return PDOStatement
     */
    public static function insert(
        $user_name,
		$password,
        $email
    )
    {
        // Sentencia INSERT
        $comando = "INSERT INTO remote_user_credentials ( " .
            "user_name," .
            " email," .
			" password)" .
            " VALUES( ?,?,?)";

        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        return $sentencia->execute(
            array(
                $user_name,
				$password,
                $email
            )
        );

    }

    /**
     * Eliminar el registro con el identificador especificado
     *
     * @param $idAlumno identificador de la tabla Alumnos
     * @return bool Respuesta de la eliminación
     */
    public static function delete($idAlumno)
    {
        // Sentencia DELETE
        $comando = "DELETE FROM Alumnos WHERE idAlumno=?";

        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        return $sentencia->execute(array($idAlumno));
    }
	
	/**
     * Comprueba las credenciales dadas
     *
     * @param $user_name Identificador del alumno
     * @param $password  Contraseña del usuario
	 * @return mixed
     */
    public static function getRemoteCredentials(
	$user_name,
	$password
	)
	
    {

		// Consulta de la tabla Alumnos
        $consulta = "SELECT *
                             FROM remote_user_credentials
                             WHERE user_name = ? AND password = ?";

        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(
				array(
					$user_name,
					$password
					)
				);
            // Capturar primera fila del resultado
            $row = $comando->fetch(PDO::FETCH_ASSOC);
			if ( $row ){
				return true;
			} else{
				return false;
			}
			
            //return $row;

        } catch (PDOException $e) {
            // Aquí puedes clasificar el error dependiendo de la excepción
            // para presentarlo en la respuesta Json
            return -1;
        }
    }
	
	
	
}

?>
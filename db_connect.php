<?php
header("Access-Control-Allow-Origin: *"); 
class DB_CONNECT {

    // constructor
    function __construct() 
    {
        // connecting to database
       $this->connect();  
    }

    // destructor
    function __destruct() {
        // closing db connection
        $this->close();
    }

     function connect()
    {
    // import database connection variables
    require_once __DIR__ . '/db_config.php';
    
    // Connecting to mysqli database
     $con = mysqli_connect('localhost', 'root','') or die(mysqli_error());

     $db = mysqli_select_db('countries', $con) or die(mysqli_error()) or die(mysqli_error());

        mysqli_query("SET NAMES 'UTF8'");
        mysqli_query("SET CHARACTER SET utf8_turkish_ci");  
       // mysqli_query("SET COLLATION_CONNECTION = 'utf8_turkish_ci'");
       mysqli_query("SET COLLATION_CONNECTION = 'utf8_general_ci'");
 
        return $con;
    }
    
    function close() 
    {
        mysqli_close();
    }
}

?>


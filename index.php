<?php
header("Access-Control-Allow-Origin: *"); 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();

// get all ulkeler from ulkeler table
$result = mysqli_query("SELECT * FROM products") or die(mysqli_error());
            

// check for empty result
if (mysqli_num_rows($result) > 0) {
    // looping through all results
    // ulkeler node
    $response["products"] = array();
 
    while ($row = mysqli_fetch_array($result)) {
        // temp user array
        $products = array();
        $products["_id"] = $row["id"];
        $products["_title"] = $row["title"];
        $products["_desc"] = $row["desc"];
        $products["_price"] = $row["price"];
        $products["_instock"] = $row["instock"];
 
        // push single musteri into final response array
        array_push($response["products"], $products);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
   //echo utf8_encode($response);
} else {
    // no musterilerim found
    $response["success"] = 0;
    $response["message"] = "No country found";
 
    // echo no users JSON
    echo json_encode($response);
    // echo utf8_encode($response);
}
?>

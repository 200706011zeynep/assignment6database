<?php
header("Access-Control-Allow-Origin: *"); 
require_once 'connection.php';  
  $response = array();  
  if(isTheseParametersAvailable(array('username', 'password'))){  
    $username = $_GET['username'];  
    $password = md5($_GET['password']);  
    $response['message'] = 'apicall'; 
    $stmt = $conn->prepare("SELECT id, username, email, gender FROM mobileprogramming WHERE username = ? AND password = ?");  
    $stmt->bind_param("ss",$username, $password);  
    $stmt->execute();  
    $stmt->store_result();  
    if($stmt->num_rows > 0){  
    $stmt->bind_result($id, $username, $email, $gender);  
    $stmt->fetch();  
    $user = array(  
    'id'=>$id,   
    'username'=>$username,   
    'email'=>$email,  
    'gender'=>$gender  
    );  
   
    $response['error'] = false;   
    $response['message'] = 'Login successfull';   
    $response['user'] = $user;   
 }  
 else{  
    $response['error'] = false;   
    $response['message'] = 'Invalid username or password';  
 }  
}  
echo json_encode($response); 

function isTheseParametersAvailable($params){  
foreach($params as $param){  
 if(!isset($_GET[$param])){  
     return false;   
  }  
}  
return true;   
}  
?>  

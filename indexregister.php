<?php
header("Access-Control-Allow-Origin: *"); 
require_once 'connection.php';  
  $response = array();  
     
    if(isTheseParametersAvailable(array('username','email','password','gender'))){  
    $username = $_GET['username'];   
    $email = $_GET['email'];   
    $password = md5($_GET['password']);  
    $gender = $_GET['gender'];   
   
    $stmt = $conn->prepare("SELECT id FROM mobileprogramming WHERE username = ? OR email = ?");  
    $stmt->bind_param("ss", $username, $email);  
    $stmt->execute();  
    $stmt->store_result();  
   
    if($stmt->num_rows > 0){  
        $response['error'] = true;  
        $response['message'] = 'User already registered';  
        $stmt->close();  
    }  
    else{  
        $stmt = $conn->prepare("INSERT INTO mobileprogramming
 (username, email, password, gender) VALUES (?, ?, ?, ?)");  
        $stmt->bind_param("ssss", $username, $email, $password, $gender);  
   
        if($stmt->execute()){  
            $stmt = $conn->prepare("SELECT id, id, username, email, gender FROM mobileprogramming
     WHERE username = ?");   
            $stmt->bind_param("s",$username);  
            $stmt->execute();  
            $stmt->bind_result($userid, $id, $username, $email, $gender);  
            $stmt->fetch();  
   
            $user = array(  
            'id'=>$id,   
            'username'=>$username,   
            'email'=>$email,  
            'gender'=>$gender  
            );  
   
            $stmt->close();  
            $response['error'] = false;   
            $response['message'] = 'User registered successfully';   
            $response['user'] = $user;   
        }  
    }   
}  
else{  
    $response['error'] = true;   
    $response['message'] = 'required parameters are not available';   
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

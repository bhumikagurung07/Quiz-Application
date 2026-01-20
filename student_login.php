<?php
// Database connection
$host = "localhost";  
$user = "root";       
$password = "";       
$database = "my_app"; 

$conn = mysqli_connect($host, $user, $password, $database);

if (!$conn) {
    die(json_encode(["status" => "error", "message" => "Database Connection Failed: " . mysqli_connect_error()]));
}

// Check if request method is POST
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $spid = $_POST['spid'];
    $password = $_POST['password'];

    // Check if SPID exists in registered_info table
    $checkSpidQuery = "SELECT * FROM registered_info WHERE spid='$spid'";
    $spidResult = mysqli_query($conn, $checkSpidQuery);

    if (mysqli_num_rows($spidResult) > 0) {
        // Check if password matches in student_info table
        $checkPasswordQuery = "SELECT * FROM student_info WHERE spid='$spid' AND password='$password'";
        $passwordResult = mysqli_query($conn, $checkPasswordQuery);

        if (mysqli_num_rows($passwordResult) > 0) {
            echo json_encode(["status" => "success", "message" => "Login successful"]);
        } else {
            echo json_encode(["status" => "failed", "message" => "Incorrect password"]);
        }
    } else {
        echo json_encode(["status" => "failed", "message" => "SPID not found"]);
    }
}

mysqli_close($conn);
?>

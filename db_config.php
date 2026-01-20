<?php
$servername = "localhost";  // Change if your database is hosted elsewhere
$username = "root";  // Default username for WAMP
$password = "";  // Default password for WAMP (empty)
$dbname = "my_app";  // Your database name

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die(json_encode(["success" => false, "message" => "Database connection failed: " . $conn->connect_error]));
}else{
    echo "Connection succesfull";
}?>

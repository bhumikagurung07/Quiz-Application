<?php
header("Content-Type: application/json");
error_reporting(0);
ini_set('display_errors', 0);

// Database connection details
$host = "localhost";       // Change if your DB is hosted elsewhere
$user = "root";            // DB username
$password = "";            // DB password
$database = "my_app";  // Replace with your actual DB name

// Create connection
$conn = mysqli_connect($host, $user, $password, $database);

// Check connection
if (!$conn) {
    die(json_encode(["success" => false, "message" => "Database connection failed."]));
}

// Query to fetch student details
$sql = "SELECT r_id, name, spid FROM registered_info";
$result = mysqli_query($conn, $sql);

$students = array();

if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        $students[] = $row;
    }

    echo json_encode([
        "success" => true,
        "students" => $students
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "No student records found."
    ]);
}

mysqli_close($conn);
?>

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

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $r_id = $_POST['r_id'];
    $name = $_POST['name'];
    $spid = $_POST['spid'];

    if (!empty($r_id) && !empty($name) && !empty($spid)) {
        $query = "INSERT INTO registered_info (r_id, name, spid) VALUES ('$r_id', '$name', '$spid')";
        $result = mysqli_query($conn, $query);

        if ($result) {
            $response['success'] = true;
            $response['message'] = "Student added successfully";
        } else {
            $response['success'] = false;
            $response['message'] = "Insertion failed: " . mysqli_error($conn);
        }
    } else {
        $response['success'] = false;
        $response['message'] = "All fields are required";
    }
} else {
    $response['success'] = false;
    $response['message'] = "Invalid request method";
}

echo json_encode($response);
?>

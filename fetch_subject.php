<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "my_app";  // Change to your database name

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Database connection failed: " . $conn->connect_error);
}

$base_url = "http://192.168.17.97/quizapp/uploads/"; // Update with your local server URL
$upload_dir = "uploads/";  // Folder to store images

$sql = "SELECT sub_id, sub_name, dept_id, tech_id, image FROM subject";
$result = mysqli_query($conn, $sql);

$response = array();

while ($row = mysqli_fetch_assoc($result)) {
    $imageData = $row['image']; // LONGBLOB binary data

    // Generate unique filename using sub_id
    $imageFileName = "image_" . $row['sub_id'] . ".jpg"; 
    $filePath = $upload_dir . $imageFileName;

    // Convert LONGBLOB to image file
    if (!file_exists($filePath)) {
        file_put_contents($filePath, $imageData);
    }

    // Return correct image URL
    $row['image'] = $base_url . $imageFileName;

    $response[] = $row;
}

header('Content-Type: application/json');
echo json_encode($response, JSON_PRETTY_PRINT);
?>

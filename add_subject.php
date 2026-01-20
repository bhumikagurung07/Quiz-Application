<?php
// Database connection
$host = "localhost";
$user = "root";
$pass = "";
$dbname = "my_app";

$conn = new mysqli($host, $user, $pass, $dbname);

// Check connection
if ($conn->connect_error) {
    die(json_encode(["error" => "Database connection failed: " . $conn->connect_error]));
}

header('Content-Type: application/json');
error_reporting(0); // Hide PHP warnings

// Get POST data
$sub_id = $_POST['sub_id'] ?? '';
$sub_name = $_POST['sub_name'] ?? '';
$dept_id = $_POST['dept_id'] ?? '';
$tech_id = $_POST['tech_id'] ?? '';
$image = $_POST['image'] ?? '';

// Validate input
if (empty($sub_id) || empty($sub_name) || empty($dept_id) || empty($tech_id) || empty($image)) {
    echo json_encode(["error" => "All fields are required"]);
    exit();
}

// Decode base64 image
$image_data = base64_decode($image);
if ($image_data === false) {
    echo json_encode(["error" => "Invalid image data"]);
    exit();
}

// Insert into database
$stmt = $conn->prepare("INSERT INTO subject (sub_id, sub_name, dept_id, tech_id, image) VALUES (?, ?, ?, ?, ?)");
$stmt->bind_param("sssss", $sub_id, $sub_name, $dept_id, $tech_id, $image_data);

if ($stmt->execute()) {
    echo "success";
} else {
    echo json_encode(["error" => "Database error: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>

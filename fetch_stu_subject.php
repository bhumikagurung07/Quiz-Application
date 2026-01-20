<?php
header('Content-Type: application/json');

// Database Connection
$host = 'localhost';
$user = 'root';
$password = '';
$database = 'my_app';

$conn = new mysqli($host, $user, $password, $database);
if ($conn->connect_error) {
    die(json_encode(["success" => false, "message" => "Database connection failed"]));
}

$dept_id = $_GET['dept_id'] ?? '';

$sql = "SELECT sub_id, sub_name, image FROM subject WHERE dept_id = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $dept_id);
$stmt->execute();
$result = $stmt->get_result();

$subjects = array();

while ($row = $result->fetch_assoc()) {
    $row['image'] = base64_encode($row['image']); // Convert LONGBLOB to base64
    $subjects[] = $row;
}

echo json_encode($subjects);
?>

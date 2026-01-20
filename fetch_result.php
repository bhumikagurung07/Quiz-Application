<?php
header('Content-Type: application/json');
error_reporting(E_ALL);
ini_set('display_errors', 1);

// DB connection
$host = 'localhost';
$user = 'root';
$password = '';
$database = 'my_app';

$conn = new mysqli($host, $user, $password, $database);
if ($conn->connect_error) {
    echo json_encode(["success" => false, "message" => "Database connection failed"]);
    exit();
}

if (isset($_GET['s_id'])) {
    $s_id = $_GET['s_id'];

    // Use prepared statements to avoid SQL injection
    $stmt = $conn->prepare("SELECT final_mark, time_consumed FROM result WHERE s_id = ? ORDER BY result_id DESC LIMIT 1");
    $stmt->bind_param("s", $s_id);
    $stmt->execute();
    $result = $stmt->get_result();

    $response = array();
    if ($row = $result->fetch_assoc()) {
        $response[] = $row;
    } // ✅ FIXED: Properly closed this if block

    echo json_encode($response);
} else {
    echo json_encode(["success" => false, "message" => "Missing s_id"]);
}
?>

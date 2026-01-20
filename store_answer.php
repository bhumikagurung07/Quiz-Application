<?php
header('Content-Type: application/json');
error_reporting(E_ALL);
ini_set('display_errors', 1);

$host = 'localhost';
$user = 'root';
$password = '';
$database = 'my_app';

$conn = new mysqli($host, $user, $password, $database);
if ($conn->connect_error) {
    echo json_encode(["success" => false, "message" => "Database connection failed"]);
    exit();
}

$s_id = $_POST['s_id'];
$q_id = $_POST['q_id'];
$selected_answer = $_POST['selected_answer'];
$obtained_mark = $_POST['obtained_mark'];

// Check if answer already exists
$checkStmt = $conn->prepare("SELECT * FROM answer_info WHERE s_id = ? AND q_id = ?");
$checkStmt->bind_param("ss", $s_id, $q_id);
$checkStmt->execute();
$result = $checkStmt->get_result();

if ($result->num_rows > 0) {
    echo json_encode(["success" => false, "message" => "Answer already submitted"]);
} else {
    // Insert new answer
    $stmt = $conn->prepare("INSERT INTO answer_info (s_id, q_id, selected_answer, obtained_mark) VALUES (?, ?, ?, ?)");
    $stmt->bind_param("sssi", $s_id, $q_id, $selected_answer, $obtained_mark);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "message" => "Answer stored"]);
    } else {
        echo json_encode(["success" => false, "message" => "Query failed: " . $stmt->error]);
    }
    $stmt->close();
}

$checkStmt->close();
$conn->close();
?>

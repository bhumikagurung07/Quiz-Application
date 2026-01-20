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
$s_id = $_POST['s_id'] ?? '';
$dept_id = $_POST['dept_id'] ?? '';
$sub_id = $_POST['sub_id'] ?? '';
$time_consumed = $_POST['time_consumed'] ?? 0;

if (empty($s_id) || empty($dept_id) || empty($sub_id)) {
    echo json_encode(["success" => false, "message" => "Missing required fields"]);
    exit();
}

// ✅ Step 1: Check if result already exists for this student & subject
$checkQuery = "SELECT * FROM result WHERE s_id = ? AND sub_id = ?";
$checkStmt = $conn->prepare($checkQuery);
$checkStmt->bind_param("ss", $s_id, $sub_id);
$checkStmt->execute();
$checkResult = $checkStmt->get_result();

if ($checkResult->num_rows > 0) {
    echo json_encode(["success" => false, "message" => "Result already submitted for this subject"]);
    $checkStmt->close();
    $conn->close();
    exit();
}
$checkStmt->close();

// ✅ Step 2: Get total marks from answer_info
$markQuery = "SELECT SUM(obtained_mark) AS final_mark FROM answer_info WHERE s_id = ? AND q_id IN (SELECT q_id FROM question WHERE sub_id = ?)";
$markStmt = $conn->prepare($markQuery);
$markStmt->bind_param("ss", $s_id, $sub_id);
$markStmt->execute();
$markResult = $markStmt->get_result();
$row = $markResult->fetch_assoc();
$final_mark = $row['final_mark'] ?? 0;
$markStmt->close();


// ✅ Step 3: Insert into result table
$insertQuery = "INSERT INTO result (s_id, dept_id, sub_id, final_mark, time_consumed) VALUES (?, ?, ?, ?, ?)";
$stmt = $conn->prepare($insertQuery);
$stmt->bind_param("sssii", $s_id, $dept_id, $sub_id, $final_mark, $time_consumed);

if ($stmt->execute()) {
    echo json_encode(["success" => true, "message" => "Result stored", "final_mark" => $final_mark]);
} else {
    echo json_encode(["success" => false, "message" => "Insert failed: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>

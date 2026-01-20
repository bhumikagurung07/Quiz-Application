<?php
header("Content-Type: application/json");

$host = "localhost";
$user = "root";
$pass = "";
$db = "my_app";

$conn = new mysqli($host, $user, $pass, $db);
if ($conn->connect_error) {
    echo json_encode(["success" => false, "message" => "Connection failed"]);
    exit();
}

// Get JSON data from request
$data = json_decode(file_get_contents("php://input"), true);

// Check required parameters
if (!isset($data['s_id'], $data['stu_name'], $data['phone_no'], $data['dob'])) {
    echo json_encode(["success" => false, "message" => "Missing parameters"]);
    exit();
}

$s_id = $data['s_id'];
$stu_name = $data['stu_name'];
$phone_no = $data['phone_no'];
$dob = $data['dob'];

// Update student_info table (without department)
$updateQuery = $conn->prepare("UPDATE student_info SET stu_name = ?, phone_no = ?, dob = ? WHERE s_id = ?");
$updateQuery->bind_param("sssi", $stu_name, $phone_no, $dob, $s_id);

if ($updateQuery->execute()) {
    echo json_encode(["success" => true]);
} else {
    echo json_encode(["success" => false, "message" => "Failed to update student: " . $conn->error]);
}

$conn->close();
?>

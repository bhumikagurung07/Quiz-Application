<?php
header('Content-Type: application/json');
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Database Connection
$host = 'localhost';
$user = 'root';
$password = '';
$database = 'my_app';

$conn = new mysqli($host, $user, $password, $database);
if ($conn->connect_error) {
    echo json_encode(["success" => false, "message" => "Database connection failed"]);
    exit();
}

// Validate sub_id
if (!isset($_GET['sub_id']) || empty($_GET['sub_id'])) {
    echo json_encode(["success" => false, "message" => "Missing sub_id"]);
    exit();
}

$sub_id = $_GET['sub_id'];

// Fetch 5 random questions including correct_answer
$query = "SELECT q_id, question, option_a, option_b, option_c, option_d, correct_ans 
          FROM question 
          WHERE sub_id = ? 
          ORDER BY RAND() 
          LIMIT 5";

$stmt = $conn->prepare($query);
$stmt->bind_param("s", $sub_id);
$stmt->execute();
$result = $stmt->get_result();

$questions = [];
while ($row = $result->fetch_assoc()) {
    $questions[] = $row;
}

// Return JSON
if (empty($questions)) {
    echo json_encode(["success" => false, "message" => "No questions found"]);
} else {
    echo json_encode($questions);
}

$stmt->close();
$conn->close();
?>

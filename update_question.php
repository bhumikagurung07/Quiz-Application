<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

// --- Database Connection ---
$host = "localhost";
$user = "root";
$password = "";
$database = "my_app";

$conn = mysqli_connect($host, $user, $password, $database);

// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $q_id = $_POST['q_id'];
    $question = $_POST['question'];
    $option_a = $_POST['option_a'];
    $option_b = $_POST['option_b'];
    $option_c = $_POST['option_c'];
    $option_d = $_POST['option_d'];
    $correct_ans = $_POST['correct_ans'];

    $sql = "UPDATE question SET 
                question = '$question',
                option_a = '$option_a',
                option_b = '$option_b',
                option_c = '$option_c',
                option_d = '$option_d',
                correct_ans = '$correct_ans'
            WHERE q_id = '$q_id'";

    if (mysqli_query($conn, $sql)) {
        echo json_encode(array("status" => "success", "message" => "Question updated successfully"));
    } else {
        echo json_encode(array("status" => "error", "message" => "Update failed"));
    }
} else {
    echo json_encode(array("status" => "error", "message" => "Invalid request"));
}
?>

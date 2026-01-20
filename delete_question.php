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

    $sql = "DELETE FROM question WHERE q_id = '$q_id'";

    if (mysqli_query($conn, $sql)) {
        echo json_encode(array("status" => "success", "message" => "Question deleted successfully"));
    } else {
        echo json_encode(array("status" => "error", "message" => "Delete failed"));
    }
} else {
    echo json_encode(array("status" => "error", "message" => "Invalid request"));
}
?>

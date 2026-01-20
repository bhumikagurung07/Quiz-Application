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

if (isset($_GET['sub_id'])) {
    $sub_id = $_GET['sub_id'];

    $sql = "SELECT * FROM question WHERE sub_id = '$sub_id'";
    $result = mysqli_query($conn, $sql);

    $questions = array();

    while ($row = mysqli_fetch_assoc($result)) {
        $questions[] = array(
            "q_id" => $row["q_id"],
            "question" => $row["question"],
            "option_a" => $row["option_a"],
            "option_b" => $row["option_b"],
            "option_c" => $row["option_c"],
            "option_d" => $row["option_d"],
            "correct_ans" => $row["correct_ans"]
        );
    }

    echo json_encode($questions);
} else {
    echo json_encode(array("error" => "No sub_id provided"));
}
?>


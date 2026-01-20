<?php
$servername = "localhost";
$username = "root"; // Change if needed
$password = ""; // Change if needed
$dbname = "my_app"; // Update with your database name

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sub_id = $_POST['sub_id'];
$question = $_POST['question'];
$option_a = $_POST['option_a'];
$option_b = $_POST['option_b'];
$option_c = $_POST['option_c'];
$option_d = $_POST['option_d'];
$correct_ans = $_POST['correct_ans'];

$sql = "INSERT INTO question (sub_id, question, option_a, option_b, option_c, option_d, correct_ans)
        VALUES ('$sub_id', '$question', '$option_a', '$option_b', '$option_c', '$option_d', '$correct_ans')";

if (mysqli_query($conn, $sql)) {
    echo "Success";
} else {
    echo "Error: " . mysqli_error($conn);
}
?>

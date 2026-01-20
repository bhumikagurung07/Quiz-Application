<?php
header("Content-Type: application/json");
require 'db_config.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $fullname = $_POST['fullname'];
    $username = $_POST['username'];
    $password = $_POST['password'];

    $sql = "INSERT INTO teacher_info (fullname, username, password) VALUES ('$fullname', '$username', '$password')";

    if ($conn->query($sql) === TRUE) {
        echo "Teacher added successfully!";
    } else {
        echo "Error: " . $conn->error;
    }
    $conn->close();
}
?>

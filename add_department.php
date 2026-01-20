<?php
$servername = "localhost";
$username = "root"; // Change as needed
$password = ""; // Change as needed
$dbname = "my_app"; // Update with your database name

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $dept_id = $_POST['dept_id'];
    $dept_name = $_POST['dept_name'];
    $image = $_POST['image']; // Base64 encoded image
    $image_name = uniqid() . ".jpg";
    $image_path = "uploads/" . $image_name;

    // Store image in the upload folder
    file_put_contents($image_path, base64_decode($image));

    // Insert data into the department table
    $sql = "INSERT INTO department (dept_id, dept_name, image) VALUES ('$dept_id', '$dept_name', '$image_path')";

    if ($conn->query($sql) === TRUE) {
        echo "Success";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
}
$conn->close();
?>

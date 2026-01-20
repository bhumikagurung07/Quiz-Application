<?php
$host = "localhost";
$user = "root";
$pass = "";
$db = "my_app";

$conn = new mysqli($host, $user, $pass, $db);
if ($conn->connect_error) {
    die("Connection failed");
}

if (isset($_GET['s_id'])) {
    $s_id = $_GET['s_id'];

    $query = "SELECT s.spid, s.stu_name, s.password, s.phone_no, s.dob, d.dept_name, s.image 
              FROM student_info s 
              JOIN department d ON s.dept_id = d.dept_id 
              WHERE s.s_id = ?";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("i", $s_id);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($row = $result->fetch_assoc()) {
        // Convert image path to full URL
        $baseUrl = "http://192.168.17.97/quizapp/"; // Change to your server IP/folder path
        $row['image'] = $baseUrl . $row['image'];   // Don't encode, just send URL

        echo json_encode($row);
    } else {
        echo json_encode(["success" => false, "message" => "Student not found"]);
    }
}
?>

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

// --- Fetching Data from student_info JOIN department ---
$response = array();

$sql = "SELECT s.s_id, s.spid, s.password, s.stu_name, s.phone_no, s.dob, d.dept_name 
        FROM student_info s
        INNER JOIN department d ON s.dept_id = d.dept_id";

$result = mysqli_query($conn, $sql);

if (mysqli_num_rows($result) > 0) {
    $response['success'] = true;
    $response['students'] = array();

    while ($row = mysqli_fetch_assoc($result)) {
        $response['students'][] = $row;
    }
} else {
    $response['success'] = false;
    $response['message'] = "No data found";
}

echo json_encode($response);
?>

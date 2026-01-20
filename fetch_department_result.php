<?php
header('Content-Type: application/json');
error_reporting(E_ALL);
ini_set('display_errors', 1);

$host = 'localhost';
$user = 'root';
$password = '';
$database = 'my_app';
$conn = new mysqli($host, $user, $password, $database);

if ($conn->connect_error) {
    echo json_encode(["success" => false, "message" => "Database connection failed"]);
    exit();
}

if (isset($_GET['dept_id'])) {
    $dept_id = $_GET['dept_id'];

    $sql = "SELECT s.spid, s.stu_name, r.final_mark, sub.sub_name
            FROM student_info s
            JOIN result r ON s.s_id = r.s_id
            JOIN subject sub ON r.sub_id = sub.sub_id
            WHERE s.dept_id = ?
            ORDER BY r.final_mark DESC";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $dept_id);
    $stmt->execute();
    $result = $stmt->get_result();

    $response = [];

    while ($row = $result->fetch_assoc()) {
        $response[] = [
            "spid" => $row['spid'],              // include SPID
            "stu_name" => $row['stu_name'],
            "final_mark" => $row['final_mark'],
            "sub_name" => $row['sub_name']
        ];
    }

    echo json_encode($response);
} else {
    echo json_encode(["success" => false, "message" => "Missing dept_id"]);
}

<?php
header("Content-Type: application/json");
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
    echo json_encode(["status" => "error", "message" => "Connection failed"]);
    exit();
}

// SQL Query
$sql = "SELECT 
            r.result_id, 
            s.spid, 
            d.dept_name, 
            sub.sub_name, 
            r.final_mark, 
            r.time_consumed 
        FROM result r
        JOIN student_info s ON r.s_id = s.s_id
        JOIN department d ON s.dept_id = d.dept_id
        JOIN subject sub ON r.sub_id = sub.sub_id
        ORDER BY r.final_mark DESC";

$result = mysqli_query($conn, $sql);

$response = [];

if ($result && mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        $response[] = $row;
    }
    echo json_encode(["status" => "success", "data" => $response]);
} else {
    echo json_encode(["status" => "error", "message" => "No records found"]);
}

mysqli_close($conn);
?>

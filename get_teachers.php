<?php
// Database Connection
$host = "localhost";  // Change if needed
$user = "root";       // Your MySQL username
$pass = "";           // Your MySQL password
$dbname = "my_app";  // Your database name

$conn = mysqli_connect($host, $user, $pass, $dbname);

// Check connection
if (!$conn) {
    die(json_encode(["error" => "Database connection failed"]));
}

// Query to fetch teachers
$query = "SELECT tech_id, username, password, fullname FROM teacher_info";
$result = mysqli_query($conn, $query);

if (!$result) {
    echo json_encode(["error" => "Failed to fetch data"]);
    exit();
}

$teachers = [];
while ($row = mysqli_fetch_assoc($result)) {
    $teachers[] = $row;
}

// Return JSON response
header('Content-Type: application/json');
echo json_encode($teachers);

// Close connection
mysqli_close($conn);
?>

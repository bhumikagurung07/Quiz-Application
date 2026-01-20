<?php
require 'db_config.php'; 

header('Content-Type: application/json');

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $username = $_POST['username'];
    $password = $_POST['password'];
    $table = $_POST['table'];

    if (!empty($username) && !empty($password) && !empty($table)) {
        // Validate table name to prevent SQL Injection
        if ($table !== 'admin_mst' && $table !== 'teacher_info') {
            $response['success'] = false;
            $response['message'] = 'Invalid user type';
            echo json_encode($response);
            exit();
        }

        // Prepare the SQL statement
        $stmt = $conn->prepare("SELECT * FROM $table WHERE username = ? AND password = ?");
        $stmt->bind_param("ss", $username, $password);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            $user = $result->fetch_assoc();
            $response['success'] = true;
            $response['message'] = 'Login successful';
            $response['role'] = $table;
            
            if ($table === 'teacher_info') {
                $response['tech_id'] = $user['tech_id'];
            }
            echo json_encode($response);
        } else {
            $response['success'] = false;
            $response['message'] = 'Invalid username or password';
            echo json_encode($response);
        }

        $stmt->close();
    } else {
        $response['success'] = false;
        $response['message'] = 'All fields are required';
        echo json_encode($response);
    }
} else {
    $response['success'] = false;
    $response['message'] = 'Invalid request method';
    echo json_encode($response);
}

$conn->close();
?>
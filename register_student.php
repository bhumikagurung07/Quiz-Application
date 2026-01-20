<?php
$servername = "localhost";
$username = "root"; // Change if needed
$password = ""; // Change if needed
$dbname = "my_app"; // Update with your database name

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $spid = $_POST['spid'];
    $password = $_POST['password'];
    $stu_name = $_POST['stu_name'];
    $phone_no = $_POST['phone_no'];
    $dob = $_POST['dob'];
    $dept_id = $_POST['dept_id'];
    $image = $_POST['image']; // Base64 encoded image

    // Check if SPID exists in student_info table
    $check_spid_sql = "SELECT spid FROM registered_info WHERE spid = '$spid'";
    $result = $conn->query($check_spid_sql);

    if ($result->num_rows > 0) {
        // Generate unique image name
        $image_name = uniqid() . ".jpg";
        $image_path = "uploads/" . $image_name;

        // Store image in the upload folder
        if (file_put_contents($image_path, base64_decode($image))) {
            // Insert data into student_info table
            $sql = "INSERT INTO student_info (spid, password, stu_name, phone_no, dob, dept_id, image) 
                    VALUES ('$spid', '$password', '$stu_name', '$phone_no', '$dob', '$dept_id', '$image_path')";

            if ($conn->query($sql) === TRUE) {
                // Get the last inserted student_id (s_id)
                $s_id = $conn->insert_id;

                $response["success"] = true;
                $response["s_id"] = $s_id;
                $response["dept_id"] = $dept_id;
                $response["image"] = $image_path;
            } else {
                $response["success"] = false;
                $response["message"] = "Error: " . $conn->error;
            }
        } else {
            $response["success"] = false;
            $response["message"] = "Failed to store image";
        }
    } else {
        $response["success"] = false;
        $response["message"] = "SPID not found";
    }
} else {
    $response["success"] = false;
    $response["message"] = "Invalid request";
}

echo json_encode($response);
$conn->close();
?>

<?php
$servername = "localhost";
$username = "root"; // Change as needed
$password = ""; // Change as needed
$dbname = "my_app"; // Update with your database name

$conn = new mysqli($servername, $username, $password, $dbname);

$response = array();
$base_url = "http://10.39.2.6/quizapp/uploads/"; // Correct base URL

$sql = "SELECT dept_id, dept_name, image FROM department";
$result = mysqli_query($conn, $sql);

while ($row = mysqli_fetch_assoc($result)) {
    // Ensure no duplicate 'uploads/' in the image path
    $imagePath = $row['image'];

    // Remove extra 'uploads/' if mistakenly stored in DB
    if (strpos($imagePath, 'uploads/') === 0) {
        $imagePath = substr($imagePath, strlen('uploads/'));
    }

    $row['image'] = $base_url . $imagePath;  
    $response[] = $row;
}

echo json_encode($response);
?>
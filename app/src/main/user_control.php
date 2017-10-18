<?php
require_once 'connection.php';
header('Content-Type: applications/json ');

	class User {

		private $db;
		private $connection;

		function __construct() {
			$this -> db = new databaseConnection();
			$this -> connection = $this->db->getConnection();
		}

		public function doesUserExist($email,$password)
		{
			$query = "Select * from users where username='$username' and password = '$password' ";
			$result = mysqli_query($this->connection, $query);
			if(mysqli_num_rows($result)>0){
				$json['success'] = ' Welcome '.$username;
				echo json_encode($json);
				mysqli_close($this -> connection);
			}else{
				$query = "insert into USERS (username, password) values ( '$username','$password')";
				$inserted = mysqli_query($this -> connection, $query);
				if($inserted == 1 ){
					$json['success'] = 'Account created.';
				}else{
					$json['error'] = 'Wrong password.';
				}
				echo json_encode($json);
				mysqli_close($this->connection);
			}

		}

	}

	$user = new User();
	if(isset($_POST['username'],$_POST['password'])) {
		$username = $_POST['username'];
		$password = $_POST['password'];

		if(!empty($username) && !empty($password)){

			$encrypted_password = md5($password);
			$user-> doesUserExist($username,$password);

		}else{
			echo json_encode("you must type both inputs");
		}
	}
?>
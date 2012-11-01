<?php
//+-------------------+
//| класс для работы с| 
//| учетными записями |
//+-------------------+
  class login_register
  {
  	function login_register(){}
  	
  	
  	//function register_new($name, $pass, $fio [ $priority = "user" ])
  	//register a new user
  	//returns TRUE on success, FALSE else
  	function register_new($name, $pass, $fio, $priority = "user")
  	{
  	 $name = mysql_real_escape_string($name);
  	 $cnt = mysql_fetch_array(mysql_query("SELECT count(*) FROM users WHERE name = '$name' "));
  	// print_r ($cnt);
  	 if($cnt[0]>0)return false;
  		
  		$pass = md5($pass);
  		$fio = mysql_real_escape_string($fio);
  		$priority = mysql_real_escape_string($priority);
  		$q = "INSERT INTO users (name,  password, fio, priority) VALUES ('$name', '$pass','$fio', '$priority')";
  		//echo $q;
  		$res = mysql_query($q);
  		if($res)
  		 return true; 
  		else return false; 
  	}
  	
  	
  	//do authorization into the system 
  	//TRUE on success, FALSE else
  	function authorization($name, $pass)
  	{
  		$name = mysql_real_escape_string($name);
  		$pass = md5($pass);
  		$q = "SELECT * FROM users WHERE name = '$name'";
  		
  		$data = mysql_fetch_array(mysql_query($q));
  		if($data)
  		{
	  		if(($data['password']==$pass)||($data['password']==""))
	  		{
	  			$_SESSION['name'] = $data['name'];
	  			$_SESSION['priority'] = $data['priority'];
	  			$_SESSION['uid']=$data['uid'];			
	  			return true;
	  		}else return false;
  		}else 
  		return false;
  		
  	}
  	
  	//returns TRUE if login is already used
  	//else returns FALSE
  	function check_name($name)
  	{
  		$name = mysql_real_escape_string($name);
  		$cnt = mysql_fetch_array(mysql_query("SELECT count(*) FROM users WHERE name = '$name'"));
  		if($cnt[0]>0) 
  		 return true;
  		else return false;
  	}
  	
  	function logout()
  	{
  		$_SESSION = array();
  		session_destroy();
  		
  	}
  	
  }
?>
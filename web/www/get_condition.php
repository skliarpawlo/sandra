<?php
session_start();
  include_once 'head.php';
 if(!isset($_SESSION['name']))
 {
 	die("Not logined");
 } 
$probl = mysql_fetch_array(mysql_query("SELECT * FROM problems WHERE pid='".mysql_real_escape_string($_GET['pid'])."'"));
$file = (stripslashes($probl['filepath']));

if(file_exists($file)){
	header ("Content-Type: application/octet-stream");
	
	header ("Accept-Ranges: bytes");
	
	header ("Content-Length: ".filesize($file));
	
	header ("Content-Disposition: attachment; filename=".$file);  
	
	readfile($file);
}
else
{
	echo "Файл з умовою не знайдено!";
}


?>
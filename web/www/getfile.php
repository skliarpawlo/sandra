<?php
session_start();
  include_once 'head.php';
 if(!isset($_SESSION['name']))
 {
 	die("Not logined");
 } 
$solution = mysql_fetch_array(mysql_query("SELECT * FROM solutions WHERE sid='".mysql_real_escape_string($_GET['sid'])."' AND uid='".$_SESSION['uid']."'" ));

 $file = ($solution['filepath']);
 if($_GET['file']=="log")
 {
  $file= substr($file, 0, strrpos($file, '.'));
  $file.=".test";
 }
 


if(file_exists($file)){
	header ("Content-Type: application/octet-stream");
	
	header ("Accept-Ranges: bytes");
	
	header ("Content-Length: ".filesize($file));
	
	header ("Content-Disposition: attachment; filename=".$file);  
	
	readfile($file);
}
else
{
	echo "Файл не знайдено";
}


?>
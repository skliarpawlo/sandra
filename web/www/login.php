<?php
session_start();
  include_once 'head.php';
 include_once 'classes/login.register.class.php';
 
 $LOGINER = new login_register();
  
  if($LOGINER->authorization($_POST['login'],$_POST['pass']))
  {
  	switch($_SESSION['priority'])
  	{
  		case "user":
  			{
  				header("location:index.php");
  				break;
  			}
  		case "admin":
  			{
  				header("location:admin/index.php");
  				break;
  			}
  	}
  }
  else echo "Невірний логін/пароль  <a href = '/'>На головну</a>";
?>
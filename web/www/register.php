<?php 
session_start();
include_once 'head.php';
include_once 'classes/login.register.class.php';
 
 $configs = mysql_fetch_array(mysql_query("SELECT * FROM configs WHERE id=1"));

 $LOGINER = new login_register();
 $error = "";
   if(isset($_POST['submit']))
   {
   	if(($_POST['login']!="")&&(strlen($_POST['login'])>=5))
   	{
   	 if($_POST['pass']==$_POST['pass_repeat'])
   	 {
   	 	if($_POST['pass']!="")
   	 	{
   	 		if($_POST['name']!="")
   	 		{
   	 		  if($_SESSION['capcha']==$_POST['capcha_code'])
   	 		  {
	   	 		  if( $LOGINER->register_new($_POST['login'],$_POST['pass'],$_POST['name']))
	   	 		  {
	   	 		  	$error = "Реєстрація пройшла успішно";
	   	 		  }
	   	 		  else
	   	 		  {
	   	 		   $error = "Помилка реєстрації, або вказаний логін вже використовується";	
	   	 		  }
   	 		  }
   	 		  else 
   	 		  {
   	 		  	 $error = "Невірно введено код з зображення";
   	 		  } 
   	 		}
   	 		else 
   	 		{
   	 			$error = "Ви не заповнили персональні дані";
   	 		}
   	 	}
   	 	else
   	 	{
   	 	 $error = "Пароль не може бути порожнім";	
   	 	}
   	 }
   	 else
   	 {
   	 	$error = "Пароль і підтвердження не співпадають";
   	 }
   	}
   	else
   	{
   		$error = "Логін не може бути порожнім або коротшим 4 символів";
   	}
   }
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Sandra</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="style.css" type="text/css" />
  <script type="text/javascript" src = 'js/jquery-1.3.2.min.js'></script>
  <script type="text/javascript" src = 'js/all.js'></script>
  <script type= 'text/javascript' src='js/jquery-ui-1.7.2.custom.min.js'></script>
  <link rel="stylesheet" type="text/css" href="jquery-ui-1.7.2.custom.css" />
</head>
<body>
<!-- start box -->
<div id="inner_box">
  <div id="top"></div>
  <!-- top background -->
  <!-- start middle content -->
  <div id="inner_middle">
    <!-- start nav -->
    <div id="nav">
      <!-- start logo -->
      <div id="logo">
        <h1><a href="index.php">Sandra<span>2010</span></a></h1>
      </div>
      <!-- end logo -->
      <!-- start menu -->
      <div id="menu">
        <ul>
          <?php 
	       if(isset($_SESSION['name']))
	       {
	    	echo "<font style='color:white;'>Ви ввійшли як <b>".$_SESSION['name']."</b></font> ";
	    	echo "<li><a href = 'logout.php'>Вихід</a></li>
	    		  <li><a href = 'changepass.php'>Змінити пароль</a></li>";
	       }
	        else echo "<li><a href = '#' onclick ='showLoginForm();return false;' ;>Вхід</a></li>  
	                   <li><a href = 'register.php'>Реєстрація</a></li>";
	      ?>
          <li><a href='categories.php?catid=1'>Задачі</a></li>
          <li><a href='news.php'>Новини</a></li>
          <li><a href='results.php'>Результати</a></li>
        </ul>
      </div>
      <!-- end menu -->
    </div>
    <!-- end nav -->
    <div id="inner_img"></div>
    <!-- left -->
    <div id="left">
    <div style='margin-left:30px;margin-top:20px;'>
    <?php 
       echo "<font style='color:red'>".$error."</font><br>"; 
       if($configs['can_register']==1)
       {
	    include_once 'forms/register_form.php';
	    echo "<br>
	   <img name = 'capcha' src = 'capcha.php'>
       <a href='#' onclick = \"document.capcha.src='capcha.php?'+Math.random(); return false;\">
       <img border='0' src='images/reload.gif' width='30'>
       </a>";
       }
       else
       {
        echo "На жаль, реєстрація на даний момент не доступна";
       }
	?>
	   
    </div>
    </div>
    <!--left -->
    <!-- right -->
    <div id="right">
      <h3>Меню</h3>
      <ul>
        <li><a href='categories.php?catid=1'>Задачі</a></li>
        <li><a href='news.php'>Новини</a></li>
        <li><a href='results.php'>Результати</a></li>
      </ul>
      <table height='350px'>
       <tr>
        <td>
        </td>
       </tr>
      </table>
    </div>
    <!-- right -->
  </div>
  <!-- end middle content -->
  <div id="bottom"></div>
  <!-- bottom background -->
  <!-- start footer -->
  <p id="footer">&copy Skliar & Myrik, 2010 </p>
  <!-- end footer -->
</div>
<!-- end box -->
<div style='display:none;' ; id='login'  align = "center">
   <form id = 'loginform' action = 'login.php' method='post'>
    Login<br>
    <input type = "text" name = 'login'><br><br>
    Password<br>
    <input type = 'password' name = 'pass'><br><br><br>
   </form>
  <button onclick='$("#loginform").submit(); $("#login").dialog("destroy");'>Вхід</button>
  <button onclick='$("#login").dialog("destroy")'>Закрити</button>
 </div>
</body>
</html>
 
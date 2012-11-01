<?php
  session_start();
  include_once '../head.php';
  include_once '../classes/admin.class.php';
  $ADMIN = new admin();
  $status = "";
  if($_SESSION['priority']=='admin')
  {
  	if(isset($_POST['submit']))
  	{
  		if($_POST['extensions']!=""&&$_POST['compilerpath']!="")
  		{
  			$ADMIN->update_compiler($_GET['cpid'],$_POST['extensions'],$_POST['compilerpath'],$_POST['params']);
  			$status = "Зміни збережено";
  		}else $status = "Не всі поля заповнені";
  	}
  	
  	$curr_cp=mysql_fetch_array(mysql_query("SELECT * FROM compilers WHERE cpid='".mysql_real_escape_string($_GET['cpid'])."'"));
  }
  else
  { 
   die("Немає доступу");
  }
?>





<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Sandra - Редагування компілятора</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../style.css" type="text/css" />
  <script type="text/javascript" src = '../js/jquery-1.3.2.min.js'></script>
  <script type="text/javascript" src = '../js/all.js'></script>
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
        <h1><a href="../">Sandra<span>2010</span></a>&nbsp <a href='index.php'>Панель адміністратора</a></h1>
      </div>
      <!-- end logo -->
      <!-- start menu -->
      <div id="menu">
        <ul>
          <?php 
	       if(isset($_SESSION['name']))
	       {
	    	echo "<font style='color:white;'>Ви ввійшли як <b>".$_SESSION['name']."</b></font> ";
	    	echo "<li><a href = '../logout.php'>Вихід</a></li>
	    		  <li><a href = '../changepass.php'>Змінити пароль</a></li>";
	       }
	        
	      ?>
          
        </ul>
      </div>
      <!-- end menu -->
    </div>
    <!-- end nav -->
    <div id="inner_img"></div>
    <!-- left -->
    <div id="left">
    <div style='margin-left:30px;margin-top:20px;'>
      
     <?php echo $status;
     echo " 
      <form name='compilerForm' action = '' method = 'post' onsubmit='return checkCompilerForm();'>
       Розширення, з якими зв'язаний компілятор(через кому)<br>
       <textarea name = 'extensions' >".stripslashes($curr_cp['extensions'])."</textarea><br>
        Повний шлях до файлу компілятора<br>
       <textarea name = 'compilerpath' >".stripslashes($curr_cp['compilerpath'])."</textarea><br>
       Параметри компіляції<br>
       <textarea name = 'params' >".stripslashes($curr_cp['params'])."</textarea><br>
       <input type = 'submit' name = 'submit' value = 'Зберегти'><br>
      </form>";
 	 ?>      
      
      
      
    </div>
    </div>
    <!--left -->
    <!-- right -->
    <div id="right">
      <?php include_once 'menu.php';?>
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
</body>
</html>
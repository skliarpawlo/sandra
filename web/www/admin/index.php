<?php
 session_start();
  include_once '../head.php';
  include_once '../classes/admin.class.php';
  $ADMIN = new admin();
  $status = "";
  if($_SESSION['priority']=='admin')
  {
  	
  	
  }
  else
  { 
   die("Немає доступу");
  }
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Sandra - Головна сторінка</title>
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
      
      <h3>Панель адміністратора - головна сторінка.</h3>
      <p>Адміністратор додаває нові задачі та тести до них, можне створювати <br>
	  категорії задач та додавати у них задачі, задавати нові компілятори, <br>
	  оголошувати новини с приводу нових контестів та результати тих, що <br>
	  вже відбулись. Також є можливість редагувати вже створені записи. Усі <br>
	  Необхідні операції ви можете викликати з меню справа.
	  </p>      
	  
	  <p>Адміністратор має доступ до серверної програми Sandra. Ввідний відео-курс <br>
	  по цій програмі ви можете отримати <a href='/help/video_lesson.htm'>тут</a><br>
	  </p>                  
      
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
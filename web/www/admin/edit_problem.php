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
  		if($_POST['title']!=""&&$_POST['filepath']!=""&&$_POST['comparor']!=""&&
  		   $_POST['solutionfile']!=""&&$_POST['inputfile']!=""&&$_POST['outputfile']!=""&&$_POST['timelimit']!=""
  		   )
  		{
  		 $ADMIN->update_problem($_GET['pid'],$_POST['title'],$_POST['filepath'],$_POST['comparor'],$_POST['solutionfile'],$_POST['inputfile'],$_POST['outputfile'],$_POST['timelimit']);
  		 $status = "Зміни збережено";
  		}else $status="Не всі поля заповнені";
  	}
  	
  	$curr_problem=mysql_fetch_array(mysql_query("SELECT * FROM problems WHERE pid='".mysql_real_escape_string($_GET['pid'])."'"));
  }
  else
  { 
   die("Немає доступу");
  }
?>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Sandra - Редагування задачі</title>
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
	     <form name='problemForm' action = '' method = 'post' onsubmit='return checkProblemForm();'>
	       Назва<br>
	       <textarea name = 'title' >".stripslashes($curr_problem['title'])."</textarea><br>
	       Шлях до файлу з умовою<br>
	       <textarea name = 'filepath' >".stripslashes($curr_problem['filepath'])."</textarea><br>
	       Comparor<br>
	       <textarea name = 'comparor' >".stripslashes($curr_problem['comparor'])."</textarea><br>
	       SolutionFile<br>
	       <textarea name = 'solutionfile' >".stripslashes($curr_problem['solutionfile'])."</textarea><br>
	       Inputfile<br>
	       <textarea name = 'inputfile' >".stripslashes($curr_problem['inputfile'])."</textarea><br>
	       Outputfile<br>
	       <textarea name = 'outputfile' >".stripslashes($curr_problem['outputfile'])."</textarea><br>
	       Timelimit<br>
	       <textarea name = 'timelimit' >".stripslashes($curr_problem['timelimit'])."</textarea><br>
	       <input type = 'submit' name = 'submit' value = 'Зберегти зміни'><br>
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
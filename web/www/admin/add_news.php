<?php
session_start();
  include_once '../head.php';
  include_once '../classes/admin.class.php';
  $ADMIN = new admin();
  $status = "";
  if($_SESSION['priority']=='admin')
  {
  	if(isset($_POST['submit'])&&$_POST['title']!=""&&$_POST['text']!="")
  	{
  		
  		$ADMIN->add_news($_POST['title'],$_POST['text']);
  		$status = "Новина була додана";
  	}
  }
  else
  { 
   die("Немає доступу"); 
  }
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Sandra - Додати новину</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../style.css" type="text/css" />
  <script type="text/javascript" src = '../js/jquery-1.3.2.min.js'></script>
  <script type="text/javascript" src = '../js/all.js'></script>
  <script type="text/javascript" src="../js/fckeditor/fckeditor.js"></script>
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
      
      <?php echo $status;?>
		<div >
		  <form action='' method='post' > 
		   Заголовок новини<br>
		   <input type ="text" name="title" id="title" size='50' /><br>
		   Текст новини<br>
		   <textarea name="text" id="text" rows="10" cols="40">Введіть текст новини</textarea><br>
		   <input type='submit' name='submit' value = 'Зберегти'> 
		  </form>
		</div>
		<script type="text/javascript">
			var oFCKeditor = new FCKeditor("text"); // привязка к textarea с id="body"
			oFCKeditor.ToolbarSet="Basic"; // число кнопочек на инструментальной панели (полная Default)
			oFCKeditor.BasePath="../js/fckeditor/"; //путь к fckeditor
			oFCKeditor.ReplaceTextarea(); // вставка текста из textarea с id="body"
		</script>
		      
      
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

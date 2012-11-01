<?php
session_start();
  include_once '../head.php';
  include_once '../classes/admin.class.php';
  $ADMIN = new admin();
if($_SESSION['priority']=='admin')
  {
  	 
  }else die("Немає доступу");
  
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Sandra - Додати задачі до категорій</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../style.css" type="text/css" />
  <script type="text/javascript" src = '../js/jquery-1.3.2.min.js'></script>
  <script type="text/javascript" src = '../js/all.js'></script>
  <script type="text/javascript" src = '../js/jquery.form.js'></script>
   <script type= 'text/javascript' src='../js/jquery-ui-1.7.2.custom.min.js'></script>
   <link rel="stylesheet" type="text/css" href="../jquery-ui-1.7.2.custom.css" />
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
    <div style='margin-left:30px;margin-top:20px; margin-right:20px;'>
      <div id = 'cats'  >
  
	  </div>
      <?php 
        
       $num=10;//kolichestvo zapisey na stranice
  
	  if($_GET['p']==null)
	  {
	  	$p=0;
	  }
	  else
	  {
	  	$p=mysql_real_escape_string($_GET['p'])-1;
	  }
	  $q="Select count(*) FROM problems";
	  $cnt=mysql_fetch_array(mysql_query($q));
	  $cnt=$cnt[0];
	  if($cnt!=0)
	  {
		$c=$num*$p;
	    if(($cnt%$num)==0)$pgcnt=$cnt/$num; else $pgcnt=(floor($cnt/$num)+1);
  			$pages="Сторінки<br>";
   			for($i=1; $i<=$pgcnt;$i++)
   			{
  				$pages.="<a href='problem_category_assign.php?p=$i'>$i</a> ";
  			}
  		echo "<center><h3>".$pages."</h3></center><br>";	
		
	   $q = "SELECT * FROM problems LIMIT $c,$num";
	   $go = mysql_query($q);
	  
	   $i=1;
	   echo "<table width='100%' cellspacing='0'>";
	   while(($res = mysql_fetch_array($go)))
	   { 
	  		
	  	if($i%2){$bg='#CCCCCC';}else {$bg='white';}
	  	
	  	echo "
	  	<tr>
	  	  <td style='background: $bg'>".stripslashes($res['title'])."
	  	  </td> 
	  	  <td align='right' style='background: $bg'><a href='#' onclick='getCats(".$res['pid'].");return false;'>
	  	   Додати до категорій...</a>
	  	  </td>
	  	</tr>";
	  	$i++;
	   }
	   echo "</table>";
	  }
	  else
	  { 
	   echo "Немає записів";
	  }
       
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


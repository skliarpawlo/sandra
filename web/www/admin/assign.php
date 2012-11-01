<?php
  session_start();
  include_once '../head.php';
  include_once '../classes/admin.class.php';
  $ADMIN = new admin();
  $status = "";
  if($_SESSION['priority']=='admin')
  {
    //print_r($_POST);
    
    if(isset($_POST['submit'])&&$_POST['pid']!="")
    {	
    	$pid = mysql_real_escape_string($_POST['pid']);
      
    	$q="DELETE FROM problem_category_assign WHERE pid=".$pid;
    	mysql_query($q);
    	
    	if( sizeof($_POST['cats'])!=0)
    	{
	    	foreach($_POST['cats']as $key=>$value)
	    	{
	    		$q_ins = "INSERT INTO problem_category_assign (catid, pid) VALUES ('$value','$pid')";
	    		mysql_query($q_ins);
	    	}
    	}
    	
    } 
    
  	$pid=mysql_real_escape_string($_GET['pid']);
  	$q_checked = "SELECT * FROM problem_categories JOIN problem_category_assign ON 
  	      problem_categories.catid = problem_category_assign.catid WHERE pid='$pid'";
  	$go = mysql_query($q_checked);
  	      
  	
  	//Тут надо еще отфильтровать все, кроме категорий нижних уровней
  	$q_not_checked = "SELECT * FROM problem_categories WHERE catid NOT IN 
  	 				  (SELECT catid FROM problem_category_assign WHERE pid='$pid')  	 				  
  	 				  ";
  	
  	$go_not = mysql_query($q_not_checked);      
  	      
  	
  	      echo "<form id='myForm' action ='assign.php' method='post'>"."\n";
  	      
  	      echo "<input type='hidden' id='pid' name='pid' value='$pid'>"."\n";
  	      while(($res = mysql_fetch_array($go)))
  	      {
  	        echo"<input type='checkbox' name='cats[]' value='".$res['catid']."' checked> ".stripslashes($res['name'])."<br>\n";	
  	      }
  		  while(($res_not = mysql_fetch_array($go_not)))
  	      {
  	      	$q_lower = "SELECT count(catid) FROM problem_categories WHERE parentid=".$res_not['catid'];
			
  	      	 $cnt=mysql_fetch_array(mysql_query($q_lower));
  	      	 
  	      	//Відкидаємо ті, в кого є child або це коренева категорія 
  	        if($cnt[0]==0&&$res_not['parentid']!=0) 
  	      	   echo"<input type='checkbox' name='cats[]' value='".$res_not['catid']."'> ".stripslashes($res_not['name'])."<br>\n";	
  	        echo "<input type='hidden' name='submit'>";
  	      }
  	      echo "</form>";
  	      echo "<input  type='button' onclick='$(\"#cats\").dialog(\"destroy\")' value='Закрити'>";
  	       echo"<button  name='submitbutton' value='500' onclick=\" this.disabled=true;
  	       $('#myForm').ajaxSubmit(function(data){alert('Зміни збережено')}); ;\">Зберегти</button>";
  }
  else
  { 
   die("Немає доступу");
  }
?>
 


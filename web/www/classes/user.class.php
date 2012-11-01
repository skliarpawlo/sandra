<?php
  class user
  {
  	function user(){}
  	
  	function show_child_categories($catid)
  	{
  	  $catid = mysql_real_escape_string($catid);	
  	  
  	  $curr_cat = mysql_fetch_array(mysql_query("SELECT * FROM problem_categories WHERE catid='$catid'"));
  	  
  	  if($curr_cat)
  	  {
  	  	$path="<a href='categories.php?catid=".$curr_cat['catid']."'>".stripslashes($curr_cat['name'])."</a>";
  	    $tmp = $curr_cat;
  	    while($tmp['parentid']!=0)
  	    {
  	    	$q = "SELECT * FROM problem_categories WHERE catid=".$tmp['parentid'];
  	    	$tmp = mysql_fetch_array(mysql_query($q));
  	    	$path= "<a href='categories.php?catid=".$tmp['catid']."'>".stripslashes($tmp['name'])."</a>&nbsp&#8594&nbsp".$path;
  	    	
  	    }
  	    echo "<h2>".$path."</h2>";
  	  }
  	  
  	  
  	  $q = "SELECT * FROM problem_categories WHERE parentid = '$catid'";
  	  $go = mysql_query($q);

	  $res = mysql_fetch_array($go);
	  if($res)
	  {
  	    echo "<h2>Дочірні категорії</h2>";
  	    echo "<p><a href='categories.php?catid=".$res['catid']."'>".stripslashes($res['name'])."</a></p>";
		while(($res = mysql_fetch_array($go)))
	  	{
	  	  echo "<p><a href='categories.php?catid=".$res['catid']."'>".stripslashes($res['name'])."</a></p>";
   	    }
	  }
  	  
  	  $q = "SELECT * FROM problem_category_assign JOIN problems ON 
  	        problem_category_assign.pid=problems.pid WHERE catid = '$catid'";
  	        
  	   $go = mysql_query($q);
  	   $res = mysql_fetch_array($go);
  	   if($res)
  	   {
	  	   echo "<h2>Задачі</h2>";
	  	   echo "<p><a href = 'problems.php?pid=".$res['pid']."'>".stripslashes($res['title'])."</a></p>";
	  	   while(($res = mysql_fetch_array($go)))
	  	   {
	  	   		
	  	   	 echo "<p><a href = 'problems.php?pid=".$res['pid']."'>".stripslashes($res['title'])."</a></p>";
	  	   	
	  	   }     
  	   }
  	}
  	
  	function show_problem($pid)
  	{
  		$pid = mysql_real_escape_string($pid);
  		
  		$q = "SELECT * FROM problems WHERE pid = '$pid'";
  		
  		$res = mysql_fetch_array(mysql_query($q));
  		
  		echo "Назва: ".stripslashes($res['title'])."<br>".
  		     "Умова: <a href='get_condition.php?pid=".$res['pid']."'>Завантажити</a><br>".
  		     "Очікувана назва файлу-розв'язку: ".stripslashes($res['solutionfile'])."<br>".
  		     "Назва вхідного файлу: ". stripslashes($res['inputfile'])."<br>".
  		     "Назва вихідного файлу: ".stripslashes($res['outputfile'])."<br>".
  		     "Обмеження на виконання: ".stripslashes($res['timelimit'])."<br>";
  		
  		echo "<form action = '' method='post' enctype='multipart/form-data'>
  				Файл-розв'язок:<br>
		  		<input type='file' name='solution'><br>
		  		<input type='hidden' name='pid' value='".$res['pid']."'>
		  		<input type='submit' name='submit' value='Відправити'>
  			  </form>
  		";
  	}
  	
  	
  	function show_news()
  	{
  	  $num=3;//kolichestvo zapisey na stranice
  
	  if($_GET['p']==null)
	  {
	  	$p=0;
	  }
	  else
	  {
	  	$p=mysql_real_escape_string($_GET['p'])-1;
	  }
	  $q="Select count(*) FROM news";
	  $cnt=mysql_fetch_array(mysql_query($q));
	  $cnt=$cnt[0];
	  if($cnt!=0)
	  {
		$c=$num*$p;
	    if(($cnt%$num)==0)$pgcnt=$cnt/$num; else $pgcnt=(floor($cnt/$num)+1);
  			$pages="Сторінки<br>";
   			for($i=1; $i<=$pgcnt;$i++)
   			{
  				$pages.="<a href='news.php?p=$i'>$i</a> ";
  			}
  		echo "<center><h3>".$pages."</h3></center><br>";	
		$go=mysql_query("SELECT * FROM news ORDER BY time_date DESC LIMIT $c,$num");
		//$go=mysql_query("SELECT * FROM news LIMIT $c,$num");
  		while(($res=mysql_fetch_array($go))) 
  		{
  			echo "<div style='background-color:#dddddd;margin-right:20px; padding:10px;'>
  			        Дата: ".date("d F Y",$res['time_date']).
  			        " ".date("G:i:s",$res['time_date'])."<br>".
  				    "<h3>".stripslashes($res['title'])."</h3><br>".
  				    stripslashes($res['text']).
  				 "</div><br><br>"
  				;
  				
  		}
	    
  			
	 }else
	 {
		echo "Немає записів";
	 }
  	}
  	
  	function show_results()
  	{
  		$can=mysql_fetch_array(mysql_query("SELECT * FROM configs WHERE id=1"));
  		if($can['view_results']==1)
  		{
  			$num=5;//kolichestvo zapisey na stranice
  
		  if($_GET['p']==null)
		  {
		  	$p=0;
		  }
		  else
		  {
		  	$p=mysql_real_escape_string($_GET['p'])-1;
		  }
		  
		  $q="SELECT count(*) FROM solutions JOIN results ON solutions.sid=results.sid WHERE uid='".$_SESSION['uid']."'";
		  $cnt=mysql_fetch_array(mysql_query($q));
		  $cnt=$cnt[0];
		  
		  if($cnt!=0)
		  {
			$c=$num*$p;
		    if(($cnt%$num)==0)
		      $pgcnt=$cnt/$num; 
		    else $pgcnt=(floor($cnt/$num)+1);
		    
	  	    $pages="Сторінки<br>";
	   		for($i=1; $i<=$pgcnt;$i++)
	   		{
	  		  $pages.="<a href='results.php?p=$i'>$i</a> ";
	  		}
	  		echo "<center><h3>".$pages."</h3></center><br>";

	  		 echo "<h2>Ваші результати</h2>";	
  		     $go=mysql_query("SELECT * FROM solutions JOIN results ON solutions.sid=results.sid WHERE uid='".$_SESSION['uid']."' ORDER BY intime DESC LIMIT $c,$num");
	  		 while(($res=mysql_fetch_array($go)))
	  		 {
	  		 	$probl = mysql_fetch_array(mysql_query("SELECT * FROM problems WHERE pid='".$res['pid']."'"));
	  		 	echo "<div style='background-color:#dddddd;margin-right:20px; padding:10px;'>
	  		 	 		Задача:<br>
	  		 			<a href='problems.php?pid=".$probl['pid']."'>".$probl['title']."</a><br>
	  		 			Дата здачі: 
	  		 			".date("d F Y",$res['intime']).
	  			        " ".date("G:i:s",$res['intime'])."<br>
						<a href='getfile.php?file=solution&sid=".$res['sid']."'>Ваш розв'язок</a>&nbsp &nbsp
						<a href='getfile.php?file=log&sid=".$res['sid']."'>Лог тестування</a><br>
	  		 			Результат:<br>
	  		 			".$res['results']."
	  		 	      </div><br><br>
	  		 	";
	  		 }
		  }
		  else
		  {
		  	echo "Немає записів";
		  } 
  		}
  		else
  		{
  			echo "На жаль, зараз перегляд результатів заборонено!";
  		}
  	}
  	
  	function upload_solution($pid)
  	{
  	    $configs = mysql_fetch_array(mysql_query("SELECT * FROM configs WHERE id=1"));
  	    $pid = mysql_real_escape_string($pid);
  		$upl=stripslashes($configs['upload_dir'])."/solutions/";
  		if(!is_dir($upl))
  		{
  			mkdir($upl);
  		}
  	    $upl.=$_SESSION['name'];
  		if(!is_dir($upl))
  		{
  			mkdir($upl);
  		}
  		$upl.="/".$pid;
  		
  	    if(!is_dir($upl))
  		{
  			mkdir($upl);
  		}
  			if($_FILES['solution']['name']!=""&&!$_FILES['solution']['error'])
  			{
  			  $cnt = mysql_fetch_array(mysql_query("SELECT count(*) FROM solutions WHERE 
  			         uid='".$_SESSION['uid']."' AND pid='$pid'"));
  			  
  			  $cnt=$cnt[0];//количество решений, отправленных пользователем для данной задачи
  			  $number=$cnt+1;
  			  $name=$_FILES['solution']['name'];
  			  if(!is_dir($upl."/".$number))
  		      {
  			   mkdir($upl."/".$number);
  		      }	   
  				if(move_uploaded_file($_FILES['solution']['tmp_name'],$upl."/".$number."/".$name))
  				{
  					$filepath=$upl."/".$number."/".$name;
  					$filepath=mysql_real_escape_string($filepath);
  					mysql_query("INSERT INTO solutions (uid,pid,count,intime,filepath) 
  					VALUES ('".$_SESSION['uid']."','".$pid."','$number','".time()."','$filepath')");
  					echo "Файл успішно завантажено";
  					$sid = mysql_insert_id();
  					mysql_query("INSERT INTO need_test (sid) VALUES ('$sid')");
  				}
  				else 
  				{
  					echo "Помилка при завантаженні";
  				}
  			}else
  			{
  				echo "Ви не вибрали файл";
  			}
  		
  	}
  	
  }
?>
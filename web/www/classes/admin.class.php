<?php
//+-------------------------+
//|Класс для роботи з базою |
//|задач					|
//|							|
//|							|
//+-------------------------+
   class admin
   {
   	 function admin(){}
   	 
   	 function add_problem($title, $filepath, $comparor, $solutionfile, $inputfile, $outputfile, $timelimit)
   	 {
   	 	$title = mysql_real_escape_string($title);
   	 	$filepath = mysql_real_escape_string($filepath);
   	 	$comparor = mysql_real_escape_string($comparor);
   	 	$solutionfile = mysql_real_escape_string($solutionfile);
   	 	$inputfile = mysql_real_escape_string($inputfile);
   	 	$outputfile = mysql_real_escape_string($outputfile);
   	 	$timelimit = mysql_real_escape_string($timelimit);

   	 	$q = "INSERT INTO problems (title, filepath, comparor, solutionfile, inputfile, outputfile, timelimit)
   	 		VALUES ('$title', '$filepath', '$comparor','$solutionfile','$inputfile','$outputfile', '$timelimit')
   	 	";
   	 	mysql_query($q);	
   	   	 	
   	 }
   	 
   	 
   	 function assign_problem_category($catid, $pid)
   	 {
   	 	$catid = mysql_real_escape_string($catid);
   	 	$pid = mysql_real_escape_string($pid);
   	 	$q = "INSERT INTO problem_category_assign (catid, pid) VALUES ('$catid', '$pid')";
   	 	mysql_query($q);
   	 	
   	 }
   	 
   	 function add_category($name, $parentid = 0)
   	 {
   	 	$name = mysql_real_escape_string($name);
   	 	$parentid = mysql_real_escape_string($parentid);
   	 	$q = "INSERT INTO problem_categories (name, parentid) VALUES ('$name','$parentid')";
   	 	mysql_query($q);	
   	 }
   	 
   	 function add_compiler($extensions, $compilerpath, $params)
   	 {
   	 	$extensions = mysql_real_escape_string($extensions);
   	 	$compilerpath = mysql_real_escape_string($compilerpath);
   	 	$params = mysql_real_escape_string($params);
   	 	$q = "INSERT INTO compilers (extensions, compilerpath, params) 
   	 	       VALUES ('$extensions', '$compilerpath','$params')";
   	 	 mysql_query($q);      
   	 }
   	 
   	 function add_news($title, $text)
   	 {
   	 	$title = mysql_real_escape_string($title);
   	 	$text = mysql_real_escape_string($text);
   	 	$time =  time();
   	 	$q = "INSERT INTO news (title, text, time_date) VALUES ('$title','$text', '$time')";
   	 	mysql_query($q);
   	 }
   	 
   	 function add_test($pid)
   	 {
   	    $configs = mysql_fetch_array(mysql_query("SELECT * FROM configs WHERE id=1"));
  	    $pid = mysql_real_escape_string($pid);
  		$upl=stripslashes($configs['upload_dir'])."tests/".$pid;
  		if(!is_dir($upl))
  		{
  			
  			mkdir($upl);
  		}  		
  		if(!is_dir($upl."/input"))
  		{
  			
  			mkdir($upl."/input");
  		}
   	    if(!is_dir($upl."/output"))
  		{
  			
  			mkdir($upl."/output");
  		}

  		if($_FILES['test']['name']!=""&&!$_FILES['test']['error']&&
  		   $_FILES['ans']['name']!=""&&!$_FILES['ans']['error']
  		  )
  		{
  		        if(move_uploaded_file($_FILES['test']['tmp_name'],$upl."/input/".$_FILES['test']['name'])&&
  		           move_uploaded_file($_FILES['ans']['tmp_name'],$upl."/output/".$_FILES['ans']['name'])
  		          )
  				{
  					$num = mysql_fetch_array(mysql_query("SELECT MAX(num) FROM tests WHERE pid='".$pid."'"));
  					//print_r ($num);
  					$num=$num[0]+1;
  					 mysql_query("INSERT INTO tests (pid,num,testpath, anspath) VALUES
  					 ('$pid','$num','".mysql_real_escape_string($upl."/input/".$_FILES['test']['name'])."',
  					   '".mysql_real_escape_string($upl."/output/".$_FILES['ans']['name'])."'
  					 )
  					 ");
  					 return "Тести завантажено";
  					
  				}
  				else 
  				{
  				  return "Помилка при завантаженні";
  				} 
  		}
  		else 
  	    {
  		 return "Помилка при завантаженні";
  	    } 
  		
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
		$go=mysql_query("SELECT * FROM news LIMIT $c,$num");
  		while(($res=mysql_fetch_array($go)))
  		{
  			echo "<div style='background-color:#dddddd;margin-right:20px; padding:10px;'>
  			        Дата: ".date("d F Y",$res['time_date']).
  			        " ".date("G:i:s",$res['time_date'])."<br>".
  				    "<h3>".stripslashes($res['title'])."</h3><br>".
  				    stripslashes($res['text'])
  				 
  				;
  			echo "<center>
  				  <form action='' method='post' onsubmit='return confirm(\"Ви впевнені, що хочете видалити?\");'>
  			       <input type='hidden' name='id' value='".$res['id']."'>
  			       <button onclick='document.location=\"edit_news.php?id=".$res['id']."\";return false;'>Редагувати</button>
  			       <input type='submit' name='submit' value='Видалити'>
  				  </form>
  				  </center>
  				  </div><br><br>
  			";	
  				
  		}
	  }
	  else
	  {
	  	echo "Немає записів";
	  }
	   
   	 }
   	 
   	 function show_problems()
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
  				$pages.="<a href='problems.php?p=$i'>$i</a> ";
  			}
  		echo "<center><h3>".$pages."</h3></center><br>";	
		$go=mysql_query("SELECT * FROM problems LIMIT $c,$num"); 	
			while(($res=mysql_fetch_array($go)))
			{
			  echo "<div style='background-color:#dddddd;margin-right:20px; padding:10px;'>	
				Title: ".stripslashes($res['title'])."<br>
				
				Умова: <a href='../get_condition.php?pid=".$res['pid']."'>Завантажити</a><br>
					
				filepath: ".stripslashes($res['filepath'])."<br>
					
				comparor: ".stripslashes($res['comparor'])."<br>
					
				solutionfile: ".stripslashes($res['solutionfile'])."<br>
					
				inputfile: ".stripslashes($res['inputfile'])."<br>
					
				outputfile: ".stripslashes($res['outputfile'])."<br>
					
				timelimit: ".stripslashes($res['timelimit'])."<br>";
			  
				echo "";			  
			  
			  
			   echo "<center>
  				  <form action='' method='post' onsubmit='return confirm(\"Ви впевнені, що хочете видалити?\");'>
  			       <input type='hidden' name='pid' value='".$res['pid']."'>
  			       <button onclick='document.location=\"add_test.php?pid=".$res['pid']."\";return false;'>Додати тести</button>
  			       <button onclick='document.location=\"edit_problem.php?pid=".$res['pid']."\";return false;'>Редагувати</button>
  			       <input type='submit' name='submit' value='Видалити'>
  				  </form>
  				  </center>
  				  </div><br><br>	
  			";
			}
	  }
	  else
	  {
	  	echo "Немає записів";
	  }		
   	 }
   	 
   	 function show_categories()
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
	  $q="SELECT count(*) FROM problem_categories WHERE parentid<>0";
	  $cnt=mysql_fetch_array(mysql_query($q));
	  $cnt=$cnt[0];
	  if($cnt!=0)
	  {
		$c=$num*$p;
	    if(($cnt%$num)==0)$pgcnt=$cnt/$num; else $pgcnt=(floor($cnt/$num)+1);
  			$pages="Сторінки<br>";
   			for($i=1; $i<=$pgcnt;$i++)
   			{
  				$pages.="<a href='categories.php?p=$i'>$i</a> ";
  			}
  		echo "<center><h3>".$pages."</h3></center><br>";	
		
   	 	$go=mysql_query("SELECT * FROM problem_categories WHERE parentid<>0 LIMIT $c,$num");
			while(($res=mysql_fetch_array($go)))
			{
				$parent=mysql_fetch_array(mysql_query("SELECT * FROM problem_categories WHERE catid='".$res['parentid']."'"));
				echo "<div style='background-color:#dddddd;margin-right:20px; padding:10px;'>
				Назва: ".stripslashes($res['name'])."<br>".
				"Батьківська категорія: ".stripslashes($parent['name'])."<br>
				
				";
				;
				
				echo "<center>
  				  <form action='' method='post' onsubmit='return confirm(\"Ви впевнені, що хочете видалити?\");'>
  			       <input type='hidden' name='catid' value='".$res['catid']."'>
  			       <button onclick='document.location=\"edit_category.php?catid=".$res['catid']."\";return false;'>Редагувати</button>
  			       <input type='submit' name='submit' value='Видалити'>
  				  </form>
  				  </center>
  				  </div><br><br>	
  			";
			}
	  }
	  else 
	  {
	  	echo "Немає записів";
	  }	
   	 }
   	 
   	 function show_compilers()
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
	  $q="SELECT count(*) FROM compilers";
	  $cnt=mysql_fetch_array(mysql_query($q));
	  $cnt=$cnt[0];
	  if($cnt!=0)
	  {
		$c=$num*$p;
	    if(($cnt%$num)==0)$pgcnt=$cnt/$num; else $pgcnt=(floor($cnt/$num)+1);
  			$pages="Сторінки<br>";
   			for($i=1; $i<=$pgcnt;$i++)
   			{
  				$pages.="<a href='compilers.php?p=$i'>$i</a> ";
  			}
  		echo "<center><h3>".$pages."</h3></center><br>";	
		
   	 	
   	 	
   	 	
   	 	$go=mysql_query("SELECT * FROM compilers LIMIT $c,$num");
			while(($res=mysql_fetch_array($go)))
			{
				echo "<div style='background-color:#dddddd;margin-right:20px; padding:10px;'>
				  extensions: ".stripslashes($res['extensions'])."<br>
	
				  compilerpath: ".stripslashes($res['compilerpath'])."<br>
	
				  params: ".stripslashes($res['params'])."<br>
				
				";
				echo "<center>
  				  <form action='' method='post' onsubmit='return confirm(\"Ви впевнені, що хочете видалити?\");'>
  			       <input type='hidden' name='cpid' value='".$res['cpid']."'>
  			       <button onclick='document.location=\"edit_compiler.php?cpid=".$res['cpid']."\";return false;'>Редагувати</button>
  			       <input type='submit' name='submit' value='Видалити'>
  				  </form>
  				  </center>
  				  </div><br><br>
  			";
			}
	  }
	  else 
	  {
	    echo "Немає записів";	
	  }
   	 }
   	 
   	 function show_settings()
   	 {
   	   $res = mysql_fetch_array(mysql_query("SELECT * FROM configs WHERE id=1"));
   	   if($res['view_results']==1)
   	   {
   	    $yes="checked";
   	    $no="";
   	    
   	   }else
   	   {
   	     $yes="";
   	    $no="checked";
   	   }
   	   if($res['can_register']==1)
   	   {
   	    $yes_reg="checked";
   	    $no_reg="";
   	    
   	   }else
   	   {
   	     $yes_reg="";
   	    $no_reg="checked";
   	   }
   	   echo "<form action='' method='post'>
   	   			Директорія завантаження файлів на сервер <br>
   	    		<input type='text' size='50' name='upload_dir' value='".stripslashes($res['upload_dir'])."'><br>
   	    		Дозволено перегляд результатів<br>
   	    		Так
   	    		<input type='radio' name='view_results' value='1' $yes>
   	    		<input type='radio' name='view_results' value='0' $no >
   	    		Ні<br>
   	    		Дозволено реєстрацію<br>
   	    		Так
   	    		<input type='radio' name='can_register' value='1' $yes_reg>
   	    		<input type='radio' name='can_register' value='0' $no_reg >
   	    		Ні<br>
   	    		<input type='submit' name='submit' value='Зберегти'>
   	   		 </form>
   	   ";
   	 }
   	 
   	 function update_settings($new_upload_dir,$view_results, $can_register)
   	 {
   	   $new_upload_dir=mysql_real_escape_string($new_upload_dir);
   	   $view_results=mysql_real_escape_string($view_results);
   	   $can_register=mysql_real_escape_string($can_register);
   	   mysql_query("UPDATE configs SET upload_dir='$new_upload_dir', view_results='$view_results', 
   	   can_register='$can_register' WHERE id=1");
   	 
   	 }
   	 
   	 function update_news($id,$title,$text)
   	 {
   	 	$title = mysql_real_escape_string($title);
   	 	$text = mysql_real_escape_string($text);
   	 	$id = mysql_real_escape_string($id);
   	 	mysql_query("UPDATE news SET title='$title', text='$text' WHERE id='$id'");
   	 }
   	 
   	 function update_problem($pid,$title, $filepath, $comparor, $solutionfile, $inputfile, $outputfile, $timelimit)
   	 {
   	 	$pid=mysql_real_escape_string($pid);
   	 	$title = mysql_real_escape_string($title);
   	 	$filepath = mysql_real_escape_string($filepath);
   	 	$comparor = mysql_real_escape_string($comparor);
   	 	$solutionfile = mysql_real_escape_string($solutionfile);
   	 	$inputfile = mysql_real_escape_string($inputfile);
   	 	$outputfile = mysql_real_escape_string($outputfile);
   	 	$timelimit = mysql_real_escape_string($timelimit);

   	 	$q = "UPDATE problems SET title='$title', filepath='$filepath', comparor='$comparor', 
   	 	solutionfile='$solutionfile', inputfile='$inputfile', outputfile='$outputfile', timelimit='$timelimit'
   	 		WHERE pid='$pid'
   	 	";
   	 	mysql_query($q);	
   	 
   	 }
   	 
   	 function update_category($catid, $name, $parentid)
   	 {
   	 	mysql_query("UPDATE problem_categories SET name='".mysql_real_escape_string($name)."',
   	 	parentid='".mysql_real_escape_string($parentid)."' WHERE catid='".mysql_real_escape_string($catid)."'");
   	 }
   	 
   	 function update_compiler($cpid,$extensions, $compilerpath, $params)
   	 {
   	    $cpid=mysql_real_escape_string($cpid);
   	 	$extensions = mysql_real_escape_string($extensions);
   	 	$compilerpath = mysql_real_escape_string($compilerpath);
   	 	$params = mysql_real_escape_string($params);
   	 	mysql_query("UPDATE compilers SET extensions='$extensions', compilerpath='$compilerpath',
   	 	params='$params' WHERE cpid='$cpid'
   	 	");
   	 }
   	 
   	 
   	 
     function delete_news($id)
     {
     	mysql_query("DELETE FROM news WHERE id='".mysql_real_escape_string($id)."'");
     }
   	 
   	 function delete_problem($pid)
   	 {
   	 	$pid=mysql_real_escape_string($pid);
   	 	mysql_query("DELETE FROM problems WHERE pid='$pid'");
   	 	mysql_query("DELETE FROM problem_category_assign WHERE pid='$pid'");
   	 }
   	 
   	 function delete_category($catid)
   	 {
   	 	
   	 	$catid=mysql_real_escape_string($catid);
   	 	$childs=mysql_query("SELECT * FROM problem_categories WHERE parentid='$catid'");
   	 	while(($res=mysql_fetch_array($childs)))
   	 	{
   	 	 mysql_query("DELETE FROM problem_category_assign WHERE catid='".$res['catid']."'");  	
   	 	}
   	 	mysql_query("DELETE FROM problem_categories WHERE catid='$catid' OR parentid='$catid'");
   	 	mysql_query("DELETE FROM problem_category_assign WHERE catid='$catid'");
   	 }
   	 
   	 function delete_compiler($cpid)
   	 {
   	 	mysql_query("DELETE FROM compilers WHERE cpid='".mysql_real_escape_string($cpid)."'");
   	 	
   	 }
   	 
   	
   }
?>
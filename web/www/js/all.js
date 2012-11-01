function showLoginForm()
{
	$("#login").dialog({modal:true,title:"Sandra - Вхід",resizable:false});
} 



function checkRegForm()
{
 if(document.regform.login.value.length<=4)
 { 
	 alert("Логін не може бути коротшим 5 символів");
	 document.regform.login.focus();
	 return false;
 }
 else
 {
	 if(document.regform.name.value.length==0)
	 {
		 alert("Імя не може бути порожнім");
		 document.regform.name.focus();
		 return false;
	 }
	 else
	 {
		 if(document.regform.pass.value.length==0)
		 {
			 alert("Пароль не може бути порожнім");
			 document.regform.pass.focus();
			 return false;
		 }
		 else
		 {
			 if(document.regform.pass_repeat.value.length==0)
			 {
				 alert("Повторіть пароль");
				 document.regform.pass_repeat.focus();
				 return false;
				 
			 }else return true;
		 }
	 }
	 
 } 
	
}

function checkLogin(login)
{
	if(login.length>=5)
	{
      $("#loginfree").hide();
      $.post('ajax/checklogin.php', {'login': login}, function(data)
		     {
			  if (data[0] == '1')
			  {
			  $('#loginfree').show();
				$('#loginfree').html('<font color="red">Логін зайнятий</font>');
			  }
			  else
			  {
				$('#loginfree').show();
				$('#loginfree').html('<font color="green">Логін вільний</font>');
				
			  }
		     });
	}
}


function checkProblemForm()
{
	if((document.problemForm.title.value.length==0)||(document.problemForm.filepath.value.length==0)||
	   (document.problemForm.comparor.value.length==0)||(document.problemForm.solutionfile.value.length==0)||
	   (document.problemForm.inputfile.value.length==0)||(document.problemForm.outputfile.value.length==0)||
	   (document.problemForm.timelimit.value.length==0)
	  )
	{
		alert('Заповніть всі поля');
		return false;
	}
	else return true;
	
}

function checkCatForm()
{
	if(document.catForm['name'].value.length==0)
	{
		alert('Введіть назву категорії');
		return false;
		
	}else return true;
}

function checkCompilerForm()
{
	if((document.compilerForm.extensions.value.length==0)||(document.compilerForm.compilerpath.value.length==0))
	{
		alert('Заповніть необхідні поля');
		return false;
	}
	else return true;	
}



function getCats(pid)
{

  $.get('../admin/assign.php',{'pid':pid}, function (data)
		  {
	        $('#cats').html(data);
	        $('#cats').dialog({modal:true,title:"Додати задачі до категорій",resizable:true});
		  }
	    	  
  );	
}

function enable()
{
	document.submitbutton.disabled=false;
}


 
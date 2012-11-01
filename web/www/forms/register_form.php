<form name = 'regform' action = '' method = 'post' onsubmit="return checkRegForm();">
  Логін<br>
  <input type = 'text' name = 'login'  onchange="checkLogin(this.value)">
  <div id = 'loginfree'></div><br>
  Прізвище, ім'я та по-батькові<br>
  <input type = 'text' name = 'name'><br>
  Пароль<br>
  <input type = 'password' name = 'pass'><br>
  Повтор пароля<br>
  <input type = 'password' name = 'pass_repeat'><br><br>
  Код з картинки<br>
  <input type = "text" name = "capcha_code"><br><br>
  <input type = 'submit' name = 'submit' value = 'Реєстрація'>
</form>
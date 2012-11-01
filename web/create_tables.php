<?php
 include_once 'config/config_db.php';
 include_once 'head.php';
mysql_query( "CREATE TABLE users ( " . // таблица пользователей системы
"uid INT(11) NOT NULL AUTO_INCREMENT," . // код пользовател€
"name TEXT," . // им€ пользовател€
"password TEXT," . // пароль пользовател€
"fio TEXT," . // полное им€ пользовател€
"priority ENUM( 'user', 'admin' )," . // привилегии пользовател€
"PRIMARY KEY( uid )" .
")" );

mysql_query( "CREATE TABLE problems (" . // таблица задач
"pid INT(11) NOT NULL AUTO_INCREMENT," . // код задачи
"title TEXT," . // название задачи
"filepath TEXT," . // путь к файлу услови€
"comparor TEXT," . // путь к файлу сравнилки
"solutionfile TEXT," . // им€ файла решени€
"inputfile TEXT," . // им€ входного файла
"outputfile TEXT," . // им€ выходного файла
"timelimit INT," . // тайм-лимит задачи
"PRIMARY KEY( pid )" .
")" );

mysql_query( "CREATE TABLE problem_categories (" . // таблица категорий задач
"catid INT(11) NOT NULL AUTO_INCREMENT," . // код категории
"name TEXT," . // название категории
"parentid INT(11)," . // код родительской категории, 0 если нету предка 
"PRIMARY KEY( catid )" .
")" );

mysql_query( "CREATE TABLE problem_category_assign (" . // таблица св€зей задач с категори€ми
"assignid INT(11) NOT NULL AUTO_INCREMENT," . // код св€зи
"catid INT(11)," . // код категории
"pid INT(11)," . // код задачи
"PRIMARY KEY( assignid )" .
")" );

mysql_query( "CREATE TABLE solutions (" . // таблица решений
"sid INT(11) NOT NULL AUTO_INCREMENT," . // код решени€
"uid INT(11)," . // код пользовател€
"pid INT(11)," . // код задачи
"intime TEXT," . // врем€ сдачи
"filepath TEXT," . // путь к файлу решени€
"PRIMARY KEY( sid )" .
")" );

mysql_query( "CREATE TABLE need_test (" . // таблица решений посланых на проверку
"ntid INT(11) NOT NULL AUTO_INCREMENT," . // код решени€ посланного на проверку
"sid INT(11)," . // код решени€ в таблице solutions
"PRIMARY KEY( ntid )" .
")" );

mysql_query( "CREATE TABLE results (" . // таблица результатов решений
"rid INT(11) NOT NULL AUTO_INCREMENT," . // код результата
"sid INT(11)," . // код решени€
"results TEXT," . // результат
"PRIMARY KEY( rid )" .
")" );

mysql_query( "CREATE TABLE tests (" . // таблица тестов
"tid INT(11) NOT NULL AUTO_INCREMENT," . // код теста
"pid INT(11)," . // код задачи
"num INT(11)," . // номер теста
"testpath TEXT," . // путь ко входному файлу теста
"anspath TEXT," . // путь к файлу ответа
"PRIMARY KEY( tid )" .
")" );

mysql_query( "CREATE TABLE compilers (" . // таблица компил€торов
"cpid INT(11) NOT NULL AUTO_INCREMENT," . // код компил€тора
"extensions TEXT," . // список расширений св€занных с
// компил€тором через зап€тую ( dpr,pas, )
"compilerpath TEXT," . // путь к компил€тору
"params TEXT," . // параметры компил€ции
"PRIMARY KEY( cpid )" .
")" );

?>
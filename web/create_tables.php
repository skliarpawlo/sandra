<?php
 include_once 'config/config_db.php';
 include_once 'head.php';
mysql_query( "CREATE TABLE users ( " . // ������� ������������� �������
"uid INT(11) NOT NULL AUTO_INCREMENT," . // ��� ������������
"name TEXT," . // ��� ������������
"password TEXT," . // ������ ������������
"fio TEXT," . // ������ ��� ������������
"priority ENUM( 'user', 'admin' )," . // ���������� ������������
"PRIMARY KEY( uid )" .
")" );

mysql_query( "CREATE TABLE problems (" . // ������� �����
"pid INT(11) NOT NULL AUTO_INCREMENT," . // ��� ������
"title TEXT," . // �������� ������
"filepath TEXT," . // ���� � ����� �������
"comparor TEXT," . // ���� � ����� ���������
"solutionfile TEXT," . // ��� ����� �������
"inputfile TEXT," . // ��� �������� �����
"outputfile TEXT," . // ��� ��������� �����
"timelimit INT," . // ����-����� ������
"PRIMARY KEY( pid )" .
")" );

mysql_query( "CREATE TABLE problem_categories (" . // ������� ��������� �����
"catid INT(11) NOT NULL AUTO_INCREMENT," . // ��� ���������
"name TEXT," . // �������� ���������
"parentid INT(11)," . // ��� ������������ ���������, 0 ���� ���� ������ 
"PRIMARY KEY( catid )" .
")" );

mysql_query( "CREATE TABLE problem_category_assign (" . // ������� ������ ����� � �����������
"assignid INT(11) NOT NULL AUTO_INCREMENT," . // ��� �����
"catid INT(11)," . // ��� ���������
"pid INT(11)," . // ��� ������
"PRIMARY KEY( assignid )" .
")" );

mysql_query( "CREATE TABLE solutions (" . // ������� �������
"sid INT(11) NOT NULL AUTO_INCREMENT," . // ��� �������
"uid INT(11)," . // ��� ������������
"pid INT(11)," . // ��� ������
"intime TEXT," . // ����� �����
"filepath TEXT," . // ���� � ����� �������
"PRIMARY KEY( sid )" .
")" );

mysql_query( "CREATE TABLE need_test (" . // ������� ������� �������� �� ��������
"ntid INT(11) NOT NULL AUTO_INCREMENT," . // ��� ������� ���������� �� ��������
"sid INT(11)," . // ��� ������� � ������� solutions
"PRIMARY KEY( ntid )" .
")" );

mysql_query( "CREATE TABLE results (" . // ������� ����������� �������
"rid INT(11) NOT NULL AUTO_INCREMENT," . // ��� ����������
"sid INT(11)," . // ��� �������
"results TEXT," . // ���������
"PRIMARY KEY( rid )" .
")" );

mysql_query( "CREATE TABLE tests (" . // ������� ������
"tid INT(11) NOT NULL AUTO_INCREMENT," . // ��� �����
"pid INT(11)," . // ��� ������
"num INT(11)," . // ����� �����
"testpath TEXT," . // ���� �� �������� ����� �����
"anspath TEXT," . // ���� � ����� ������
"PRIMARY KEY( tid )" .
")" );

mysql_query( "CREATE TABLE compilers (" . // ������� ������������
"cpid INT(11) NOT NULL AUTO_INCREMENT," . // ��� �����������
"extensions TEXT," . // ������ ���������� ��������� �
// ������������ ����� ������� ( dpr,pas, )
"compilerpath TEXT," . // ���� � �����������
"params TEXT," . // ��������� ����������
"PRIMARY KEY( cpid )" .
")" );

?>
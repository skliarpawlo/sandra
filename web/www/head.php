<?
 
 include_once('config/config_db.php');
 $mysql=mysql_connect($host, $db_root, $db_pass);
 $select_db=mysql_select_db($db_name, $mysql);

?>
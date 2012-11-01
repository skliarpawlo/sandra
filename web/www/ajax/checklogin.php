<?php
 include_once '../head.php';
 include_once '../classes/login.register.class.php';
 $LOGINER = new login_register();
 if($LOGINER->check_name($_POST['login']))
 {
 	echo 1;
 }else echo 0;
?>
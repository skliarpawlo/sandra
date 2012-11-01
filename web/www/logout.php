<?php
session_start();
  include_once 'head.php';
 include_once 'classes/login.register.class.php';
 
 $LOGINER = new login_register();
 $LOGINER->logout();
 header("location: index.php");
?>
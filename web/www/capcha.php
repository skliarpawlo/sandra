<?php
session_start();

$r=rand(10000,99999);
$_SESSION['capcha']=$r;
for($i=0;$i < 5;$i++)//разбиваем секретный код на массив чисел
  $arr[$i]=substr($r,$i,1);

$im=imagecreate(130,40);//создаем картинку
imagecolorallocate($im,255,255,255);
$a=0;
$color_w=imagecolorallocate($im,255,255,255);
$color_g=imagecolorallocate($im,50,50,50);
for($i=0;$i < 5;$i++)//наносим код на картинку
  $arr_=imagettftext($im, 20, rand(-30,30), $a+=21, rand(17,33),$color_g,
	"CyrilicOld.ttf",$arr[$i]);

for($i=20;$i <=100;$i+=40)//ставим 3 защитных квадратика
  rev($im,$i,rand(10,30));
	
function rev(&$im,$x,$y)//функция реверсии цвета
{
global $color_w,$color_g;
$w=$h=rand(15,30);
for($i=$x-$w/2;$i < $x+$w/2;$i++)
  for($k=$y-$h/2;$k < $y+$h/2;$k++,$color_p=imagecolorat($im,$i,$k))
    if($color_p>$color_w)
       imagesetpixel($im,$i,$k,$color_w);
    else 
       imagesetpixel($im,$i,$k,$color_g);
}
header ("Content-type: image/png");
imagepng($im); //выводим капчу
imagedestroy($im);
?>
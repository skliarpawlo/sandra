uses Dos;

const
   checker = 'c:\SandraTmp\lines\comparer.exe';
   tmpFile = 'cmp.tmp';
   outFile = 'cmp.info';

var
   i : integer;
   params : string;

procedure callChecker( param : string );
begin
   Exec( getenv( 'COMSPEC' ), '/c ' + checker + ' ' + param + ' > ' + tmpFile );
end;

function checkParams() : string;
var
   params : string;
begin
   params := ParamStr( 1 );

   for i := 2 to Paramcount() do
   begin
      params := params + ' ' + ParamStr( i );
   end;

   checkParams := params;
end;

procedure reorganizeOutput();
var
   o1, o2 : text;
   ans : string;
begin

   assign( o1, tmpFile );reset( o1 );
   assign( o2, outFile );rewrite( o2 );

   { FOR LINES !!! And Menshikov }
   readln( o1, ans );
   if ( 'pe' = ans ) then
   begin
      writeln( o2, '-' );
   end else if ( 'a' = ans ) then
   begin
      writeln( o2, '+' );
   end else
   begin
      writeln( o2, '-' );
   end;

   writeln( o2, ans );
   { FOR LINES !!! And Menshikov }

   close( o1 );
   close( o2 );

end;

begin

   params := '';{checkParams();}

   callChecker( params );

   reorganizeOutput(  );

end.

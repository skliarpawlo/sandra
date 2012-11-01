package utils;

import java.util.ArrayList;

/**
 * Класс операций со строками
 * @author Pawlo
 */
public class StringOperator
{

    private static String sep = "::";

    /**
     * Возвращает разделитель в файле дампе бд
     * @return строка из двух символов
     */
    public static String separator()
    {
        return sep;
    }

    /**
     * Функция разделяет строку на массив строк по заданному разделителю
     * @param s
     * @return
     */
    public static String[] split( String s )
    {
        ArrayList< String > a = new ArrayList< String >();

        int j = 0;
        String cur = "";
        while ( j + 1 < s.length() )
        {
            if ( s.charAt( j ) == sep.charAt( 0 ) && s.charAt( j + 1 ) == sep.charAt( 1 ) )
            {
                a.add( cur );
                j += 2;
                cur = "";
            }
            else
            {
                cur = cur.concat( Character.toString( s.charAt( j ) ) );
                j++;
            }
        }


        String[] ans = new String[ a.size() ];
        for ( int i = 0; i < a.size(); i++ ) { ans[ i ] = a.get( i ); }

        return ans;
    }

    /**
     * Функция создает строку елементов массива перечисленых через запятую
     * @param a массив
     * @param k начальный индекс
     * @return
     */
    public static String comas( String[] a, int k )
    {
        String res = a[ k ];

        for ( int i = k + 1; i < a.length; i++ )
        {
            res = res.concat( "," + a[ i ] );
        }

        return res;
    }


}

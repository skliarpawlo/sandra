package testing;

import java.util.ArrayList;

/**
 * Класс представляющий результат тестирования
 *
 * @author pawlo
 */
public class Result
{

    /**
     * массив с результатами
     */
    private ArrayList< ArrayList< String > > res;

    /**
     * конструктор для создания обычного результата
     */
    public Result()
    {
        res = new ArrayList< ArrayList< String > >();
    }

    /**
     * конструктор для создания результата с заданым количеством тестов
     *
     * @param amount количество тестов
     */
    public Result( int amount )
    {
        res = new ArrayList< ArrayList< String > >( amount );
    }

    /**
     * размер ответа - количество тестов.
     *
     * @return размер ответа
     */
    public int size()
    {
        return res.size();
    }

    /**
     * Добавление нового результата теста в конец
     *
     * @param x результат теста
     */
    public void put( ArrayList< String > x )
    {
        res.add( x );
    }

    /**
     * Задает результат теста с заданным номером
     *
     * @param i номер теста
     * @param x результат теста
     */
    public void put( int i, ArrayList< String > x )
    {
        res.set( i, x );
    }

    /**
     * возвращает занчение заданого теста
     *
     * @param i номер теста
     * @return результат теста
     */
    public ArrayList< String > get( int i )
    {
        return res.get( i );
    }
    
}

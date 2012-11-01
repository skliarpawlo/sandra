package structures;

import java.io.Serializable;

/**
 * Ячейка в таблице задач
 * @author pawlo
 */
public class ProblemListItem implements Serializable
{

    /**
     * Представление в талице или списке
     */
    private String name;

    /**
     * Код задачи
     */
    private Integer id;

    /**
     * Конструктор создающий елемент по имени и коду задачи
     *
     * @param _name имя
     * @param _id код решения
     */
    public ProblemListItem( String _name, int _id )
    {
        name = _name;
        id = _id;
    }

    /**
     * Возвращает представление в таблице или списке
     * @return строка
     */
    @Override
    public String toString()
    {
        return name;
    }

    /**
     * Возвращает код хранимой задачи
     * @return код задачи
     */
    public Integer getId()
    {
        return id;
    }

}

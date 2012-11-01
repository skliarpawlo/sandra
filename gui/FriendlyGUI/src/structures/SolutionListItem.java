package structures;

/**
 * Класс представляющий решение в таблице
 * @author pawlo
 */
public class SolutionListItem
{

    /**
     * Представление в талице или списке
     */
    private String name;
    
    /**
     * Код решения
     */
    private Integer id;

    /**
     * Конструктор создающий елемент по имени и коду решения
     *
     * @param _name имя
     * @param _id код решения
     */
    public SolutionListItem( String _name, Integer _id )
    {
        id = _id;
        name = _name;
    }

    /**
     * Возвращает код хранимого решения
     * @return код решения
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * Возвращает представление в таблице или списке
     * @return представление в виде строки
     */
    @Override
    public String toString()
    {
        return name;
    }

}

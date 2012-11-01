package structures;

/**
 * Класс представляющий пользователя в списке
 *
 * @author Pawlo
 */
public class UserListItem
{
    /**
     * Представление в талице или списке
     */
    private String name;

    /**
     * Код пользователя
     */
    private Integer id;

    /**
     * Конструктор создающий елемент по имени и коду решения
     *
     * @param _name имя
     * @param _id код пользователя
     */
    public UserListItem( String _name, Integer _id )
    {
        id = _id;
        name = _name;
    }

    /**
     * Возвращает код хранимого пользователя
     * @return код пользователя
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

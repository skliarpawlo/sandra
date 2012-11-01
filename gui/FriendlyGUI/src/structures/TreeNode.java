package structures;

/**
 * Класс представляющий узел дерева. хранит кроме названия, также информацию о
 * представляемой им категории
 *
 * @author Pawlo
 */
public class TreeNode
{

    /**
     * имя узла
     */
    private String name;

    /**
     * код категории
     */
    private int id;

    /**
     * Конструктор которому передается название и код для узла
     *
     * @param _name имя
     * @param _id код
     */
    public TreeNode( String _name, int _id )
    {
        name = _name;
        id = _id;
    }

    /**
     * Переопределяемый метод для возвращения имени
     *
     * @return представлние в виде строки
     */
    @Override
    public String toString()
    {
        return name;
    }

    /**
     * Возвращает код категории
     *
     * @return код категории
     */
    public int getId()
    {
        return id;
    }
    
}

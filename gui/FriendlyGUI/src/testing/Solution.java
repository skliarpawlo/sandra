package testing;

import java.io.File;

/**
 * Класс представляющий решение задачи
 *
 * @author pawlo
 */
public class Solution
{

    /**
     * exe-файл решения
     */
    private File source;

    /**
     * задача для которой данный клас представляет решение
     */
    private Problem problem;

    /**
     * Конструктор решения по файлу решения и задаче
     *
     * @param _source файл решения
     * @param _problem объект задачи
     */
    public Solution( File _source,  Problem _problem )
    {
        source = _source;
        problem = _problem;
    }

    /**
     * Возвращает полный путь к решению ( ехе-файлу )
     * 
     * @return полный путь к решению ( ехе-файлу )
     */
    public String getSource()
    {
        return source.getPath();
    }

    /**
     * Возвращает путь к папке с решением
     *
     * @return путь к папке с решением
     */
    public String getSourceDir()
    {
        return source.getParent();
    }

    /**
     * Возвращает имя ехе-файла решения
     *
     * @return имя ехе-файла решения
     */
    public String getSourceName()
    {
        return source.getName();
    }

    /**
     * Возвращает задачу для которой хранит решение данный обьект
     *
     * @return задача для которой хранит решение данный обьект
     */
    public Problem getProblem()
    {
        return problem;
    }
    
}

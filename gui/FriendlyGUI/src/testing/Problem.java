package testing;

//import database.DataBaseOperator;
import entities.Problems;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import main.ErrorMessage;

/**
 * Класс представляющий задачу
 *
 * @author pawlo
 */
public class Problem
{

    /**
     * код (номер) задачи
     */
    private int problemId;

    /**
     * название задачи
     */
    private String title;

    /**
     * путь к файлу уловия ( .doc, .rtf, и т.п. )
     */
    private String filePath;

    /**
     * путь к сравнилке для данной задаче
     */
    private String comparor;

    /**
     * входной файл
     */
    private String inputFile;

    /**
     * выходной файл
     */
    private String outputFile;

    /**
     * временное ограничение для задачи
     */
    private long timeLimit;

    /**
     * Конструктор создающий задачу по ее номеру
     *
     * @param _problemId номер задачи в базе данных
    */
    public Problem( int _problemId )
    {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory( "FriendlyGUIPU" );
        EntityManager em = emf.createEntityManager();

        problemId = _problemId;

        try
        {

            Query query = em.createQuery( "SELECT obj FROM Problems obj WHERE obj.pid=" + problemId );
            Problems res = ( Problems ) query.getSingleResult();

            title = res.getTitle();
            filePath = res.getFilepath();
            comparor = res.getComparor();
            inputFile = res.getInputfile();
            outputFile = res.getOutputfile();
            timeLimit = res.getTimelimit();

        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка при тестировании : " + e.toString() );
        }
    }

    /**
     * Возвращает номер задачи в базе данных
     *
     * @return номер задачи в базе данных
     */
    public int getProlemId()
    {
        return problemId;
    }

    /**
     * возвращает назвение задачи
     *
     * @return название задачи
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Возвращает путь к ехе-файлу сравнилки ответов
     *
     * @return путь к ехе-файлу сравнилки ответов
     */
    public String getComparor()
    {
        return comparor;
    }

    /**
     * Возвращает имя входного файла для даной задачи
     *
     * @return имя входного файла для даной задачи
     */
    public String getInputFile()
    {
        return inputFile;
    }
    
    /**
     * Возвращает имя выходного файла для даной задачи
     *
     * @return имя выходного файла для даной задачи
     */
    public String getOutputFile()
    {
        return outputFile;
    }

    /**
     * Возвращает временное ограничение даной задачи
     * 
     * @return временное ограничение даной задачи
     */
    public long getTimeLimit()
    {
        return timeLimit;
    }

}

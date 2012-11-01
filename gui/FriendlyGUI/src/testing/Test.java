package testing;

//import database.DataBaseOperator;
import entities.Tests;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Класс представляющий собой тест для задачи
 *
 * @author pawlo
 */
public class Test
{

    /**
     * входной файл теста
     */
    private File input;

    /**
     * файл ответа на тест
     */
    private File output;

    /**
     * Конструктор создания теста по входному файлу и ответу
     *
     * @param _input входной файл
     * @param _output файл верного ответа
     */
    public Test( File _input, File _output )
    {
        input = _input;
        output = _output;
    }

    /**
     * Возвращает входной файл
     *
     * @return возвращает входной файл
     */
    public File getInputFile()
    {
        return input;
    }

    /**
     * Возвращает файл ответа
     *
     * @return файл ответа
     */
    public File getOutputFile()
    {
        return output;
    }

    /**
     * Возвращает набор тестов для заданой задачи.
     * если такой задачи нету или тестов к ней нету в базе возвращается
     * пустой список
     *
     * @param id код задачи
     * @return набор тестов для заданой задачи
     */
    public static ArrayList< Test > getTestsByProblemId( int id )
    {
        try
        {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory( "FriendlyGUIPU" );
            EntityManager em = emf.createEntityManager();

            Query query = em.createQuery( "SELECT obj FROM Tests obj WHERE obj.pid=" + id + " ORDER BY obj.num ASC" );

            List testSet = query.getResultList();
            
            ArrayList< Test > res = new ArrayList< Test >();

            for ( int i = 0; i < testSet.size(); i++ )
            {
                res.add( new Test( new File( ( ( Tests ) testSet.get( i ) ).getTestpath() ),
                                   new File( ( ( Tests ) testSet.get( i ) ).getAnspath() ) ) );
            }

            return res;
        }
        catch ( Exception e )
        {
            return null;
        }
    }

}

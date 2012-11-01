package structures;

import entities.Problems;
import java.util.List;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import utils.Paths;

/**
 * Класс операций с таблицами
 *
 * @author pawlo
 */
public class Tables
{

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory( "FriendlyGUIPU" );
    private static EntityManager em = emf.createEntityManager();


    /**
     * Возвращает список всех задач
     *
     * @return список всех задач
     */
    public static ProblemListItem[][] getAllProblemsList()
    {
        Query query = em.createQuery( "SELECT obj FROM Problems obj" );
        List res = query.getResultList();

        ProblemListItem[][] list = new ProblemListItem[ res.size() ][];

        for ( int i = 0; i < res.size(); i++ ) 
        {
            list[ i ] = new ProblemListItem[ 7 ];

            list[ i ][ 0 ] = new ProblemListItem( ( ( Problems ) res.get( i ) ).getTitle(), ( ( Problems ) res.get( i ) ).getPid() );
            list[ i ][ 1 ] = new ProblemListItem( Paths.deconv( ( ( Problems ) res.get( i ) ).getFilepath() ), ( ( Problems ) res.get( i ) ).getPid() );
            list[ i ][ 2 ] = new ProblemListItem( Paths.deconv( ( ( Problems ) res.get( i ) ).getComparor() ), ( ( Problems ) res.get( i ) ).getPid() );
            list[ i ][ 3 ] = new ProblemListItem( ( ( Problems ) res.get( i ) ).getSolutionfile(), ( ( Problems ) res.get( i ) ).getPid() );
            list[ i ][ 4 ] = new ProblemListItem( ( ( Problems ) res.get( i ) ).getInputfile(), ( ( Problems ) res.get( i ) ).getPid() );
            list[ i ][ 5 ] = new ProblemListItem( ( ( Problems ) res.get( i ) ).getOutputfile(), ( ( Problems ) res.get( i ) ).getPid() );
            list[ i ][ 6 ] = new ProblemListItem( ( ( Problems ) res.get( i ) ).getTimelimit().toString(), ( ( Problems ) res.get( i ) ).getPid() );
        }

        return list;
    }

    public static Problems getProblemById( Integer id )
    {
        try
        {
            Query query = em.createQuery( "SELECT obj FROM Problems obj WHERE obj.pid=" + id );
            Problems res = ( Problems ) query.getSingleResult();
            return res;
        }
        catch ( Exception e )
        {
            return null;
        }
    }

}

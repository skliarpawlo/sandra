package structures;

//import database.DataBaseOperator;
import entities.ProblemCategories;
import entities.Problems;
import entities.Solutions;
import entities.Users;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.tree.DefaultMutableTreeNode;
import utils.Paths;

/**
 * Класс для работы с древовидными струтурами
 *
 * @author Pawlo
 */
public class Trees
{

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory( "FriendlyGUIPU" );
    private static EntityManager em = emf.createEntityManager();

    /**
     * Создает по базе данных дерево категорий задач
     *
     * @param id код корня
     * @return корень созданного дерева
     */
    public static DefaultMutableTreeNode makeProblemTree( int id )
    {
        try
        {

            Query query = em.createQuery( "SELECT obj FROM ProblemCategories obj WHERE obj.catid=" + id );
            
            ProblemCategories cur = ( ProblemCategories ) query.getSingleResult();

            DefaultMutableTreeNode node = new DefaultMutableTreeNode();

            node.setUserObject( new TreeNode( cur.getName(), cur.getCatid() ) );

            Query subquery = em.createQuery( "SELECT obj FROM ProblemCategories obj WHERE obj.parentid=" + id );

            List kids = subquery.getResultList();

            for ( int i = 0; i < kids.size(); i++ )
            {
                DefaultMutableTreeNode x = makeProblemTree( ( ( ProblemCategories ) kids.get( i ) ).getCatid() );
                if ( x != null ) node.add( x );
            }

            return node;
        }
        catch ( Exception e )
        {
            return null;
        }
    }

    /**
     * Удаляет из базы и из дерева всю ветку с корнем с заданым кодом
     *
     * @param cur узел дерева с которого начинается поиск удляемого поддерева
     * @param idToDell код корня удаляемого поддерева
     * @param remove вспомагательный флаг задавайте false
     */
    public static void removeProblemSubtree( DefaultMutableTreeNode cur, int idToDell, boolean remove )
    {
        TreeNode node = ( TreeNode ) cur.getUserObject();

        for ( int i = 0; i < cur.getChildCount(); i++ )
        {
            removeProblemSubtree( ( DefaultMutableTreeNode ) cur.getChildAt( i ), idToDell, node.getId() == idToDell );
        }

        if ( ( node.getId() == idToDell ) || remove )
        {
            em.getTransaction().begin();

            Query query = em.createQuery( "DELETE FROM ProblemCategories obj WHERE obj.catid=" + node.getId() );
            query.executeUpdate();

            em.getTransaction().commit();

            cur.removeFromParent();
        }
    }

    /**
     * Добавляет решения из папки solsfile и ее подпапок. Флаг isDozd ознает яклются ли добавляемые решения досдачей.
     *
     * @param solsfile папка с решениями
     * @param isDozd флаг досдачи
     * @return
     */
    public static ArrayList< Solutions > getAllSolutionsInFolder( File solsfile, boolean isDozd )
    {
        ArrayList< Solutions > res = new ArrayList< Solutions >();

        if ( !solsfile.exists() ) return res;

        if ( solsfile.isFile() )
        {
            String parentdir = solsfile.getParentFile().getName();

//            System.out.println( parentdir );
            
            List user = em.createQuery( "SELECT obj FROM Users obj WHERE obj.name='" + parentdir + "'" ).getResultList();

            if ( user.size() == 1 )
            {
                Users curUser = ( Users ) user.get( 0 );

                String prob = solsfile.getName();
                String problemName = prob.substring( 0, prob.lastIndexOf( "." ) );
                String ext = prob.substring( prob.lastIndexOf( "." ) + 1 );

//                System.out.println( ext );
//                System.out.println( ">>" + problemName );

                List probs = em.createQuery( "SELECT obj FROM Problems obj WHERE obj.solutionfile='" + problemName + "'" ).getResultList();
                List compiler = em.createQuery( "SELECT obj FROM Compilers obj WHERE obj.extensions LIKE '%" + ext + "%'" ).getResultList();

                if ( probs.size() == 1 && compiler.size() == 1 )
                {
                    Problems curProblem = ( Problems ) probs.get( 0 );

                    List count = em.createQuery( "SELECT obj FROM Solutions obj WHERE obj.uid=" + curUser.getUid() + " AND obj.pid=" + curProblem.getPid() ).getResultList();

                    // проверяем нету ли в базе такого решения
                    List already = em.createQuery( "SELECT obj FROM Solutions obj WHERE obj.filepath='" + Paths.conv( solsfile.getAbsolutePath() ) + "'" ).getResultList();
                    //

                    if ( already.isEmpty() )
                    {
                        Solutions sol = new Solutions( curUser.getUid(), curProblem.getPid(), String.valueOf( new Date().getTime() / 1000 ), Paths.conv( solsfile.getAbsolutePath() ), count.size(), isDozd );
                        res.add( sol );
                    }
                }


            }
        }
        else // если папка
        {
            // берем все папки в указанной
            String[] subs = solsfile.list();

            // идем по всем файлам в папке
            for ( int i = 0; i < subs.length; i++ )
            {
                ArrayList< Solutions > curres = getAllSolutionsInFolder( new File( solsfile, subs[ i ] ), isDozd );
                res.addAll( curres );
            }

        }
        
        return res;
    }

}

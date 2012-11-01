package testing;

//import database.DataBaseOperator;
import java.io.File;
import java.util.ArrayList;
import compiling.Compiler;
import entities.NeedTest;
import entities.Results;
import entities.Solutions;
import java.io.IOException;
import java.util.List;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import main.ErrorMessage;
import main.MainForm;
import utils.Paths;

/**
 * Поток для онлайн тестирования. В бесконечном цикле он читает из
 * таблици onlinetest базы данных новые поступления одно за другим
 * и поочередно проверяет их через обьект тестирования Tester, и
 * занося результаты проверки в таблицу onlineresult
 *
 * @author pawlo
 */
public class OnlineTesterThread extends Thread
{

    private JTextPane txtLog;
    private JProgressBar totalProgress;
    private JProgressBar progress;

    public OnlineTesterThread( JTextPane _txtLog, JProgressBar _totalProgress, JProgressBar _progress )
    {
        txtLog = _txtLog;
        progress = _progress;
        totalProgress = _totalProgress;
    }

    /**
     * При необходимости компилирует переданный исходник и возвращает путь к ехе файлу результату.
     *
     * @param file путь к файлу который возможно необходимо скомпилировать.
     * @return путь к ехе-файлу
     */
    public String prepeare( String file ) throws Exception
    {
        String ext = file.substring( file.lastIndexOf( "." ) + 1 );
        String part = file.substring( 0, file.lastIndexOf( "." ) + 1 );

        //System.out.println( ext );
        //System.out.println( part );

        try
        {
            Compiler.compile( new File( file ) );
                
            MainForm.insertText( txtLog, " [ OK ]" + "\n", MainForm.OK );
            MainForm.insertText( txtLog, "Компиляция файла " + file, MainForm.INFO );
        }
        catch ( Exception e )
        {
            MainForm.insertText( txtLog, " [ ОШИБКА ]" + "\n", MainForm.ALARM );
            MainForm.insertText( txtLog, "Компиляция файла " + file, MainForm.INFO );
                
            throw new IOException( "Ошибка компиляции" );
        }

        MainForm.insertText( txtLog, " [ OK ]" + "\n", MainForm.OK );
        MainForm.insertText(txtLog, "Тестирование файла " + part + "exe", MainForm.INFO );

        return part + "exe";
    }

    /**
     * Основной метод класса потока. В нем в безсконечном цикле
     * формируются решения и тесты из таблици после чего отправляются на проверку
     * ( класс Tester ) после окончания проверки результат записывается в базу
     * данных
     */
    @Override
    public void run()
    {

        MainForm.insertText( txtLog, "Тестирующий поток запущен" + "\n", MainForm.NOT_SIGN );

        EntityManagerFactory emf = Persistence.createEntityManagerFactory( "FriendlyGUIPU" );
        EntityManager em = emf.createEntityManager();

        while ( !Thread.currentThread().isInterrupted() )
        {
            try
            {

                Query curSolQuery = em.createQuery( "SELECT obj FROM NeedTest obj" );
                List curSols = ( List ) curSolQuery.getResultList();


                for ( int j = 0; j < curSols.size(); j++ )
                {

                    NeedTest curSol = ( NeedTest ) curSols.get( j );

                    MainForm.insertText( txtLog, "Тестирование началось" + "\n", MainForm.NOT_SIGN );

                    Query curProQuery = em.createQuery( "SELECT obj FROM Solutions obj WHERE obj.sid=" + curSol.getSid() );
                    Solutions curPro = ( Solutions ) curProQuery.getSingleResult();

                    Result rs = null;
                    Solution sol = null;

                    try
                    {
                        sol = new Solution( new File( prepeare( Paths.deconv( curPro.getFilepath() ) ) ),
                                            new Problem( curPro.getPid() ) );

                        ArrayList< Test > tes = Test.getTestsByProblemId( sol.getProblem().getProlemId() );

                        Tester tester = new Tester( sol, tes, progress, txtLog );

                        tester.startTest();

                        rs = tester.getResult();

                    }
                    catch ( Exception e )
                    {
                        int ansize = Test.getTestsByProblemId( curPro.getPid() ).size();
                        rs = new Result( ansize );

                        for ( int i = 0; i < ansize; i++ )
                        {
                            ArrayList< String > tmp = new ArrayList< String >();
                            tmp.add( "c" );
                            rs.put( tmp );
                        }
                        
                        //System.out.println( "test exception : " + e.toString() );
                        ErrorMessage.error( "Ошибка при тестировании : " + e.toString() + " ::: " + rs.size() );

                    }
                        
                    String curres = "";

                    for ( int i = 0; i < rs.size(); i++ )
                    {
                        curres = curres + rs.get( i ).get( 0 );
                    }

                    em.getTransaction().begin();
                    em.createQuery( "DELETE FROM NeedTest obj WHERE obj.ntid=" + curSol.getNtid() ).executeUpdate();
                    em.createQuery( "DELETE FROM Results obj WHERE obj.sid=" + curSol.getSid() ).executeUpdate();
                    em.persist( new Results( curSol.getSid(), curres ) );
                    em.getTransaction().commit();

                    MainForm.insertText( txtLog, "Тестирование завершено" + "\n", MainForm.NOT_SIGN );

                    //////////

                    final int finalj = j;
                    final List finalCurSols = curSols;

                    java.awt.EventQueue.invokeLater( new Runnable()
                    {
                        public void run()
                        {
                            totalProgress.setValue( Math.round( ( float ) ( 100.0 * ( ( finalj + 1.0 ) / finalCurSols.size() ) ) ) );
                        }
                    });

                    //////////

                }

                //////////////
                //////////////
                java.awt.EventQueue.invokeLater( new Runnable()
                {
                    public void run()
                    {
                        progress.setValue( 0 );
                        totalProgress.setValue( 0 );
                    }
                });
                ///////////////
                ///////////////

            }
            catch ( Exception e )
            {

                try
                {
                    em.getTransaction().rollback();
                }
                catch ( Exception ex )
                {
                    ErrorMessage.error( "Ошибка отката : " + ex.toString() );
                }

                ErrorMessage.error( "Ошибка тестирвоания : " + e.toString() );
                
                try 
                {
                    Thread.sleep( 1500 );
                }
                catch ( Exception ex )
                {
                    ErrorMessage.error( "Ошибка тестирования (прерывание) : " + ex.toString() );
                }
            }
        }

    }

}

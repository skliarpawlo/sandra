package compiling;

import entities.Compilers;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import main.ErrorMessage;

/**
 * Класс для компиляции заданой программы. Его функцией является
 * определение компилятора по расширению файла, компиляция и
 * возврат, скомпилированого ехе-файла
 *
 * @author pawlo
 */
public class Compiler
{

    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory( "FriendlyGUIPU" );
    public static EntityManager em = emf.createEntityManager();

    /**
     * Принимает файл который необходимо скомпилировать. Компилирует его в
     * соответствии с расширением и связанным сним компилятором в базе данных.
     * Результирующий ехе-файл записывается в той же директории что и заданый файл
     * исходник.
     * 
     * @param source
     * @throws ClassNotFoundException проблема с драйвером базы данных
     * @throws SQLException проблема с операциями в базе данных
     * @throws IOException проблема с операциями ввода вывода файлов
     * @throws InterruptedException проблема с процесом компиляции
     */
    public static void compile( File source ) throws ClassNotFoundException, IOException, InterruptedException
    {
        
        if ( !source.exists() ) { throw new IOException( "Файл исходного кода не существует" ); }
        
        // извлечение иформации из имени файла для определения компилятора
        String fileName = source.getName();
        String ext = fileName.substring( fileName.lastIndexOf( "." ) + 1 );

        //удаление ехе файла если он есть
        String tmp = source.getAbsolutePath();
        tmp = tmp.substring( 0, tmp.lastIndexOf( "." ) ) + ".exe";
        File exefile = new File( tmp );
        if ( exefile.exists() ) utils.FileDelete.delete( tmp );

        // запрос на поиск компилятора
        Query query = em.createQuery( "SELECT obj FROM Compilers obj WHERE obj.extensions LIKE '%" + ext + "%'" );
        List res = query.getResultList();
        
        if ( res.isEmpty() ) { throw new IOException( "В базе данных не найден копмилатор для данного расширения" ); }
        
        String compilerPath = ( ( Compilers ) res.get( 0 ) ).getCompilerpath();

        String params = ( ( Compilers ) res.get( 0 ) ).getParams();
        tmp = source.getName();
        tmp = tmp.substring( 0, tmp.lastIndexOf( "." ) );
        params = params.replaceAll( "file_exe", tmp + ".exe" );
        //ErrorMessage.error( "FILE = " + params );

        StringTokenizer token = new StringTokenizer( params );
        String[] paramsArr = new String[ token.countTokens() + 2 ];

        int top = 0;

        paramsArr[ top++ ] = compilerPath;

        while ( token.hasMoreTokens() )
        {
            paramsArr[ top++ ] = token.nextToken();
        }
        
        String dir = source.getParent();
        paramsArr[ top++ ] = dir + File.separator + fileName;

        // Создание процесса для компиляции файла
        Runtime rt = Runtime.getRuntime();

        //String[] compileArgs = { compilerPath, dir + File.separator + fileName };

        final Process compiling = rt.exec( paramsArr, null, new File( dir ) );

        Thread timmer = new Thread( new Runnable() {
            public void run() {
                try
                {
                    Thread.sleep( 10000 );
                    compiling.destroy();
                }
                catch ( Exception e )
                {
                }
            }
        });

        timmer.start();

        compiling.waitFor();

        if ( !exefile.exists() ) throw new IOException( "Ошибка компиляции" );
        
    }

}

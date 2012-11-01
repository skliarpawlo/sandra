package testing;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Отдельный поток для одного теста. Нужен для того чтобы можно было
 * Не ожидать полного тайм лимита
 *
 * @author pawlo
 */
public class OneTest extends Thread
{

    /**
     * имя файла программы
     */
    private String program;

    /**
     * директория с программой
     */
    private File dir;

    /**
     * вызывающий поток. Нужен для вызова метода по зварешении
     */
    private TestThread tt;

    /**
     * тайм-лимит на тест
     */
    private long time;

    /**
     * Конструктор. принимает в качестве параметров ссылку на вызывающий обьект,
     * имя и директорию запускаемой программы
     *
     * @param _tt
     * @param _program
     * @param _dir
     */
    public OneTest( TestThread _tt, String _program, File _dir, long _time )
    {
        program = _program;
        dir = _dir;
        tt = _tt;
        time = _time;
    }

    /**
     * метод который запускается при ставрте потока. запускает проверяемую программу,
     * ждет завершения работы, вызывает метод обьекта TestThread означающую завершение
     * работы проверяемой программы.
     */
    @Override
    public void run()
    {
        try
        {

            Runtime rt = Runtime.getRuntime();
    
            String[] params = { "utils" + File.separator + "timecheck.exe", dir.getAbsolutePath() + File.separator + program, "" + time };

            Process process = rt.exec( params, null, dir );

            Scanner sc = new Scanner( process.getInputStream() );

            process.waitFor();

            String result = sc.nextLine();
            String rettime = sc.nextLine();

            tt.testIsFinished( result, rettime );
        }
        catch ( IOException e )
        {
            tt.testIsFinished( "error", "0" );
        }
        catch ( InterruptedException e )
        {
            tt.testIsFinished( "error", "0" );
        }

    }
    
}

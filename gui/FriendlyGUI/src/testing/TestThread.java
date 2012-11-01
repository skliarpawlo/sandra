package testing;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import main.ErrorMessage;

/**
 * Класс потока тестирования. Решение запускается в этом потоке на одном тесте.
 * Результат тестирования доступен из метода getResult()
 *
 * @author pawlo
 */
public class TestThread extends Thread
{

    /**
     * Путь к директории тестируемой программы
     */
    private String dir;

    /**
     * exe-файл тестируемой прогрыммы
     */
    private String program;

    /**
     * путь к ехе-файлу программы сравнения ответов
     */
    private String comparor;

    /**
     * тайм лимит на данный тест
     */
    private long timeLimit;

    /**
     * переменная для хранения результата теста
     */
    private String result = null;

    /**
     * время работы программы
     */
    private String time = null;

    /**
     * входной файл проверяемой программы
     */
    private String inputFile;

    /**
     * выходной файл проверяющей программы
     */
    private String outputFile;

    /**
     * полный путь к файлу верного ответа для теста
     */
    private String correctFile;

    /**
     * Поток проверяемой программы
     */
    private Thread progThread;

    /**
     * Конструктор для потока тестирования
     *
     * @param _dir каталог проверяемой программы
     * @param _program имя ехе-файла проверяемой программы
     * @param _comparor путь и имя ехе-файла программы сравнения ответов
     * @param _inputFile входной файл проверяемой программы
     * @param _outputFile выходной файл проверяемой программы
     * @param _correctFile полный путь к файлу с верным ответом для теста
     * @param _timeLimit ограничение по времени для теста
     */
    public TestThread( String _dir, String _program, String _comparor, String _inputFile, String _outputFile, String _correctFile, long _timeLimit )
    {
        dir = _dir;
        program = _program;
        timeLimit = _timeLimit;
        comparor = _comparor;
        inputFile = _inputFile;
        outputFile = _outputFile;
        correctFile = _correctFile;
    }

    /**
     * Переопределяемая функция потока. выполняет тестирование программы с учетом тайм лимита.
     * Результат выполнения теста можно получить после выполнения методом getResult()
     */
    @Override
    public void run()
    {
        
        try
        {
            File testingDir = new File( dir );

            progThread = new OneTest( this, program, testingDir, timeLimit );
            progThread.start();
            progThread.join();

        }
        catch ( Exception e )
        {
            ErrorMessage.error( "ERROR in TESTING class : " + e.toString() );
            result = "e";
        }
    }

    /**
     * Метод вызываемый потоком тестирования одного теста OneTest
     * при окончании работы тестируемой программы. Он передает в
     * качеве параметров статус завершения программы для определения корректности
     * завершения работы программы и время работы программы. <br>
     * <br>
     * Возможные значения зависят от программы timecheck.exe, и по умолчанию
     * такие : <br>
     * <b>okay</b> - своевременное и коректное завершение программы <br>
     * <b>timelimit</b> - программа привысила ограничение по времени и была прервана<br>
     * <b>error</b> - некоректные данные переданы программе timecheck.exe<br>
     * <b>runtime</b> - ошибка времени выполнения в программе-решении<br>
     * <br>
     * Этот метод запускает сравнилку для ответа проверяемой программы и
     * правильного ответа.
     * 
     * @param status статус завершения работы тетируемой программы
     * @param timeFor время работы тестируемой программы
     */
    public synchronized void testIsFinished( String status, String timeFor )
    {

        time = timeFor;
        
        try
        {

            Runtime rt = Runtime.getRuntime();
            File testingDir = new File( dir );                    

            if ( status.equals( "timelimit" ) )
            {
                result = "t";
            }
            else if ( status.equals( "okay" ) )
            {
                String[] compargs = { comparor, outputFile, correctFile, inputFile };

                Process comp = rt.exec( compargs, null, testingDir );

                comp.waitFor();

                if ( comp.exitValue() == 0 )
                {
                    try
                    {
                        File cmpfile = new File( testingDir, "cmp.info" );
                        
                        FileInputStream fis = new FileInputStream( cmpfile );
                        BufferedInputStream bis = new BufferedInputStream( fis );

                        Scanner sc = new Scanner( bis );
                        
                        result = sc.nextLine();
                        
                        sc.close();
                        //utils.FileDelete.delete( cmpfile.getAbsolutePath() );
                    }
                    catch ( Exception e )
                    {
                        ErrorMessage.error( "(may be) NO LINES in cmp.info file! : " + e.getMessage() );
                    }
                }
                else
                {
                    result = "c";
                }

            }
            else if ( status.equals( "runtime" ) )
            {
                result = "r";
            }
        }
        catch ( IOException e )
        {
            ErrorMessage.error( "Ошбика : " + e.toString() );
            result = "e";
        }
        catch ( InterruptedException e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
            result = "e";
        }
    }

    /**
     * Возвращает результат выполнения программой теста.
     * Возможные значения результата : <br>
     * "+" - верный ответ <br>
     * "-" - неверный ответ <br>
     * "r" - ошибка выполнения программы решения <br>
     * "t" - программа не вложилась в ограничение по времени <br>
     * "e" - внутренняя ошибка класса ( возможно изза несовместимости с программой-решенем ) <br>
     * "c" - ошибка сравнителя ( возможно изза того что передаваемый ей файл не найден ) <br>
     * <br>
     * <b>Предупреждение</b>: "+" и "-" это значение возращаемое сравнителем, если в сравнителе заданы
     * другие условные обоначения то именно они будут возвращены
     *
     * @return результат прохождения теста
     */
    public synchronized ArrayList< String > getResult()
    {
        ArrayList< String > testres = new ArrayList< String >();

        testres.add( result );
        testres.add( time );

        return testres;
    }

}

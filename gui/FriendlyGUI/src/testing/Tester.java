package testing;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import main.ErrorMessage;
import main.MainForm;
import utils.FileCopy;
import utils.FileDelete;
import utils.MakeDir;

/**
 * Класс для проверки решения заданой задачи на наборе тестов
 *
 * @author pawlo
 */
public class Tester
{

    /**
     * Решение которое проверяется
     */
    private Solution solution;

    /**
     * Набор тестов для задачи
     */
    private ArrayList< Test > testSet;

    /**
     * Результат прохождения тестов
     */
    private Result result;

    /**
     * отображение прогресса тестирования
     */
    private JProgressBar progress;

    /**
     * для вывода лога результатов тестирования
     */
    private JTextPane txtLog;

    /**
     * Конструктор задающий решение которое неободимо проверить
     *
     * @param _solution решение для проверки
     */
    public Tester( Solution _solution, ArrayList< Test > _testSet, JProgressBar _progress, JTextPane _txtLog )
    {
        solution = _solution;
        testSet = _testSet;
        progress = _progress;
        txtLog = _txtLog;
    }

    /**
     * Начать тестирование на заданном наборе тестов
     */
    public void startTest()
    {
        // создаем новый обьект для результатов
        result = new Result( testSet.size() );

        // создаем временную директорию для тестирования

        String dir = "temporary";
        try
        {
            MakeDir.makeDir( dir );
        }
        catch ( Exception e )
        {
            result = null;
            return;
        }

        String tmp = solution.getSource();
        String sol = solution.getSourceName();

        // копируем ехе-файл во временную директорию
        try
        {
            FileCopy.copy( tmp, dir + File.separator + sol );
        }
        catch ( IOException e )
        {
            result = null;
            return;
        }

        // извлекаем сравнилку, тайм-лимит, входной и выходной файл
        String comparor = solution.getProblem().getComparor();

        String inputFile = solution.getProblem().getInputFile();
        String outputFile = solution.getProblem().getOutputFile();

        long timeLimit = solution.getProblem().getTimeLimit();

        MainForm.insertText( txtLog, "\n", MainForm.OK );

        PrintWriter sumWriter = null;
        File sumfile = new File( dir + File.separator + "test.sum" );
        try
        {
            sumWriter = new PrintWriter( new FileOutputStream( sumfile, true ) );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
            result = null;
            return;
        }
        
        for ( int i = 0; i < testSet.size(); i++ )
        {
            try
            {

                final int finali = i;

                java.awt.EventQueue.invokeLater( new Runnable()
                {
                    public void run()
                    {
                        progress.setValue( Math.round( ( float )( 100.0 * ( 1.0 + finali ) / testSet.size() ) ) );
                    }
                });

                // извлекаем очередной тест : вход и выход
                String testInputFile = testSet.get( i ).getInputFile().getPath();
                String testOutputFile = testSet.get( i ).getOutputFile().getPath();

                String testOutputFileName = testSet.get( i ).getOutputFile().getName();

                // копируем их во временную директорию
                FileCopy.copy( testInputFile, dir + File.separator + inputFile );
                FileCopy.copy( testOutputFile, dir + File.separator + testOutputFileName );

                // запускаем поток на данный тест и ждем окончания и ответа
                TestThread tt = new TestThread( dir, sol, comparor, inputFile, outputFile, testOutputFileName, timeLimit );
                tt.start();
                tt.join();

                //System.out.println( "TEST " + i + " : " + tt.getResult() );

                // выводим в консоль
                String res = tt.getResult().get( 0 );
                if ( "+".equals( res ) )
                {
                    MainForm.insertText( txtLog, res, MainForm.OK );
                }
                else if ( "-".equals( res ) )
                {
                    MainForm.insertText( txtLog, res, MainForm.ALARM );
                }
                else
                {
                    MainForm.insertText( txtLog, res, MainForm.INFO );
                }

                // заносим результат в обьект результатов
                result.put( tt.getResult() );

                // копируем содержимое файла cmp.info в общий файл

                File cmpfile = new File( dir + File.separator + "cmp.info" );

                sumWriter.print( "тест " + i + " : " );

                if ( cmpfile.exists() )
                {
                    FileInputStream fis = new FileInputStream( cmpfile );
                    BufferedInputStream bis = new BufferedInputStream( fis );

                    Scanner sc = new Scanner( bis );

                    while ( sc.hasNextLine() )
                    {
                        sumWriter.println( sc.nextLine() );
                    }

                    sc.close();
                }
                else
                {
                    sumWriter.println( "не найден файл cmp.info. Возможные причины : timelimit, runtime error или тестилка его не генерирует " );
                }

                // удаляем файлы текущего теста
                FileDelete.delete( cmpfile.getAbsolutePath() );
                FileDelete.delete( dir + File.separator + inputFile );
                FileDelete.delete( dir + File.separator + testOutputFileName );

            }
            catch ( IOException e )
            {
                ArrayList< String > testres = new ArrayList< String >();
                testres.add( "e" );
                result.put( testres );
            }
            catch ( InterruptedException e )
            {
                ArrayList< String > testres = new ArrayList< String >();
                testres.add( "e" );
                result.put( testres );
            }

        }

        sumWriter.flush();
        sumWriter.close();
        
        // Копируем файл с результатами тестирования в папку пользователя
        try
        {
            String fileName = solution.getSourceName();
            fileName = fileName.substring( 0, fileName.lastIndexOf( "." ) );
            FileCopy.copy( sumfile.getAbsolutePath(), solution.getSourceDir() + File.separator + fileName + ".test" );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка копирования файла с результатами тестирования : " + e.toString() );
        }

        // Удаляем решение и выходной файл
        FileDelete.delete( sumfile.getAbsolutePath() );
        FileDelete.delete( dir + File.separator + sol );
        FileDelete.delete( dir + File.separator + outputFile );

        // Удаляем временную папку
        FileDelete.delete( dir );

    }

    /**
     * Возвращает результат тестирования
     *
     * @return результат тестирования
     */
    public Result getResult()
    {
        return result;
    }

}

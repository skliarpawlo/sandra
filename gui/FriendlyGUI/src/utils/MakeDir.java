package utils;

import java.io.File;

/**
 * Утилита для создания директории
 *
 * @author pawlo
 */
public class MakeDir
{

    /**
     * Создает директорию и все не существующие директории высшего уровня
     * 
     * @param dir создаваемая директория
     */
    public static void makeDir( String dir )
    {
        boolean status = new File( dir ).mkdirs();
    }

}

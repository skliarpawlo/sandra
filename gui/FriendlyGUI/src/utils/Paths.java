package utils;

import java.io.File;

/**
 * Класс для конвертирования ( слеширования ) путей при записи в БД. Сделан для совместимости с php частью системы
 *
 * @author Pawlo
 */
public class Paths
{
    /**
     * Убирает лишние слеши в переданой строке
     *
     * @param s путь с лишними слешами
     * @return путь без слешей
     */
    public static String deconv( String s )
    {
        return s.replaceAll("[/\\\\]+", "\\" + File.separator);
    }

    /**
     * Слеширует путь переданый в качестве параметра
     * @param s путь
     * @return слешированный путь
     */
    public static String conv( String s )
    {
        return s.replaceAll( "\\" + File.separator, "\\" + File.separator + "\\" + File.separator );
    }

}

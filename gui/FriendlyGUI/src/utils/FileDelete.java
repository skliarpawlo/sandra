package utils;

import java.io.File;

/**
 * Утилита для удаления файла
 *
 */
public class FileDelete
{

    /**
     * Удаляет заданый файл
     * 
     * @param fileName удаляемый файл
     * @throws IllegalArgumentException
     */
    public static boolean delete( String fileName ) throws IllegalArgumentException
    {
        // A File object to represent the filename
        File f = new File( fileName );

        // Make sure the file or directory exists and isn't write protected
        if ( !f.exists() ) return false;
            //throw new IllegalArgumentException( "Delete: no such file or directory: " + fileName );

        if ( !f.canWrite() ) return false;
            //throw new IllegalArgumentException( "Delete: write protected: " + fileName );

        // Attempt to delete it
        boolean success = f.delete();
        return success;
    }

}
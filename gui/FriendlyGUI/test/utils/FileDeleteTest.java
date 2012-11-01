package utils;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

/**
 *
 * @author Павло
 */
public class FileDeleteTest 
{
    
    public FileDeleteTest() 
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception 
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception 
    {
    }
    
    @Before
    public void setUp() 
    {
    }
    
    @After
    public void tearDown() 
    {
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();    
    
    @Test
    public void testDeleteIfFileExists() throws Exception 
    {
        File tempFile = tempFolder.newFile( "temp.txt" );
        String fileName = tempFile.getAbsolutePath();
        
        FileDelete.delete( fileName );
        
        assertTrue( "File was not deleted", !tempFile.exists() );
    }
    
    @Test
    public void testDeleteIfFileNotExists() throws Exception 
    {
        File tempFile = new File( "C:\\a.txt" );
        
        assertTrue( "Method returned true, when file not exist", 
                    !FileDelete.delete( tempFile.getAbsolutePath() ) );
        assertTrue( "File exists", !tempFile.exists() );
    }

    @Test
    public void testDeleteIfNoPremission() throws Exception 
    {
        File tempFile = new File( "E:\\1234.wav" );
        
        assertTrue( "Strange : file was deleted, or method returned true without reason", 
                    !FileDelete.delete( tempFile.getAbsolutePath() ) );
        
    }
}

package utils;

import java.io.File;
import java.io.IOException;
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
public class FileCopyTest 
{
    
    public FileCopyTest() 
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
    public void testCopyIfFromFileExists() throws Exception 
    {
        File folder = tempFolder.newFolder( "temporary" );
        File fileFrom = tempFolder.newFile( "temporary\\temp.from" );        
        File fileTo = new File( folder.getAbsolutePath() + "\\temp.to" );        

        String fromFileName = fileFrom.getAbsolutePath();
        String toFileName = fileTo.getAbsolutePath();
        
        FileCopy.copy(fromFileName, toFileName);
        
        assertTrue( "File was not created", fileTo.exists() );
    }

    @Test
    public void testCopyIfFromFileNotExists() throws Exception 
    {
        
        String fromFileName = "C:\\a.txt";
        String toFileName = "C:\\b.txt";
        
        exception.expect( IOException.class );        
        FileCopy.copy( fromFileName, toFileName );        
        
    }
}

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
public class MakeDirTest 
{
    
    public MakeDirTest() 
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
    public void testMakeDirIfDirNotExist() {
        String dir = tempFolder.getRoot().getAbsolutePath()+"\\myTemp";
        MakeDir.makeDir( dir );
        assertTrue( "Folder was not created", new File( dir ).exists() );
    }

    @Test
    public void testMakeDirIfPathNotExist() {
        String dir = tempFolder.getRoot().getAbsolutePath()+"\\this\\is\\my\\Temp";
        MakeDir.makeDir( dir );
        assertTrue( "Folder was not created", new File( dir ).exists() );
    }
    
    @Test
    public void testMakeDirIfNoPermisson() {
        String dir = "C:\\Windows\\my\\Temp";
        MakeDir.makeDir( dir );
        assertTrue( "Wow you've cheated", !new File( dir ).exists() );
    }
}

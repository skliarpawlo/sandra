package compiling;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
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
public class CompilerTest 
{
    
    public CompilerTest() 
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
    public void testCompileWhenFileNotExists() throws Exception 
    {
        File source = new File( "C://sd.qwe" );
        
        exception.expect( IOException.class );        
        Compiler.compile( source );                        
    }

    @Test
    public void testCompileWhenFileHasBadExtension() throws Exception 
    {
        File folder = tempFolder.newFolder( "forTest" );
        File source = tempFolder.newFile( "forTest//program.java" );
        
        exception.expect( IOException.class );        
        Compiler.compile( source );
    }

    @Test
    public void testCompileWhenFileIsGood() throws Exception 
    {
        File folder = tempFolder.newFolder( "forTest" );
        File source = tempFolder.newFile( "forTest//program.pas" );
        File exe = new File( folder.getAbsolutePath() + "//program.exe" );
        
        assertTrue( "Precondition is false", !exe.exists() );
        
        PrintStream ps = new PrintStream( source );
        ps.print( "begin end." );
        ps.close();        
        
        //exception.expect( IOException.class );        
        Compiler.compile( source );
        
        assertTrue( "Compiled not well", exe.exists() );
    }
    
}

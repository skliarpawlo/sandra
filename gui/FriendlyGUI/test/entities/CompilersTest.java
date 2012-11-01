package entities;

import java.util.List;
import javax.persistence.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Павло
 */
public class CompilersTest 
{
    
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory( "FriendlyGUIPU" );
    private static EntityManager em = emf.createEntityManager();    
    
    public CompilersTest() 
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

    @Test
    public void testGetSet() 
    {
        try 
        {
            Compilers instance = new Compilers();
        
            Query testQuery = em.createQuery( "SELECT obj FROM Compilers obj" );
        
            List<Compilers> testResult = testQuery.getResultList();
            
            for ( Compilers c : testResult ) 
            {
                System.out.println( "ENTITY : " + c.toString() );
            }
        
            Integer expResult = null;
            Integer result = instance.getCpid();
        
        }
        catch ( Exception e ) 
        {
            fail( "Error with Persistance : cannot apply SELECT query to Compilers, " + e.toString() );
        }
    }
    
    @Test
    public void testCreate() 
    {
        try 
        {
            em.getTransaction().begin();
            System.out.println( "Transaction begin" );
            
            Compilers instance = new Compilers();
            instance.setCompilerpath( "c:\\test\\path" );
            instance.setExtensions( "test" );
            instance.setParams( "test" );
            em.persist( instance );
            
            System.out.println( "Test item added" );
            
            assertTrue( "Entity Manage does not contain added test item", em.contains( instance ) );              
        
            em.remove( instance );
            
            System.out.println( "Test item removed" );
            
            assertTrue( "Entity Manage still contains just deleted test item", !em.contains( instance ) );              
                
            em.getTransaction().commit();
            
            System.out.println( "Transaction end." );
            
        }
        catch ( Exception e ) 
        {
            fail( "Error with Persistance : cannot apply SELECT query to Compilers, " + e.toString() );
        }
    }

}

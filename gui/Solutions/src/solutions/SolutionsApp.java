/*
 * SolutionsApp.java
 */

package solutions;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class SolutionsApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        try
        {
            for ( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() )
            {
                if ( "Nimbus".equals( info.getName() ) )
                {
                    UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }

        }
        catch ( Exception e )
        {
            System.out.println( "Look And Feel error" );
        }
        show(new SolutionsView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of SolutionsApp
     */
    public static SolutionsApp getApplication() {
        return Application.getInstance(SolutionsApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(SolutionsApp.class, args);
    }
}

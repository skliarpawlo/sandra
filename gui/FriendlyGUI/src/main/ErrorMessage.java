package main;

import java.awt.Component;
import java.awt.EventQueue;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

/**
 *
 * @author Pawlo
 */
public class ErrorMessage
{
    
    public static void error( final String msg )
    {
        EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                JOptionPane.showMessageDialog( null, msg, ResourceBundle.getBundle( "main.HelpTips" ).getString( "Ошибка" ), JOptionPane.ERROR_MESSAGE );
            }
        });
    }

    public static void error( final Component par, final String msg )
    {
        EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                JOptionPane.showMessageDialog( par, msg, ResourceBundle.getBundle( "main.HelpTips" ).getString( "Ошибка" ), JOptionPane.ERROR_MESSAGE );
            }
        });
    }

}

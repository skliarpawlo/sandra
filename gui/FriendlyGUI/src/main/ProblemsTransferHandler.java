package main;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;
import structures.ProblemListItem;
import structures.Tables;
import utils.Paths;

/**
 * Обработчик для перетаскивания задач на темп-панель
 *
 * @author pawlo
 */
public class ProblemsTransferHandler extends TransferHandler
{

    /**
     * Внутренний класс для передачи данных в ДнД
     */
    public static class Problem implements Transferable
    {

        private ArrayList< ProblemListItem > x;

        public Problem( ArrayList< ProblemListItem > _x )
        {
            x = _x;
        }

        public ArrayList< ProblemListItem > getTransferData( DataFlavor flavor )
        {
            return x;
        }

        public boolean isDataFlavorSupported( DataFlavor flavor )
        {
            try
            {
                return flavor.equals( DataFlavor.stringFlavor );
            }
            catch ( Exception e )
            {
                return false;
            }
        }

        public DataFlavor[] getTransferDataFlavors()
        {
            try
            {
                DataFlavor[] res = new DataFlavor[] { DataFlavor.stringFlavor };
                return res;
            }
            catch ( Exception e )
            {
                return null;
            }
        }

    }

    @Override
    public int getSourceActions( JComponent x )
    {
        return COPY;
    }

    @Override
    public Transferable createTransferable( JComponent x )
    {
        if ( x instanceof JTable )
        {
            JTable table = ( JTable ) x;
            int[] rows = table.getSelectedRows();

            ArrayList< ProblemListItem > vals = new ArrayList< ProblemListItem >();
            for ( int i = 0; i < rows.length; i++ )
            {
                vals.add( ( ProblemListItem ) table.getValueAt( rows[ i ], 0 ) );
            }

            return new Problem( vals );
        }
        else if ( x instanceof JList )
        {
            JList list = ( JList ) x;

            Object[] rows = list.getSelectedValues();
            
            ArrayList< ProblemListItem > vals = new ArrayList< ProblemListItem >();
            for ( int i = 0; i < rows.length; i++ )
            {
                vals.add( ( ProblemListItem ) rows[ i ] );
            }

            return new Problem( vals );
        }
        else
        {
            return null;
        }
    }

    @Override
    public void exportDone( JComponent x, Transferable data, int action )
    {                
    }

    @Override
    public boolean canImport( TransferSupport sup )
    {
        try
        {
            String name = sup.getComponent().getAccessibleContext().getAccessibleName();
            boolean flag1 = !"allProblemsTable".equals( name );
            boolean flag2 = sup.isDataFlavorSupported( DataFlavor.stringFlavor );
            return flag1 && flag2;
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка (canImport) : " + e.toString() );
            return false;
        }
    }

    @Override
    public boolean importData( TransferSupport sup )
    {
        try 
        {
            Component x = sup.getComponent();

            if ( x instanceof JList )
            {
                JList list = ( JList ) x;

                String differ = list.getAccessibleContext().getAccessibleName();

                ArrayList< ProblemListItem > p = ( ArrayList< ProblemListItem > ) sup.getTransferable().getTransferData(  DataFlavor.stringFlavor );

                if ( "testProblem".equals( differ ) || "solToShow".equals( differ ) )
                {
                    DefaultListModel model = ( DefaultListModel ) list.getModel();

                    model.clear();
                    
                    if ( model.size() == 0 )
                    {
                        model.addElement( p.get( 0 ) );
                    }
                    else
                    {
                        model.setElementAt( p.get( 0 ), 0 );
                    }

                    list.setSelectedIndex( 0 );
                }
                else
                {
                    for ( ProblemListItem item : p )
                    {
                        ( ( DefaultListModel ) list.getModel() ).addElement( item );
                    }
                }

                return true;
            }
            else if ( x instanceof JTable )
            {
                JTable table = ( JTable ) x;

                ArrayList< ProblemListItem > allp = ( ArrayList< ProblemListItem > ) sup.getTransferable().getTransferData(  DataFlavor.stringFlavor );

                for ( ProblemListItem p : allp )
                {

                    entities.Problems prob = Tables.getProblemById( p.getId() );

                    ProblemListItem[] row = {
                        new ProblemListItem( prob.getTitle(), p.getId() ),
                        new ProblemListItem( Paths.deconv( prob.getFilepath() ), p.getId() ),
                        new ProblemListItem( Paths.deconv( prob.getComparor() ), p.getId() ),
                        new ProblemListItem( prob.getSolutionfile(), p.getId() ),
                        new ProblemListItem( prob.getInputfile(), p.getId() ),
                        new ProblemListItem( prob.getOutputfile(), p.getId() ),
                        new ProblemListItem( String.valueOf( prob.getTimelimit() ), p.getId() ) };

                    String differ = table.getAccessibleContext().getAccessibleName();

                    if ( "problemsInCategoryTable".equals( differ ) )
                    {
                        DefaultTableModel model = ( DefaultTableModel ) table.getModel();
                        model.insertRow( model.getRowCount() - 1, row );
                        //model.setRowCount( model.getRowCount() + 1 );
                    }
                    else
                    {
                        ( ( DefaultTableModel ) table.getModel() ).addRow( row );
                    }
                }                
                MainForm.packColumns( table );
            }

            return false;
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка (importData) : " + e );
            return false;
        }
    }

}

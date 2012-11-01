/*
 * MainForm.java
 *
 * Created on 11.03.2010, 8:19:20
 */

package main;

import entities.Compilers;
import entities.NeedTest;
import entities.ProblemCategories;
import entities.ProblemCategoryAssign;
import java.awt.Color;
import java.io.File;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.DefaultListModel;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import structures.ProblemListItem;
import structures.Tables;
import structures.TreeNode;
import structures.Trees;
import testing.OnlineTesterThread;
import entities.Problems;
import entities.Results;
import entities.Solutions;
import entities.Tests;
import entities.Users;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JList;
import java.util.List;
import structures.SolutionListItem;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import structures.UserListItem;
import java.awt.Toolkit;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Scanner;
import javax.persistence.EntityTransaction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import utils.Paths;
import utils.StringOperator;

/**
 * Главная форма системы
 *
 * @author Pawlo
 */
public class MainForm extends javax.swing.JFrame {

    /**
     * стиль для ошибки или минуса в консоли проверки
     */
    public static SimpleAttributeSet ALARM = new SimpleAttributeSet();

    /**
     * стиль для незначительной информации в консоли проверки
     */
    public static SimpleAttributeSet NOT_SIGN = new SimpleAttributeSet();

    /**
     * стиль для подтверждения и плюса в консоли проверки
     */
    public static SimpleAttributeSet OK = new SimpleAttributeSet();

    /**
     * стиль для информации в консоли проверки
     */
    public static SimpleAttributeSet INFO = new SimpleAttributeSet();

    public static final int CELL_HEIGHT = 30;

    /**
     * фабрика для получения менеджера работы с базой данных через JPA
     */
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory( "FriendlyGUIPU" );

    /**
     * менеджер для работы с базой данных через JPA
     */
    private static EntityManager em = emf.createEntityManager();

    /**
     * инициализирует главную форму программы
     */
    public MainForm( String lookAndFeel ) {

        try
        {

            for ( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() )
            {
                if ( lookAndFeel.equals( info.getName() ) )
                {
                    UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }

            try
            {
                Properties prop = new Properties();
                prop.load( new FileInputStream( new File( "settings.info" ) ) );

                String skin = prop.getProperty( "LookAndFeelSkin" );
                String laf = prop.getProperty( "LookAndFeelMainClass" );

                SubstanceLookAndFeel.setSkin( skin );
                UIManager.setLookAndFeel( laf );

                //SubstanceLookAndFeel.setSkin("org.pushingpixels.substance.api.skin.TwilightSkin");
                //UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceTwilightLookAndFeel");

                //SubstanceLookAndFeel.setSkin("org.pushingpixels.substance.api.skin.MistSilverSkin");
                //UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceMistSilverLookAndFeel");
            }
            catch ( Exception e )
            {
                ErrorMessage.error( "Error : " + e.toString() );
            }

        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка при инициализации стиля " + e.toString() );
        }

        initComponents();

        /*
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        // Get size of each screen
        for (int i=0; i < gs.length; i++)
        {
            DisplayMode dm = gs[i].getDisplayMode();
            int screenWidth = dm.getWidth();
            int screenHeight = dm.getHeight();

            this.setSize( screenWidth, screenHeight );

        }*/
        this.setResizable( true );

        initStyles();

        ////////////////////////

        tempList.setModel( new DefaultListModel() );
        listCurProblem.setModel( new DefaultListModel() );
        listResProblems.setModel( new DefaultListModel() );
        listResUsers.setModel( new DefaultListModel() );
        listSolToShow.setModel( new DefaultListModel() );

        initTestTable();
        initContentTable();
        initCategoryTree();
        initProblemsList();
        initSolutionsTable();
        initCompilersTable();
        initUsersTable();
        initResultUserList();
        initNeedTestTable();
        initUserCategoryList();

        TransferHandler th = new ProblemsTransferHandler();

        problemsList.setTransferHandler( th );
        tempList.setTransferHandler( th );
        tblContent.setTransferHandler( th );
        listCurProblem.setTransferHandler( th );
        listResProblems.setTransferHandler( th );
        listSolToShow.setTransferHandler( th );

        tblResults.setAutoCreateRowSorter( true );

    }

    /**
     * Выравнивание столбцов таблици по ширине (максимального по длинне елемента)
     * 
     * @param table 
     */
    public static void packColumns( JTable table ) 
    {
        return;
        /*
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for ( int i = 0, columnCount = table.getColumnCount(); i < columnCount; i++ ) 
        {
            TableCellRenderer renderer = table.getDefaultRenderer(
                table.getModel().getColumnClass( i )
            );
 
            int maxWidth = 0;
 
            for ( int j = 0, rowCount = table.getRowCount(); j < rowCount; j++ ) 
            {
                Component component = renderer.getTableCellRendererComponent(
                    table, table.getValueAt(j, i), true, true, j, i
                );
 
                maxWidth = Math.max(
                    maxWidth, component.getPreferredSize().width
                );
            }
 
            TableColumn column = table.getColumnModel().getColumn(i);
 
            column.setMinWidth(
                maxWidth + table.getIntercellSpacing().width
            );
        }*/
    }    
    
    /**
     * инициализация списка категорий пользователей
     */
    public void initUserCategoryList()
    {
        DefaultListModel model = new DefaultListModel();
        
        List res = em.createQuery( "SELECT DISTINCT obj.usercategory FROM Users obj" ).getResultList();

        for ( int i = 0; i < res.size(); i++ )
        {
            model.addElement( ( String ) res.get( i ) );
        }

        listUserCategories.setModel( model );
    }

    /**
     * инициализация таблици решений посланных на проверку
     */
    public void initNeedTestTable()
    {

        try
        {

            String[] cols = { "Задача", "Пользователь", "Решение" };
            DefaultTableModel model = new DefaultTableModel( cols, 0 );

            List res = em.createQuery( "SELECT obj FROM NeedTest obj" ).getResultList();

            for ( int i = 0; i < res.size(); i++ )
            {
                NeedTest cur = ( NeedTest ) res.get( i );

                List sol = em.createQuery( "SELECT obj FROM Solutions obj WHERE obj.sid=" + cur.getSid() ).getResultList();

                if ( sol.size() == 1 )
                {

                    List prob = em.createQuery( "SELECT obj FROM Problems obj WHERE obj.pid=" + ( ( Solutions ) sol.get( 0 ) ).getPid() ).getResultList();
                    List user = em.createQuery( "SELECT obj FROM Users obj WHERE obj.uid=" + ( ( Solutions ) sol.get( 0 ) ).getUid() ).getResultList();

                    if ( prob.size() == 1 && user.size() == 1 )
                    {

                        String[] rows = { 
                            ( ( Problems ) prob.get( 0 ) ).getTitle(),
                            ( ( Users ) user.get( 0 ) ).getFio(),
                            Paths.deconv( ( ( Solutions ) sol.get( 0 ) ).getFilepath() ) };

                        model.addRow( rows );
                    }
                }
            }

            tblNeedTest.setModel( model );
            tblNeedTest.setAutoCreateRowSorter( true );
            tblNeedTest.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
            tblNeedTest.setRowHeight( CELL_HEIGHT );
            packColumns( tblNeedTest );             
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
        }
    }

    /**
     * инициализация списка пользователей для которых сводится таблица результатов
     */
    public void initResultUserList()
    {
        try
        {
            DefaultListModel model = new DefaultListModel();

            List res = em.createQuery( "SELECT obj FROM Users obj" ).getResultList();

            for ( int i = 0; i < res.size(); i++ )
            {
                Users user = ( Users ) res.get( i );
                model.addElement( new UserListItem( user.getFio(), user.getUid() ) );
            }

            listResUsers.setModel( model );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
        }
    }

    /**
     * инициализация таблици пользователей
     */
    public void initUsersTable()
    {
        try
        {
            String[] cols = { "Имя", "Логин", "Категория" };
            DefaultTableModel model = new DefaultTableModel( cols, 0 )
            {
                @Override
                public boolean isCellEditable( int x, int y )
                {
                    return false;
                }
            };

            List users = em.createQuery( "SELECT obj FROM Users obj" ).getResultList();

            for ( int i = 0; i < users.size(); i++ )
            {
                Users user = ( Users ) users.get( i );
                UserListItem[] row = { new UserListItem( user.getFio(), user.getUid() ),
                                       new UserListItem( user.getName(), user.getUid() ),
                                       new UserListItem( user.getUsercategory(), user.getUid() ) };

                model.addRow( row );
            }

            tblUsers.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
            tblUsers.setModel( model );
            tblUsers.setAutoCreateRowSorter( true );
            tblUsers.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
            tblUsers.setRowHeight( CELL_HEIGHT );
            packColumns( tblUsers );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
        }
    }

    /**
     * Инициализация таблици тестов
     */
    public void initTestTable()
    {
        String[] cols = { "Номер", "Входной файл", "Выходной файл" };
        DefaultTableModel model = new DefaultTableModel( cols, 0 )
        {
            @Override
            public boolean isCellEditable( int x, int y )
            {
                return false;
            }
        };

        tblTests.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        tblTests.setModel( model );
        tblTests.setAutoCreateRowSorter( true );
        tblTests.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        tblTests.setRowHeight( CELL_HEIGHT );
        packColumns( tblTests );
    }

    /**
     * Инициализация дерева категорий
     */
    public void initCategoryTree()
    {
        try
        {
            Query query = em.createQuery( "SELECT obj FROM ProblemCategories obj WHERE obj.parentid=0" );

            ProblemCategories category = ( ProblemCategories ) query.getResultList().get( 0 );

            TreeModel model = new DefaultTreeModel( Trees.makeProblemTree( category.getCatid() ) );
            model.addTreeModelListener( new ChangeListener() );

            contentTree.setEditable( false );
            contentTree.getSelectionModel().setSelectionMode( DefaultTreeSelectionModel.SINGLE_TREE_SELECTION );
            contentTree.setModel( model );

        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e );
        }
    }

    /**
     * Инициализация таблици решений
     */
    public void initSolutionsTable()
    {
        try
        {
            DefaultListModel listmod = (DefaultListModel)listSolToShow.getModel();

            Query query = null;
            if ( listmod.getSize() > 0 )
            {
                ProblemListItem pli = (ProblemListItem) listmod.getElementAt( 0 );
                query = em.createQuery( "SELECT obj FROM Solutions obj WHERE obj.pid=" + pli.getId().toString() );
            } 
            else 
            {
                query = em.createQuery( "SELECT obj FROM Solutions obj" );
            }

            List res = query.getResultList();

            String[] cols = { "Задача", "Пользователь", "Решение" };

            DefaultTableModel tableModel = new DefaultTableModel( cols, 0 )
            {
                @Override
                public boolean isCellEditable( int x, int y )
                {
                    return false;
                }
            };

            for ( int i = 0; i < res.size(); i++ )
            {
                Solutions sol = ( Solutions ) res.get( i );

                Query problemQuery = em.createQuery( "SELECT obj FROM Problems obj WHERE obj.pid=" + sol.getPid() );
                Problems problem = ( Problems ) problemQuery.getSingleResult();

                Query userQuery = em.createQuery( "SELECT obj FROM Users obj WHERE obj.uid=" + sol.getUid() );
                try
                {
                    Users user = ( Users ) userQuery.getSingleResult();

                    SolutionListItem[] row = {
                        new SolutionListItem( problem.getTitle(), sol.getSid() ),
                        new SolutionListItem( user.getFio(), sol.getSid() ),
                        new SolutionListItem( Paths.deconv( sol.getFilepath() ), sol.getSid() ) };
                    tableModel.addRow( row );

                }
                catch ( Exception e )
                {
                    SolutionListItem[] row = {
                        new SolutionListItem( problem.getTitle(), sol.getSid() ),
                        new SolutionListItem( "anonymus", -1 ),
                        new SolutionListItem( Paths.deconv( sol.getFilepath() ), sol.getSid() ) };

                    tableModel.addRow( row );
                }

            }

            tblSolutions.setModel( tableModel );
            tblSolutions.setAutoCreateRowSorter( true );
            tblSolutions.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
            tblSolutions.setRowHeight( CELL_HEIGHT );
            packColumns( tblSolutions );

        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
        }
    }

    /**
     * Инициализация таблици компиляторов
     */
    public void initCompilersTable()
    {
        try
        {

            Query query = em.createQuery( "SELECT obj FROM Compilers obj ORDER BY obj.cpid ASC" );

            List res = query.getResultList();

            String[] cols = { "Расширения", "Компилятор", "Параметры" };
            DefaultTableModel tableModel = new DefaultTableModel( cols, 0 );

            for ( int i = 0; i < res.size(); i++ )
            {
                Compilers compiler = ( Compilers ) res.get( i );

                String[] row = {
                    compiler.getExtensions(),
                    Paths.deconv( compiler.getCompilerpath() ),
                    compiler.getParams() };

                tableModel.addRow( row );
            }

            tblCompilers.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
            tblCompilers.setModel( tableModel );
            tblCompilers.setAutoCreateRowSorter( true );
            tblCompilers.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
            tblCompilers.setRowHeight( CELL_HEIGHT );
            packColumns( tblCompilers );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
        }

    }

    /**
     * Инициализация таблици содержимого категорий
     */
    public void initContentTable()
    {
        String[] cols = { "Название", "Условие", "Сравнитель", "Имя программы", "Входной файл", "Выходной файл", "Тайм-лимит" };
        DefaultTableModel model = new DefaultTableModel( cols, 1 )
        {
            @Override
            public boolean isCellEditable( int x, int y )
            {
                return false;
            }
        };

        tblContent.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        tblContent.setModel( model );
        tblContent.setAutoCreateRowSorter( true );
        tblContent.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        tblContent.setRowHeight( CELL_HEIGHT );
        packColumns( tblContent );

    }

    /**
     * Инициализация списка задач
     */
    public void initProblemsList()
    {
        ProblemListItem[][] res = Tables.getAllProblemsList();
        
        String[] cols = { "Название", "Условие", "Сравнитель", "Имя программы", "Входной файл", "Выходной файл", "Тайм-лимит" };
        DefaultTableModel model = new DefaultTableModel() 
        {
            @Override
            public boolean isCellEditable( int x, int y ) 
            {
                return false;
            }
        };

        model.setColumnIdentifiers( cols );
        model.setRowCount( res.length );

        for ( int i = 0; i < res.length; i++ )
        {
            for ( int j = 0; j < res[ i ].length; j++ )
            {
                model.setValueAt( res[ i ][ j ], i, j );
            }
        }

        problemsList.setModel( model );
        problemsList.setAutoCreateRowSorter( true );
        problemsList.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        problemsList.setRowHeight( CELL_HEIGHT );

    }

    /**
     * инициализация стилей для отображения в консоли тестирования
     */
    public void initStyles()
    {
        StyleConstants.setForeground( INFO, new Color( 0x000000 ) );
        StyleConstants.setBold( INFO, true );
        StyleConstants.setFontFamily( INFO, "Monospaced" );
        StyleConstants.setFontSize( INFO, 14 );

        StyleConstants.setForeground( ALARM, new Color( 0xAA, 0x11, 0x11, 0xFF ) );
        StyleConstants.setBold( ALARM, true);
        StyleConstants.setFontFamily( ALARM, "Monospaced" );
        StyleConstants.setFontSize( ALARM, 14 );

        StyleConstants.setForeground( NOT_SIGN, new Color( 0xAAAAAA ) );
        StyleConstants.setFontFamily( NOT_SIGN, "Monospaced" );
        StyleConstants.setFontSize( NOT_SIGN, 14 );

        StyleConstants.setForeground( OK, new Color( 0x11, 0xAA, 0x11, 0xFF ) );
        StyleConstants.setBold( OK, true );
        StyleConstants.setFontFamily( OK, "Monospaced" );
        StyleConstants.setFontSize( OK, 14 );
    }

    /**
     * Добаление сообщения в логгер
     *
     * @param txtLog
     * @param text
     * @param set
     */
    public static void insertText( JTextPane txtLog, String text, AttributeSet set )
    {
        try
        {
            txtLog.getDocument().insertString( 0, text, set );
        }
        catch ( BadLocationException e )
        {
            e.printStackTrace();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        txtExtensions = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        btnCompilerOpen = new javax.swing.JButton();
        txtCompilerParams = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtCompiler = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        txtUserLogin = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtUserCategory = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        listResProblems = new javax.swing.JList();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        listResUsers = new javax.swing.JList();
        jLabel18 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        listUserCategories = new javax.swing.JList();
        jLabel20 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnOutFile = new javax.swing.JButton();
        txtOutFile = new javax.swing.JTextField();
        btnInFile = new javax.swing.JButton();
        txtInFile = new javax.swing.JTextField();
        txtNum = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtProblemIn = new javax.swing.JTextField();
        txtProblemProgram = new javax.swing.JTextField();
        txtProblemOut = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnProblemFile = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtProblemName = new javax.swing.JTextField();
        btnComparorFile = new javax.swing.JButton();
        txtProblemTime = new javax.swing.JTextField();
        txtProblemFile = new javax.swing.JTextField();
        txtProblemComparor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        txtNewCategory = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        btnOpenSolution = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txtSolution = new javax.swing.JTextField();
        isDozdCheckBox = new javax.swing.JCheckBox();
        jButton5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jToolBar8 = new javax.swing.JToolBar();
        btnAddNewCategory = new javax.swing.JButton();
        btnRemoveCategory = new javax.swing.JButton();
        btnSaveCategory = new javax.swing.JButton();
        AddCategoryCollapsiblePane = new org.jdesktop.swingx.JXCollapsiblePane();
        jScrollPane2 = new javax.swing.JScrollPane();
        contentTree = new javax.swing.JTree();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblContent = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        btnAddProblem = new javax.swing.JButton();
        btnProblemRemove = new javax.swing.JButton();
        AddProblemCollapsiblePane = new org.jdesktop.swingx.JXCollapsiblePane();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        problemsList = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        btnAddTest = new javax.swing.JButton();
        btnTestSave = new javax.swing.JButton();
        AddTestCollapsiblePane = new org.jdesktop.swingx.JXCollapsiblePane();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblTests = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        listCurProblem = new javax.swing.JList();
        jLabel10 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnAddCompiler = new javax.swing.JButton();
        btnCompilerSave = new javax.swing.JButton();
        btnCompilersRefresh = new javax.swing.JButton();
        addCompilerCollapsiblePane = new org.jdesktop.swingx.JXCollapsiblePane();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblCompilers = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jToolBar6 = new javax.swing.JToolBar();
        btnAddSolution = new javax.swing.JButton();
        btnCheck = new javax.swing.JButton();
        btnRefreshSolTables = new javax.swing.JButton();
        btnRemoveSolutions = new javax.swing.JButton();
        addSolCollapsiblePane = new org.jdesktop.swingx.JXCollapsiblePane();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblNeedTest = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblSolutions = new javax.swing.JTable();
        jScrollPane16 = new javax.swing.JScrollPane();
        listSolToShow = new javax.swing.JList();
        jLabel23 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextPane();
        jToolBar4 = new javax.swing.JToolBar();
        btnStart = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jToolBar7 = new javax.swing.JToolBar();
        btnShowResult = new javax.swing.JButton();
        btnExcelExport = new javax.swing.JButton();
        resultsCollapsiblePane = new org.jdesktop.swingx.JXCollapsiblePane();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tblResults = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jToolBar5 = new javax.swing.JToolBar();
        btnAddUser = new javax.swing.JButton();
        btnUsersRemove = new javax.swing.JButton();
        btnUsersRefresh = new javax.swing.JButton();
        addUserCollapsiblePane = new org.jdesktop.swingx.JXCollapsiblePane();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tblUsers = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tempList = new javax.swing.JList();
        progressBar = new javax.swing.JProgressBar();
        totalProgress = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        nimbusLookMenuItem = new javax.swing.JMenuItem();
        windowsLookMenuItem = new javax.swing.JMenuItem();
        lightThemeMenuItem = new javax.swing.JMenuItem();
        BusinessThemeMenuItem = new javax.swing.JMenuItem();
        darkThemeMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        DBExportMenuItem = new javax.swing.JMenuItem();
        DBImportMenuItem = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Добавление компилятора"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("main/HelpTips"); // NOI18N
        txtExtensions.setToolTipText(bundle.getString("Расширения файлов")); // NOI18N

        jLabel14.setText("Расширения файлов");

        btnCompilerOpen.setText("Открыть");
        btnCompilerOpen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCompilerOpenMouseClicked(evt);
            }
        });

        txtCompilerParams.setToolTipText(bundle.getString("Параметры компиляции")); // NOI18N

        jLabel15.setText("Компилятор");

        txtCompiler.setToolTipText(bundle.getString("Компилятор")); // NOI18N

        jLabel16.setText("Параметры компиляции");

        jButton4.setText("Добавить");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton4MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtExtensions, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCompilerParams, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCompiler, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCompilerOpen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtExtensions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtCompiler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCompilerOpen))
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCompilerParams, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addComponent(jButton4)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(106, 106, 106))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Добавление пользователей"));

        txtUserLogin.setToolTipText(bundle.getString("Логин пользователя")); // NOI18N

        jLabel19.setText("Логин");

        txtUserName.setToolTipText(bundle.getString("Имя пользователя")); // NOI18N

        jLabel21.setText("Имя");

        txtUserCategory.setToolTipText(bundle.getString("Категория пользователя")); // NOI18N

        jLabel12.setText("Категория");

        jButton7.setText("Добавить");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton7MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel21)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtUserCategory, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(txtUserLogin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(txtUserName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addGap(289, 289, 289))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUserLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUserCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jButton7))
                .addGap(19, 19, 19))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Формирование результатов"));

        jLabel17.setText("Задачи");

        listResProblems.setToolTipText(bundle.getString("Список задач для формирования результатов")); // NOI18N
        listResProblems.setDragEnabled(true);
        listResProblems.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                listResProblemsKeyPressed(evt);
            }
        });
        jScrollPane12.setViewportView(listResProblems);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jLabel17)))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        listResUsers.setToolTipText(bundle.getString("Список пользователей для формирования результатов")); // NOI18N
        jScrollPane13.setViewportView(listResUsers);

        jLabel18.setText("Пользователи");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel18)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        listUserCategories.setToolTipText(bundle.getString("Список категорий пользователей для формирования результатов")); // NOI18N
        listUserCategories.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listUserCategoriesValueChanged(evt);
            }
        });
        jScrollPane15.setViewportView(listUserCategories);

        jLabel20.setText("Категории");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jLabel20)))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jButton6.setText("Сформировать");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton6MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton6)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel25, jPanel26, jPanel27});

        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel27, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel25, jPanel26, jPanel27});

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Добавление тестов"));

        jLabel8.setText("Входной файл");

        jLabel9.setText("Выходной файл");

        btnOutFile.setText("Открыть");
        btnOutFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOutFileMouseClicked(evt);
            }
        });

        txtOutFile.setToolTipText(bundle.getString("выходные файлы тестов")); // NOI18N

        btnInFile.setText("Открыть");
        btnInFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnInFileMouseClicked(evt);
            }
        });

        txtInFile.setToolTipText(bundle.getString("входные файлы тестов")); // NOI18N

        txtNum.setToolTipText(bundle.getString("номер теста")); // NOI18N

        jLabel11.setText("Номер теста");

        jButton3.setText("Добавить");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton3MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11))
                        .addGap(9, 9, 9))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtOutFile)
                    .addComponent(txtInFile, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnInFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnOutFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(228, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(btnInFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOutFile))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtInFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtOutFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtInFile, txtOutFile});

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Добавление задачи"));

        jLabel6.setText("Входной файл");

        jLabel7.setText("Выходной файл");

        jLabel5.setText("Имя программы");

        txtProblemIn.setToolTipText(bundle.getString("имя входного файла")); // NOI18N

        txtProblemProgram.setToolTipText(bundle.getString("название программы")); // NOI18N
        txtProblemProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProblemProgramActionPerformed(evt);
            }
        });

        txtProblemOut.setToolTipText(bundle.getString("имя выходного файла")); // NOI18N

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtProblemOut, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtProblemIn, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtProblemProgram, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProblemProgram, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProblemIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProblemOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap())
        );

        jLabel2.setText("Файл условия");

        btnProblemFile.setText("Открыть");
        btnProblemFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProblemFileMouseClicked(evt);
            }
        });

        jLabel1.setText("Название");

        jLabel3.setText("Сравнитель");

        txtProblemName.setToolTipText(bundle.getString("Название новой задачи")); // NOI18N

        btnComparorFile.setText("Открыть");
        btnComparorFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnComparorFileMouseClicked(evt);
            }
        });

        txtProblemTime.setToolTipText(bundle.getString("тайм лимит")); // NOI18N

        txtProblemFile.setToolTipText(bundle.getString("файл условия")); // NOI18N

        txtProblemComparor.setToolTipText(bundle.getString("Путь к файлу сравнителя")); // NOI18N

        jLabel4.setText("Тайм-лимит");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtProblemTime)
                    .addComponent(txtProblemComparor)
                    .addComponent(txtProblemFile)
                    .addComponent(txtProblemName, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnProblemFile)
                    .addComponent(btnComparorFile))
                .addGap(12, 12, 12))
        );

        jPanel24Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnComparorFile, btnProblemFile});

        jPanel24Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtProblemComparor, txtProblemFile, txtProblemName, txtProblemTime});

        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProblemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProblemFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btnProblemFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProblemComparor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(btnComparorFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProblemTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap())
        );

        jButton2.setText("Добавить");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton2MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(132, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );

        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder("Добавление категории"));

        txtNewCategory.setToolTipText(bundle.getString("Имя новой категории")); // NOI18N
        txtNewCategory.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel22.setText("Название категории");

        jButton1.setText("Добавить");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNewCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtNewCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(360, 360, 360))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Добавление решений"));

        btnOpenSolution.setText("Открыть");
        btnOpenSolution.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenSolutionMouseClicked(evt);
            }
        });

        jLabel13.setText("Решения");

        txtSolution.setToolTipText(bundle.getString("Папка с решениями")); // NOI18N

        isDozdCheckBox.setText("досдача");

        jButton5.setText("Добавить");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton5MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSolution, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(isDozdCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnOpenSolution, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(197, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtSolution, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenSolution))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(isDozdCheckBox)
                    .addComponent(jButton5))
                .addGap(21, 21, 21))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sandra2010");

        jPanel1.setPreferredSize(new java.awt.Dimension(785, 710));
        jPanel1.setLayout(new org.jdesktop.swingx.VerticalLayout());

        jToolBar8.setBorder(null);
        jToolBar8.setFloatable(false);
        jToolBar8.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        jToolBar8.setRollover(true);
        jToolBar8.setPreferredSize(new java.awt.Dimension(361, 50));

        btnAddNewCategory.setText("Добавить подкатегорию");
        btnAddNewCategory.setToolTipText(bundle.getString("Добавление новой категории")); // NOI18N
        jToolBar8.add(btnAddNewCategory);

        btnRemoveCategory.setText("Удалить категорию");
        btnRemoveCategory.setToolTipText(bundle.getString("Удаление категории")); // NOI18N
        btnRemoveCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemoveCategoryMouseClicked(evt);
            }
        });
        jToolBar8.add(btnRemoveCategory);

        btnSaveCategory.setText("Сохранить изменения");
        btnSaveCategory.setToolTipText(bundle.getString("Сохранить изменения")); // NOI18N
        btnSaveCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveCategoryMouseClicked(evt);
            }
        });
        jToolBar8.add(btnSaveCategory);

        jPanel1.add(jToolBar8);

        AddCategoryCollapsiblePane.setCollapsed(true);
        jPanel1.add(AddCategoryCollapsiblePane);
        AddCategoryCollapsiblePane.add( jPanel30 );
        btnAddNewCategory.setAction( AddCategoryCollapsiblePane.getActionMap().get( "toggle" ) );
        btnAddNewCategory.setText( "Добавить категорию" );

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        contentTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        contentTree.setToolTipText(bundle.getString("Дерево категорий задач")); // NOI18N
        contentTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                contentTreeValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(contentTree);

        jPanel1.add(jScrollPane2);

        tblContent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblContent.setToolTipText(bundle.getString("Список задач выбранной категории")); // NOI18N
        tblContent.setDragEnabled(true);
        tblContent.setDropMode(javax.swing.DropMode.ON_OR_INSERT);
        tblContent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblContentKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblContent);
        tblContent.getAccessibleContext().setAccessibleName("problemsInCategoryTable");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addGap(34, 34, 34))
        );

        jPanel1.add(jPanel29);

        jTabbedPane1.addTab("Категории", jPanel1);

        jPanel3.setLayout(new org.jdesktop.swingx.VerticalLayout());

        jToolBar2.setBorder(null);
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setPreferredSize(new java.awt.Dimension(214, 50));

        btnAddProblem.setText("Добавить задачу");
        btnAddProblem.setToolTipText(bundle.getString("Добавить задачу")); // NOI18N
        jToolBar2.add(btnAddProblem);

        btnProblemRemove.setText("Удалить выделенные");
        btnProblemRemove.setToolTipText(bundle.getString("Удалить выделенные задачи")); // NOI18N
        btnProblemRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProblemRemoveMouseClicked(evt);
            }
        });
        jToolBar2.add(btnProblemRemove);

        jPanel3.add(jToolBar2);

        AddProblemCollapsiblePane.setCollapsed(true);
        jPanel3.add(AddProblemCollapsiblePane);
        AddProblemCollapsiblePane.add( jPanel8 );
        btnAddProblem.setAction( AddProblemCollapsiblePane.getActionMap().get( "toggle" ) );
        btnAddProblem.setText( "Добавить задачу" );

        problemsList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        problemsList.setToolTipText(bundle.getString("список всех задач")); // NOI18N
        problemsList.setDragEnabled(true);
        problemsList.setDropMode(javax.swing.DropMode.ON);
        jScrollPane5.setViewportView(problemsList);
        problemsList.getAccessibleContext().setAccessibleName("allProblemsTable");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel17);

        jTabbedPane1.addTab("Задачи", jPanel3);

        jPanel5.setLayout(new org.jdesktop.swingx.VerticalLayout());

        jToolBar3.setBorder(null);
        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);
        jToolBar3.setPreferredSize(new java.awt.Dimension(200, 50));

        btnAddTest.setText("Добавить тест");
        btnAddTest.setToolTipText(bundle.getString("добавление тестов")); // NOI18N
        jToolBar3.add(btnAddTest);

        btnTestSave.setText("Сохранить изменения");
        btnTestSave.setToolTipText(bundle.getString("Сохранить изменения")); // NOI18N
        btnTestSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTestSaveMouseClicked(evt);
            }
        });
        jToolBar3.add(btnTestSave);

        jPanel5.add(jToolBar3);

        AddTestCollapsiblePane.setCollapsed(true);
        jPanel5.add(AddTestCollapsiblePane);
        AddTestCollapsiblePane.add( jPanel7 );

        btnAddTest.setAction( AddTestCollapsiblePane.getActionMap().get( "toggle" ) );
        btnAddTest.setText( "Добавить тесты" );

        tblTests.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        tblTests.setToolTipText(bundle.getString("Таблица всех тестов для задачи")); // NOI18N
        tblTests.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblTestsKeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(tblTests);

        listCurProblem.setToolTipText(bundle.getString("задача для которой добавляются тесты")); // NOI18N
        listCurProblem.setDragEnabled(true);
        listCurProblem.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listCurProblemValueChanged(evt);
            }
        });
        jScrollPane7.setViewportView(listCurProblem);
        listCurProblem.getAccessibleContext().setAccessibleName("testProblem");

        jLabel10.setText("Задача");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel18);

        jTabbedPane1.addTab("Тесты", jPanel5);

        jPanel11.setLayout(new org.jdesktop.swingx.VerticalLayout());

        jToolBar1.setBorder(null);
        jToolBar1.setFloatable(false);
        jToolBar1.setForeground(new java.awt.Color(153, 153, 153));
        jToolBar1.setRollover(true);
        jToolBar1.setPreferredSize(new java.awt.Dimension(337, 50));

        btnAddCompiler.setText("Добавить компилятор");
        btnAddCompiler.setToolTipText(bundle.getString("добавить компилятор")); // NOI18N
        jToolBar1.add(btnAddCompiler);

        btnCompilerSave.setText("Сохранить изменения");
        btnCompilerSave.setToolTipText(bundle.getString("Сохранить изменения")); // NOI18N
        btnCompilerSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCompilerSaveMouseClicked(evt);
            }
        });
        jToolBar1.add(btnCompilerSave);

        btnCompilersRefresh.setText("Обновить таблицу");
        btnCompilersRefresh.setToolTipText(bundle.getString("Обновить таблицу")); // NOI18N
        btnCompilersRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCompilersRefreshMouseClicked(evt);
            }
        });
        jToolBar1.add(btnCompilersRefresh);

        jPanel11.add(jToolBar1);

        addCompilerCollapsiblePane.setCollapsed(true);
        jPanel11.add(addCompilerCollapsiblePane);
        addCompilerCollapsiblePane.add( jPanel12 );
        btnAddCompiler.setAction( addCompilerCollapsiblePane.getActionMap().get( "toggle" ) );
        btnAddCompiler.setText( "Добавить компилятор" );

        tblCompilers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblCompilers.setToolTipText(bundle.getString("Таблица компиляторов")); // NOI18N
        tblCompilers.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblCompilersKeyPressed(evt);
            }
        });
        jScrollPane10.setViewportView(tblCompilers);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel11.add(jPanel19);

        jTabbedPane1.addTab("Компиляторы", jPanel11);

        jPanel9.setLayout(new org.jdesktop.swingx.VerticalLayout());

        jToolBar6.setBorder(null);
        jToolBar6.setFloatable(false);
        jToolBar6.setRollover(true);
        jToolBar6.setPreferredSize(new java.awt.Dimension(452, 50));

        btnAddSolution.setText("Добавить решение");
        btnAddSolution.setToolTipText(bundle.getString("Добавить решение")); // NOI18N
        jToolBar6.add(btnAddSolution);

        btnCheck.setText("Проверить выделенные");
        btnCheck.setToolTipText(bundle.getString("Проверить выделенные")); // NOI18N
        btnCheck.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCheckMouseClicked(evt);
            }
        });
        jToolBar6.add(btnCheck);

        btnRefreshSolTables.setText("Обновить таблици");
        btnRefreshSolTables.setToolTipText(bundle.getString("Обновить таблицу")); // NOI18N
        btnRefreshSolTables.setFocusable(false);
        btnRefreshSolTables.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRefreshSolTables.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRefreshSolTables.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshSolTablesMouseClicked(evt);
            }
        });
        jToolBar6.add(btnRefreshSolTables);

        btnRemoveSolutions.setText("Удалить выделенные");
        btnRemoveSolutions.setToolTipText(bundle.getString("Удалить выделенные решения")); // NOI18N
        btnRemoveSolutions.setFocusable(false);
        btnRemoveSolutions.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemoveSolutions.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveSolutions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemoveSolutionsMouseClicked(evt);
            }
        });
        jToolBar6.add(btnRemoveSolutions);

        jPanel9.add(jToolBar6);

        addSolCollapsiblePane.setCollapsed(true);
        jPanel9.add(addSolCollapsiblePane);
        addSolCollapsiblePane.add( jPanel10 );
        btnAddSolution.setAction( addSolCollapsiblePane.getActionMap().get( "toggle" ) );
        btnAddSolution.setText( "Добавить решения" );

        tblNeedTest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblNeedTest.setToolTipText(bundle.getString("Очередь на проверку")); // NOI18N
        jScrollPane8.setViewportView(tblNeedTest);

        tblSolutions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSolutions.setToolTipText(bundle.getString("Таблица решений")); // NOI18N
        jScrollPane9.setViewportView(tblSolutions);

        listSolToShow.setToolTipText(bundle.getString("Фильтр решений")); // NOI18N
        listSolToShow.setDragEnabled(true);
        listSolToShow.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listSolToShowValueChanged(evt);
            }
        });
        listSolToShow.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                listSolToShowKeyPressed(evt);
            }
        });
        jScrollPane16.setViewportView(listSolToShow);
        listSolToShow.getAccessibleContext().setAccessibleName("solToShow");

        jLabel23.setText("Задача");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(45, 45, 45)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel23)
                    .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel9.add(jPanel20);

        jTabbedPane1.addTab("Решения", jPanel9);

        txtLog.setEditable(false);
        txtLog.setToolTipText(bundle.getString("Консоль тестирования")); // NOI18N
        jScrollPane3.setViewportView(txtLog);

        jToolBar4.setBorder(null);
        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        btnStart.setText("Запустить тестирование");
        btnStart.setToolTipText(bundle.getString("Запустить Тестирование")); // NOI18N
        btnStart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnStartMouseClicked(evt);
            }
        });
        jToolBar4.add(btnStart);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jToolBar4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        jTabbedPane1.addTab("Тестирование", jPanel2);

        jPanel6.setLayout(new org.jdesktop.swingx.VerticalLayout());

        jToolBar7.setBorder(null);
        jToolBar7.setFloatable(false);
        jToolBar7.setRollover(true);
        jToolBar7.setPreferredSize(new java.awt.Dimension(212, 50));

        btnShowResult.setText("Сформировать таблицу");
        btnShowResult.setToolTipText(bundle.getString("Сформировать таблицу")); // NOI18N
        jToolBar7.add(btnShowResult);

        btnExcelExport.setText("Экспорт в Excel");
        btnExcelExport.setToolTipText(bundle.getString("Экспорт в Excel")); // NOI18N
        btnExcelExport.setFocusable(false);
        btnExcelExport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExcelExport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExcelExport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExcelExportMouseClicked(evt);
            }
        });
        jToolBar7.add(btnExcelExport);

        jPanel6.add(jToolBar7);

        resultsCollapsiblePane.setCollapsed(true);
        jPanel6.add(resultsCollapsiblePane);
        resultsCollapsiblePane.add( jPanel13 );
        btnShowResult.setAction( resultsCollapsiblePane.getActionMap().get( "toggle" ) );
        btnShowResult.setText( "Формирование рейтинга" );

        tblResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Пользователь", "Задача 1", "Задача 2", "Задача 3"
            }
        ));
        tblResults.setToolTipText(bundle.getString("Таблица результатов")); // NOI18N
        jScrollPane11.setViewportView(tblResults);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.add(jPanel21);

        jTabbedPane1.addTab("Результаты", jPanel6);

        jPanel15.setLayout(new org.jdesktop.swingx.VerticalLayout());

        jToolBar5.setBorder(null);
        jToolBar5.setFloatable(false);
        jToolBar5.setRollover(true);
        jToolBar5.setPreferredSize(new java.awt.Dimension(357, 50));

        btnAddUser.setText("Добавить пользователя");
        btnAddUser.setToolTipText(bundle.getString("Добавить пользователя")); // NOI18N
        jToolBar5.add(btnAddUser);

        btnUsersRemove.setText("Удалить пользователя");
        btnUsersRemove.setToolTipText(bundle.getString("Удалить пользователя")); // NOI18N
        btnUsersRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUsersRemoveMouseClicked(evt);
            }
        });
        jToolBar5.add(btnUsersRemove);

        btnUsersRefresh.setText("Обновить таблицу");
        btnUsersRefresh.setToolTipText(bundle.getString("Обновить таблицу")); // NOI18N
        btnUsersRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUsersRefreshMouseClicked(evt);
            }
        });
        jToolBar5.add(btnUsersRefresh);

        jPanel15.add(jToolBar5);

        addUserCollapsiblePane.setCollapsed(true);
        jPanel15.add(addUserCollapsiblePane);
        addUserCollapsiblePane.add( jPanel16 );
        btnAddUser.setAction( addUserCollapsiblePane.getActionMap().get( "toggle" ) );
        btnAddUser.setText( "Добавить пользователя" );

        tblUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblUsers.setToolTipText(bundle.getString("Таблица пользователей")); // NOI18N
        jScrollPane14.setViewportView(tblUsers);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel15.add(jPanel22);

        jTabbedPane1.addTab("Пользователи", jPanel15);

        jPanel14.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        tempList.setToolTipText(bundle.getString("буфер для задач")); // NOI18N
        tempList.setDragEnabled(true);
        tempList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tempListKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(tempList);
        tempList.getAccessibleContext().setAccessibleName("problemsBuffer");

        progressBar.setToolTipText(bundle.getString("локальный поргрес тестирования")); // NOI18N

        totalProgress.setForeground(new java.awt.Color(204, 255, 204));
        totalProgress.setToolTipText(bundle.getString("общий прогрес тестировния")); // NOI18N

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalProgress, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(totalProgress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(364, 364, 364))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                .addGap(32, 32, 32))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu1.setText("Меню");

        jMenu3.setText("Стиль");

        nimbusLookMenuItem.setText("Nimbus");
        nimbusLookMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                nimbusLookMenuItemMousePressed(evt);
            }
        });
        jMenu3.add(nimbusLookMenuItem);

        windowsLookMenuItem.setText("Windows");
        windowsLookMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                windowsLookMenuItemMousePressed(evt);
            }
        });
        jMenu3.add(windowsLookMenuItem);

        lightThemeMenuItem.setText("Light");
        lightThemeMenuItem.setSelected(true);
        lightThemeMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lightThemeMenuItemMousePressed(evt);
            }
        });
        jMenu3.add(lightThemeMenuItem);

        BusinessThemeMenuItem.setText("Medium");
        BusinessThemeMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                BusinessThemeMenuItemMousePressed(evt);
            }
        });
        jMenu3.add(BusinessThemeMenuItem);

        darkThemeMenuItem.setText("Dark");
        darkThemeMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                darkThemeMenuItemMousePressed(evt);
            }
        });
        jMenu3.add(darkThemeMenuItem);

        jMenu1.add(jMenu3);

        jMenu2.setText("База данных");

        DBExportMenuItem.setText("Експорт в файл");
        DBExportMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                DBExportMenuItemMousePressed(evt);
            }
        });
        jMenu2.add(DBExportMenuItem);

        DBImportMenuItem.setText("Импорт из файла");
        DBImportMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                DBImportMenuItemMousePressed(evt);
            }
        });
        jMenu2.add(DBImportMenuItem);

        jMenu4.setText("Таблицы");

        jMenuItem2.setText("Компиляторы");
        jMenuItem2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem2MousePressed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuItem3.setText("Пользователи");
        jMenuItem3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem3MousePressed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        jMenuItem4.setText("Очередь на проверку");
        jMenuItem4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem4MousePressed(evt);
            }
        });
        jMenu4.add(jMenuItem4);

        jMenuItem5.setText("Категории задач");
        jMenuItem5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem5MousePressed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuItem6.setText("Задачи");
        jMenuItem6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem6MousePressed(evt);
            }
        });
        jMenu4.add(jMenuItem6);

        jMenuItem7.setText("Результаты");
        jMenuItem7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem7MousePressed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuItem8.setText("Тесты");
        jMenuItem8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem8MousePressed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuItem9.setText("Задачи-Категории");
        jMenuItem9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem9MousePressed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuItem10.setText("Решения");
        jMenuItem10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem10MousePressed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenu2.add(jMenu4);

        jMenu1.add(jMenu2);
        jMenu1.add(jSeparator1);

        jMenuItem1.setText("Выход");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem1MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MousePressed

        Runtime.getRuntime().exit( 0 );

    }//GEN-LAST:event_jMenuItem1MousePressed

    private void btnStartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStartMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;
        if ( tester != null && !tester.isInterrupted() ) return;

        tester = new OnlineTesterThread( txtLog, totalProgress, progressBar );
        tester.start();

    }//GEN-LAST:event_btnStartMouseClicked

    private void btnRemoveCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveCategoryMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        EntityTransaction et = null;

        try
        {

            TreePath path = contentTree.getSelectionPath();
            DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) path.getLastPathComponent();

            Integer catid = ( ( TreeNode ) node.getUserObject() ).getId();

            List res = em.createQuery( "SELECT obj FROM ProblemCategories obj WHERE obj.catid=" + catid ).getResultList();

            if ( ( ( ProblemCategories ) res.get( 0 ) ).getParentid() == 0 )
            {
                ErrorMessage.error( "Нельзя удалить корневую катеорию." );
                return;
            }

            et = em.getTransaction();
            et.begin();
            em.createQuery( "DELETE FROM ProblemCategoryAssign obj WHERE obj.catid=" + catid ).executeUpdate();
            et.commit();

            Trees.removeProblemSubtree( node, catid, false );

            ProblemCategories cat = ( ProblemCategories ) em.createQuery( "SELECT obj FROM ProblemCategories obj WHERE obj.parentid=0" ).getResultList().get( 0 );

            TreeModel model = new DefaultTreeModel( Trees.makeProblemTree( cat.getCatid() ) );
            contentTree.setModel( model );
        
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка удаления категории : " + e.toString() );
            
            try 
            {
                et.rollback();
            }
            catch ( Exception ex )
            {
                ErrorMessage.error( "Ошибка отката базы данных : " + ex.toString() );
            }
        }


    }//GEN-LAST:event_btnRemoveCategoryMouseClicked

    private void btnInFileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInFileMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        JFileChooser fc = new JFileChooser( new File( "." ) );

        fc.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );

        int res = fc.showOpenDialog( this );

        if ( res == JFileChooser.APPROVE_OPTION )
        {
            txtInFile.setText( fc.getSelectedFile().getAbsolutePath() );
        }

    }//GEN-LAST:event_btnInFileMouseClicked

    private void btnOutFileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOutFileMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        JFileChooser fc = new JFileChooser( new File( "." ) );

        fc.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );

        int res = fc.showOpenDialog( this );

        if ( res == JFileChooser.APPROVE_OPTION )
        {
            txtOutFile.setText( fc.getSelectedFile().getAbsolutePath() );
        }

    }//GEN-LAST:event_btnOutFileMouseClicked

    private void tempListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tempListKeyPressed

        if ( evt.getKeyCode() == KeyEvent.VK_DELETE )
        {
            JList list = ( JList ) evt.getSource();

            int[] indx = list.getSelectedIndices();

            for ( int i = indx.length - 1; i >= 0; i-- )
            {
                ( ( DefaultListModel ) list.getModel() ).remove( indx[ i ] );
            }
        }

    }//GEN-LAST:event_tempListKeyPressed

    public void refreshTestTable()
    {
        try
        {
            if ( ( ( DefaultListModel ) listCurProblem.getModel()).getSize() < 1 ) return;
            
            ProblemListItem prob = ( ProblemListItem ) ( ( DefaultListModel ) listCurProblem.getModel() ).getElementAt( 0 );

            Integer id = prob.getId();

            Query query = em.createQuery( "SELECT obj FROM Tests obj WHERE obj.pid=" + id + " ORDER BY obj.num ASC" );
            List tests = query.getResultList();

            String[] cols = { "Номер", "Входной файл", "Выходной файл" };
            DefaultTableModel model = new DefaultTableModel( cols, 0 )
            {
                @Override
                public boolean isCellEditable( int x, int y )
                {
                    return false;
                }
            };

            for ( int i = 0; i < tests.size(); i++ )
            {
                Object[] row = { ( ( entities.Tests ) tests.get( i ) ).getNum(),
                                 Paths.deconv( ( ( entities.Tests ) tests.get( i ) ).getTestpath() ),
                                 Paths.deconv( ( ( entities.Tests ) tests.get( i ) ).getAnspath() ) };
                model.addRow( row );
            }

            tblTests.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
            tblTests.setModel( model );

        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e );
        }       
    }

    private void listCurProblemValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listCurProblemValueChanged

        refreshTestTable();

    }//GEN-LAST:event_listCurProblemValueChanged

    private void btnProblemFileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProblemFileMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;
        
        JFileChooser fc = new JFileChooser( new File( "." ) );

        int res = fc.showOpenDialog( this );

        if ( res == JFileChooser.APPROVE_OPTION )
        {
            txtProblemFile.setText( fc.getSelectedFile().getAbsolutePath() );
        }

    }//GEN-LAST:event_btnProblemFileMouseClicked

    private void btnComparorFileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnComparorFileMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;
        
        JFileChooser fc = new JFileChooser( new File( "." ) );

        int res = fc.showOpenDialog( this );

        if ( res == JFileChooser.APPROVE_OPTION )
        {
            txtProblemComparor.setText( fc.getSelectedFile().getAbsolutePath() );
        }

    }//GEN-LAST:event_btnComparorFileMouseClicked

    private int cur = 1;
    private String[] infs = { ".dat", ".in" };
    private String[] outfs = { ".res", ".out" };

    private void txtProblemProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProblemProgramActionPerformed

        cur++;
        cur %= 2;

        txtProblemIn.setText( txtProblemProgram.getText() + infs[ cur ] );
        txtProblemOut.setText( txtProblemProgram.getText() + outfs[ cur ] );

    }//GEN-LAST:event_txtProblemProgramActionPerformed

    private void btnSaveCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveCategoryMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        EntityTransaction et = null;

        try
        {

            TreePath path = contentTree.getSelectionPath();
            DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) path.getLastPathComponent();
            TreeNode treeNode = ( TreeNode ) node.getUserObject();
            Integer catid = treeNode.getId();

            et = em.getTransaction();
            et.begin();
            
            em.createQuery( "DELETE FROM ProblemCategoryAssign obj WHERE obj.catid=" + catid ).executeUpdate();

            DefaultTableModel model = ( DefaultTableModel ) tblContent.getModel();

            for ( int i = 0; i < model.getRowCount(); i++ )
            {
                if ( model.getValueAt( i, 0 ) instanceof ProblemListItem )
                {
                    ProblemListItem item = ( ProblemListItem ) model.getValueAt( i, 0 );
                    em.persist( new entities.ProblemCategoryAssign( catid, item.getId() ) );
                }
            }

            et.commit();
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
            try
            {
                et.rollback();
            }
            catch ( Exception ex )
            {
                ErrorMessage.error( "Ошибка отката базы данных : " + ex.toString() );
            }
        }


    }//GEN-LAST:event_btnSaveCategoryMouseClicked

    private void contentTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_contentTreeValueChanged

        try
        {

            TreePath path = contentTree.getSelectionPath();

            if ( path == null ) return;

            DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) path.getLastPathComponent();
            TreeNode treeNode = ( TreeNode ) node.getUserObject();
            Integer catid = treeNode.getId();

            String[] cols = { "Название", "Условие", "Сравнитель", "Имя программы", "Входной файл", "Выходной файл", "Тайм-лимит" };
            DefaultTableModel model = new DefaultTableModel( cols, 0 )
            {
                @Override
                public boolean isCellEditable( int x, int y )
                {
                    return false;
                }
            };

            Query query = em.createQuery( "SELECT obj FROM ProblemCategoryAssign obj WHERE obj.catid=" + catid );
            List res = query.getResultList();

            for ( int i = 0; i < res.size(); i++ )
            {
                entities.ProblemCategoryAssign cur = ( entities.ProblemCategoryAssign ) res.get( i );
                Integer pid = cur.getPid();

                Query problemQuery = em.createQuery( "SELECT obj FROM Problems obj WHERE obj.pid=" + pid );
                Problems prob = ( Problems ) problemQuery.getSingleResult();
            
                ProblemListItem[] row = {
                            new ProblemListItem( prob.getTitle(), prob.getPid() ),
                            new ProblemListItem( Paths.deconv( prob.getFilepath() ), prob.getPid() ),
                            new ProblemListItem( Paths.deconv( prob.getComparor() ), prob.getPid() ),
                            new ProblemListItem( prob.getSolutionfile(), prob.getPid() ),
                            new ProblemListItem( prob.getInputfile(), prob.getPid() ),
                            new ProblemListItem( prob.getOutputfile(), prob.getPid() ),
                            new ProblemListItem( String.valueOf( prob.getTimelimit() ), prob.getPid() ) };
            
                model.addRow( row );
            }

            model.setRowCount( model.getRowCount() + 1 );

            tblContent.setModel( model );

        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
        }
        

    }//GEN-LAST:event_contentTreeValueChanged

    private void btnOpenSolutionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenSolutionMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;
        
        JFileChooser fc = new JFileChooser( new File( "." ) );

        fc.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );

        int res = fc.showOpenDialog( this );

        if ( res == JFileChooser.APPROVE_OPTION )
        {
            txtSolution.setText( fc.getSelectedFile().getAbsolutePath() );
        }

    }//GEN-LAST:event_btnOpenSolutionMouseClicked

    private void btnCompilerOpenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCompilerOpenMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;
        
        JFileChooser fc = new JFileChooser( new File( "." ) );

        int res = fc.showOpenDialog( this );

        if ( res == JFileChooser.APPROVE_OPTION )
        {
            txtCompiler.setText( fc.getSelectedFile().getAbsolutePath() );
        }
        
    }//GEN-LAST:event_btnCompilerOpenMouseClicked

    private void btnCheckMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCheckMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        EntityTransaction et = null;

        try
        {

            DefaultTableModel model = ( DefaultTableModel ) tblSolutions.getModel();

            et = em.getTransaction();
            et.begin();

            int[] rows = tblSolutions.getSelectedRows();

            //ErrorMessage.error( rows[ 0 ] + ",  " + rows[ 1 ] );

            for ( int i = 0; i < rows.length; i++ )
            {
                SolutionListItem item = ( SolutionListItem ) tblSolutions.getValueAt( rows[ i ], 0 );
                //ErrorMessage.error( "" + model.getValueAt( rows[ i ], 0 ) );
                //ErrorMessage.error( "" + tblSolutions.getValueAt( rows[ i ], 0 ) );

                em.persist( new NeedTest( new Solutions( item.getId() ) ) );
            }

            et.commit();

            initNeedTestTable();

        }
        catch ( Exception e )         
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
            try
            {
                et.rollback();
            }
            catch ( Exception ex )
            {
                ErrorMessage.error( "Ошибка отката базы данных : " + ex.toString() );
            }
        }

    }//GEN-LAST:event_btnCheckMouseClicked

    /**
     * задает правильное упорядочивание баллов вместо упорядочивания по алфавиту
     */
    private TableRowSorter< TableModel > setNumberSorter( DefaultTableModel model )
    {
        TableRowSorter< TableModel > sorter = new TableRowSorter< TableModel >( model );
        int i = 2;
        while ( i < model.getColumnCount() )
        {
            sorter.setComparator( i, new Comparator< String >()
            {
                public int compare( String a, String b )
                {
                    double dif = Double.parseDouble( a ) - Double.parseDouble( b );

                    if ( dif > 0 ) return 1;
                    if ( dif == 0 ) return 0;
                    return -1;
                }
            });
            i += 2;
        }

        sorter.setComparator( model.getColumnCount() - 1, new Comparator< String >()
        {
            public int compare( String a, String b )
            {
                double dif = Double.parseDouble( a ) - Double.parseDouble( b );

                if ( dif > 0 ) return 1;
                if ( dif == 0 ) return 0;
                return -1;
            }
        });

        return sorter;
    }

    private void listResProblemsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listResProblemsKeyPressed

        if ( evt.getKeyCode() == KeyEvent.VK_DELETE )
        {
            JList list = ( JList ) evt.getSource();

            int[] indx = list.getSelectedIndices();

            for ( int i = indx.length - 1; i >= 0; i-- )
            {
                ( ( DefaultListModel ) list.getModel() ).remove( indx[ i ] );
            }
        }
        
    }//GEN-LAST:event_listResProblemsKeyPressed

    private void btnTestSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTestSaveMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        EntityTransaction et = null;

        try
        {
            DefaultTableModel model = ( DefaultTableModel ) tblTests.getModel();

            Integer pid = ( ( ProblemListItem ) ( ( DefaultListModel ) listCurProblem.getModel() ).getElementAt( 0 ) ).getId();

            et = em.getTransaction();
            et.begin();

            em.createQuery( "DELETE FROM Tests obj WHERE obj.pid=" + pid ).executeUpdate();

            for ( int i = 0; i < model.getRowCount(); i++ )
            {
                Integer num = ( Integer ) model.getValueAt( i, 0 );
                String in = ( String ) model.getValueAt( i, 1 );
                String out = ( String )model.getValueAt( i, 2 );

                em.persist( new entities.Tests( pid, num, Paths.conv( in ), Paths.conv( out ) ) );
                //System.out.println( "TEST [ " + i + " ] = " + in + ", " + out );
            }

            et.commit();

            refreshTestTable();
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
            try
            {
                et.rollback();
            }
            catch ( Exception ex )
            {
                ErrorMessage.error( "Ошибка отката базы данных : " + ex.toString() );
            }
        }

    }//GEN-LAST:event_btnTestSaveMouseClicked

    private void tblTestsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTestsKeyPressed

        if ( evt.getKeyCode() == KeyEvent.VK_DELETE )
        {
            JTable table = ( JTable ) evt.getSource();

            int[] indx = table.getSelectedRows();

            for ( int i = indx.length - 1; i >= 0; i-- )
            {
                ( ( DefaultTableModel ) table.getModel() ).removeRow( indx[ i ] );
            }
        }

    }//GEN-LAST:event_tblTestsKeyPressed

    private void btnCompilerSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCompilerSaveMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        EntityTransaction et = null;

        try
        {
            DefaultTableModel model = ( DefaultTableModel ) tblCompilers.getModel();

            et = em.getTransaction();
            et.begin();

            em.createQuery( "DELETE FROM Compilers obj" ).executeUpdate();

            for ( int i = 0; i < model.getRowCount(); i++ )
            {
                String ext = ( String ) model.getValueAt( i, 0 );
                String compiler = ( String ) model.getValueAt( i, 1 );
                String param = ( String ) model.getValueAt( i, 2 );

                em.persist( new entities.Compilers( ext, Paths.conv( compiler ), Paths.conv( param ) ) );
            }

            et.commit();

            initCompilersTable();
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
            try
            {
                et.rollback();
            }
            catch ( Exception ex )
            {
                ErrorMessage.error( "Ошибка отката базы данных : " + ex.toString() );
            }
        }

    }//GEN-LAST:event_btnCompilerSaveMouseClicked

    private void tblCompilersKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCompilersKeyPressed

        if ( evt.getKeyCode() == KeyEvent.VK_DELETE )
        {
            JTable table = ( JTable ) evt.getSource();

            int[] indx = table.getSelectedRows();

            for ( int i = indx.length - 1; i >= 0; i-- )
            {
                ( ( DefaultTableModel ) table.getModel() ).removeRow( indx[ i ] );
            }
        }

    }//GEN-LAST:event_tblCompilersKeyPressed

    private void btnProblemRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProblemRemoveMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        EntityTransaction et = null;

        try
        {
            DefaultTableModel model = ( DefaultTableModel ) problemsList.getModel();

            int[] rows = problemsList.getSelectedRows();
        
            et = em.getTransaction();
            et.begin();

            for ( int i = rows.length - 1; i >= 0; i-- )
            {
                ProblemListItem item = ( ProblemListItem ) model.getValueAt( rows[ i ], 0 );

                DefaultListModel mod = ( DefaultListModel ) tempList.getModel();
                for ( int j = mod.getSize() - 1; j >= 0; j-- )
                {
                    if ( ( ( ProblemListItem ) mod.getElementAt( j ) ).getId().equals( item.getId() ) )
                    {
                        mod.removeElementAt( j );
                    }
                }
                
                Query query = em.createQuery( "SELECT obj FROM Solutions obj WHERE obj.pid=" + item.getId() );
                List res = query.getResultList();

                for ( int j = 0; j < res.size(); j++ )
                {
                    Solutions curSol = ( Solutions ) res.get( j );
                    em.createQuery( "DELETE FROM Results obj WHERE obj.sid=" + curSol.getSid() ).executeUpdate();
                }
                
                em.createQuery( "DELETE FROM Solutions obj WHERE obj.pid=" + item.getId() ).executeUpdate();
                em.createQuery( "DELETE FROM ProblemCategoryAssign obj WHERE obj.pid=" + item.getId() ).executeUpdate();
                em.createQuery( "DELETE FROM Tests obj WHERE obj.pid=" + item.getId() ).executeUpdate();
                em.createQuery( "DELETE FROM Problems obj WHERE obj.pid=" + item.getId() ).executeUpdate();

                model.removeRow( rows[ i ] );
            }

            et.commit();

            initProblemsList();
            initSolutionsTable();
            initCategoryTree();
            initContentTable();
            initTestTable();
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
            try
            {
                et.rollback();
            }
            catch ( Exception ex )
            {
                ErrorMessage.error( "Ошибка отката базы данных: " + ex.toString() );
            }
        }

    }//GEN-LAST:event_btnProblemRemoveMouseClicked

    private void tblContentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblContentKeyPressed

        if ( evt.getKeyCode() == KeyEvent.VK_DELETE )
        {
            JTable table = ( JTable ) evt.getSource();

            int[] indx = table.getSelectedRows();

            int len = ( indx[ indx.length - 1 ] == table.getRowCount() - 1 ) ? indx.length - 2 : indx.length - 1;

            for ( int i = len; i >= 0; i-- )
            {
                ( ( DefaultTableModel ) table.getModel() ).removeRow( indx[ i ] );
            }
        }

    }//GEN-LAST:event_tblContentKeyPressed

    private void btnCompilersRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCompilersRefreshMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;
        initCompilersTable();
        
    }//GEN-LAST:event_btnCompilersRefreshMouseClicked

    private void btnUsersRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUsersRefreshMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;
        initUsersTable();

    }//GEN-LAST:event_btnUsersRefreshMouseClicked

    private void btnUsersRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUsersRemoveMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        EntityTransaction et = null;

        try
        {
            DefaultTableModel model = ( DefaultTableModel ) tblUsers.getModel();

            int[] rows = tblUsers.getSelectedRows();

            et = em.getTransaction();
            et.begin();

            for ( int i = rows.length - 1; i >= 0; i-- )
            {
                UserListItem item = ( UserListItem ) model.getValueAt( rows[ i ], 0 );

                Query query = em.createQuery( "SELECT obj FROM Solutions obj WHERE obj.uid=" + item.getId() );
                List res = query.getResultList();

                for ( int j = 0; j < res.size(); j++ )
                {
                    Solutions curSol = ( Solutions ) res.get( j );
                    em.createQuery( "DELETE FROM Results obj WHERE obj.sid=" + curSol.getSid() ).executeUpdate();
                }

                em.createQuery( "DELETE FROM Solutions obj WHERE obj.uid=" + item.getId() ).executeUpdate();
                em.createQuery( "DELETE FROM Users obj WHERE obj.uid=" + item.getId() ).executeUpdate();

                model.removeRow( rows[ i ] );
            }

            et.commit();

            initUsersTable();
            initSolutionsTable();
            initResultUserList();

        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
            try
            {
                et.rollback();
            }
            catch ( Exception ex )
            {
                ErrorMessage.error( "Ошибка отката базы данных : " + ex.toString() );
            }
        }

    }//GEN-LAST:event_btnUsersRemoveMouseClicked

    private void btnExcelExportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExcelExportMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;
        try
        {
            JFileChooser fc = new JFileChooser( new File( "." ) );

            int res = fc.showOpenDialog( this );

            if ( res == JFileChooser.APPROVE_OPTION )
            {
                File excel = fc.getSelectedFile();

                WritableWorkbook wb = Workbook.createWorkbook( excel );

                WritableSheet sheet = wb.createSheet( "RESULTS", wb.getNumberOfSheets() );

                DefaultTableModel model = ( DefaultTableModel ) tblResults.getModel();

                // названия столбиков
                for ( int i = 0; i < model.getColumnCount(); i++ )
                {
                    WritableFont font = new WritableFont( WritableFont.COURIER, 10, WritableFont.BOLD );
                    font.setColour( Colour.BLUE_GREY );

                    WritableCellFormat format = new WritableCellFormat( font );
                    format.setBackground( Colour.LIGHT_GREEN );

                    Label lab = new Label( i, 0, model.getColumnName( i ), format );

                    sheet.addCell( lab );
                }

                // все результаты
                for ( int i = 0; i < model.getRowCount(); i++ )
                {
                    for ( int j = 0; j < model.getColumnCount(); j++ )
                    {
                        WritableFont font = new WritableFont( WritableFont.COURIER, 10 );
                        font.setColour( Colour.BLACK );

                        WritableCellFormat format = new WritableCellFormat( font );

                        Label lab = new Label( j, i + 1, model.getValueAt( i, j ).toString(), format );

                        jxl.write.WritableCell wc = null;

                        try
                        {
                            int ball = Integer.parseInt( model.getValueAt( i, j ).toString() );
                            jxl.write.Number num = new jxl.write.Number( j, i + 1, ball  );
                            wc = num;
                        }
                        catch ( Exception e )
                        {
                            wc = lab;
                        }
                        sheet.addCell( wc );
                    }
                }

                wb.write();
                wb.close();
            }
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
        }
    }//GEN-LAST:event_btnExcelExportMouseClicked

    private void btnRefreshSolTablesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshSolTablesMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        initSolutionsTable();
        initNeedTestTable();

    }//GEN-LAST:event_btnRefreshSolTablesMouseClicked

    private void listUserCategoriesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listUserCategoriesValueChanged

        listResUsers.clearSelection();

        DefaultListModel model = ( DefaultListModel ) listResUsers.getModel();

        DefaultListModel mod = ( DefaultListModel ) listUserCategories.getModel();
        Set< String > catset = new HashSet< String >();
        int[] catind = listUserCategories.getSelectedIndices();
        for ( int i = 0; i < catind.length; i++ )
        {
            catset.add( ( String ) mod.get( catind[ i ] ) );
        }

        ArrayList< Integer > inx = new ArrayList< Integer >();
        for ( int i = 0; i < model.getSize(); i++ )
        {
            UserListItem item = ( UserListItem ) model.get( i );
            Integer id = item.getId();
            List res = em.createQuery( "SELECT obj FROM Users obj WHERE obj.uid=" + id ).getResultList();

            if ( res.size() == 1 )
            {
                Users user = ( Users ) res.get( 0 );
                if ( catset.contains( user.getUsercategory() ) )
                {
                    inx.add( i );
                }
            }
        }

        int[] idx = new int[ inx.size() ];
        for ( int i = 0; i < idx.length; i++ )
        {
            idx[ i ] = ( Integer ) inx.get( i );
        }
        
        listResUsers.setSelectedIndices( idx );

    }//GEN-LAST:event_listUserCategoriesValueChanged

    private void btnRemoveSolutionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveSolutionsMouseClicked

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        EntityTransaction et = null;

        try
        {
            DefaultTableModel model = ( DefaultTableModel ) tblSolutions.getModel();
            int[] rows = tblSolutions.getSelectedRows();

            et = em.getTransaction();
            et.begin();

            for ( int i = 0; i < rows.length; i++ )
            {
                SolutionListItem item = ( SolutionListItem ) model.getValueAt( rows[ i ], 0 );
                Integer id = item.getId();

                em.createQuery( "DELETE FROM Results obj WHERE obj.sid=" + id ).executeUpdate();
                em.createQuery( "DELETE FROM Solutions obj WHERE obj.sid=" + id).executeUpdate();
            }

            et.commit();

            initSolutionsTable();
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
            try
            {
                et.rollback();
            }
            catch ( Exception ex )
            {
                ErrorMessage.error( "Ошибка отката базы данных : " + ex.toString() );
            }
            
        }

    }//GEN-LAST:event_btnRemoveSolutionsMouseClicked

    private void DBExportMenuItemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DBExportMenuItemMousePressed

        try
        {

            JFileChooser fc = new JFileChooser( new File( "." ) );

            fc.setFileSelectionMode( JFileChooser.FILES_ONLY );
            fc.setDialogType( JFileChooser.SAVE_DIALOG );

            int res = fc.showOpenDialog( this );

            if ( res == JFileChooser.APPROVE_OPTION )
            {
                File dbOutFile = new File( fc.getSelectedFile().getAbsolutePath() );
                PrintWriter dbOut = new PrintWriter( dbOutFile );

                ////////////////

                String sep = StringOperator.separator();

                List<Compilers> compilers = ( List<Compilers> ) em.createQuery( "SELECT obj FROM Compilers obj" ).getResultList();
                for ( Compilers cur : compilers )
                {
                    dbOut.println( Paths.conv( "compilers" + sep + cur.getCpid() + sep + "'" + cur.getExtensions() + "'" + sep + "'" + cur.getCompilerpath() + "'" + sep + "'" + cur.getParams() + "'" + sep ) );
                }

                List<ProblemCategories> pc = ( List<ProblemCategories> ) em.createQuery( "SELECT obj FROM ProblemCategories obj" ).getResultList();
                for ( ProblemCategories cur : pc )
                {
                    dbOut.println( Paths.conv( "problem_categories" + sep + cur.getCatid() + sep + "'" + cur.getName() + "'" + sep + cur.getParentid() + sep ) );
                }

                List<ProblemCategoryAssign> pca = ( List<ProblemCategoryAssign> ) em.createQuery( "SELECT obj FROM ProblemCategoryAssign obj" ).getResultList();
                for ( ProblemCategoryAssign cur : pca )
                {
                    dbOut.println( Paths.conv( "problem_category_assign" + sep + cur.getAssignid() + sep + cur.getCatid() + sep + cur.getPid() + sep ) );
                }

                List<Problems> problems = ( List<Problems> ) em.createQuery( "SELECT obj FROM Problems obj" ).getResultList();
                for ( Problems cur : problems )
                {
                    dbOut.println( Paths.conv( "problems" + sep + cur.getPid() + sep + "'" + cur.getTitle() + "'" + sep + "'" + cur.getFilepath() + "'" + sep + "'" + cur.getComparor() + "'" + sep + "'" + cur.getSolutionfile() + "'" + sep + "'" + cur.getInputfile() + "'" + sep + "'" + cur.getOutputfile() + "'" + sep + cur.getTimelimit() + sep ) );
                }

                List<Results> results = ( List<Results> ) em.createQuery( "SELECT obj FROM Results obj" ).getResultList();
                for ( Results cur : results )
                {
                    dbOut.println( Paths.conv( "results" + sep + cur.getRid() + sep + cur.getSid() + sep + "'" + cur.getResults() + "'" + sep ) );
                }

                List<Solutions> sols = ( List<Solutions> ) em.createQuery( "SELECT obj FROM Solutions obj" ).getResultList();
                for ( Solutions cur : sols )
                {
                    dbOut.println( Paths.conv( "solutions" + sep + cur.getSid() + sep + cur.getUid() + sep + cur.getPid() + sep + cur.getCount() + sep + cur.getIsdozd() + sep + "'" + cur.getIntime() + "'" + sep + "'" + cur.getFilepath() + "'" + sep ) );
                }

                List<Tests> tests = ( List<Tests> ) em.createQuery( "SELECT obj FROM Tests obj" ).getResultList();
                for ( Tests cur : tests )
                {
                    dbOut.println( Paths.conv( "tests" + sep + cur.getTid() + sep + cur.getPid() + sep + cur.getNum() + sep + "'" + cur.getTestpath() + "'" + sep + "'" + cur.getAnspath() + "'" + sep ) );
                }
                
                List<Users> users = ( List<Users> ) em.createQuery( "SELECT obj FROM Users obj" ).getResultList();
                for ( Users cur : users )
                {
                    dbOut.println( Paths.conv( "users" + sep + cur.getUid() + sep + "'" + cur.getName() + "'" + sep + "'" + ( (cur.getPassword() == null ) ? "" : cur.getPassword() ) + "'" + sep + "'" + cur.getUsercategory() + "'" + sep + "'" + cur.getFio() + "'" + sep + "'" + cur.getPriority() + "'" + sep ) );
                }
                ////////////////

                dbOut.close();
            }


        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка експорта базы данных : " + e.toString() );
        }

    }//GEN-LAST:event_DBExportMenuItemMousePressed

    private void DBImportMenuItemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DBImportMenuItemMousePressed

        JFileChooser fc = new JFileChooser( new File( "." ) );

        fc.setFileSelectionMode( JFileChooser.FILES_ONLY );
        fc.setDialogType( JFileChooser.OPEN_DIALOG );

        int res = fc.showOpenDialog( this );

        if ( res == JFileChooser.APPROVE_OPTION )
        {

            File dbInFile = new File( fc.getSelectedFile().getAbsolutePath() );
            if ( !dbInFile.exists() )
            {
                ErrorMessage.error( "Выбранный файл не существует" );
                return;
            }

            EntityTransaction et = null;

            try
            {

                et = em.getTransaction();
                et.begin();

                em.createQuery( "DELETE FROM Compilers obj" ).executeUpdate();
                em.createQuery( "DELETE FROM NeedTest obj" ).executeUpdate();
                em.createQuery( "DELETE FROM ProblemCategories obj" ).executeUpdate();
                em.createQuery( "DELETE FROM ProblemCategoryAssign obj" ).executeUpdate();
                em.createQuery( "DELETE FROM Problems obj" ).executeUpdate();
                em.createQuery( "DELETE FROM Results obj" ).executeUpdate();
                em.createQuery( "DELETE FROM Solutions obj" ).executeUpdate();
                em.createQuery( "DELETE FROM Tests obj" ).executeUpdate();
                em.createQuery( "DELETE FROM Users obj" ).executeUpdate();

                Scanner dbIn = new Scanner( dbInFile );
                while ( dbIn.hasNextLine() )
                {
                    String s = dbIn.nextLine();
                    String[] parts = StringOperator.split( s );
                    String params = StringOperator.comas( parts, 1 );

                    // DEBUG!!
                    //if ( parts[ 0 ].equals( "compilers" ) )
                    //    ErrorMessage.error( "DEBUG : " + parts[ 0 ] + "!!" + params );

                    em.createNativeQuery( "INSERT INTO " + parts[ 0 ] + " VALUES( " + params + ")" ).executeUpdate();
                }

                et.commit();

                this.initCategoryTree();
                this.initCompilersTable();
                this.initContentTable();
                this.initNeedTestTable();
                this.initProblemsList();
                this.initResultUserList();
                this.initSolutionsTable();
                this.initTestTable();
                this.initUserCategoryList();
                this.initUsersTable();
                
            }
            catch ( Exception e )
            {
                ErrorMessage.error( "Ошибка импорта базы из файла : " + e.toString() );
                try
                {
                    et.rollback();
                }
                catch ( Exception ex )
                {
                    ErrorMessage.error( "Ошибка возврата базы данных : " + ex.toString() );
                }
            }
        }

    }//GEN-LAST:event_DBImportMenuItemMousePressed

    private void windowsLookMenuItemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_windowsLookMenuItemMousePressed
        try
        {
            for ( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() )
            {
                if ( "Windows".equals( info.getName() ) )
                {
                    UIManager.setLookAndFeel( info.getClassName() );
                    SwingUtilities.updateComponentTreeUI( this );
                    break;
                }
            }
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка смены стиля : " + e.toString() );
        }
    }//GEN-LAST:event_windowsLookMenuItemMousePressed

    private void nimbusLookMenuItemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nimbusLookMenuItemMousePressed
        try
        {
            for ( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() )
            {
                if ( "Nimbus".equals( info.getName() ) )
                {
                    UIManager.setLookAndFeel( info.getClassName() );
                    SwingUtilities.updateComponentTreeUI( this );
                    break;
                }
            }
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка смены стиля : " + e.toString() );
        }
    }//GEN-LAST:event_nimbusLookMenuItemMousePressed

    private void lightThemeMenuItemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lightThemeMenuItemMousePressed
        try
        {
            final javax.swing.JFrame finalthis = this;
            EventQueue.invokeLater( new Runnable()
            {
                public void run()
                {
                    SubstanceLookAndFeel.setSkin("org.pushingpixels.substance.api.skin.NebulaBrickWallSkin");
                    //UIManager.setLookAndFeel( "org.pushingpixels.substance.api.skin.SubstanceNebulaBrickWallLookAndFeel" );
                    SwingUtilities.updateComponentTreeUI( finalthis );
                }
            } );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка смены стиля : " + e.toString() );
        }
    }//GEN-LAST:event_lightThemeMenuItemMousePressed

    private void darkThemeMenuItemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_darkThemeMenuItemMousePressed
        try
        {
            SubstanceLookAndFeel.setSkin( "org.pushingpixels.substance.api.skin.GraphiteGlassSkin" );
            //UIManager.setLookAndFeel( "org.pushingpixels.substance.api.skin.SubstanceGraphiteGlassLookAndFeel" );
            SwingUtilities.updateComponentTreeUI( this );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка смены стиля : " + e.toString() );
        }
    }//GEN-LAST:event_darkThemeMenuItemMousePressed

    private void jMenuItem2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MousePressed

        try
        {
            Runtime.getRuntime().exec( "javaw -jar Compilers.jar" );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Невозможно запустить : " + e.toString() );
        }

    }//GEN-LAST:event_jMenuItem2MousePressed

    private void jMenuItem3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem3MousePressed

        try
        {
            Runtime.getRuntime().exec( "javaw -jar Users.jar" );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Невозможно запустить : " + e.toString() );
        }

    }//GEN-LAST:event_jMenuItem3MousePressed

    private void jMenuItem4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem4MousePressed

        try
        {
            Runtime.getRuntime().exec( "javaw -jar NeedToTest.jar" );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Невозможно запустить : " + e.toString() );
        }

    }//GEN-LAST:event_jMenuItem4MousePressed

    private void jMenuItem5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem5MousePressed

        try
        {
            Runtime.getRuntime().exec( "javaw -jar ProblemCategories.jar" );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Невозможно запустить : " + e.toString() );
        }

    }//GEN-LAST:event_jMenuItem5MousePressed

    private void jMenuItem6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem6MousePressed

        try
        {
            Runtime.getRuntime().exec( "javaw -jar Problems.jar" );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Невозможно запустить : " + e.toString() );
        }

    }//GEN-LAST:event_jMenuItem6MousePressed

    private void jMenuItem7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem7MousePressed

        try
        {
            Runtime.getRuntime().exec( "javaw -jar Results.jar" );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Невозможно запустить : " + e.toString() );
        }

    }//GEN-LAST:event_jMenuItem7MousePressed

    private void jMenuItem8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem8MousePressed

        try
        {
            Runtime.getRuntime().exec( "javaw -jar Tests.jar" );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Невозможно запустить : " + e.toString() );
        }

    }//GEN-LAST:event_jMenuItem8MousePressed

    private void jMenuItem9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem9MousePressed

        try
        {
            Runtime.getRuntime().exec( "javaw -jar ProblemCategoryAssign.jar" );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Невозможно запустить : " + e.toString() );
        }

    }//GEN-LAST:event_jMenuItem9MousePressed

    private void jMenuItem10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem10MousePressed

        try
        {
            Runtime.getRuntime().exec( "javaw -jar Solutions.jar" );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Невозможно запустить : " + e.toString() );
        }

    }//GEN-LAST:event_jMenuItem10MousePressed

    private void listSolToShowValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listSolToShowValueChanged

        initSolutionsTable();
        initNeedTestTable();
    }//GEN-LAST:event_listSolToShowValueChanged

    private void listSolToShowKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listSolToShowKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_DELETE )
        {
            JList list = ( JList ) evt.getSource();

            int[] indx = list.getSelectedIndices();

            for ( int i = indx.length - 1; i >= 0; i-- )
            {
                ( ( DefaultListModel ) list.getModel() ).remove( indx[ i ] );
            }
        }
    }//GEN-LAST:event_listSolToShowKeyPressed

    private void BusinessThemeMenuItemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BusinessThemeMenuItemMousePressed

        try
        {
            final javax.swing.JFrame finalthis = this;
            EventQueue.invokeLater( new Runnable()
            {
                public void run()
                {
                    SubstanceLookAndFeel.setSkin( "org.pushingpixels.substance.api.skin.BusinessBlackSteelSkin" );
                    //UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceNebulaBrickWallLookAndFeel");
                    SwingUtilities.updateComponentTreeUI( finalthis );
                }
            } );

        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка смены стиля : " + e.toString() );
        }
        
    }//GEN-LAST:event_BusinessThemeMenuItemMousePressed

    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
        
        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        if ( "".equals( txtNewCategory.getText() ) )
        {
            ErrorMessage.error( "Вы не ввели имя подкатегории." );
            return;
        }

        EntityTransaction et = null;

        try
        {
            TreePath path = contentTree.getSelectionPath();
            DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) path.getLastPathComponent();

            TreeNode newNode = new TreeNode( txtNewCategory.getText(), ( ( TreeNode ) node.getUserObject() ).getId() );

            et = em.getTransaction();
            et.begin();
            em.persist( new ProblemCategories( newNode.toString(), newNode.getId() ) );
            et.commit();

            Query query = em.createQuery( "SELECT obj FROM ProblemCategories obj WHERE obj.parentid=0" );
            ProblemCategories cat = ( ProblemCategories ) query.getResultList().get( 0 );

            TreeModel model = new DefaultTreeModel( Trees.makeProblemTree( cat.getCatid() ) );
            contentTree.setModel( model );

        }
        catch ( Exception e )
        {
            try
            {
                et.rollback();
            }
            catch ( Exception ex )
            {
                ErrorMessage.error( "Ошибка1 " + e.toString() );
            }

            ErrorMessage.error( "Ошибка2 " + e.toString() );
        }

    }//GEN-LAST:event_jButton1MousePressed

    private void jButton2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MousePressed
        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        EntityTransaction et = null;

        try
        {

            String name = txtProblemName.getText();
            String comparor = txtProblemComparor.getText();
            String inFile = txtProblemIn.getText();
            String outFile = txtProblemOut.getText();
            Integer time = Integer.parseInt( txtProblemTime.getText() );
            String file = txtProblemFile.getText();
            String prog = txtProblemProgram.getText();

            if ( !new File( comparor ).exists() || !new File( file ).exists() )
            {
                ErrorMessage.error( "Ошибка : некоторые из указанных файлов не существуют." );
                return;
            }

            et = em.getTransaction();
            et.begin();
            em.persist( new Problems( name, Paths.conv( file ), Paths.conv( comparor ), prog, inFile, outFile, time ) );
            et.commit();

            txtProblemName.setText( "" );
            txtProblemComparor.setText( "" );
            txtProblemIn.setText( "" );
            txtProblemOut.setText( "" );
            txtProblemTime.setText( "" );
            txtProblemProgram.setText( "" );
            txtProblemFile.setText( "" );

            initProblemsList();
        }
        catch ( NumberFormatException e )
        {
            txtProblemTime.setText( "" );
            ErrorMessage.error( e.toString() );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );

            try
            {
                et.rollback();
            }
            catch ( Exception ex )
            {
                ErrorMessage.error( "Ошибка отката базы данных : " + ex.toString() );
            }

        }
    }//GEN-LAST:event_jButton2MousePressed

    private void jButton3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MousePressed
        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        try
        {
            if ( listCurProblem.getSelectedIndex() == -1 )
            {
                return;
            }

            Integer pid = ( ( ProblemListItem ) ( ( DefaultListModel ) listCurProblem.getModel() ).get( 0 ) ).getId();

            File input = new File( txtInFile.getText() );
            File output = new File( txtOutFile.getText() );

            if ( !input.exists() || !output.exists() )
            {
                ErrorMessage.error( "Указанные файлы input или output не существуют" );
                return;
            }

            if ( input.isFile() && output.isFile() )
            {

                Integer num = Integer.parseInt( txtNum.getText() );
                String inf = txtInFile.getText();
                String outf = txtOutFile.getText();

                DefaultTableModel model = ( DefaultTableModel ) tblTests.getModel();
                Object[] row = { num, inf, outf };
                model.addRow( row );

                //em.getTransaction().begin();
                //em.persist( new entities.Tests( pid, num, inf, outf ) );
                //em.getTransaction().commit();
            }
            else if ( input.isDirectory() && output.isDirectory() )
            {

                String[] tests = input.list();
                String[] outs = output.list();

                Arrays.sort( tests );
                Arrays.sort( outs );

                DefaultTableModel model = ( DefaultTableModel ) tblTests.getModel();

                for ( int i = 0; i < Math.min( tests.length, outs.length ); i++ )
                {

                    String inf = input.getAbsolutePath() + File.separator + tests[ i ];
                    String outf = output.getAbsolutePath() + File.separator + outs[ i ];

                    Object[] row = { i, inf, outf };
                    model.addRow( row );

                    //em.getTransaction().begin();
                    //em.persist( new entities.Tests( pid, i, inf, outf ) );
                    //em.getTransaction().commit();
                    //System.out.println( "TEST [ " + i + " ] = " + tests[ i ] + ", " + outs[ i ] );
                }

            }

            //refreshTestTable();
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
        }
    }//GEN-LAST:event_jButton3MousePressed

    private void jButton4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MousePressed

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        if ( "".equals( txtExtensions.getText() ) || "".equals( txtCompiler.getText() ) )
        {
            ErrorMessage.error( "Ошибка : не все необходимые поля заполнены" );
            return;
        }

        if ( !new File( txtCompiler.getText() ).exists() )
        {
            ErrorMessage.error( "Ошибка : файл компилятора не существует" );
            return;
        }

        DefaultTableModel model = ( DefaultTableModel ) tblCompilers.getModel();
        Object[] row = { txtExtensions.getText(), txtCompiler.getText(), txtCompilerParams.getText() };
        model.addRow( row );

        txtExtensions.setText( "" );
        txtCompiler.setText( "" );
        txtCompilerParams.setText( "" );

    }//GEN-LAST:event_jButton4MousePressed

    private void jButton5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MousePressed

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        EntityTransaction et = null;

        try
        {

            String sols = txtSolution.getText();
            boolean isDozd = isDozdCheckBox.isSelected();
            ArrayList< Solutions > innerSols = Trees.getAllSolutionsInFolder( new File( sols ), isDozd );

            et = em.getTransaction();
            et.begin();

            for ( int i = 0; i < innerSols.size(); i++ )
            {
                em.persist( innerSols.get( i ) );
            }

            et.commit();

            initSolutionsTable();

            ////////////////////
            tblSolutions.clearSelection();
            DefaultListSelectionModel model = ( DefaultListSelectionModel ) tblSolutions.getSelectionModel();
            DefaultTableModel tbl = ( DefaultTableModel ) tblSolutions.getModel();
            String folder = txtSolution.getText();

            for ( int i = 0; i < tbl.getRowCount(); i++ )
            {
                SolutionListItem item = ( SolutionListItem ) tbl.getValueAt( i, 2 );
                if ( item.toString().startsWith( folder ) )
                {
                    model.addSelectionInterval( i, i );
                }
            }
            //////////////////////

            if ( innerSols.size() == 0 )
            {
                ErrorMessage.error( "В этой папке не найдено ни одного нового решения." );
            }

            txtSolution.setText( "" );
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
            try
            {
                et.rollback();
            }
            catch ( Exception ex )
            {
                ErrorMessage.error( "Ошибка отката базы данных : " + ex.toString() );
            }
        }

    }//GEN-LAST:event_jButton5MousePressed

    private void jButton6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MousePressed

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        try
        {
            DefaultListModel problemsModel = ( DefaultListModel ) listResProblems.getModel();

            int[] selProb = listResProblems.getSelectedIndices();

            String[] cols = new String[ 2 * selProb.length + 2 ];

            cols[ 0 ] = "Пользователи";
            cols[ cols.length - 1 ] = "Сумма";

            for ( int i = 0; i < selProb.length; i++ )
            {
                cols[ 2 * i + 1 ] = ( ( ProblemListItem ) problemsModel.getElementAt( selProb[ i ] ) ).toString();
                cols[ 2 * i + 2 ] = ( ( ProblemListItem ) problemsModel.getElementAt( selProb[ i ] ) ).toString();
            }

            DefaultTableModel model = new DefaultTableModel( cols, 0 )
            {
                @Override
                public boolean isCellEditable( int x, int y )
                {
                    return false;
                }
            };

            DefaultListModel usersModel = ( DefaultListModel ) listResUsers.getModel();

            int[] selUsers = listResUsers.getSelectedIndices();

            for ( int l = 0; l < selUsers.length; l++ )
            {

                Integer uid = ( ( UserListItem ) usersModel.getElementAt( selUsers[ l ] ) ).getId();

                String[] row = new String[ 2 * selProb.length + 2 ];

                row[ 0 ] = ( ( UserListItem ) usersModel.getElementAt( selUsers[ l ] ) ).toString();

                double sumball = 0;

                for ( int i = 0; i < selProb.length; i++ )
                {
                    ProblemListItem problem = ( ( ProblemListItem ) problemsModel.getElementAt( selProb[ i ] ) );
                    Query query = em.createQuery( "SELECT obj FROM Solutions obj WHERE obj.pid=" + problem.getId() + " AND obj.uid=" + uid + " ORDER BY obj.count ASC" );
                    List solutions = query.getResultList();

                    String all = "";

                    for ( int j = 0; j < solutions.size(); j++ )
                    {
                        Integer sid = ( ( Solutions ) solutions.get( j ) ).getSid();
                        Query resQuery = em.createQuery( "SELECT obj FROM Results obj WHERE obj.sid=" + sid );
                        List results = resQuery.getResultList();

                        String result = "";

                        if ( results.size() == 1 )
                        {
                            result = ( ( Results )results.get( 0 ) ).getResults();

                            if ( result.length() == 0 )
                            {
                                result = "не проверено";
                            }

                            if ( "".equals( all ) )
                            {
                                Solutions cursol = (Solutions) solutions.get( j );
                                if ( cursol.getIsdozd() > 0 )
                                {
                                    //all = result;
                                    //all.replaceAll( "\\+", "±" );
                                    for ( int k = 0; k < result.length(); k++ )
                                    {
                                        all = all + (( result.charAt( k ) == '+' ) ?  "±" : String.valueOf( result.charAt( k ) ));
                                    }
                                }
                                else
                                {
                                    all = result;
                                }
                            }
                            else if ( !"не проверено".equals( all ) && !"не проверено".equals( result ) )
                            {
                                String newall = "";
                                for ( int t = 0; t < all.length(); t++ )
                                {
                                    if ( result.charAt( t ) == '+' && all.charAt( t ) != '+' )
                                    {
                                        newall = newall + "±";
                                    }
                                    else if ( result.charAt( t ) == '¤' && all.charAt( t ) != '¤' )
                                    {
                                        newall = newall + "♥";
                                    }
                                    else
                                    {
                                        newall = newall + result.charAt( t );
                                    }
                                }
                                all = newall;
                            }
                        }
                    }

                    if ( all.length() > 0 && !"не проверено".equals( all ) )
                    {
                        double ball = 0;
                        ball = 0;

                        /////
                        String r = all;

                        ball = 0;
                        for ( int zz = 0; zz < r.length(); zz++ )
                        {
                            char ch = r.charAt( zz );
                            if ( '+' == ch )
                            {
                                ball += 1;
                            }
                            else if ( '±' == ch )
                            {
                                ball += 0.5;
                            }
                            else if ( '¤' == ch )
                            {
                                ball += 0.75;
                            }
                            else if ( '♥' == ch )
                            {
                                ball += 0.25;
                            }
                        }
                        ball /= r.length();
                        ////

                        ball *= 100;
                        row[ 2 * i + 1 ] = all;
                        row[ 2 * i + 2 ] = String.valueOf( Math.round( ball ) );
                        sumball += ball;
                    }
                    else
                    {
                        row[ 2 * i + 1 ] = " ";
                        row[ 2 * i + 2 ] = " ";
                    }
                }

                row[ row.length - 1 ] = String.valueOf( Math.round( sumball ) );

                model.addRow( row );
            }

            ///////
            tblResults.setModel( model );
            tblResults.setRowSorter( setNumberSorter( model ) );
            tblResults.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
            tblResults.setRowHeight( CELL_HEIGHT );
            ///////

            DefaultTableColumnModel column = ( DefaultTableColumnModel ) tblResults.getColumnModel();

            int count = tblResults.getColumnCount();

            for ( int i = 0; i < count; i++ )
            {
                TableColumn col = column.getColumn( i );
                col.setCellRenderer( new ResultCellRenderer() );
            }

        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
        }

    }//GEN-LAST:event_jButton6MousePressed

    private void jButton7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MousePressed

        if ( evt.getButton() != MouseEvent.BUTTON1 ) return;

        EntityTransaction et = null;

        try
        {
            String name = txtUserName.getText();
            String login = txtUserLogin.getText();
            String cat = txtUserCategory.getText();

            if ( "".equals( name ) || "".equals( login ) || "".equals( cat ) )
            {
                ErrorMessage.error( "Ошибка : не все поля заполнены для добавления пользователя" );
                return;
            }

            txtUserName.setText( "" );
            txtUserLogin.setText( "" );

            et = em.getTransaction();
            et.begin();
            em.persist( new Users( name, login, cat, "user" ) );
            et.commit();

            initUsersTable();
            initResultUserList();
            initUserCategoryList();
        }
        catch ( Exception e )
        {
            ErrorMessage.error( "Ошибка : " + e.toString() );
            try
            {
                et.rollback();
            }
            catch ( Exception ex )
            {
                ErrorMessage.error( "Ошибка отката базы данных: " + ex.toString() );
            }
        }
        
    }//GEN-LAST:event_jButton7MousePressed

    private Thread tester;

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {

        JFrame.setDefaultLookAndFeelDecorated( true );
        JDialog.setDefaultLookAndFeelDecorated( true );

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //
                MainForm mainform = new MainForm( "Nimbus" );
                Image image = Toolkit.getDefaultToolkit().getImage( MainForm.class.getResource( "/resources/heart.png" ) );
                mainform.setIconImage( image );

                mainform.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXCollapsiblePane AddCategoryCollapsiblePane;
    private org.jdesktop.swingx.JXCollapsiblePane AddProblemCollapsiblePane;
    private org.jdesktop.swingx.JXCollapsiblePane AddTestCollapsiblePane;
    private javax.swing.JMenuItem BusinessThemeMenuItem;
    private javax.swing.JMenuItem DBExportMenuItem;
    private javax.swing.JMenuItem DBImportMenuItem;
    private org.jdesktop.swingx.JXCollapsiblePane addCompilerCollapsiblePane;
    private org.jdesktop.swingx.JXCollapsiblePane addSolCollapsiblePane;
    private org.jdesktop.swingx.JXCollapsiblePane addUserCollapsiblePane;
    private javax.swing.JButton btnAddCompiler;
    private javax.swing.JButton btnAddNewCategory;
    private javax.swing.JButton btnAddProblem;
    private javax.swing.JButton btnAddSolution;
    private javax.swing.JButton btnAddTest;
    private javax.swing.JButton btnAddUser;
    private javax.swing.JButton btnCheck;
    private javax.swing.JButton btnComparorFile;
    private javax.swing.JButton btnCompilerOpen;
    private javax.swing.JButton btnCompilerSave;
    private javax.swing.JButton btnCompilersRefresh;
    private javax.swing.JButton btnExcelExport;
    private javax.swing.JButton btnInFile;
    private javax.swing.JButton btnOpenSolution;
    private javax.swing.JButton btnOutFile;
    private javax.swing.JButton btnProblemFile;
    private javax.swing.JButton btnProblemRemove;
    private javax.swing.JButton btnRefreshSolTables;
    private javax.swing.JButton btnRemoveCategory;
    private javax.swing.JButton btnRemoveSolutions;
    private javax.swing.JButton btnSaveCategory;
    private javax.swing.JButton btnShowResult;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnTestSave;
    private javax.swing.JButton btnUsersRefresh;
    private javax.swing.JButton btnUsersRemove;
    private javax.swing.JTree contentTree;
    private javax.swing.JMenuItem darkThemeMenuItem;
    private javax.swing.JCheckBox isDozdCheckBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JToolBar jToolBar8;
    private javax.swing.JMenuItem lightThemeMenuItem;
    private javax.swing.JList listCurProblem;
    private javax.swing.JList listResProblems;
    private javax.swing.JList listResUsers;
    private javax.swing.JList listSolToShow;
    private javax.swing.JList listUserCategories;
    private javax.swing.JMenuItem nimbusLookMenuItem;
    private javax.swing.JTable problemsList;
    private javax.swing.JProgressBar progressBar;
    private org.jdesktop.swingx.JXCollapsiblePane resultsCollapsiblePane;
    private javax.swing.JTable tblCompilers;
    private javax.swing.JTable tblContent;
    private javax.swing.JTable tblNeedTest;
    private javax.swing.JTable tblResults;
    private javax.swing.JTable tblSolutions;
    private javax.swing.JTable tblTests;
    private javax.swing.JTable tblUsers;
    private javax.swing.JList tempList;
    private javax.swing.JProgressBar totalProgress;
    private javax.swing.JTextField txtCompiler;
    private javax.swing.JTextField txtCompilerParams;
    private javax.swing.JTextField txtExtensions;
    private javax.swing.JTextField txtInFile;
    private javax.swing.JTextPane txtLog;
    private javax.swing.JTextField txtNewCategory;
    private javax.swing.JTextField txtNum;
    private javax.swing.JTextField txtOutFile;
    private javax.swing.JTextField txtProblemComparor;
    private javax.swing.JTextField txtProblemFile;
    private javax.swing.JTextField txtProblemIn;
    private javax.swing.JTextField txtProblemName;
    private javax.swing.JTextField txtProblemOut;
    private javax.swing.JTextField txtProblemProgram;
    private javax.swing.JTextField txtProblemTime;
    private javax.swing.JTextField txtSolution;
    private javax.swing.JTextField txtUserCategory;
    private javax.swing.JTextField txtUserLogin;
    private javax.swing.JTextField txtUserName;
    private javax.swing.JMenuItem windowsLookMenuItem;
    // End of variables declaration//GEN-END:variables

}

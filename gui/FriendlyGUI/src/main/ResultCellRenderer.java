package main;

import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;

public class ResultCellRenderer extends DefaultTableCellRenderer
{
    @Override
    public Component getTableCellRendererComponent( JTable table,
                                                    Object value,
                                                    boolean isSelected,
                                                     boolean hasFocus,
                                                    int row,
                                                    int column )
    {
        Component c =
            super.getTableCellRendererComponent( table, value,
                                                 isSelected, hasFocus,
                                                 row, column);

        c.setFont( new Font( Font.MONOSPACED, Font.PLAIN , 12 ) );
        return c;
  }
}
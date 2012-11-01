package main;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import structures.TreeNode;

public class ChangeListener implements TreeModelListener
{
    public void treeNodesChanged( TreeModelEvent e )
    {
        DefaultMutableTreeNode node;
        node = ( DefaultMutableTreeNode ) ( e.getTreePath().getLastPathComponent() );

        try
        {
            int index = e.getChildIndices()[ 0 ];
            node = ( DefaultMutableTreeNode ) ( node.getChildAt( index ) );
        } 
        catch ( NullPointerException exc )
        {
        }

    }
    public void treeNodesInserted( TreeModelEvent e )
    {
    }
    public void treeNodesRemoved( TreeModelEvent e )
    {
    }
    public void treeStructureChanged( TreeModelEvent e )
    {
    }
}

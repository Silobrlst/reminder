import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

class MyTextCellRenderer extends DefaultTableCellRenderer
{
    // Метод получения компонента для прорисовки
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }
}
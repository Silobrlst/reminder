import javax.swing.table.DefaultTableModel;

class MyTableModel extends DefaultTableModel
{
    public boolean isCellEditable(int row, int column){
        if(column == 0){
            return true;
        }
        return false;
    }
    // Тип хранимых в столбцах данных
    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0: return Boolean.class;
            default: return Object.class;
        }
    }
}
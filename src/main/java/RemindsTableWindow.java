import javax.swing.*;

public class RemindsTableWindow extends JFrame {
    // Данные для таблиц
    private Object[][] array = new String[][] {{ "Сахар" , "кг", "1.5" },
            { "Мука"  , "кг", "4.0" },
            { "Молоко", "л" , "2.2" }};
    // Заголовки столбцов
    private Object[] columnsHeader = new String[] {"Наименование", "Ед.измерения",
            "Количество"};

    RemindsTableWindow(){
        super("напоминания");

        JTable table = new JTable(array, columnsHeader);

        JPanel windowPanel = new JPanel();
        this.getContentPane().add(windowPanel);
        windowPanel.setLayout(new BoxLayout(windowPanel, BoxLayout.Y_AXIS));
        windowPanel.add(table);
        this.pack();
        this.setVisible(true);
    }
}

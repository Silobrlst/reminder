import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.GregorianCalendar;

public class Main extends JFrame {
    private static DefaultTableModel tableModel;
    private Object[] columnsHeader = new String[] {"активно", "время", "следующая дата", "сообщение"};

    Main(){
        super("напоминания");

        GUI();
    }

    public static void main(String[] args){
        new Main();

        GregorianCalendar calendar = new GregorianCalendar();
    }

    static void addRemind(boolean activeIn, String timeIn, String nextDateIn, String messageIn){
        tableModel.addRow(new String[]{"", timeIn, nextDateIn, messageIn});
    }

    void GUI(){
        JButton addButton = new JButton("добавить");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RemindWindow remindWindow = new RemindWindow(new ApplyRemindListener() {
                    public void addChangeRemind(Remind remindIn) {
                        boolean active = true;

                        if(remindIn.operationType == 2){
                            active = remindIn.active;
                        }

                        System.err.println("qweqweqweqwewqe");

                        addRemind(active, remindIn.time, remindIn.date, remindIn.message);
                    }
                });
            }
        });

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnsHeader);

        JTable table = new JTable(tableModel);

        JPanel windowPanel = new JPanel();
        windowPanel.add(addButton);
        this.getContentPane().add(windowPanel);
        windowPanel.setLayout(new BoxLayout(windowPanel, BoxLayout.Y_AXIS));
        windowPanel.add(table);
        this.pack();
        this.setVisible(true);

        setTrayIcon();
    }

    private static void setTrayIcon() {
        PopupMenu trayMenu = new PopupMenu();
        MenuItem item = new MenuItem("Exit");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayMenu.add(item);

        Image icon = Toolkit.getDefaultToolkit().getImage("Lassie-icon.png");
        TrayIcon trayIcon = new TrayIcon(icon, "напоминалка", trayMenu);
        trayIcon.setImageAutoSize(true);

        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        trayIcon.displayMessage("напоминалка", "Application started!",
                TrayIcon.MessageType.INFO);
    }
}

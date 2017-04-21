import javafx.scene.input.MouseButton;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Main extends JFrame {
    private static MyTableModel tableModel;
    private Object[] columnsHeader = new String[]{"активно", "время", "сообщение", "следующая дата", "remind"};

    final String globalConfig = "globalConfig.json";
    ArrayList<Remind> reminds;

    Main() {
        super("напоминания");

        GUI();

        reminds = new ArrayList<>();
        loadDataBase(globalConfig);
    }

    public static void main(String[] args) {
        new Main();
    }

    JSONObject toJSON(Remind remindIn) {
        JSONObject obj = new JSONObject();

        obj.put("id", remindIn.id);
        obj.put("period", remindIn.period.ordinal());
        obj.put("message", remindIn.message);
        obj.put("date", remindIn.date);
        obj.put("time", remindIn.time);
        obj.put("active", remindIn.active);
        obj.put("nDays", remindIn.nDays);
        obj.put("limNum", remindIn.limNum);
        obj.put("curNum", remindIn.curNum);

        JSONArray days = new JSONArray();
        for(Integer day: remindIn.days){
            days.put(day);
        }
        obj.put("days", days);

        return obj;
    }

    Remind fromJSON(JSONObject objIn) {
        String id = objIn.getString("id");
        String time = objIn.getString("time");
        String date = objIn.getString("date");
        String message = objIn.getString("message");
        boolean active = objIn.getBoolean("active");
        Period period = Period.values()[objIn.getInt("period")];
        int nDays = objIn.getInt("nDays");
        int limNum = objIn.getInt("limNum");
        int curNum = objIn.getInt("curNum");
        ArrayList<Integer> days = new ArrayList<>();
        for (int i = 0; i < objIn.getJSONArray("days").length(); i++) {
            days.add(objIn.getJSONArray("days").getInt(i));
        }

        return new Remind(id, active, period, days, message, time, date, nDays, limNum, curNum);
    }

    void loadDataBase(String fileNameIn) {
        try {
            FileReader inputHeuristic = new FileReader(fileNameIn);
            BufferedReader bufferReader = new BufferedReader(inputHeuristic);
            String line = "";
            String all = "";

            while ((line = bufferReader.readLine()) != null) {
                all += line;
            }

            bufferReader.close();

            JSONObject obj = new JSONObject(all);
            JSONArray remindsJSON = obj.getJSONArray("reminds");
            for (int i = 0; i < remindsJSON.length(); i++) {
                Remind remind = fromJSON(remindsJSON.getJSONObject(i));
                reminds.add(remind);
                addRemindGUI(remind);
            }
            System.err.print(getFirstNextDateRemind().message);
        } catch (Exception e) {
            System.out.println("Error reading file " + e.getMessage());
        }
    }

    void saveDataBase(String fileNameIn) {
        try {
            JSONObject obj = new JSONObject();
            JSONArray days = new JSONArray();
            for (Remind remind : reminds) {
                days.put(toJSON(remind));
            }
            obj.put("reminds", days);

            PrintWriter writer = new PrintWriter(fileNameIn, "UTF-8");
            writer.print(obj.toString(5));
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }

    static void addRemindGUI(Remind remindIn) {
        tableModel.addRow(new Object[]{remindIn.active, remindIn.time, remindIn.message, getNextDate(remindIn), remindIn});
    }

    static String getNextDate(Remind remindIn){
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC+5"));
        int min;

        switch (remindIn.period){
            case Month:
                min = 31;
                for(Integer day: remindIn.days){
                    if(day-zdt.getDayOfMonth() >= 0){
                        min = Math.min(min, day);
                    }
                }
                zdt.plusDays(min - zdt.getDayOfMonth());
                return zdt.format(DateTimeFormatter.ofPattern("d.MM.yyyy"));
            case Ndays:
                return "";
            case OneTime:
                return remindIn.date;
            case Week:
                min = 7;
                for(Integer day: remindIn.days){
                    if(day-zdt.getDayOfWeek().ordinal() >= 0){
                        min = Math.min(min, day);
                    }
                }
                zdt.plusDays(min - zdt.getDayOfWeek().ordinal());
                return zdt.format(DateTimeFormatter.ofPattern("d.MM.yyyy"));
            case Year:
                min = 365;
                for(Integer day: remindIn.days){
                    if(day-zdt.getDayOfYear() >= 0){
                        min = Math.min(min, day);
                    }
                }
                zdt.plusDays(min - zdt.getDayOfYear());
                return zdt.format(DateTimeFormatter.ofPattern("d.MM.yyyy"));
        }

        return "";
    }

    Remind getFirstNextDateRemind(){
        DateTimeFormatter f = DateTimeFormatter.ofPattern("d.MM.yyyy k:mm");
        Remind firstRemind = reminds.get(0);
        String str = getNextDate(firstRemind)+" "+firstRemind.time;
        LocalDateTime firstDate = LocalDateTime.from(f.parse(str));

        for(Remind remind: reminds){
            str = getNextDate(remind)+" "+remind.time;
            LocalDateTime dateTime = LocalDateTime.from(f.parse(str));
            if(dateTime.isBefore(firstDate)){
                firstRemind = remind;
                firstDate = dateTime;
            }
        }

        return firstRemind;
    }

    void GUI() {
        JButton addButton = new JButton("добавить");
        JButton changeButton = new JButton("изменить");
        JButton deleteButton = new JButton("удалить");

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(addButton);
        buttonsPanel.add(changeButton);
        buttonsPanel.add(deleteButton);

        tableModel = new MyTableModel();
        tableModel.setColumnIdentifiers(columnsHeader);

        JTable table = new JTable(tableModel);
        table.setShowVerticalLines(false);
        table.setRowSelectionAllowed(true);
        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPopupMenu cm = new JPopupMenu();
        JMenuItem addMenu = new JMenuItem("добавить");
        cm.add(addMenu);
        JMenuItem changeMenu = new JMenuItem("изменить");
        cm.add(changeMenu);
        JMenuItem deleteMenu = new JMenuItem("удалить");
        cm.add(deleteMenu);

        ActionListener addAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddChangeRemindGUI(new ApplyRemindListener() {
                    public void addChangeRemind(Remind remindIn) {
                        addRemindGUI(remindIn);
                        reminds.add(remindIn);
                        saveDataBase(globalConfig);
                    }
                });
            }
        };
        ActionListener changeAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if(row != -1) {
                    Remind remind = (Remind) tableModel.getValueAt(row, 4);

                    new AddChangeRemindGUI(new ApplyRemindListener() {
                        public void addChangeRemind(Remind remindIn) {
                            addRemindGUI(remindIn);
                            reminds.add(remindIn);
                            saveDataBase(globalConfig);
                        }
                    }, remind);
                }
            }
        };
        ActionListener deleteAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if(row != -1){
                    Remind remind = (Remind) tableModel.getValueAt(row, 4);
                    reminds.remove(remind);
                    tableModel.removeRow(row);
                    saveDataBase(globalConfig);

                    if(table.getRowCount() > row){
                        table.setRowSelectionInterval(row, row);
                    }
                }
            }
        };

        addButton.addActionListener(addAction);
        addMenu.addActionListener(addAction);
        changeButton.addActionListener(changeAction);
        changeMenu.addActionListener(changeAction);
        deleteButton.addActionListener(deleteAction);
        deleteMenu.addActionListener(deleteAction);

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY.ordinal()) {
                    cm.show(table, e.getX(), e.getY());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        JPanel windowPanel = new JPanel();
        windowPanel.setLayout(new BoxLayout(windowPanel, BoxLayout.Y_AXIS));
        windowPanel.add(buttonsPanel);
        windowPanel.add(new JScrollPane(table));
        this.getContentPane().add(windowPanel);
        this.setSize(400, 400);
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


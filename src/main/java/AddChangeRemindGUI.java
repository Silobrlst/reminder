import boofcv.gui.ListDisplayPanel;
import com.sun.javaws.jnl.JavaFXRuntimeDesc;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRules;
import java.util.*;

public class AddChangeRemindGUI extends JFrame {
    //<Период>----------------------------
    private ButtonGroup group;
    private JRadioButton week;
    private JRadioButton month;
    private JRadioButton year;
    private JRadioButton nDays;
    private JRadioButton oneTime;
    private JTextField nDaysText;

    private JPanel dateTimePanel;
    private JTextField dayDate;
    private JTextField monthDate;
    private JTextField yearDate;
    private JTextField hTime;
    private JTextField mTime;
    //<Период/>----------------------------

    //<дни и время появления>----------------------------
    private JPanel daysPanel;
    private JTextField daysText;
    private JPanel timePanel;
    private JTextField hTime2;
    private JTextField mTime2;
    //</дни и время появления>----------------------------

    private JTextArea messageText;

    //<дополнительно>----------------------------
    private JPanel additionallyPanel;
    private JCheckBox limitCheck;
    private JTextField limitText;
    //<дополнительно/>----------------------------

    private String addZero(String strIn, int numIn){
        String str = "";
        for(int i=strIn.length(); i<numIn; i++){
            str+="0";
        }
        return str+strIn;
    }

    private void init(ApplyRemindListener applyRemindListenerIn){
        daysPanel = new JPanel();
        daysText = new JTextField();
        limitCheck = new JCheckBox("ограниченное количество появлений", false);
        timePanel = new JPanel();
        hTime2 = new JTextField(2);
        mTime2 = new JTextField(2);

        //<Период>----------------------------
        JPanel periodPanel = new JPanel();
        periodPanel.setLayout(new BoxLayout(periodPanel, BoxLayout.Y_AXIS));
        periodPanel.setBorder(BorderFactory.createTitledBorder("Период"));

        group = new ButtonGroup();
        week = new JRadioButton("за неделю", true);
        week.setMnemonic(Period.Week.ordinal());
        group.add(week);
        month = new JRadioButton("за месяц", false);
        month.setMnemonic(Period.Month.ordinal());
        group.add(month);
        year = new JRadioButton("за год", false);
        year.setMnemonic(Period.Year.ordinal());
        group.add(year);
        nDays = new JRadioButton("за N дней", false);
        nDays.setMnemonic(Period.Ndays.ordinal());
        group.add(nDays);
        oneTime = new JRadioButton("разово", false);
        oneTime.setMnemonic(Period.OneTime.ordinal());
        group.add(oneTime);

        nDaysText = new JTextField();
        nDaysText.setEnabled(false);

        periodPanel.add(week);
        periodPanel.add(month);
        periodPanel.add(year);
        periodPanel.add(nDays);
        periodPanel.add(nDaysText);
        periodPanel.add(oneTime);

        dateTimePanel = new JPanel();
        dayDate = new JTextField(2);
        monthDate = new JTextField(2);
        yearDate = new JTextField(4);
        hTime = new JTextField(2);
        mTime = new JTextField(2);

        dateTimePanel.add(dayDate);
        dateTimePanel.add(new JLabel("."));
        dateTimePanel.add(monthDate);
        dateTimePanel.add(new JLabel("."));
        dateTimePanel.add(yearDate);
        dateTimePanel.add(new JLabel("  "));
        dateTimePanel.add(hTime);
        dateTimePanel.add(new JLabel(":"));
        dateTimePanel.add(mTime);
        periodPanel.add(dateTimePanel);

        ActionListener radioGroupListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(nDays.isSelected()){
                    nDaysText.setEnabled(true);
                }else{
                    nDaysText.setEnabled(false);
                }

                if(oneTime.isSelected()){
                    dateTimePanel.setEnabled(true);
                    dayDate.setEnabled(true);
                    monthDate.setEnabled(true);
                    yearDate.setEnabled(true);
                    hTime.setEnabled(true);
                    mTime.setEnabled(true);
                    daysPanel.setEnabled(false);
                    timePanel.setEnabled(false);
                    hTime2.setEnabled(false);
                    mTime2.setEnabled(false);
                    daysText.setEnabled(false);
                    limitCheck.setEnabled(false);
                }else{
                    dateTimePanel.setEnabled(false);
                    dayDate.setEnabled(false);
                    monthDate.setEnabled(false);
                    yearDate.setEnabled(false);
                    hTime.setEnabled(false);
                    mTime.setEnabled(false);
                    daysPanel.setEnabled(true);
                    timePanel.setEnabled(true);
                    hTime2.setEnabled(true);
                    mTime2.setEnabled(true);
                    daysText.setEnabled(true);
                    limitCheck.setEnabled(true);
                }
            }
        };

        week.addActionListener(radioGroupListener);
        month.addActionListener(radioGroupListener);
        year.addActionListener(radioGroupListener);
        nDays.addActionListener(radioGroupListener);
        oneTime.addActionListener(radioGroupListener);
        //<Период/>----------------------------

        //<дни и время появления>----------------------------
        daysPanel.setLayout(new BoxLayout(daysPanel, BoxLayout.X_AXIS));
        daysPanel.setBorder(BorderFactory.createTitledBorder("дни и время появления"));

        GhostText ghostText = new GhostText(daysText, "1, 2, 3-7, ...");
        daysPanel.add(daysText);

        timePanel.add(hTime2);
        timePanel.add(new JLabel(":"));
        timePanel.add(mTime2);
        daysPanel.add(timePanel);
        //</дни и время появления>----------------------------

        messageText = new JTextArea(3, 0);
        messageText.setBorder(BorderFactory.createTitledBorder("сообщение"));
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);

        //<дополнительно>----------------------------
        additionallyPanel = new JPanel();
        additionallyPanel.setLayout(new BoxLayout(additionallyPanel, BoxLayout.Y_AXIS));
        additionallyPanel.setBorder(BorderFactory.createTitledBorder("дополнительно"));

        limitCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        additionallyPanel.add(limitCheck);
        limitText = new JTextField();
        limitText.setEnabled(false);
        additionallyPanel.add(limitText);

        limitCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(limitCheck.isSelected()){
                    limitText.setEnabled(true);
                }else{
                    limitText.setEnabled(false);
                }
            }
        });
        //<дополнительно/>----------------------------

        //<кнопки принять/отмена>---------------------
        JPanel acceptPanel = new JPanel();
        acceptPanel.setLayout(new BoxLayout(acceptPanel, BoxLayout.X_AXIS));

        JButton accept = new JButton("принять");
        JButton cancel = new JButton("отмена");

        JFrame thisContext = this;
        cancel.addActionListener(e -> thisContext.dispose());
        accept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Period period = Period.values()[group.getSelection().getMnemonic()];
                String message = messageText.getText();

                int ndays = 0;
                if(nDaysText.getText().matches("\\d\\d*")){
                    ndays = Integer.parseInt(nDaysText.getText());
                }

                int limNum = 0;
                if(nDaysText.getText().matches("\\d\\d*")){
                    ndays = Integer.parseInt(limitText.getText());
                }

                int curNum = 0;

                String timeStr;
                String dateStr = "";
                if(oneTime.isSelected()){
                    dateStr = dayDate.getText()+"."+addZero(monthDate.getText(), 2)+"."+addZero(yearDate.getText(), 4);
                    timeStr = hTime.getText()+":"+addZero(mTime.getText(), 2);
                }else{
                    timeStr = hTime2.getText()+":"+addZero(mTime2.getText(), 2);
                }

                ArrayList<Integer> days = new ArrayList<>();
                if(daysText.getText().matches("^\\s*\\d*\\s*$")){
                    days.add(Integer.parseInt(daysText.getText()));
                }else if(daysText.getText().matches("(,?\\s*(\\d|\\d\\s*-\\s*\\d)\\s*,?)*")){
                    String[] parts = daysText.getText().split("\\s*,\\s*");
                    for(int i=0; i<parts.length; i++){
                        if(parts[i].matches("\\d*\\s*-\\s*\\d*")){
                            String[] parts2 = parts[i].split("\\s*-\\s*");
                            for(int j=Integer.parseInt(parts2[0]); j<Integer.parseInt(parts2[1])+1; j++){
                                days.add(j);
                            }
                        }else if(parts[i].matches("\\d*")){
                            days.add(Integer.parseInt(parts[i]));
                        }
                    }
                }else if(daysText.getText().matches("\\d*\\s*-\\s*\\d*")){
                    String[] parts2 = daysText.getText().split("\\s*-\\s*");
                    for(int j=Integer.parseInt(parts2[0]); j<Integer.parseInt(parts2[1])+1; j++){
                        days.add(j);
                    }
                }

                if(days.size() > 0 && !ghostText.isEmpty || oneTime.isSelected()){
                    applyRemindListenerIn.addChangeRemind(new Remind(UUID.randomUUID().toString(), true, period, days, message, timeStr, dateStr, ndays, limNum, curNum));
                    thisContext.dispose();
                }
            }
        });

        acceptPanel.add(accept);
        acceptPanel.add(cancel);
        //</кнопки принять/отмена>---------------------

        JPanel windowPanel = new JPanel();
        this.getContentPane().add(windowPanel);
        windowPanel.setLayout(new BoxLayout(windowPanel, BoxLayout.Y_AXIS));
        windowPanel.add(periodPanel);
        windowPanel.add(daysPanel);
        windowPanel.add(messageText);
        windowPanel.add(additionallyPanel);
        windowPanel.add(acceptPanel);
        this.pack();
        this.setVisible(true);
    }

    //создать новое напоминание
    AddChangeRemindGUI(ApplyRemindListener applyRemindListenerIn){
        super("добавить напоминание");
        init(applyRemindListenerIn);

        //<Период>----------------------------
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC+5"));
        dayDate.setText(Integer.toString(zdt.getMonth().getValue()));
        monthDate.setText(Integer.toString(zdt.getDayOfMonth()));
        yearDate.setText(Integer.toString(zdt.getYear()));
        hTime.setText(Integer.toString(zdt.getHour()));
        mTime.setText(Integer.toString(zdt.getMinute()));

        dayDate.setEnabled(false);
        monthDate.setEnabled(false);
        yearDate.setEnabled(false);
        hTime.setEnabled(false);
        mTime.setEnabled(false);
        //<Период/>----------------------------

        //<дни и время появления>----------------------------
        hTime2.setText(Integer.toString(zdt.getHour()));
        mTime2.setText(Integer.toString(zdt.getMinute()));
        //</дни и время появления>----------------------------
    }

    //изменить напоминание
    AddChangeRemindGUI(ApplyRemindListener applyRemindListenerIn, Remind remindIn){
        super("изменить напоминание");

        init(applyRemindListenerIn);

        //<Период>----------------------------
        switch(remindIn.period){
            case Week:
                group.setSelected(week.getModel(), true);
                break;
            case Month:
                group.setSelected(month.getModel(), true);
                break;
            case Year:
                group.setSelected(year.getModel(), true);
                break;
            case Ndays:
                group.setSelected(nDays.getModel(), true);
                nDaysText.setText(String.valueOf(remindIn.nDays));
                break;
            case OneTime:
                group.setSelected(oneTime.getModel(), true);
                String[] dateSplit = remindIn.date.split("\\.");
                String[] timeSplit = remindIn.time.split(":");
                dayDate.setText(dateSplit[0]);
                monthDate.setText(dateSplit[1]);
                yearDate.setText(dateSplit[2]);
                hTime.setText(timeSplit[0]);
                mTime.setText(timeSplit[1]);

                dateTimePanel.setEnabled(true);
                dayDate.setEnabled(true);
                monthDate.setEnabled(true);
                yearDate.setEnabled(true);
                hTime.setEnabled(true);
                mTime.setEnabled(true);
                daysPanel.setEnabled(false);
                timePanel.setEnabled(false);
                hTime2.setEnabled(false);
                mTime2.setEnabled(false);
                daysText.setEnabled(false);
                limitCheck.setEnabled(false);
                break;
        }
        //<Период/>----------------------------

        //<дни и время появления>----------------------------
        String days = "";
        int first = remindIn.days.get(0);
        int last = first;
        boolean addComma = false;
        for(Integer day: remindIn.days){
            if(day == last+1){
                last = day;
            }else if(first == last){
                if(addComma){
                    days = days + ", ";
                }
                days = days + String.valueOf(first);
                addComma = true;
            }else if(last == first+1){
                if(addComma){
                    days = days + ", ";
                }
                days = days + String.valueOf(first);
                days = days + ", ";
                days = days + String.valueOf(last);
                addComma = true;
            }else{
                if(addComma){
                    days = days + ", ";
                }
                days = days + String.valueOf(first);
                days = days + "-";
                days = days + String.valueOf(last);
                addComma = true;
            }
        }

        daysText.setText(days);
        //</дни и время появления>----------------------------

        messageText.setText(remindIn.message);

        //<дополнительно>----------------------------
        //<дополнительно/>----------------------------
    }
}

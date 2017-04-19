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

public class RemindWindow extends JFrame {
    //создать новое напоминание
    RemindWindow(ApplyRemindListener applyRemindListenerIn){
        super("напоминание");

        final JPanel daysPanel = new JPanel();
        final JTextField daysText = new JTextField();
        final JSpinner timeSpinner = new JSpinner( new SpinnerDateModel() );
        final JCheckBox limitCheck = new JCheckBox("ограниченное количество появлений", false);
        JPanel timePanel = new JPanel();
        JTextField hTime2 = new JTextField(2);
        JTextField mTime2 = new JTextField(2);

        //<Период>----------------------------
        JPanel periodPanel = new JPanel();
        periodPanel.setLayout(new BoxLayout(periodPanel, BoxLayout.Y_AXIS));
        Border periodBorder = BorderFactory.createTitledBorder("Период");
        periodPanel.setBorder(periodBorder);

        ButtonGroup group = new ButtonGroup();
        JRadioButton week = new JRadioButton("за неделю", true);
        week.setMnemonic(Period.Week.ordinal());
        group.add(week);
        JRadioButton month = new JRadioButton("за месяц", false);
        month.setMnemonic(Period.Month.ordinal());
        group.add(month);
        JRadioButton year = new JRadioButton("за год", false);
        year.setMnemonic(Period.Year.ordinal());
        group.add(year);
        final JRadioButton nDays = new JRadioButton("за N дней", false);
        nDays.setMnemonic(Period.Ndays.ordinal());
        group.add(nDays);
        final JRadioButton oneTime = new JRadioButton("разово", false);
        oneTime.setMnemonic(Period.OneTime.ordinal());
        group.add(oneTime);

        periodPanel.add(week);
        periodPanel.add(month);
        periodPanel.add(year);
        periodPanel.add(nDays);

        final JTextField nDaysText = new JTextField();
        nDaysText.setEnabled(false);
        periodPanel.add(nDaysText);

        periodPanel.add(oneTime);

        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC+5"));
        JPanel dateTimePanel = new JPanel();
        JTextField dayDate = new JTextField(new Integer(zdt.getDayOfMonth()).toString(), 2);
        dayDate.setEnabled(false);
        JTextField monthDate = new JTextField(new Integer(zdt.getMonth().getValue()).toString(), 2);
        monthDate.setEnabled(false);
        JTextField yearDate = new JTextField(new Integer(zdt.getYear()).toString(), 4);
        yearDate.setEnabled(false);
        JTextField hTime = new JTextField(new Integer(zdt.getHour()).toString(), 2);
        hTime.setEnabled(false);
        JTextField mTime = new JTextField(new Integer(zdt.getMinute()).toString(), 2);
        mTime.setEnabled(false);
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
                    timeSpinner.setEnabled(false);
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
                    timeSpinner.setEnabled(true);
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
        Border daysBorder = BorderFactory.createTitledBorder("дни и время появления");
        daysPanel.setBorder(daysBorder);

        GhostText ghostText = new GhostText(daysText, "1, 2, 3-7, ...");
        daysPanel.add(daysText);

        hTime2.setText(new Integer(zdt.getHour()).toString());
        mTime2.setText(new Integer(zdt.getMinute()).toString());
        timePanel.add(hTime2);
        timePanel.add(new JLabel(":"));
        timePanel.add(mTime2);
        daysPanel.add(timePanel);
        //<дни и время появления/>----------------------------

        JTextArea messageText = new JTextArea(3, 0);
        Border messageBorder = BorderFactory.createTitledBorder("сообщение");
        messageText.setBorder(messageBorder);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);

        //<дополнительно>----------------------------
        JPanel additionallyPanel = new JPanel();
        additionallyPanel.setLayout(new BoxLayout(additionallyPanel, BoxLayout.Y_AXIS));
        Border additionallyBorder = BorderFactory.createTitledBorder("дополнительно");
        additionallyPanel.setBorder(additionallyBorder);

        limitCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        additionallyPanel.add(limitCheck);
        final JTextField limitText = new JTextField();
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
                    dateStr = dayDate.getText()+"."+monthDate.getText()+"."+yearDate.getText();
                    timeStr = hTime.getText()+":"+mTime.getText();
                }else{
                    timeStr = hTime2.getText()+":"+mTime2.getText();
                }

                ArrayList<Integer> days = new ArrayList<>();
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

    //изменить напоминание
    RemindWindow(ApplyRemindListener applyRemindListenerIn, Remind remindIn){
        super("напоминание");

        final JPanel daysPanel = new JPanel();
        final JTextField daysText = new JTextField();
        final JSpinner timeSpinner = new JSpinner( new SpinnerDateModel() );
        final JCheckBox limitCheck = new JCheckBox("ограниченное количество появлений", false);

        //<Период>----------------------------
        JPanel periodPanel = new JPanel();
        periodPanel.setLayout(new BoxLayout(periodPanel, BoxLayout.Y_AXIS));
        Border periodBorder = BorderFactory.createTitledBorder("Период");
        periodPanel.setBorder(periodBorder);

        ButtonGroup group = new ButtonGroup();
        JRadioButton week = new JRadioButton("за неделю", true);
        group.add(week);
        JRadioButton month = new JRadioButton("за месяц", false);
        group.add(month);
        JRadioButton year = new JRadioButton("за год", false);
        group.add(year);
        final JRadioButton nDays = new JRadioButton("за N дней", false);
        group.add(nDays);
        final JRadioButton oneTime = new JRadioButton("разово", false);
        group.add(oneTime);

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
                break;
            case OneTime:
                group.setSelected(oneTime.getModel(), true);
                break;
        }

        periodPanel.add(week);
        periodPanel.add(month);
        periodPanel.add(year);
        periodPanel.add(nDays);

        final JTextField nDaysText = new JTextField();
        nDaysText.setEnabled(false);
        periodPanel.add(nDaysText);

        periodPanel.add(oneTime);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        final JSpinner oneTimeSpinner = new JSpinner(dateModel);
        oneTimeSpinner.setValue(new Date());
        oneTimeSpinner.setEnabled(false);

        periodPanel.add(oneTimeSpinner);

        ActionListener radioGroupListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(nDays.isSelected()){
                    nDaysText.setEnabled(true);
                }else{
                    nDaysText.setEnabled(false);
                }

                if(oneTime.isSelected()){
                    oneTimeSpinner.setEnabled(true);
                    daysPanel.setEnabled(false);
                    daysText.setEnabled(false);
                    timeSpinner.setEnabled(false);
                    limitCheck.setEnabled(false);
                }else{
                    oneTimeSpinner.setEnabled(false);
                    daysPanel.setEnabled(true);
                    daysText.setEnabled(true);
                    timeSpinner.setEnabled(true);
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
        Border daysBorder = BorderFactory.createTitledBorder("дни и время появления");
        daysPanel.setBorder(daysBorder);

        GhostText ghostText = new GhostText(daysText, "1, 2, 3-7, ...");
        daysPanel.add(daysText);

        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date());
        daysPanel.add(timeSpinner);
        //<дни и время появления/>----------------------------

        JTextArea messageText = new JTextArea(3, 0);
        Border messageBorder = BorderFactory.createTitledBorder("сообщение");
        messageText.setBorder(messageBorder);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);

        //<дополнительно>----------------------------
        JPanel additionallyPanel = new JPanel();
        additionallyPanel.setLayout(new BoxLayout(additionallyPanel, BoxLayout.Y_AXIS));
        Border additionallyBorder = BorderFactory.createTitledBorder("дополнительно");
        additionallyPanel.setBorder(additionallyBorder);

        limitCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        additionallyPanel.add(limitCheck);
        final JTextField limitText = new JTextField();
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
}

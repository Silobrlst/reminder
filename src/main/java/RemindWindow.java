import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RemindWindow extends JFrame {
    //создать новое напоминание
    RemindWindow(ApplyRemindListener applyRemindListenerIn){
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

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateRemindWindow extends JFrame {
    CreateRemindWindow(){
        super("создать напоминание");

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

        periodPanel.add(week);
        periodPanel.add(month);
        periodPanel.add(year);
        periodPanel.add(nDays);

        final JTextField nDaysText = new JTextField();
        nDaysText.setEnabled(false);
        periodPanel.add(nDaysText);

        ActionListener nDaysListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(nDays.isSelected()){
                    nDaysText.setEnabled(true);
                }else{
                    nDaysText.setEnabled(false);
                }
            }
        };

        week.addActionListener(nDaysListener);
        month.addActionListener(nDaysListener);
        year.addActionListener(nDaysListener);
        nDays.addActionListener(nDaysListener);
        //<Период/>----------------------------

        //<дни появления>----------------------------
        JPanel daysPanel = new JPanel();
        daysPanel.setLayout(new BoxLayout(daysPanel, BoxLayout.X_AXIS));
        Border daysBorder = BorderFactory.createTitledBorder("дни появления");
        daysPanel.setBorder(daysBorder);

        JTextField daysText = new JTextField();
        GhostText ghostText = new GhostText(daysText, "1, 2, 3-7, ...");
        daysPanel.add(daysText);
        //<дни появления/>----------------------------

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

        final JCheckBox limitCheck = new JCheckBox("ограниченное количество появлений", false);
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

        JPanel windowPanel = new JPanel();
        this.getContentPane().add(windowPanel);
        windowPanel.setLayout(new BoxLayout(windowPanel, BoxLayout.Y_AXIS));
        windowPanel.add(periodPanel);
        windowPanel.add(daysPanel);
        windowPanel.add(messageText);
        windowPanel.add(additionallyPanel);
        this.pack();
        this.setVisible(true);
    }
}

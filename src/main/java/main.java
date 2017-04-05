import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

public class main {
    public static void main(String[] args){
        GregorianCalendar calendar = new GregorianCalendar();

        final JFrame window = new JFrame("Caption");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        JButton minButton = new JButton("Свернуть");
        minButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                //Действие
                window.setState(JFrame.ICONIFIED);
            }
        });

        ButtonGroup group = new ButtonGroup();
        JRadioButton week = new JRadioButton("за неделю", false);
        group.add(week);
        JRadioButton month = new JRadioButton("за месяц", true);
        group.add(month);
        JRadioButton year = new JRadioButton("за год", true);
        group.add(year);
        JRadioButton nDays = new JRadioButton("за N дней", true);
        group.add(nDays);

        panel.add(week);
        panel.add(month);
        panel.add(year);
        panel.add(nDays);

        window.getContentPane().add(panel);
        window.pack();
        window.setVisible(true);
    }
}

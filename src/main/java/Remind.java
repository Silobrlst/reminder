import java.util.ArrayList;

public class Remind {
    String id;
    Boolean active;
    Period period; //период
    ArrayList<Integer> days; //дни появления
    String message;
    String time; //время появления
    String date; //дата появления (разово)
    int nDays; //N-период
    int limNum; //количество появлений (если 0, то неограниченное)
    int curNum; //количество пройденных появлений

    //доп. атрибуты
    int operationType; //1 - создание, 2 - изменение

    Remind(String idIn,
           Boolean activeIn,
           Period periodIn,
           ArrayList<Integer> daysIn,
           String messageIn,
           String timeIn,
           String dateIn,
           int nDaysIn,
           int limNumIn,
           int curNumIn){
        id = idIn;
        active = activeIn;
        period = periodIn;
        days = daysIn;
        message = messageIn;
        time = timeIn;
        date = dateIn;
        nDays = nDaysIn;
        limNum = limNumIn;
        curNum = curNumIn;

        operationType = 0;
    }
}

package com.sosgps.v21.util.timer;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class EmailTimerManager {

    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    private static final int RUNNING_TIME = 0;

    public EmailTimerManager() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, RUNNING_TIME);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime(); //��һ��ִ�ж�ʱ�����ʱ��
        if (date.before(new Date())) {
            date = this.addDay(date, 1);
        }

        Timer timer = new Timer();
        TimerTask task = new EmailTimerTask(RUNNING_TIME);
        //����ָ����������ָ����ʱ�俪ʼ�����ظ��Ĺ̶��ӳ�ִ�С�
        timer.schedule(task, date, PERIOD_DAY);
    }

    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}

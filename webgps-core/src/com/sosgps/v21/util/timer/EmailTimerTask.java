package com.sosgps.v21.util.timer;

import java.util.TimerTask;

/**
 * This is not thread-safe.Please create one in a time.
 * 
 * @author Qiang Zhou
 * 
 */

public class EmailTimerTask extends TimerTask {

    private int runningTime_;

    public EmailTimerTask(int runningTime) {
        runningTime_ = runningTime;
    }

    @Override
    public void run() {
        //Get which time to send email
        int time = 2;
        if (time >= runningTime_) {
            try {
                Thread.sleep((time - runningTime_) * 3600 * 1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //Sending emails
    }

}

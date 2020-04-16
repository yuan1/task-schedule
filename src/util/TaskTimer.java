package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class TaskTimer extends TimerTask {


    public void run() {
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format0.format(new Date().getTime());//这个就是把时间戳经过处理得到期望格式的时
        System.out.println("timer to check task now is " + time);
        TaskUtil.checkTaskIsOver();
    }
}
package listener;


import util.TaskUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class ServletRequestListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // TODO Auto-generated method stub
        System.out.println("destroyed");
        TaskUtil.updateAll();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("init");//在这个方法里进行所需要的操作
        TaskUtil.initComputer();
        TaskUtil.initTask();
        TaskUtil.initTimer();
    }
}

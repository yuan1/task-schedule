package listener;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class ServletRequestListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("init");//在这个方法里进行所需要的操作
    }
}

package com.log.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Класс, осуществляющий инициализацию всех необходимых переменных, 
 * загрзку необходимых данных
 * @author admin
 */
public class InitListener implements ServletContextListener
{

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        //контекст приложения
        ServletContext servletContext = servletContextEvent.getServletContext();
        //
        Context context = Context.getInstance(servletContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
    }
}

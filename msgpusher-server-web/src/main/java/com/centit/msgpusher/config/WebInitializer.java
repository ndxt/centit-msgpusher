package com.centit.msgpusher.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by zou_wy on 2017/3/29.
 */


public class WebInitializer implements WebApplicationInitializer {


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        initializeSpringConfig(servletContext);

        initializeSpringMvcConfig(servletContext);
    }

    private void initializeSpringConfig(ServletContext servletContext){
        AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
        springContext.scan("com.centit");
        servletContext.addListener(new ContextLoaderListener(springContext));
    }

    private void initializeSpringMvcConfig(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext contextSer = new AnnotationConfigWebApplicationContext();
        contextSer.register(NormalSpringMvcConfig.class);
        ServletRegistration.Dynamic msgpusher  = servletContext.addServlet("msgpusher", new DispatcherServlet(contextSer));
        msgpusher.addMapping("/msgpusher/*");
        msgpusher.setLoadOnStartup(1);
        msgpusher.setAsyncSupported(true);
    }
}

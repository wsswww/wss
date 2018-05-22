package com.yiren.server.commander;

import com.yiren.server.commander.handler.CommanderServer;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@EnableAutoConfiguration
@SpringBootApplication
public class CommanderApplication extends SpringBootServletInitializer implements CommandLineRunner {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommanderApplication.class.getSimpleName());

    public static void main(String[] args) {
        SpringApplication.run(CommanderApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("netty启动");
//        CommanderServer.getINSTANCE().run();
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CommanderApplication.class);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
    }
}

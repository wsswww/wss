package com.yiren.server.commander.servlet;

import com.yiren.server.commander.CommanderServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by wengyuwei on 2017/12/2.
 */
public class CommanderServlet extends HttpServlet{
    @Override
    public void init() throws ServletException {
        super.init();
        CommanderServer.getINSTANCE().run();
    }
}

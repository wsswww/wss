package com.yiren.server.commander.servlet;

import com.yiren.server.commander.CommanderServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SwitchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ak = req.getParameter("ak");
        String facilityId = req.getParameter("facility_id");
        int io = Integer.MIN_VALUE;
        try {
            io = Integer.parseInt(req.getParameter("switch"));
        } catch (Exception e) {
        }
        if (ak == null || io == Integer.MIN_VALUE || facilityId == null) {
            resp.getWriter().print("{\"code\":501,\"msg\":\"params missing \"}");
        }
        CommanderServer commander = CommanderServer.getINSTANCE();
        if (commander.isDeviceOnline(facilityId)) {
            CommanderServer.getINSTANCE().cmdSwitch(ak, facilityId, io);
            resp.getWriter().print("{\"code\":0,\"msg\":\"success\",\"data\":[]}");
        } else {
            resp.setContentType("application/javascript;charset=UTF-8");
            resp.getWriter().print("{\"code\":410,\"msg\":\"device offline\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}

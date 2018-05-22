package com.yiren.server.commander.servlet;

import com.yiren.server.commander.CommanderServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 实时回传最新数据servlet
 */
public class InstantCmdServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String facilityId = request.getParameter("facility_id");
        String clientId = request.getParameter("client_id");
        String ak = request.getParameter("ak");
        PrintWriter out = response.getWriter();
        response.setContentType("application/json;charset=UTF-8");
        if (facilityId == null || clientId == null || ak == null) {
            out.print("{\"code\":501,\"msg\":\"params missing \"}");
        } else {
            CommanderServer commander = CommanderServer.getINSTANCE();
            if (commander.isDeviceOnline(facilityId)) {
                long seq = commander.cmdInstantPic(facilityId);
                int count = 0;
                while (count < 20) {
                    String data = CommanderServer.getINSTANCE().getPicData(seq);
                    if (data != null) {
                        out.print("{\"code\":0,\"data\":" + data + "}");
                        return;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
                out.print("{\"code\":403,\"msg\":\"can not get pic\"}");
            } else {
                response.setContentType("application/javascript;charset=UTF-8");
                out.print("{\"code\":410,\"msg\":\"device offline\"}");
            }
        }
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

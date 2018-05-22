package com.yiren.server.commander.servlet;

import com.yiren.server.commander.CommanderServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UpdateCmdServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String apkUrl = request.getParameter("apkUrl");
        int ver = 0;
        try {
            ver = Integer.parseInt(request.getParameter("ver"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (apkUrl == null || ver == 0) {
            return;
        }
        CommanderServer.getINSTANCE().cmdUpdate(ver, apkUrl);
        PrintWriter out = response.getWriter();
        out.print("{\"code\":200,\"msg\":" + apkUrl + "}");
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

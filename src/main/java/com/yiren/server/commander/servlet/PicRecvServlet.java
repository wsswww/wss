package com.yiren.server.commander.servlet;

import com.yiren.server.commander.CommanderServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PicRecvServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String seqid = request.getParameter("seqid");
        String facility_id = request.getParameter("facility_id");
        String start_time = request.getParameter("start_time");
        String end_time = request.getParameter("end_time");
        String pic_num = request.getParameter("pic_num");
        String client_id = request.getParameter("client_id");

        PrintWriter out = response.getWriter();

        if (seqid == null || facility_id == null ||
                start_time == null || end_time == null ||
                pic_num == null || client_id == null) {
            out.print("error");
        } else {
            CommanderServer commander = CommanderServer.getINSTANCE();
            if (commander.isDeviceOnline(facility_id)) {
                CommanderServer.getINSTANCE().cmdPic(facility_id, seqid, Long.parseLong(start_time), Long.parseLong(end_time));
                response.setContentType("application/json;charset=UTF-8");
                out.print("{\"code\":0,\"msg\":\"success\",\"data\":[]}");
            } else {
                response.setContentType("application/javascript;charset=UTF-8");
                out.print("{\"code\":410,\"msg\":\"device offline\",\"data\":[]}");
            }
        }
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

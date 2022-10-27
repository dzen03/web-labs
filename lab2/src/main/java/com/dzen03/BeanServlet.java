package com.dzen03;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "BeanServlet", value = "/BeanServlet")
public class BeanServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String msg = request.getParameter("message");
        BeanClass bean = new BeanClass();
        bean.setMessage(msg);
        request.getSession().setAttribute("msg1", bean);

        RequestDispatcher dispatcher = request.getRequestDispatcher("bean.jsp");
        dispatcher.forward(request, response);

    }
}

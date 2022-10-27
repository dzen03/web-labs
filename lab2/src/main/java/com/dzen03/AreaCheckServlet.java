package com.dzen03;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

@WebServlet(name = "AreaCheckServlet", value = "/AreaCheckServlet")
public class AreaCheckServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        RequestDispatcher dispatcher = request.getRequestDispatcher("index");
        dispatcher.forward(request, response);
    }

    static String PAGE_HEADER = "<html><head><style>tr {text-align: center;}</style><link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi\" crossorigin=\"anonymous\">\n" +
            "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3\" crossorigin=\"anonymous\"></script></head><body><a href=\"index\" class=\"btn btn-primary my-3\" role=\"button\">Go home</a>";

    static String PAGE_FOOTER = "</body></html>";

    private boolean check(double x, double y, int r)
    {
        if (x > 0)
        {
            if (y > 0)
                return (y <= (-1 * x + r));
            else
                return y*y <= (r*r - x*x);
        }
        else
        {
            if (y > 0)
                return false;
            else
                return (x >= -r && y >= -r);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        ServletContext context = getServletContext();
        ArrayList<Object[]> data = new ArrayList<>();
        Object raw_data = context.getAttribute("data");
        if (raw_data == null)
            context.setAttribute("data", data);
        else
            data = (ArrayList<Object[]>) raw_data;
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        double x,y;
        int r;

        try
        {
            x = Double.parseDouble(request.getParameter("x"));
            y = Double.parseDouble(request.getParameter("y") + request.getParameter("y_text"));
            r = Integer.parseInt(request.getParameter("r"));
        }
        catch (Exception ex)
        {
            writer.println("Invalid data!");
            return;
        }
        if (x > 5 || x < -5 || y > 2 || y < -2 || r < 1 || r > 5)
        {
            RequestDispatcher dispatcher = request.getRequestDispatcher("index");
            dispatcher.forward(request, response);
        }
        writer.println(PAGE_HEADER);
        data.add(new Object[]{x, y, r, check(x, y, r)});

        StringBuilder graph = new StringBuilder("<div style=\"position: absolute; width: 50%\"><img src=\"drawing.svg\" style=\"position: absolute; width: 100%; transform: scale(").append(r * 20).append("%)\">");

        graph.append("<img src=\"graph.svg\" style=\"position: absolute; width: 100%\">");
        graph.append("<svg style=\"position: absolute; width: 100%\" viewBox=\"0 0 100 100\" xmlns=\"http://www.w3.org/2000/svg\">");


//          writer.println("<p>" + request.getParameter("x") + " " + request.getParameter("y") + " " + request.getParameter("r") + "</p>");
//
        StringBuilder table = new StringBuilder("<div style=\"text-align: center; position: relative; left: 50%; width: 50%\"><table style=\"table-layout: fixed; width: 100%;\"><tr><th>X</th><th>Y</th><th>R</th><th>Is inside?</th></tr>");//TODO add </div>
        for (Object[] el: data)
        {
            double el_x = (double) el[0], el_y = (double) el[1];
            int el_r = (int) el[2];
            boolean el_ans = (boolean) el[3];
            table.append("<tr>");
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            table.append("<td>").append(decimalFormat.format(el_x)).append("</td>");
            table.append("<td>").append(decimalFormat.format(el_y)).append("</td>");
            table.append("<td>").append(el_r).append("</td>");
            table.append("<td>").append((el_ans ? "&#10003;" : "&#10007;")).append("</td>");
            table.append("</tr>");

            graph.append("<circle cx=\"").append((int)(50 + el_x * 8)).append("%\" cy=\"").append((int)(50 - el_y * 8)).append("%\" r=\"2%\" fill=\"yellow\"/>");
        }
        graph.append("<circle cx=\"").append((int)(50 + x * 8)).append("%\" cy=\"").append((int)(50 - y * 8)).append("%\" r=\"2%\" fill=\"red\"/>");
        table.append("</table></div>");
        graph.append("</svg></div>");
        writer.println(graph);
        writer.println(table);
        writer.println(PAGE_FOOTER);
        writer.close();
    }
}

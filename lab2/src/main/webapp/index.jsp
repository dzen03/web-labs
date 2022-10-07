<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lab2</title>

    <script>

        function validate_by_id(id, min, max) {
            const input = document.getElementById(id);
            const submit = document.getElementById('submit');
            var is_parsed = try_parse(input.value);
            if (is_parsed[0] && min <= is_parsed[1] && is_parsed[1] <= max) {
                input.className = "";
                return true;
            } else {
                input.className = "incorrect";
                return false;
            }
        }

        function validate()
        {
            var fl = true;
            fl &= validate_by_id('x', -5, 5);
            fl &= (validate_by_id('y', -2, 2) || validate_by_id('y_text', -2, 2));
            submit.disabled = !fl;
            return fl;
        }

        function try_parse(x) {
            try {
                var a = parseFloat(x);
                const reg = new RegExp("^-?\\d*\\.?\\d*$");
                if (isNaN(a) || !reg.test(x))
                    return [false];
                return [true, a];
            } catch (exc) {
                return [false];
            }
        }

        function point_clicked(e)
        {
            var element = document.getElementById('graph');
            var click_x = e.clientX, click_y = e.clientY;
            var pos = element.getBoundingClientRect();
            var screen_x = pos.x, screen_y = pos.y;
            var scale = (element.getBoundingClientRect().width / 100);
            var x = ((click_x - screen_x) / scale - 50) / 8, y = -((click_y - screen_y) / scale - 50) / 8;
            console.log(click_x, click_y);
            console.log(screen_x, screen_y);
            console.log(x, y);

            document.getElementById('x').value = x;
            document.getElementById('y').value = "";
            document.getElementById('y_text').value = y;
            if (validate())
            {
                document.getElementById('y').hidden = true;
                document.getElementById('y_text').hidden = false;
                document.getElementById('submit').click();
            }
            else
            {
                document.getElementById('x').value = "";
                document.getElementById('y').value = "2.0";
                document.getElementById('y_text').value = "";
                document.getElementById('y').hidden = false;
                document.getElementById('y_text').hidden = true;
                validate();
            }
        }

        function loaded()
        {
            validate();
            document.getElementById('graph').addEventListener("click", point_clicked, false);
        }
    </script>

    <style>
        header {
            font-family: sans-serif;
            font-size: 3vw;
            height: 3%;
            background-color: darkgrey;
            margin: 10px;
        }

        table {
            table-layout: fixed;
            width: 100%;
        }

        .midsplit {
            width: 50%;
            text-align: center;
        }

        #results tr {
            text-align: center;
        }

        .incorrect
        {
            border:1px solid #CC0000;
            background-color:#ffeeee;
        }

        img[src$=".png"]
        {
            max-width: 100%;
        }

        form{
            font-size: large;
        }

    </style>
</head>
<body onload="loaded()">
<header>Ilya Dzenzelyuk; P32102; 18040</header>
<table>
    <tr>
        <td class="midsplit">
<%--            <img src="graph.svg" id="graph">--%>
            <svg style="width: 100%;" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" id="graph">
                <image height="100%" width="100%" href="graph.svg"></image>

                <% ServletContext context = request.getServletContext();
                    ArrayList<Object[]> data = new ArrayList<>();
                    Object raw_data = context.getAttribute("data");
                    if (raw_data == null)
                        context.setAttribute("data", data);
                    else
                        data = (ArrayList<Object[]>) raw_data;
                    for (Object[] el: data) {%>

                    <circle cx="<%=(int)(50 + (double) el[0] * 8)%>%" cy="<%=(int)(50 - (double) el[1] * 8)%>%" r="2%" fill="yellow"></circle>
                <%}%>
            </svg>
        </td>
        <td class="midsplit">
            <form action="index" method="post" id="input_form">

                <label for="x"><p>Select X:</p></label>
                <input name="x" id="x" type="text" required oninput="validate()">

                <label for="y"><p>Select Y:</p></label>
                <select name="y" id="y" oninput="validate()">
                    <option value="" hidden></option>
                    <% for(double i = -2; i <= 2; i += 0.5) {%>
                        <option value="<%=i%>" selected><%=i%></option>
                    <%}%>
                </select>
                <input type="text" hidden name="y_text" id="y_text">

                <p>Select R:</p>
                <% for(int i = 1; i <= 5; ++i) {%>
                    <input type="radio" name="r" id="r_<%=i%>" value="<%=i%>" required>
                    <label for="r_<%=i%>"><%=i%></label>
                <%}%>

                <p><input type="submit" id="submit"></p>
            </form>
            <form action="index" method="post">
                <input type="hidden" name="clear" value="true">
                <!--                    <label for="destroy"></label>-->
                <input type="submit" value="Clear data" id="clear_data">
            </form>
        </td>
    </tr>
</table>

</body>
</html>
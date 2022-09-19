<?php
session_start();
if (!isset($_SESSION["data"]))
    $_SESSION["data"] = array();
?>
<?php
$start_time = microtime(true);

header("Expires: Tue, 03 Jul 2001 06:00:00 GMT");
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");
header("Cache-Control: no-store, no-cache, must-revalidate, max-age=0");
header("Cache-Control: post-check=0, pre-check=0", false);
header("Pragma: no-cache");


$r_names = ["r_1", "r_2", "r_3", "r_4", "r_5"];
$r_packed = array();

foreach ($r_names as $r_name) {
    if (isset($_GET[$r_name])) {
        $r_packed[] = $_GET[$r_name];
    }
}

if (isset($_GET["clear"])) {
    $_SESSION["data"] = array();
}


if (isset($_GET["x"]) && isset($_GET["y"]) && count($r_packed) > 0) {
    foreach ($r_packed as $r) {
        $x = $_GET["x"];
        $y = $_GET["y"];
        $x = strval($x) === strval(intval($x)) ? intval($x) : null;
        $y = is_numeric($y) ? floatval($y) : null;
        $r = strval($r) === strval(intval($r)) ? intval($r) : null;
        if ($x !== null && $y !== null && $r !== null && $x <= 5 && $x >= -3 && $y <= 3 && $y >= -3 &&
            $r <= 5 && $r >= 1)
        {
            $_SESSION["data"][] = [$x, $y, $r, is_inside($x, $y, $r)];
        }

    }

}


$data = array_reverse($_SESSION["data"]);
//$data = $_SESSION["data"];


function is_inside($x, $y, $r): bool
{
    $ans = false;
    if ($x >= 0) {
        if ($y >= 0) {
            if ($y <= -2 * $x + $r)
                $ans = true;

        } else {
            if ($y * $y <= $r * $r - $x * $x)
                $ans = true;
        }
    } else {
        if ($y < 0) {
            if ($x >= $r && $y >= $r / 2)
                $ans = true;
        }
    }
    return $ans;
}

?>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lab1 </title>

    <script>

        function validate() {
            const y_input = document.getElementById('y');
            const submit = document.getElementById('submit');
            // console.log(try_parse(y_input.value));
            var is_parsed = try_parse(y_input.value);
            if (is_parsed[0] && -3 <= is_parsed[1] && is_parsed[1] <= 3) {
                submit.disabled = false;
                y_input.className = "";
            } else {
                submit.disabled = true;
                y_input.className = "incorrect";
            }
        }

        function try_parse(x) {
            try {
                var a = parseFloat(x);
                const reg = new RegExp("^[\\d.]+$");
                // console.log(reg.test(x));
                if (isNaN(a) || !reg.test(x))
                    return [false];
                return [true, a];
            } catch (exc) {
                return [false];
            }
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

            /*text-align: center;*/
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

        img:hover
        {
            /*scale: 200%;*/
            /*position: fixed;*/
        }

        #input_form > label, #input_form > p
        {
            font-family: monospace;
        }

        .cursive{
            /* not working due to cascading */
            font-family: cursive;
        }

        form{
            font-size: large;
        }

    </style>
</head>
<body onload="validate()">
<header>Ilya Dzenzelyuk; P32102; 23200</header>
<table>
    <tr>
        <td class="midsplit">
            <img src="areas.png" alt="">
        </td>
        <td class="midsplit">
            <form action="lab1.php" method="get" id="input_form">

                <p class="cursive">Select X:</p>
                <input type="radio" name="x" id="x_-3" value="-3" required>
                <label for="x_-3">-3</label>
                <input type="radio" name="x" id="x_-2" value="-2">
                <label for="x_-2">-2</label>
                <input type="radio" name="x" id="x_-1" value="-1">
                <label for="x_-1">-1</label>
                <input type="radio" name="x" id="x_0" value="0">
                <label for="x_0">0</label>
                <input type="radio" name="x" id="x_1" value="1">
                <label for="x_1">1</label>
                <input type="radio" name="x" id="x_2" value="2">
                <label for="x_2">2</label>
                <input type="radio" name="x" id="x_3" value="3">
                <label for="x_3">3</label>
                <input type="radio" name="x" id="x_4" value="4">
                <label for="x_4">4</label>
                <input type="radio" name="x" id="x_5" value="5">
                <label for="x_5">5</label>

                <!--                    <p>Select Y:</p>-->
                <label for="y"><p>Select Y:</p></label>
                <input name="y" id="y" type="text" required oninput="validate()">

                <p>Select R:</p>
                <input type="checkbox" name="r_1" id="r_1" value="1">
                <label for="r_1">1</label>
                <input type="checkbox" name="r_2" id="r_2" value="2">
                <label for="r_2">2</label>
                <input type="checkbox" name="r_3" id="r_3" value="3">
                <label for="r_3">3</label>
                <input type="checkbox" name="r_4" id="r_4" value="4">
                <label for="r_4">4</label>
                <input type="checkbox" name="r_5" id="r_5" value="5">
                <label for="r_5">5</label>

                <p><input type="submit" id="submit"></p>
            </form>
            <form action="lab1.php" method="get">
                <input type="hidden" name="clear" value="true">
                <!--                    <label for="destroy"></label>-->
                <input type="submit" value="Clear data" id="clear_data">
            </form>
        </td>
    </tr>
</table>
<?php if (count($data) > 0) { ?>
    <table id="results">
        <tr>
            <th>X</th>
            <th>Y</th>
            <th>R</th>
            <th>Is located inside</th>
        </tr>
        <?php foreach ($data as $element) {
            [$x, $y, $r, $ans] = $element; ?>
            <tr>
                <td><?php echo $x ?></td>
                <td><?php echo $y ?></td>
                <td><?php echo $r ?></td>
                <td><?php
                    echo $ans ? "&#10003;" : "&#10007;" //✓ or ✗?></td>
            </tr>
        <?php } ?>
    </table>
<?php } ?>
<?php
$end_time = microtime(true);
$execution_time = ($end_time - $start_time);
?>

<footer style="text-align: center">
    <p>Current time: <?php echo date("h:i:sa"); ?>.</p>
    <p>Execution time: <?php echo number_format($execution_time, 6); ?> seconds.</p>
</footer>

</body>
</html>

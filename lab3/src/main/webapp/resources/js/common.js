var r_selected = false;

function validate_by_id(id, min, max, id_to_disable) {
    const input = document.getElementById(id);
    const disable = document.getElementById(id_to_disable);
    const submit = document.getElementById('input_form:submit');
    const is_parsed = try_parse(input.value);
    if (is_parsed[0] && min <= is_parsed[1] && is_parsed[1] <= max) {
        disable.className = "";
        return true;
    } else {
        disable.className = "incorrect";
        return false;
    }
}

function validate()
{
    let fl = true;
    const submit = document.getElementById('input_form:submit');
    fl &= validate_by_id('input_form:x', -5, 5, 'input_form:x');
    fl &= r_selected;
    // fl &= validate_by_id('input_form:y_text', -4, 4, 'input_form:y');
    submit.disabled = !fl;
    return fl;
}

function try_parse(x) {
    try {
        const a = parseFloat(x);
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
    if (!r_selected)
    {
        alert("Select R!!!")
        return;
    }
    const element = document.getElementById('graph');
    const click_x = e.clientX, click_y = e.clientY;
    const pos = element.getBoundingClientRect();
    const screen_x = pos.x, screen_y = pos.y;
    const scale = (element.getBoundingClientRect().width / 100);
    const x = ((click_x - screen_x) / scale - 50) / 8, y = -((click_y - screen_y) / scale - 50) / 8;
    console.log(click_x, click_y);
    console.log(screen_x, screen_y);
    console.log(x, y);

    document.getElementById('input_form:x').value = x;
    document.getElementById('input_form:y_text').value = y;
    if (validate())
    {
        // document.getElementById('input_form:y').hidden = true;
        // document.getElementById('input_form:y_text').hidden = false;
        document.getElementById('input_form:submit').click();
    }
    else
    {
        // document.getElementById('input_form:x').value = "";
        // document.getElementById('input_form:y').value = "0.0";
        // document.getElementById('input_form:y_text').value = "";
        // document.getElementById('input_form:y').hidden = true;
        // document.getElementById('input_form:y_text').hidden = false;
        // validate();
    }
}

function loaded(circles_raw)
{
    // circles = circles.replaceAll('&lt;', '<').replaceAll('&gt;', '>');
    // // alert(circles);
    // console.log(circles_raw);
    // console.log(circles);
    // document.getElementById('input_form:hide_x').hidden=true;
    // document.getElementById('input_form:hide_y').hidden=true;
    // document.getElementById('input_form:hide_r').hidden=true;
    validate();
    document.getElementById('graph').addEventListener("click", point_clicked, false);
    // document.getElementById('graph').append(`<circle cx=\"${50 + coords[0] * 8}%\" cy=\"${50 - coords[1] * 8}%\" r=\"2%\" fill=\"yellow\"></circle>`);


}

function redrawGraph(circles_raw)
{
    const svgns = "http://www.w3.org/2000/svg";
    let circles = eval(circles_raw);
    document.getElementById('graph').innerHTML = '';

    for (let i = 0; i < circles.length; ++i)
    {
        let coords = circles[i];
        let circle = document.createElementNS(svgns, 'circle');
        circle.setAttribute('cx', `${50 + coords[0] * 8}%`);
        circle.setAttribute('cy', `${50 - coords[1] * 8}%`);
        circle.setAttribute('r', '2%');
        circle.setAttribute('fill', `${coords[3] ? 'green' : 'red'}`);
        document.getElementById('graph').appendChild(circle);
    }
}

function r_change(r)
{
    const r_val = parseInt(r.value);
    document.getElementById('draw').setAttribute("style", "scale: " + 20 * r_val + "%; top: 0; position: absolute");
    r_selected = true;
    validate();
    // console.log(r);
}
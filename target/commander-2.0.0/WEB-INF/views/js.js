/*!
 * jQuery JavaScript Library v3.3.1
 * https://jquery.com/
 *
 * Includes Sizzle.js
 * https://sizzlejs.com/
 *
 * Copyright JS Foundation and other contributors
 * Released under the MIT license
 * https://jquery.org/license
 *
 * Date: 2018-01-20T17:24Z
 */
function requestData() {
    $.ajax({
        url: "http://localhost:8080/dashboard",
        type: "get",
        dataType: "json",
        success: function (data) {
            showData(data);//数据展示
        },
        error: function (msg) {
            alert("ajax连接异常：" + msg);
        }
    });
}

function showData(data) {
    var str = "";//定义用于拼接的字符串
    for (var i = 0; i < data.length; i++) {
        //拼接表格的行和列
        str = "<tr><td>" + data[i].name + "</td><td>" + data[i].password + "</td></tr>";
        //追加到table中
        $("#tab").append(str);
    }
}

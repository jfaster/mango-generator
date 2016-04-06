<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html" charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="shortcut icon" href="">
    <title>mango源码生成器</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <script type="text/javascript" src="/static/lib/jquery/1.11.3/jquery.min.js"></script>

    <link rel="stylesheet" href="/static/lib/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/lib/bootstrap/3.3.5/css/bootstrap-theme.min.css">
    <script src="/static/lib/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <link rel="stylesheet" href="/static/lib/remodal/1.0.5/remodal.css">
    <link rel="stylesheet" href="/static/lib/remodal/1.0.5/remodal-default-theme.css">
    <script src="/static/lib/remodal/1.0.5/remodal.min.js"></script>

    <link href="/static/lib/rainbow/1.1.9/themes/blackboard.css" rel="stylesheet" type="text/css">
    <script src="/static/lib/rainbow/1.1.9/js/rainbow.min.js"></script>
    <script src="/static/lib/rainbow/1.1.9/js/language/generic.js"></script>
    <script src="/static/lib/rainbow/1.1.9/js/language/java.js"></script>

    <script src="/static/lib/zeroclipboard/2.2.0/ZeroClipboard.min.js"></script>

    <link rel="stylesheet" href="/static/css/generator.css" />
    <script type="text/javascript" src="/static/js/generator.js"></script>
</head>
<body>
<div class="content_bg">
    <div class="content">
        <form id="form">
            <div class="content_box1 clearfix">
                <span class="name">数据库表名</span>
                <div class="inp_box">
                    <div class="inp_box_con1 clearfix">
                        <label class="inp_box_style1">
                            <input name="tableName" type="text" placeholder="填写数据库表名，如：t_user"/>
                        </label>
                    </div>
                </div>
            </div>
            <div class="content_box2 clearfix">
                <span class="name">POJO类全名</span>
                <div class="inp_box">
                    <div class="inp_box_con1 clearfix">
                        <label class="inp_box_style1">
                            <input name="pojoName" type="text" placeholder="填写pojo类全名，如：test.User"/>
                        </label>
                    </div>
                </div>
            </div>
            <div class="content_box3 clearfix">
                <span class="name">DAO类全名</span>
                <div class="inp_box">
                    <div class="inp_box_con1 clearfix">
                        <label class="inp_box_style1">
                            <input name="daoName" type="text" placeholder="填写dao类全名，如：test.UserDao"/>
                        </label>
                    </div>
                </div>
            </div>
            <div class="content_box4 clearfix">
                <span class="name">主键</span>
                <div class="inp_box">
                    <div class="inp_box_con1 clearfix">
                        <label class="inp_box_style2">
                            <input onchange="autoProperty(this)" name="columns" type="text" placeholder="数据库字段"/>
                        </label>
                        <label class="inp_box_style2 mar1">
                            <input name="properties" type="text" placeholder="类属性"/>
                        </label>
                    </div>
                    <div class="inp_box_con2">
                        <select name="keyPropertyClassName" tabIndex="-1">
                            <option value="int">int</option>
                            <option value="long">long</option>
                            <option value="String">String</option>
                        </select>
                        <label class="inp_box_style3">
                            <input name="strAutoInc" value="true" type="checkbox" tabIndex="-1" />
                            <span>是否自增</span>
                        </label>
                    </div>
                </div>
            </div>
            <div class="content_box5 clearfix">
                <div class="content_bt1">
                    <label for="inp_sub" class="bt_sub" style="cursor: pointer">提交</label>
                    <input type="button" value="" id="inp_sub" tabIndex="-1"/>
                </div>
                <div class="content_bt2">
                    <a href="javascript:;" id="bt_add" style="text-decoration:none">增加一行</a>
                </div>
                <div class="content_bt3">
                    <a href="http://mango.jfaster.org" target="_blank" style="text-decoration:none">帮助</a>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="remodal" data-remodal-id="modal" data-remodal-options="hashTracking: false, closeOnOutsideClick: false">
    <button data-remodal-action="close" class="remodal-close"></button>
    <h1 id="codeName"></h1>
    <div>
        <pre id="codeContent" style="text-align:left"></pre>
    </div>
    <br>
    <button id="copy" data-clipboard-target="copyContent" class="remodal-cancel" value="复制">复制</button>
    <button data-remodal-action="confirm" class="remodal-confirm">关闭</button>
</div>

<div class="remodal" data-remodal-id="errorMsg" data-remodal-options="hashTracking: false, closeOnOutsideClick: false">
    <button data-remodal-action="close" class="remodal-close"></button>
    <h1 id="errorMsg"></h1>
    <button data-remodal-action="confirm" class="remodal-confirm">确定</button>
</div>

<div style="display: none">
    <textarea id="copyContent"></textarea>
</div>

<script>
    var clip = new ZeroClipboard(document.getElementById("copy"));
</script>
</body>
</html>


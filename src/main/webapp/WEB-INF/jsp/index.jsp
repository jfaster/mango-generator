<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <meta charset="UTF-8">

    <script src="static/jquery/1.11.3/jquery.min.js"></script>

    <link rel="stylesheet" href="static/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="static/bootstrap/3.3.5/css/bootstrap-theme.min.css">
    <script src="static/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <link rel="stylesheet" href="static/remodal/1.0.5/remodal.css">
    <link rel="stylesheet" href="static/remodal/1.0.5/remodal-default-theme.css">
    <script src="static/remodal/1.0.5/remodal.min.js"></script>

    <link href="static/rainbow/1.1.9/themes/blackboard.css" rel="stylesheet" type="text/css">
    <script src="static/rainbow/1.1.9/js/rainbow.min.js"></script>
    <script src="static/rainbow/1.1.9/js/language/generic.js"></script>
    <script src="static/rainbow/1.1.9/js/language/java.js"></script>

    <script src="static/zeroclipboard/2.2.0/ZeroClipboard.min.js"></script>
</head>


<body>
<div class="contentpanel">
    <div class="panel panel-default">
        <div class="row">
            <div class="col-lg-3"></div>
            <div class="col-lg-6">
                <form id="form" action="do" method="get">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">数据库表名</label>
                        <div class="col-sm-9">
                            <input type="text" name="tableName" id="tableName" placeholder="填写数据库表名" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">pojo类全名</label>
                        <div class="col-sm-9">
                            <input type="text" name="pojoName" id="pojoName" placeholder="填写pojo类全名" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">dao类全名</label>
                        <div class="col-sm-9">
                            <input type="text" name="daoName" id="daoName" placeholder="填写dao类全名" class="form-control">
                        </div>
                    </div>
                    <div class="form-group" id="mapping">
                        <div class="row item">
                            <label class="col-sm-1 control-label"></label>
                            <div class="col-sm-10">
                                <div class="row">
                                    <label class="col-sm-6 control-label" style="text-align:center">
                                        数据库字段
                                    </label>
                                    <label class="col-sm-6 control-label" style="text-align:center">
                                        类属性
                                    </label>
                                </div>
                            </div>
                            <label class="col-sm-1"></label>
                        </div>
                        <div class="row item">
                            <label class="col-sm-1 control-label">主键</label>
                            <div class="col-sm-10">
                                <div class="row">
                                    <div class="col-sm-6">
                                        <input name="columns" type="text" class="form-control">
                                    </div>
                                    <div class="col-sm-6">
                                        <input name="properties" type="text" class="form-control">
                                    </div>
                                </div>
                            </div>
                            <label class="col-sm-1">
                                <div class="controls">
                                    <select name="keyPropertyClassName" class="input-xlarge">
                                        <option value="String">String</option>
                                        <option value="int">int</option>
                                        <option value="long">long</option>
                                    </select>
                                </div>
                            </label>
                        </div>
                    </div>
                    <div>
                        <div class="row">
                            <label class="col-sm-3 control-label"></label>
                            <div class="col-sm-6"></div>
                            <label class="col-sm-3">
                                <button type="button" id="add" onclick="addRow()" class="btn btn-orange btn-xs">增加一行</button>
                                &nbsp;&nbsp;
                            </label>
                        </div>
                    </div>
                    <div>
                        <button type="button" class="btn btn-primary" onclick="save()">提交</button>
                    </div>
                    <div>
                    </div>
                </form>
            </div>
            <div class="col-lg-3"></div>
        </div>
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

<div style="display: none">
    <textarea id="copyContent"></textarea>
</div>

<script>
    function addRow() {
        var div = '<div class="row item">' +
                '<label class="col-sm-1 control-label"></label>' +
                '<div class="col-sm-10">' +
                '<div class="row">' +
                '<div class="col-sm-6">' +
                '<input name="columns" type="text" class="form-control">' +
                '</div>' +
                '<div class="col-sm-6">' +
                '<input name="properties"  type="text" class="form-control">' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<label class="col-sm-1">' +
                '<button type="button" onclick="delRow(this)" class="btn btn-danger btn-xs">删除</button>' +
                '</label>' +
                '</div>';
        $("#mapping").append(div);
    }

    function delRow(obj) {
        $(obj).parent().parent().remove();
    }

    function save() {
        $.ajax({
            type:'get',
            url: 'do',
            data: $("#form").serialize(),
            success: function(data) {
                if (data.status == 1) {
                    $('#codeName').html(data.codeName);
                    $('#copyContent').html(data.codeContent);
                    Rainbow.color(data.codeContent, 'java', function(highlighted) {
                        $('#codeContent').html(highlighted);
                        $('[data-remodal-id=modal]').remodal().open();
                    });
                } else {
                    alert(data.msg);
                }
            },
            dataType: 'json'
        });
    }

    var clip = new ZeroClipboard(document.getElementById("copy"));
</script>
</body>
</html>

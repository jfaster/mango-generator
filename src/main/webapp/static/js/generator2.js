var SEARCH = {};

$(function () {

    var supportPlaceholder = 'placeholder'in document.createElement('input');
    var placeholder = function (input) {
        var text = input.attr('placeholder'),
            defaultValue = input.defaultValue;
        if (!defaultValue) {
            input.val(text).addClass("phcolor");
        }
        input.focus(function () {
            if (input.val() == text) {
                $(this).val("");
            }
        });
        input.blur(function () {
            if (input.val() == "") {
                $(this).val(text).addClass("phcolor");
            }
        });
        input.keydown(function () {
            $(this).removeClass("phcolor");
        });
    }
    if (!supportPlaceholder) {
        $('input').each(function () {
            text = $(this).attr("placeholder");
            if ($(this).attr("type") == "text") {
                placeholder($(this));
            }
        });
    }

    $("#bt_add").on("click", function () {
        var txt = "<div class='inp_box_con1 mar2 clearfix'><label class='inp_box_style2'><input type='text' onchange='autoProperty(this)'  name='columns' placeholder='数据库字段'/></label><label class='inp_box_style2 mar1'><input type='text' name='properties' placeholder='类属性'/></label><a tabIndex='-1' href='javascript:;' class='bt_del'></a></div>";
        $(".content_box4").find(".inp_box").append(txt);
        if (!supportPlaceholder) {
            $('input').each(function () {
                text = $(this).attr("placeholder");
                if ($(this).attr("type") == "text") {
                    placeholder($(this));
                }
            });
        }
        $(".inp_box_style1,.inp_box_style2").find("input").on({
            focus: function () {
                var box = $(this).parent();
                box.addClass("on");
            },
            blur: function () {
                var box = $(this).parent();
                box.removeClass("on");
            }
        });

        $("input[name='columns']:last")[0].focus();
    })

    $(".content_box4").on("click", function (e) {
        var tar = $(e.target);
        if (tar.hasClass("bt_del")) {
            tar.parents(".inp_box_con1").remove();
        }
    });

    $(".inp_box_style1,.inp_box_style2").find("input").on({
        focus: function () {
            var box = $(this).parent();
            box.addClass("on");
        },
        blur: function () {
            var box = $(this).parent();
            box.removeClass("on");
        }
    });

    $("#inp_sub").on("click", function (e) {
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
                    $('#errorMsg').html(data.msg);
                    $('[data-remodal-id=errorMsg]').remodal().open();
                }
            },
            dataType: 'json'
        });
    });

});

function autoProperty(obj) {
    var pv = $($(obj).parent().parent().find("input")[1]);
    var cv = $(obj).val().toLowerCase();
    var r = "";
    var u = false;
    for(var i = 0; i < cv.length; i++) {
        var c = cv.charAt(i)
        if (c == '_') {
            u = true;
        } else {
            if (u) {
                c = c.toUpperCase();
                u = false;
            }
            r = r + c;
        }
    }
    pv.val(r);
}
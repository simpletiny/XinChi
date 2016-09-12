var batchCreationLayer;
var ProjectsContext = function () {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.batchId = $("#hidden_batchId").val();
    self.batch = ko.observable({});
    self.batch().parameters = ko.observableArray([]);
    self.parameters = ko.observableArray([]);
    self.allStatus = ['未发送', '发送中', '发送完毕'];

    $.getJSON(self.apiurl + '/wechat/batch/' + self.batchId + "/fast", {}, function (batch) {

        self.batch(batch);
        if (self.batch().parameters == null) {
            var par = new Par();
            var arr = new Array();
            arr.push(par);
            arr.push(par);
            arr.push(par);
            self.parameters(arr);
        } else {
            self.parameters(self.batch().parameters);
        }
    });


    self.sendContent = function () {
        $.getJSON(self.apiurl + '/wechat/batch/' + self.batchId + '/fast', {}, function (smsBatch) {
            if (smsBatch.status != '未发送') {
                fail_msg("请将发送状态设置为【未发送】，保存后重新发送");
            } else {
                if (self.batch().targetsCount == 0) {
                    fail_msg("没有导入名单！");
                    return;
                }
                $.ajax({
                    type: "PUT",
                    contentType: "application/json",
                    url: self.apiurl + '/wechat/batch/' + self.batchId + '/send',
                    data: ko.toJSON({})
                }).done(function () {
                    success_msg("内容推送中, 请关注批次状态");
                }).fail(function () {
                    fail_msg("内容推送失败");
                });
            }
        });
    }

    self.updateBatch = function () {
        if (!$("form").valid()) {
            return;
        }
        startLoadingIndicator("正在保存批次内容");
        self.batch().parameters = getParameter();

        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/wechat/batch/' + self.batchId + '/fast',
            data: ko.toJSON(self.batch)
        }).done(function () {
            success_msg("保存成功");
        }).fail(function () {
            fail_msg("保存失败");
        }).always(function () {
            endLoadingIndicator();
        });
    }
};

var ctx = new ProjectsContext();
$(document).ready(function () {
    ko.applyBindings(ctx);
});

$('#create-batch-btn').on('click', function () {
    batchCreationLayer = $.layer({
        type: 1,
        title: ['新建批次', ''],
        maxmin: false,
        closeBtn: [1, true],
        shadeClose: false,
        area: ['600px', '450px'],
        offset: ['100px', ''],
        page: {
            dom: '#batch-creation'
        },
        end: function () {
            console.log("Done");
        }
    });
});

var Par = function () {
    var name = "";
    var value = "";
}

var getParameter = function () {
    var children = $("#parameter-div").children();
    var result = new Array();
    for (var i = 0; i < children.length; i++) {
        var child = children[i];
        var name = $(child).find("input.name").val();
        var value = $(child).find("input.value").val();
        if (name == "" || value == "")
            continue
        var par = new Par();
        par.name = name;
        par.value = value;
        result.push(par);
    }
    return result;
}

var addParameter = function () {
    var children = $("#parameter-div").children();
    var len = children.length;
    var child = $("<div class='input-row clearfloat'>" +
    "<label class='l'>参数" + (len + 1) + "：</label>" +
    "<div class='col-md-3'><input type='text' class='ip- name' placeholder='参数'/></div>" +
    "<label class='l'>值：</label>" +
    "<div class='col-md-3'><input type='text' class='ip- value' placeholder='值'/></div>" +
    "</div>");
    $("#parameter-div").append(child);
}
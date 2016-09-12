var importRecordsLayer;

var SmsBatchContext = function () {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.projectId = $("#hidden_projectId").val();
    self.smsId = $("#hidden_smsId").val();
    self.allStatus = ['未发送', '发送中', '发送完毕'];
    self.smsBatch = ko.observable({});
    //self.importNumber = ko.observable(500);
    self.query = ko.observable({});

    self.allPositionTypes = ko.observableArray(['行政文员类', '销售类', '客服类', '财务类', '贸易类', '其他类', '技术类', '市场类', '运营类', '产品类', '设计类', '金融类', '行政文员类2', '销售行政类', '待定', '生产制造类', '服务类', '物流类', '采购类']);
    self.allGenders = ko.observableArray(['男', '女']);
    self.query().importNumber = ko.observable(500)
    self.providers = ['创世去哪儿', '创世北京美菜', '创世宜信', '创世上海美菜', '创世51Talk', '创世京科联通', '创世健道科技', '创世FESCO中国银行',
        '创世众信旅游', '创世天津家有购物', '创世KFC', '创世仁科互动/销售易', '创世点融网（天津）', '创世互联企信', '创世慧聪网',
        '创世贝塔斯曼-人人行项目', '创世维音-乐视', '创世掌众金融', '创世上海宜信', '创世上海无忧', '创世沪江网', '创世58同城',
        '创世大宇宙', '创世大唐软件', '创世美联英语', '创世上海点融', '创世天首集团', '创世微信专用', '创世北京奥克巴尔', '创世顺信益',
        '创世溢美金融', '创世百度', '创世利多多', '创世华尔街', '创世盛景网联', '创世网利宝', '创世美利金融', '创世贝塔斯曼乐视', '创世中天嘉华',
        '创世赶集网', '创世上海斑马', '创世上海奥克巴尔', '创世上海特朗思', '创世天津人人车', '创世一点达', '创世北京人人车', '创世京北方', '创世北京银行',
        '创世中信银行', '创世小区网', '创世东方银谷', '创世备用账号'];


    self.userSources = [
        {'name': '邀约名单', 'phase': 'CANDIDATE'}, {'name': '预约名单', 'phase': 'RESERVE'}, {
            'name': '到访名单',
            'phase': 'SIGNIN'
        },
        {'name': '面试名单', 'phase': 'INTERVIEW'}, {'name': 'offer名单', 'phase': 'OFFER'}, {
            'name': '入职名单',
            'phase': 'ONBOARD'
        }
    ];
    self.chosenUserSource = ko.observable();

    self.getSmsBatch = function () {
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/sms-batches/' + self.smsId + '/fast', {}, function (smsBatch) {
            self.smsBatch(smsBatch);
            endLoadingIndicator();
        });
    };

    self.updateBatch = function () {

        if (self.smsBatch().messageType == 'wechat') {
            if (!$("form").valid()) {
                return;
            }
            startLoadingIndicator("正在保存微信批次内容");
        } else {
            startLoadingIndicator("正在保存短信批次内容");
        }

        self.smsBatch().projectId = $("#hidden_projectId").val();
        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/sms-batches/' + self.smsId + '/fast',
            data: ko.toJSON(self.smsBatch)
        }).done(function () {
            success_msg("保存成功");
        }).fail(function () {
            fail_msg("保存失败");
        }).always(function () {
            endLoadingIndicator();
        });
    };

    self.sendSMS = function () {
        if(self.smsBatch().targetsCount==0){
            fail_msg("请导入名单！");
            return;
        }
        self.smsBatch().projectId = $("#hidden_projectId").val();

        $.getJSON(self.apiurl + '/sms-batches/' + self.smsId + '/fast', {}, function (smsBatch) {
            if (smsBatch.status != '未发送') {
                fail_msg("请将发送状态设置为【未发送】，保存后重新发送");
            } else {
                $.ajax({
                    type: "PUT",
                    contentType: "application/json",
                    url: self.apiurl + '/sms-batches/' + self.smsId + '/send',
                    data: ko.toJSON({})
                }).done(function () {
                    if (self.smsBatch().messageType == 'wechat') {
                        success_msg("微信推送中, 请关注微信批次状态");
                    } else {
                        success_msg("群发短信中, 请关注短信批次状态");
                    }
                }).fail(function () {
                    if (self.smsBatch().messageType == 'wechat') {
                        fail_msg("推送微信失败");
                    } else {
                        fail_msg("发送短信通知失败");
                    }
                });
            }
        });
    };

    self.marketing = function () {
        if(self.smsBatch().targetsCount==0){
            fail_msg("请导入名单！");
            return;
        }
        self.smsBatch().projectId = $("#hidden_projectId").val();

        startLoadingIndicator("增加营销次数中");
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/sms-batches/' + self.smsId + '/marketing',
            data: ko.toJSON({})
        }).done(function () {
            success_msg("营销次数已增加");
        }).fail(function () {
            fail_msg("营销次数增加失败");
        }).always(function () {
            endLoadingIndicator();
        });
    };

    self.importUserRecord = function () {
        layer.close(importRecordsLayer);
        startLoadingIndicator("短信名单导入中");

        if (self.chosenUserSource().phase == 'CANDIDATE') {
            console.log(ko.toJSON(self.query))
            self.query().ignoreSMSBatchAssigned = 'true';
            $.ajax({
                type: "PUT",
                contentType: "application/json",
                url: self.apiurl + '/sms-batches/' + self.smsId + '/import-candidates-condition',
                data: ko.toJSON(self.query)
            }).done(function () {
                success_msg("导入成功");
                layer.close(importRecordsLayer);
                self.getSmsBatch();
            }).fail(function () {
                fail_msg("导入失败");
            }).always(function () {
                endLoadingIndicator();
            });
        } else {
            $.ajax({
                type: "PUT",
                contentType: "application/json",
                url: self.apiurl + '/sms-batches/' + self.smsId
                + '/import-participants/project-phase/' + self.chosenUserSource().phase + '/' + self.query().importNumber(),
                data: ko.toJSON({})
            }).done(function () {
                success_msg("导入成功");
                layer.close(importRecordsLayer);
                self.getSmsBatch();
            }).fail(function () {
                fail_msg("导入失败");
            }).always(function () {
                endLoadingIndicator();
            });
        }
    };

    self.changeType = function () {
        if (self.chosenUserSource().phase == 'CANDIDATE') {
            $("#select_gender").attr("disabled", false);
            $("button.ms-choice").attr("disabled", false);
        } else {
            $("#select_gender").attr("disabled", true);
            $("button.ms-choice").attr("disabled", true);
        }
    };
};

var ctx = new SmsBatchContext();

$(document).ready(function () {
    ko.applyBindings(ctx);
    ctx.getSmsBatch();

    $('.multi-select').multipleSelect({
        placeholder: '不限',
        selectAllText: '全选',
        width: '180px',
        minimumCountSelected: 1,
        countSelected: '已选: #',
        allSelected: '已全选'
    });
});

$('#import-record-btn').on('click', function () {
    var title = '导入短信名单';
    if (ctx.smsBatch().messageType == "wechat") {
        title = '导入微信名单';
    }
    importRecordsLayer = $.layer({
        type: 1,
        title: [title, ''],
        maxmin: false,
        closeBtn: [1, true],
        shadeClose: false,
        //area : ['600px' , '300px'],
        area: ['600px', '400px'],
        offset: ['100px', ''],
        page: {
            dom: '#page-import-records'
        },
        end: function () {
            console.log("Done");
        }
    });
});

$('#send-sms-btn').on('click', function () {
    var msg = '是否确定群发短信?';
    if (ctx.smsBatch().messageType == "wechat") {
        msg = '是否确定推送微信?';
    }

    $("#send-sms-btn").attr("disabled", true);
    $.layer({
        shade: [0],
        area: ['auto', 'auto'],
        dialog: {
            msg: msg,
            btns: 2,
            type: 14,
            btn: ['确定', '放弃'],
            yes: function () {
                ctx.sendSMS();
                $("#send-sms-btn").attr("disabled", false);
            }, no: function () {
                $("#send-sms-btn").attr("disabled", false);
            }
        }
    });
});

$(':input[type=number]').on('mousewheel', function (e) {
    $(this).blur();
});

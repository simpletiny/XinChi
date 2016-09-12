var SmsBatchContext = function () {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.smsBatch = ko.observable({status: '未发送'});
    self.providers = ['创世去哪儿', '创世北京美菜', '创世宜信', '创世上海美菜', '创世51Talk', '创世京科联通', '创世健道科技', '创世FESCO中国银行',
        '创世众信旅游', '创世天津家有购物', '创世KFC', '创世仁科互动/销售易', '创世点融网（天津）', '创世互联企信', '创世慧聪网',
        '创世贝塔斯曼-人人行项目', '创世维音-乐视', '创世掌众金融', '创世上海宜信', '创世上海无忧', '创世沪江网', '创世58同城',
        '创世大宇宙', '创世大唐软件', '创世美联英语', '创世上海点融', '创世天首集团', '创世微信专用', '创世北京奥克巴尔', '创世顺信益',
        '创世溢美金融', '创世百度', '创世利多多', '创世华尔街', '创世盛景网联', '创世网利宝', '创世美利金融', '创世贝塔斯曼乐视', '创世中天嘉华',
        '创世赶集网', '创世上海斑马', '创世上海奥克巴尔', '创世上海特朗思', '创世天津人人车', '创世一点达', '创世北京人人车', '创世京北方', '创世北京银行',
        '创世中信银行', '创世小区网', '创世东方银谷', '创世备用账号'];

    self.createBatch = function () {
        // TODO: content should be valid JSON when messageType is wechat

        if (!$("form").valid()) {
            return;
        }

        self.smsBatch().projectId = $("#hidden_projectId").val();
        self.smsBatch().messageType = $("#hidden_message_type").val();

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/sms-batches',
            data: ko.toJSON(self.smsBatch)
        }).done(function (smsBatch) {
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
        }).fail(function (response) {
            fail_msg(response.responseJSON.message);
        });
    };
};

var ctx = new SmsBatchContext();

$(document).ready(function () {
    ko.applyBindings(ctx);
});

$(':input[type=number]').on('mousewheel', function (e) {
    $(this).blur();
});
var CandidateContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.candidate = ko.observable({});
    self.allPositionTypes = ko.observableArray(['行政文员类','销售类','客服类','财务类','贸易类','其他类','技术类','市场类','运营类','产品类','设计类','金融类','行政文员类2','销售行政类','待定','生产制造类','服务类','物流类','采购类']);
    self.genders = ko.observableArray(['男','女']);
    self.createCandidate = function() {
        if (!$("form").valid()) {
            return;
        }
        self.candidate().projectId = $("#hidden_projectId").val();

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/projects/'+ $("#hidden_projectId").val()+  '/candidates',
            data: ko.toJSON([self.candidate])
        }).done(function(candidate) {
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
        });
    };
};

var ctx = new CandidateContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
});

$(':input[type=number]').on('mousewheel',function(e){ $(this).blur();});

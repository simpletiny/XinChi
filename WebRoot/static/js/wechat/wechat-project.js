var batchCreationLayer;
var ProjectsContext = function () {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.query = ko.observable({});
    self.allProjectProgress = ['未开始', '进行中', '已结束'];
    self.batches = ko.observable([]);
    self.project = ko.observable({});
    self.batch  = ko.observable({});

    $.getJSON(self.apiurl + '/wechat/batch/search', {}, function (batches) {
        self.batches(batches);
    });

    self.createBatch = function(){
        if (!$("form").valid()) {
            return;
        }
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/wechat/batch/create',
            data: ko.toJSON(self.batch)
        }).done(function (batch) {
            layer.close(batchCreationLayer);
            self.search();
        }).fail(function (response) {
            //fail_msg(response.responseJSON.message);
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
        area : ['600px' , '251px'],
        offset: ['100px', ''],
        page: {
            dom: '#batch-creation'
        },
        end: function () {
            console.log("Done");
        }
    });
});

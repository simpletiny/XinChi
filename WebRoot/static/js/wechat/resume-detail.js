var importResumesLayer;
var ResumesContext = function () {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.query = ko.observable({});
    self.resumes = ko.observable({total: 0, items: []});
    self.resumeUpdateStartDateRaw = ko.observable();
    self.resumeUpdateEndDateRaw = ko.observable();
    self.loginStartDateRaw = ko.observable();
    self.loginEndDateRaw = ko.observable();
    self.totalCount = ko.observable();

    self.batches = ko.observableArray([]);
    self.chosenBatch = ko.observable({});
    self.exportCount = ko.observable(500);

    // start pagination
    self.currentPage = ko.observable(1);
    self.perPage = 10;
    self.pageNums = ko.observableArray();
    self.totalCount = ko.observable(1);
    self.startIndex = ko.computed(function () {
        return (self.currentPage() - 1) * self.perPage;
    });
    // end pagination

    $.getJSON(self.apiurl + '/wechat/batch/search', {}, function (batches) {
        self.batches(batches);
    });

    self.searchResumes = function () {

        self.query().start = self.startIndex();
        self.query().count = self.perPage;

        self.query().resumeUpdateStartTime = date2iso(self.resumeUpdateStartDateRaw());
        self.query().resumeUpdateEndTime = date2iso(self.resumeUpdateEndDateRaw());
        self.query().loginStartTime = date2iso(self.loginStartDateRaw());
        self.query().loginEndTime = date2iso(self.loginEndDateRaw());
        //console.log(ko.toJSON(self.query));
        startLoadingSimpleIndicator("加载中");
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/wechat/resumes/search',
            data: ko.toJSON(self.query)
        }).done(function (resumes) {
            if (resumes) {

                self.resumes(resumes);
                self.totalCount(Math.ceil(resumes.total / self.perPage));
                console.log(self.totalCount());
                self.setPageNums(self.currentPage());
                $("#search-total-count").html('<div style="padding-top: 3px;"><label style="background-color: #6495ED; font-size: smaller; padding-right: 10px;padding-left: 10px;" class="label">共&nbsp;'
                + resumes.total + '&nbsp;条结果</label></div>');
            }
        }).always(function () {
            endLoadingIndicator();
        });
    };

    self.exportResumesToBatch = function () {

        if (self.chosenBatch() ==null) {
            fail_msg("请选择批次");
            return;
        }
        var batchId = self.chosenBatch().id;

        self.query().start = 0;
        self.query().count = self.exportCount();

        layer.close(importResumesLayer);
        startLoadingIndicator("简历导入中");
        console.log(ko.toJSON(self.query));
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/wechat/resumes/' + batchId + '/export-candidate',
            data: ko.toJSON(self.query)
        }).done(function (result) {
            success_msg("成功: " + result.successCount + ", 失败: " + result.failedCount + ", 跳过: " + result.ignoredCount);
        }).fail(function () {
            fail_msg("导入到批次出错");
        }).always(function () {
            endLoadingIndicator();
        });
    };


    // start pagination
    self.resetPage = function () {
        self.currentPage(1);
    };

    self.previousPage = function () {
        if (self.currentPage() > 1) {
            self.currentPage(self.currentPage() - 1);
            self.refreshPage();
        }
    };

    self.nextPage = function () {
        if (self.currentPage() < self.pageNums().length) {
            self.currentPage(self.currentPage() + 1);
            self.refreshPage();
        }
        console.log(self.currentPage()+":"+self.pageNums().length)
    };

    self.turnPage = function (pageIndex) {
        self.currentPage(pageIndex);
        self.refreshPage();
    };

    self.setPageNums = function (curPage) {
        var startPage = curPage - 4 > 0 ? curPage - 4 : 1;
        var endPage = curPage + 4 <= self.totalCount() ? curPage + 4 : self.totalCount();
        var pageNums = [];
        for (var i = startPage; i <= endPage; i++) {
            pageNums.push(i);

        }
        self.pageNums(pageNums);
    };

    self.refreshPage = function () {
        self.searchResumes();
    };
    // end pagination
};

var ctx = new ResumesContext();
var virtual_board;
$(document).ready(function () {
    ko.applyBindings(ctx);
});

$('#import-resumes-btn').on('click', function () {
    importResumesLayer = $.layer({
        type: 1,
        title: ['导入到项目', ''],
        maxmin: false,
        closeBtn: [1, true],
        shadeClose: false,
        area: ['280px', '260px'],
        offset: ['100px', ''],
        page: {
            dom: '#page-import-resumes'
        },
        end: function () {
            console.log("Done");
        }
    });
});

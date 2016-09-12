var importResumesLayer;

var ResumesContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.query = ko.observable({});
    self.allStatuses = ['未参加面试', '初试通过', '初试未通过', '初试待定', '复试通过', '复试未通过', '复试待定',
        '终试通过', '终试未通过', '终试待定', 'Offer待定', 'Offer接受', 'Offer拒绝', '未入职', '已入职'];
    self.projects = ko.observableArray([]);
    self.chosenProject = ko.observable({});
    self.resumes = ko.observable({total: 0, items: []});
    self.allProjectNames = ko.computed(function(){
        var res = [];
        $(self.projects()).each(function(idx, project){
            res.push(project.name);
        });
        return res;
    });

    self.allDegreeNames = ko.observableArray(['本科','硕士','博士','大专','中专','高中','中技','初中']);
    self.allGenders = ko.observableArray(['男','女']);
    self.allSources = ko.observableArray(['WSHH', '51Job', 'ZhaoPin', '智联', '58同城', '58TC', 'LZ', 'LZ2', '前程无忧', '智联招聘', '赶集网', '智联(北京)', '智联(天津)', '智联(上海)', '58同城(北京)', '58同城(天津)', '58同城(上海)', '前程(北京)', '前程(天津)', '前程(上海)', '赶集(北京)', '赶集(天津)', '赶集(上海)']);

    self.exportCount = ko.observable(10000);

    self.resumeUpdateStartDateRaw =  ko.observable();
    self.resumeUpdateEndDateRaw = ko.observable();
    self.projectStartDateRaw =  ko.observable();
    self.projectEndDateRaw = ko.observable();

    // start pagination
    self.currentPage = ko.observable(1);
    self.perPage = 10;
    self.pageNums = ko.observableArray();
    self.totalCount = ko.observable(1);
    self.startIndex = ko.computed(function () {
        return (self.currentPage() - 1) * self.perPage;
    });
    // end pagination

    $.getJSON(self.apiurl + '/projects/search', {}, function (projects) {
        self.projects(projects);

        $('.multi-select').multipleSelect({
            placeholder: '不限',
            selectAllText: '全选',
            width: '180px',
            minimumCountSelected: 1,
            countSelected: '已选: #',
            allSelected: '已全选'
        });
    });

    self.searchResumes = function() {
        console.log(self.query());

        self.query().start = self.startIndex();
        self.query().count = self.perPage;

        self.query().resumeUpdateStartDate = date2iso(self.resumeUpdateStartDateRaw());
        self.query().resumeUpdateEndDate = date2iso(self.resumeUpdateEndDateRaw());

        self.query().projectStartDate = date2iso(self.projectStartDateRaw());
        self.query().projectEndDate = date2iso(self.projectEndDateRaw());

        startLoadingSimpleIndicator("加载中");
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/resumes/search',
            data: ko.toJSON(self.query)
        }).done(function(resumes) {
            if (resumes) {
                self.resumes(resumes);

                self.totalCount(Math.ceil(resumes.total / self.perPage));
                self.setPageNums(self.currentPage());

                $("#search-total-count").html('<div style="padding-top: 3px;"><label style="background-color: #6495ED; font-size: smaller; padding-right: 10px;padding-left: 10px;" class="label">共&nbsp;'
                + resumes.total+'&nbsp;条结果</label></div>');
            }
        }).always(function(){
            endLoadingIndicator();
        });
    };

    self.exportResumesToProject = function() {
        if (self.chosenProject() == null) {
            fail_msg("请选择项目");
            return;
        }

        self.query().start = 0;
        self.query().count = self.exportCount();

        layer.close(importResumesLayer);
        startLoadingIndicator("简历导入中");

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/resumes/projects/' + self.chosenProject().id + '/export-candidates',
            data: ko.toJSON(self.query)
        }).done(function(result) {
            success_msg("成功: " + result.successCount + ", 失败: " + result.failedCount + ", 跳过: " + result.ignoredCount);
        }).fail(function(){
            fail_msg("导入到项目出错");
        }).always(function(){
            endLoadingIndicator();
        });
    };

    self.initCompanyNameAutoComplete = function() {
        $.getJSON(self.apiurl + '/auto-suggest/companies', {}, function (companyNames) {
            self.allCompanyNames = companyNames;

            $('#autocomplete-company-name').autocomplete({
                lookup: self.allCompanyNames,

                onSelect: function(suggestion) {
                    self.query().companyName = suggestion.value;
                }
            });
        });
    };

    // start pagination
    self.resetPage = function() {
        self.currentPage(1);
    }

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

    self.refreshPage = function() {
        self.searchResumes();
    }
    // end pagination
};

var ctx = new ResumesContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
    ctx.initCompanyNameAutoComplete();
});

$('#import-resumes-btn').on('click', function(){
    importResumesLayer = $.layer({
        type: 1,
        title: ['导入到项目', ''],
        maxmin: false,
        closeBtn: [1, true],
        shadeClose: false,
        area : ['320px' , '260px'],
        offset : ['100px', ''],
        page: {
            dom: '#page-import-resumes'
        },
        end: function() {
            console.log("Done");
        }
    });
});

$(':input[type=number]').on('mousewheel',function(e){ $(this).blur();});

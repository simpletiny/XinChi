$.ajaxSettings.traditional = true;
var assignInterviewerLayer;

var ProjectExecContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.projectId = $("#hidden_projectId").val();
    self.signins = ko.observable({"total": 0, "items": []});
    self.interviews = ko.observable({"total": 0, "items": []});
    self.offers = ko.observable({"total": 0, "items": []});
    self.onboards = ko.observable({"total": 0, "items": []});
    self.project = ko.observable({});
    self.stat = ko.observable({});
    self.allOfferStatuses = ['Offer待定', 'Offer接受', 'Offer拒绝'];
    self.allOnboardStatuses = ['已入职', '未入职'];

    self.query = ko.observable({projectId: self.projectId, interviewStatus: [], interviewer: []});

    self.interviewStatuses = ['等待初试', '初试通过', '初试未通过', '初试待定', '等待复试', '复试通过', '复试未通过', '复试待定', '等待终试', '终试通过', '终试未通过', '终试待定'];
    self.notInterviewStatuses = ['未参加面试'];

    self.interviewers = ko.observableArray([]);
    self.chosenInterviewer = ko.observable({});
    self.interviewerQQ = ko.observable();
    self.chosenSignins = ko.observableArray([]);

    // start pagination
    self.currentPage = ko.observable(1);
    self.perPage = 10;
    self.pageNums = ko.observableArray();
    self.totalCount = ko.observable(1);
    self.startIndex = ko.computed(function () {
        return (self.currentPage() - 1) * self.perPage;
    });
    // end pagination

    $.getJSON(self.apiurl + '/projects/' + self.projectId, {}, function (project) {
        self.project(project);
        $.getJSON(self.apiurl + '/employees/companies/' + self.project().companyId + '/interviewers', {}, function (employees) {
            if (employees) {
                $(employees).each(function (idx, employee) {
                    $.getJSON(self.apiurl + '/company-contacts/employees/' + employee.id, {}, function (contact) {
                        if (contact.qq) {
                            employee.qq = contact.qq;
                            self.changeInterviewer();
                        }
                    });
                });
                self.interviewers(employees);
            }
        });
    });

    self.changeInterviewer = function () {
        if (self.chosenInterviewer() && self.chosenInterviewer().qq) {
            self.interviewerQQ(self.chosenInterviewer().qq);
        }
    };

    self.assignInterviewers = function() {
        console.log("assign-interviewers");
        $(self.chosenSignins()).each(function(idx, participantId) {
            $.ajax({
                type: "PUT",
                contentType: "application/json",
                url: self.apiurl + '/participants/' + participantId + '/assign-interviewers',
                data: ko.toJSON({
                    'interviewers': [self.chosenInterviewer().id],
                    'interviewerQQ': self.interviewerQQ()
                })
            }).done(function(res) {
                success_msg("分配面试官成功");
                layer.close(assignInterviewerLayer);
            });
        });
    };

    self.searchSignins = function() {
        self.query().start = self.startIndex();
        self.query().count = self.perPage;
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/participants/signin-search', $.param(self.query()), function(signins) {
            if(signins) {
                self.signins(signins);

                self.totalCount(Math.ceil(signins.total / self.perPage));
                self.setPageNums(self.currentPage());
            }
            endLoadingIndicator();
        });
    };

    self.searchInterviews = function() {
        self.query().start = self.startIndex();
        self.query().count = self.perPage;
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/participants/interview-search', $.param(self.query()), function(interviews) {
            if(interviews) {
                self.interviews(interviews);

                self.totalCount(Math.ceil(interviews.total / self.perPage));
                self.setPageNums(self.currentPage());
            }
            endLoadingIndicator();
        });
    };

    self.searchOffers = function() {
        self.query().start = self.startIndex();
        self.query().count = self.perPage;
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/participants/offer-search', $.param(self.query()), function(offers) {
            if (offers.items) {
                $(offers.items).each(function(idx, item){
                    item.currentOfferStatus = ko.observable(item.currentOfferStatus);
                    item.estimateOnboardDate = ko.observable(item.estimateOnboardDate);
                });
            }

            if(offers) {
                self.offers(offers);

                self.totalCount(Math.ceil(offers.total / self.perPage));
                self.setPageNums(self.currentPage());

                // inlineDate node is rendered after offers is set
                $('.date-picker').datetimepicker({
                    format: 'Y-m-d',
                    timepicker:false,
                    scrollInput: false,
                    defaultDate: new Date(),
                    lang: 'zh'
                });
            }
            endLoadingIndicator();
        });
    };

    self.searchOnboards = function() {
        self.query().start = self.startIndex();
        self.query().count = self.perPage;
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/participants/onboard-search', $.param(self.query()), function(onboards) {
            if (onboards.items) {
                $(onboards.items).each(function(idx, item){
                    item.currentOnboardStatus = ko.observable(item.currentOnboardStatus);
                });
            }

            if(onboards) {
                self.onboards(onboards);

                self.totalCount(Math.ceil(onboards.total / self.perPage));
                self.setPageNums(self.currentPage());
            }
            endLoadingIndicator();
        });
    };

    self.searchStat = function() {
        $.getJSON(self.apiurl + '/projects/' + self.projectId + '/stat', $.param(self.query()), function(stat) {
            self.stat(stat);
        });
    };

    self.updateOfferStatus = function(data) {
        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/participants/' + data.id + '/offer-status',
            data: ko.toJSON({status: data.currentOfferStatus()})
        }).done(function(){
        });
    };

    self.updateOnboardStatus = function(data) {
        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/participants/' + data.id + '/onboard-status',
            data: ko.toJSON({status: data.currentOnboardStatus()})
        }).done(function(){
        });
    };

    self.updateEstimateOnboardDate = function(data) {
        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/participants/' + data.id + '/estimate-onboard-date',
            data: ko.toJSON({estimateOnboardDate: data.estimateOnboardDate()})
        }).done(function(){
        });
    };

    self.updateStat = function() {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/projects/' + self.projectId + '/refresh-stat',
            data: ko.toJSON({})
        }).done(function(stat){
            self.stat(stat);
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: self.apiurl + '/user-histories/projects/' + self.projectId + '/process',
                data: ko.toJSON({})
            }).done(function(){
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: self.apiurl + '/user-tags/projects/' + self.projectId + '/process',
                    data: ko.toJSON({})
                }).done(function(){
                    success_msg("统计成功");
                }).fail(function(){
                    fail_msg("统计失败");
                });
            }).fail(function(){
                fail_msg("统计失败");
            });
        }).fail(function(){
            fail_msg("统计失败");
        });
    };

    // start pagination
    self.resetPage = function() {
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
        var phase = $("#hidden_phase").val();
        if (phase=='signin') {
            self.searchSignins();
        } else if (phase=='interview') {
            self.searchInterviews();
        } else if (phase=='offer') {
            self.searchOffers();
        } else if (phase=='onboard') {
            self.searchOnboards();
        }
    };
    // end pagination
};

var ctx = new ProjectExecContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
    var phase = $("#hidden_phase").val();
    if (phase=='signin') {
        ctx.query().interviewStatus = ['未参加面试'];
        ctx.searchSignins();
    } else if (phase=='interview') {
        ctx.searchInterviews();
    } else if (phase=='offer') {
        ctx.searchOffers();
    } else if (phase=='onboard') {
        ctx.searchOnboards();
    } else if (phase=='analysis') {
        ctx.searchStat();
    }
});

$('#assign-interview').on('click', function(){
    assignInterviewerLayer = $.layer({
        type: 1,
        title: ['安排面试', ''],
        maxmin: false,
        closeBtn: [1, true],
        shadeClose: false,
        area : ['380px' , '340px'],
        offset : ['100px', ''],
        page: {
            dom: '#page-assign-interview'
        },
        end: function() {

        }
    });
});

$(':input[type=number]').on('mousewheel',function(e){ $(this).blur();});

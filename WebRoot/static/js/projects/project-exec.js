var importCandidatesLayer;
var ProjectExecContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.projectId = $("#hidden_projectId").val();
    self.candidates = ko.observable({"total": 0, "items": []});
    self.smsBatches = ko.observableArray([]);
    self.reservations = ko.observable({"total": 0, "items": []});
    self.signins = ko.observable({"total": 0, "items": []});
    self.interviews = ko.observable({"total": 0, "items": []});
    self.offers = ko.observable({"total": 0, "items": []});
    self.onboards = ko.observable({"total": 0, "items": []});
    self.stat = ko.observable({});
    self.allOfferStatuses = ['Offer待定', 'Offer接受', 'Offer拒绝'];
    self.allOnboardStatuses = ['已入职', '未入职'];
    self.genders = ['男','女'];
    self.allPositionTypes = ko.observableArray(['行政文员类','销售类','客服类','财务类','贸易类','其他类','技术类','市场类','运营类','产品类','设计类','金融类','行政文员类2','销售行政类','待定','生产制造类','服务类','物流类','采购类']);
    self.allNotifyCounts = [0,1, 2, 3, 4, 5];
    self.query = ko.observable({});
    self.job = ko.observable({});
    self.candidateUpdateStartDateRaw =  ko.observable();
    self.candidateUpdateEndDateRaw = ko.observable();
    self.chosenSmsBatch = ko.observable({});
    self.importNumber = ko.observable(500);
    // start pagination
    self.currentPage = ko.observable(1);
    self.perPage = 10;
    self.pageNums = ko.observableArray();
    self.totalCount = ko.observable(1);
    self.startIndex = ko.computed(function () {
        return (self.currentPage() - 1) * self.perPage;
    });
    // end pagination

    $.getJSON(self.apiurl + '/projects/' + self.projectId + '/stat', {}, function(stat) {
        self.stat(stat);
    });

    $.getJSON(self.apiurl + '/projects/' + self.projectId + '/job', {}, function(job) {
        self.job(job);
    });

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

    self.getCandidates = function() {
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/projects/' + self.projectId + '/candidates', $.param({start: self.startIndex(), count: self.perPage}), function (candidates) {
            self.candidates(candidates);
            self.totalCount(Math.ceil(candidates.total / self.perPage));
            self.setPageNums(self.currentPage());
            addTotalCountLabel("candidates-total-count", candidates.total);
            endLoadingIndicator();
        });
    };

    self.searchCandidates = function() {
        startLoadingSimpleIndicator("加载中");
        self.query().startUpdateTime = null;
        self.query().start =self.startIndex();
        self.query().count = 20;
        if (self.candidateUpdateStartDateRaw()) {
            self.query().startUpdateTime = date2iso(self.candidateUpdateStartDateRaw());
        }
        self.query().endUpdateTime = null;
        if (self.candidateUpdateEndDateRaw()) {
            self.query().endUpdateTime = date2iso(self.candidateUpdateEndDateRaw());
        }
        $.getJSON(self.apiurl + '/projects/' + self.projectId + '/candidates', $.param(self.query()), function (candidates) {
            self.candidates(candidates);
            self.totalCount(Math.ceil(candidates.total / self.perPage));
            self.setPageNums(self.currentPage());
            addTotalCountLabel("search-candidates-total-count", candidates.total);
            endLoadingIndicator();
        });
    };

    self.getSmsBatches = function() {
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/projects/' + self.projectId + '/sms-batches', {}, function(smsBatches) {
            self.smsBatches(smsBatches);
            endLoadingIndicator();
        });
    };

    self.getSmsBatchesFast = function() {
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/projects/' + self.projectId + '/sms-batches/fast', {}, function(smsBatches) {
            self.smsBatches(smsBatches);
            endLoadingIndicator();
        });
    };

    self.getReservations = function() {
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/participants', $.param({projectId:self.projectId, projectPhase:'RESERVE', start: self.startIndex(), count: self.perPage}), function(reservations) {
            self.reservations(reservations);
            self.totalCount(Math.ceil(reservations.total / self.perPage));
            self.setPageNums(self.currentPage());
            addTotalCountLabel("appoint-total-count", reservations.total);
            endLoadingIndicator();
        });
    };

    self.getSignins = function() {
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/participants', $.param({projectId:self.projectId, projectPhase:'SIGNIN', start: self.startIndex(), count: self.perPage}), function(signins) {
            self.signins(signins);
            self.totalCount(Math.ceil(signins.total / self.perPage));
            self.setPageNums(self.currentPage());
            addTotalCountLabel("visit-total-count", signins.total);
            endLoadingIndicator();
        });
    };

    self.getInterviews = function() {
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/participants', $.param({projectId:self.projectId, projectPhase:'INTERVIEW', start: self.startIndex(), count: self.perPage}), function(interviews) {
            self.interviews(interviews);
            self.totalCount(Math.ceil(interviews.total / self.perPage));
            self.setPageNums(self.currentPage());
            addTotalCountLabel("interview-total-count", interviews.total);
            endLoadingIndicator();
        });
    };

    self.getOffers = function() {
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/participants', $.param({projectId:self.projectId, projectPhase:'OFFER', start: self.startIndex(), count: self.perPage}), function(offers) {
            if (offers.items) {
                $(offers.items).each(function(idx, item){
                    item.currentOfferStatus = ko.observable(item.currentOfferStatus);
                    item.estimateOnboardDate = ko.observable(item.estimateOnboardDate);
                });
            }
            self.offers(offers);
            self.totalCount(Math.ceil(offers.total / self.perPage));
            self.setPageNums(self.currentPage());
            addTotalCountLabel("offer-total-count", offers.total);
            endLoadingIndicator();

            // inlineDate node is rendered after offers is set
            $('.date-picker').datetimepicker({
                format: 'Y-m-d',
                timepicker:false,
                scrollInput: false,
                defaultDate: new Date(),
                lang: 'zh'
            });
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

    self.getOnboards = function() {
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/participants', $.param({projectId:self.projectId, projectPhase:'ONBOARD', start: self.startIndex(), count: self.perPage}), function(onboards) {
            if (onboards.items) {
                $(onboards.items).each(function(idx, item){
                    item.currentOnboardStatus = ko.observable(item.currentOnboardStatus);
                });
            }

            self.onboards(onboards);
            self.totalCount(Math.ceil(onboards.total / self.perPage));
            self.setPageNums(self.currentPage());
            addTotalCountLabel("onboard-total-count", onboards.total);
            endLoadingIndicator();
        });
    };

    self.interviewNotify = function () {
        if (self.job().id == null) {
            fail_msg("未找到职位定义");
            return;
        }

        startLoadingSimpleIndicator("发送中");
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/jobs/' + self.job().id + '/interview-reminder',
            data: ko.toJSON({})
        }).done(function(res){
            endLoadingIndicator();
            success_msg("成功：" + res.successCount + "，失败：" + res.failedCount + "，跳过：" + res.ignoredCount);
        }).fail(function (reason) {
            fail_msg("通知失败："  + reason.responseJSON.message)
        })
    };

    self.importCandidatesToSmsBatch = function () {
        startLoadingSimpleIndicator("导入中");
        layer.close(importCandidatesLayer);
        self.query().startUpdateTime = null;
        if (self.candidateUpdateStartDateRaw()) {
            self.query().startUpdateTime = date2iso(self.candidateUpdateStartDateRaw());
        }
        self.query().endUpdateTime = null;
        if (self.candidateUpdateEndDateRaw()) {
            self.query().endUpdateTime = date2iso(self.candidateUpdateEndDateRaw());
        }
        self.query().importNumber = self.importNumber();
        self.query().ignoreSMSBatchAssigned = 'false';
        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/sms-batches/' + self.chosenSmsBatch().id + '/import-candidates-condition',
            data: ko.toJSON(self.query())
        }).done(function(res){
            endLoadingIndicator();
            success_msg("导入成功");
        }).fail(function (reason) {
            fail_msg("失败："  + reason.responseJSON.message)
        })
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
        if (phase=='candidates') {
            self.getCandidates();
        } else if(phase=='search-candidates'){
            self.searchCandidates();
        }
        else if (phase=='sms') {
            self.getSmsBatches();
        } else if (phase=='appoint') {
            self.getReservations();
        } else if (phase=='visit') {
            self.getSignins();
        } else if (phase=='interview') {
            self.getInterviews();
        } else if (phase=='offer') {
            self.getOffers();
        } else if (phase=='onboard') {
            self.getOnboards();
        }
    };
    // end pagination
};

var ctx = new ProjectExecContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
    var phase = $("#hidden_phase").val();
    if (phase=='candidates') {
        ctx.getCandidates();
    } else if (phase=='sms') {
        ctx.getSmsBatches();
    } else if (phase=='appoint') {
        ctx.getReservations();
    } else if (phase=='visit') {
        ctx.getSignins();
    } else if (phase=='interview') {
        ctx.getInterviews();
    } else if (phase=='offer') {
        ctx.getOffers();
    } else if (phase=='onboard') {
        ctx.getOnboards();
    } else if (phase=='search-candidates') {
        ctx.getSmsBatchesFast();
        ctx.searchCandidates();
    }

    $('.multi-select').multipleSelect({
        placeholder: '不限',
        selectAllText: '全选',
        width: '100%',
        minimumCountSelected: 1,
        countSelected: '已选: #',
        allSelected: '已全选'
    });
});

$('#create-sms-batch').on('click', function(){
    $.layer({
        type: 2,
        title: ['新建短消息批次', ''],
        maxmin: false,
        closeBtn: [1, true],
        shadeClose: false,
        area : ['600px' , '510px'],
        offset : ['100px', ''],
        iframe: {src: '/projects/' + ctx.projectId + '/phases/sms/create-sms-batch'},
        end: function() {
            ctx.getSmsBatches();
        }
    });
});

$('#create-wechat-batch').on('click', function(){
    $.layer({
        type: 2,
        title: ['新建微信批次', ''],
        maxmin: false,
        closeBtn: [1, true],
        shadeClose: false,
        area : ['600px' , '620px'],
        offset : ['100px', ''],
        iframe: {src: '/projects/' + ctx.projectId + '/phases/sms/create-wechat-batch'},
        end: function() {
            ctx.getSmsBatches();
        }
    });
});

$('#create-candidate').on('click', function(){
    $.layer({
        type: 2,
        title: ['新建被邀约人', ''],
        maxmin: false,
        closeBtn: [1, true],
        shadeClose: false,
        area : ['600px' , '540px'],
        offset : ['100px', ''],
        iframe: {src: '/projects/' + ctx.projectId + '/phases/create-candidate'},
        end: function() {
            console.log('create candidate successfully');
            success_msg("新建被邀约人成功");
        }
    });
});

$('#candidate-upload-sync-btn').fileupload({
    url: '/import-candidate/' + ctx.projectId,
    done: function (e, data) {
        console.log(data.result);
        ctx.getCandidates();
        success_msg("文件上传成功!");
    },
    fail: function (e, data) {
        fail_msg("文件上传失败!");
    }
});

$('#signin-upload-sync-btn').fileupload({
    url: '/import-signin/' + ctx.projectId,
    done: function (e, data) {
        console.log(data.result);
        ctx.getSignins();
        success_msg("文件上传成功!");
    },
    fail: function (e, data) {
        fail_msg("文件上传失败!");
    }
});

$('#interview-upload-sync-btn').fileupload({
    url: '/import-interview/' + ctx.projectId,
    done: function (e, data) {
        console.log(data.result);
        ctx.getInterviews();
        success_msg("文件上传成功!");
    },
    fail: function (e, data) {
        fail_msg("文件上传失败!");
    }
});

$('#import-candidates-to-smsbatch-btn').on('click', function(){
    importCandidatesLayer = $.layer({
        type: 1,
        title: ['导入短信名单', ''],
        maxmin: false,
        closeBtn: [1, true],
        shadeClose: false,
        area : ['600px' , '300px'],
        offset : ['100px', ''],
        page: {
            dom: '#page-import-candidates-to-smsbatch'
        },
        end: function() {
            console.log("Done");
        }
    });
});
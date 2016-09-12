var importResumesLayer;
var importBlackLayer;
var ResumesExtContext = function () {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.query = ko.observable({});
    self.projects = ko.observableArray([]);
    self.chosenProject = ko.observable();
    self.chosenProjectId = ko.observable();
    self.resumes = ko.observable({total: 0, items: []});
    self.allProjectIds = ko.computed(function () {
        var res = [];
        $(self.projects()).each(function (idx, project) {
            res.push(project.id);
        });
        return res;
    });
    self.allProjectNames = ko.computed(function () {
        var res = [];
        $(self.projects()).each(function (idx, project) {
            res.push(project.name);
        });
        return res;
    });


    self.allDegreeNames = ko.observableArray(['本科', '硕士', '博士', '大专', '中专', '高中', '中技', '初中']);
    self.allGenders = ko.observableArray(['男', '女']);
    self.allSources = ko.observableArray(['前程无忧', '智联招聘', '58同城', '赶集网', '赶集(北京)', 'WSHH', '51Job', 'ZhaoPin', '智联', '58TC', 'LZ', 'LZ2']);
    //self.allSources = ko.observableArray(['WSHH', '51Job', 'ZhaoPin', '智联', '58同城', '58TC', 'LZ', 'LZ2', '前程无忧', '智联招聘', '赶集网', '智联(北京)', '智联(天津)', '智联(上海)', '58同城(北京)', '58同城(天津)', '58同城(上海)', '前程(北京)', '前程(天津)', '前程(上海)', '赶集(北京)', '赶集(天津)', '赶集(上海)']);
    self.mofangSources = ko.observableArray(['MOFANG','WECHAT']);
    
    self.exportCount = ko.observable(10000);
    self.allPositionTypes = ko.observableArray(['行政文员类', '销售类', '客服类', '财务类', '贸易类', '其他类', '技术类', '市场类', '运营类', '产品类', '设计类', '金融类', '行政文员类2', '销售行政类', '待定','生产制造类','服务类','物流类','采购类']);
    self.allPreferredCities = ko.observableArray(['北京', '天津', '上海', '深圳']);
    self.resumeUpdateStartDateRaw = ko.observable();
    self.resumeUpdateEndDateRaw = ko.observable();
    self.allReasons = ko.observableArray(['已入职', '已offer', '已到访', '投诉', '职位不匹配']);
    // start pagination
    self.currentPage = ko.observable(1);
    self.perPage = 10;
    self.pageNums = ko.observableArray();
    self.totalCount = ko.observable(1);
    self.startIndex = ko.computed(function () {
        return (self.currentPage() - 1) * self.perPage;
    });
    self.blacklistMobile = ko.observable({});
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

    self.searchResumesExt = function () {

        if($("#page_id").val()=="resumes-mofang"){
            console.log(self.query().sources);
            if(self.query().sources==null || self.query().sources.length==0){
                self.query().sources=['MOFANG','WECHAT'];
            }
        }

        self.query().start = self.startIndex();
        self.query().count = self.perPage;

        self.query().resumeUpdateStartDate = date2iso(self.resumeUpdateStartDateRaw());
        self.query().resumeUpdateEndDate = date2iso(self.resumeUpdateEndDateRaw());

        startLoadingSimpleIndicator("加载中");
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/resumes/search/external',
            data: ko.toJSON(self.query)
        }).done(function (resumes) {
            if (resumes) {
                self.resumes(resumes);

                self.totalCount(Math.ceil(resumes.total / self.perPage));
                self.setPageNums(self.currentPage());

                $("#search-total-count").html('<div style="padding-top: 3px;"><label style="background-color: #6495ED; font-size: smaller; padding-right: 10px;padding-left: 10px;" class="label">共&nbsp;'
                + resumes.total + '&nbsp;条结果</label></div>');
            }
        }).always(function () {
            endLoadingIndicator();
        });
    };

    self.exportResumesToProject = function () {
        var chosenProjectId = $(".chosenProjectId").val();
        if (chosenProjectId == "") {
            fail_msg("请选择项目");
            return;
        }

        self.query().start = 0;
        self.query().count = self.exportCount();

        layer.close(importResumesLayer);
        startLoadingIndicator("简历导入中");
        console.log(  self.apiurl + '/resumes/projects/' + chosenProjectId + '/export-candidates/external' );
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/resumes/projects/' + chosenProjectId + '/export-candidates/external',
            data: ko.toJSON(self.query)
        }).done(function (result) {
            success_msg("成功: " + result.successCount + ", 失败: " + result.failedCount + ", 跳过: " + result.ignoredCount);
        }).fail(function () {
            fail_msg("导入到项目出错");
        }).always(function () {
            endLoadingIndicator();
        });
    };
    self.exportResumes = function () {
        self.query().resumeUpdateStartDate = null;
        if (self.resumeUpdateStartDateRaw()) {
            self.query().resumeUpdateStartDate = self.resumeUpdateStartDateRaw();
        }
        self.query().resumeUpdateEndDate = null;
        if (self.resumeUpdateEndDateRaw()) {
            self.query().resumeUpdateEndDate = self.resumeUpdateEndDateRaw();
        }

        var param = $.param(self.query(), true);
        window.location.href = self.apiurl + '/resumes/export?' + param;
    };
    self.initCompanyNameAutoComplete = function () {
        $.getJSON(self.apiurl + '/auto-suggest/companies', {}, function (companyNames) {
            self.allCompanyNames = companyNames;

            $('#autocomplete-company-name').autocomplete({
                lookup: self.allCompanyNames,

                onSelect: function (suggestion) {
                    self.query().companyName = suggestion.value;
                }
            });
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
        self.searchResumesExt();
    };
    self.exportResumesToBlack = function () {
        if(!$("#form1").valid())
            return;
        layer.close(importBlackLayer);
        startLoadingIndicator("黑名单导入中");

        self.query().resumeUpdateStartDate = date2iso(self.resumeUpdateStartDateRaw());
        self.query().resumeUpdateEndDate = date2iso(self.resumeUpdateEndDateRaw());
        self.query().blackStartDate = date2iso(self.query().blackStartDate);
        self.query().blackEndDate = date2iso(self.query().blackEndDate);
        console.log(ko.toJSON(self.query))
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/resumes/export-resumes/black',
            data: ko.toJSON(self.query)
        }).done(function (result) {
            success_msg("成功: " + result.successCount + ", 失败: " + result.failedCount + ", 跳过: " + result.ignoredCount);
        }).fail(function () {
            fail_msg("导入到黑名单出错");
        }).always(function () {
            endLoadingIndicator();
        });
    }
    // end pagination
};

var ctx = new ResumesExtContext();
var virtual_board;
$(document).ready(function () {
    ko.applyBindings(ctx);
    ctx.initCompanyNameAutoComplete();

    //create virtual choosen board
    virtual_board = $("<div class='form-control' style='top:84px;left:40px;width:240px;height:100%;max-height:200px;overflow-y: auto;z-index:99;position:absolute'>").hide();
    $("#display-employee").append(virtual_board);
});

$('#import-resumes-btn').on('click', function () {
    importResumesLayer = $.layer({
        type: 1,
        title: ['导入到项目', ''],
        maxmin: false,
        closeBtn: [1, true],
        shadeClose: false,
        area: ['320px', '260px'],
        offset: ['100px', ''],
        page: {
            dom: '#page-import-resumes'
        },
        end: function () {
            console.log("Done");
            virtual_board.hide();
        }
    });
});
$('#export-black-btn').on('click', function () {
    importBlackLayer = $.layer({
        type: 1,
        title: ['导入黑名单', ''],
        maxmin: false,
        closeBtn: [1, true],
        shadeClose: false,
        area: ['600px', '350px'],
        offset: ['50px', ''],
        page: {
            dom: '#page-export-black'
        },
        end: function () {
            console.log("Done");
        }
    });
});

$('#batch-import-resume-btn').fileupload({
    url: '/batch-import-external-resumes',
    done: function (e, data) {
        success_msg("成功: " + data.result.successCount + ", 失败: " + data.result.failedCount + ", 跳过: " + data.result.ignoredCount);
    },
    fail: function (e, data) {
        fail_msg("批量导入简历失败!");
    }
});

$(':input[type=number]').on('mousewheel', function (e) {
    $(this).blur();
});

//all for searching options while choosing project
function focusProject() {
    if (virtual_board.html() == "") {
        $(ctx.allProjectNames()).each(function (idx, name) {
            var son = $("<div style='overflow:hidden;cursor:pointer' class='board_son' onclick='chooseProject(this)' onmouseover='this.style.backgroundColor=\"#088DF7\";this.style.color=\"white\"' onmouseout='this.style.backgroundColor=\"\";this.style.color=\"\"'>");
            var span = $("<span>").html(name);
            var txt = $("<input type='hidden'>").val(ctx.allProjectIds()[idx]);
            son.append(span);
            son.append(txt);
            virtual_board.append(son);
        })
    }
    refreshBoard();
    virtual_board.show();
}
function chooseProject(div) {
    $(".project").val($(div).find("span").html().trim());
    $(".chosenProjectId").val($(div).find("input").val());

    virtual_board.hide();
}
function refreshBoard() {
    var txt = $(".project").val().trim();
    if (txt == "")
        $(".chosenProjectId").val("");
    var options = $(".board_son");
    for (var i = 0; i < options.length; i++) {
        var option = options[i];
        if ($(option).find("span").html().trim().indexOf(txt) > -1) {
            $(option).show();
        } else {
            $(option).hide();
        }
    }
}

$("#form1").validate({
    highlight: function(element) {
        $(element).closest('.ip').removeClass('has-success').addClass('has-error');
    },
    unhighlight: function(element) {
        $(element).closest('.ip').removeClass('has-error').addClass('has-success');
    },
    rules: {
    },
    errorElement: 'span',
    errorClass: 'help-block',
    errorPlacement: function(error, element) {
        if(element.closest(".ip").length) {
            element.closest(".ip").append(error);
        } else {
            error.insertAfter(element);
        }
    },
    messages: {
        c_password: {
            equalTo: '两次密码输入不一致'
        }
    }
});
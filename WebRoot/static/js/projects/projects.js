var ProjectsContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.projects = ko.observableArray([]);
    self.query = ko.observable({projectProgress: ["进行中"],projectManager:[]});
    self.projectPhases = ["未开始", "进行中", "已结束", "企业发布"];
    self.projectManagersMap = ko.observable({});
    self.projectManagers = ko.observableArray([]);
    self.chosenProjectManagers = ko.observableArray([]);
    self.chosenProjects = ko.observableArray([]);

    $.getJSON(self.apiurl + '/employees/roles/PM', {}, function (employees) {
        self.projectManagers(employees);
        $(employees).each(function(idx, employee){
            self.projectManagersMap[employee.id] = employee.employeeName;
        });
    });

    self.searchProjects = function() {
        startLoadingSimpleIndicator("加载中");
        $(":input[value!='']").serialize();
        $.getJSON(self.apiurl + '/projects/search', $.param(self.query(), true).replace(/&?[^=&]+=(&|$)/g,''), function (projects) {
            $(projects).each(function(idx, project) {
                project.pmNames = ko.computed(function(){
                    var res = [];
                    $(project.projectManagers).each(function(idx, pmId){
                        if (self.projectManagersMap[pmId]) {
                            res.push(self.projectManagersMap[pmId]);
                        }
                    });
                    return res;
                });

                project.stat = ko.observable({});
                $.getJSON(self.apiurl + '/projects/' + project.id + '/stat', {}, function (stat) {
                    project.stat(stat);
                });
            });
            self.projects(projects);
            endLoadingIndicator();
        });
    };

    self.batchPublishJobs = function () {
        if (self.chosenProjects().length==0) {
            fail_msg("请选择项目");
            return;
        }
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/projects/change-job-status',
            data: ko.toJSON({projectIds: self.chosenProjects, status: '已发布'})
        }).done(function(res){
            success_msg("成功：" + res.successCount + "，失败：" + res.failedCount + "，跳过：" + res.ignoredCount);
        }).fail(function(res) {
            fail_msg(res.responseJSON.message);
        });
    };

    self.batchPublishProjects = function () {
        if (self.chosenProjects().length==0) {
            fail_msg("请选择项目");
            return;
        }
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/projects/change-project-progress',
            data: ko.toJSON({projectIds: self.chosenProjects, progress: '进行中'})
        }).done(function(res){
            success_msg("成功：" + res.successCount + "，失败：" + res.failedCount + "，跳过：" + res.ignoredCount);
        }).fail(function(res) {
            fail_msg(res.responseJSON.message);
        });
    };

    self.batchUnpublishJobs = function () {
        if (self.chosenProjects().length==0) {
            fail_msg("请选择项目");
            return;
        }
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/projects/change-job-status',
            data: ko.toJSON({projectIds: self.chosenProjects, status: '未发布'})
        }).done(function(res){
            success_msg("成功：" + res.successCount + "，失败：" + res.failedCount + "，跳过：" + res.ignoredCount);
        }).fail(function(res) {
            fail_msg(res.responseJSON.message);
        });
    };

    self.batchUnpublishProjects = function () {
        if (self.chosenProjects().length==0) {
            fail_msg("请选择项目");
            return;
        }
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/projects/change-project-progress',
            data: ko.toJSON({projectIds: self.chosenProjects, progress: '已结束'})
        }).done(function(res){
            success_msg("成功：" + res.successCount + "，失败：" + res.failedCount + "，跳过：" + res.ignoredCount);
        }).fail(function(res) {
            fail_msg(res.responseJSON.message);
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

    self.initProjectNameAutoComplete = function() {
        $.getJSON(self.apiurl + '/auto-suggest/projects', {}, function (projectNames) {
            self.allProjectNames = projectNames;

            $('#autocomplete-project-name').autocomplete({
                lookup: self.allProjectNames,

                onSelect: function(suggestion) {
                    self.query().projectName = suggestion.value;
                }
            });
        });
    };
};

var ctx = new ProjectsContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
    ctx.searchProjects();
    ctx.initCompanyNameAutoComplete();
    ctx.initProjectNameAutoComplete();
});


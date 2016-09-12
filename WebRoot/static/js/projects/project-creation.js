var ProjectContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.project = ko.observable({});
    self.projectIntro = ko.observable({});
    self.chosenCompany = ko.observable({});
    self.chosenCompanyId = ko.observable();
    self.wagesLevels = ['3000以下', '3000-5000', '5000-8000', '8000-10000', '10000-15000', '15000-20000', '20000以上'];
    self.laborRelations = ['与公司签', '第三方派遣', '魔方派遣', '全风险外包'];
    self.insurances = ['最低缴纳', '部分缴纳', '全额缴纳', '只缴纳社保', '不缴纳社保公积金'];
    self.degrees = ['本科及以上', '大专及以上', '中专及以上', '高中及以上', '初中及以上'];
    self.genders = ['男', '女', '不限'];
    self.companies = ko.observableArray([{id: -1, name: '加载中...'}]);
    self.chosenProjectManagers = ko.observableArray([]);
    self.projectManagers = ko.observableArray([]);

    self.allProjectProgress = ['未开始', '进行中', '已结束'];

    $.getJSON(self.apiurl + '/companies/all', {}, function (companies) {
        self.companies(companies);
    });

    $.getJSON(self.apiurl + '/employees/roles/PM', {}, function (employees) {
        self.projectManagers(employees);
    });

    self.createProject = function() {
        if (!$("form").valid()) {
            return;
        }

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/projects',
            data: ko.toJSON({companyId: self.chosenCompanyId, name: self.projectIntro().projectName, progress: self.project().progress, projectManagers: self.chosenProjectManagers()})
        }).done(function(project){
            console.log('created project ' + project.id);
            console.log(self.projectIntro());
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: self.apiurl + '/projects/' + project.id + '/intro',
                data: ko.toJSON(self.projectIntro)
            }).done(function(projectIntro) {
                window.location.replace("/projects/" + project.id + '/detail');
            });
        });
    };

    self.projectId = $("#hidden_projectId").val();
    if (self.projectId) {
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/projects/' + self.projectId + '/intro', {}, function (projectIntro) {
            if (projectIntro) {
                self.projectIntro(projectIntro);
            }
        });
        $.getJSON(self.apiurl + '/projects/' + self.projectId, {}, function (project) {
            self.project(project);
            if (project.projectManagers) {
                var tempPMs = [];
                $(project.projectManagers).each(function(){
                    tempPMs.push(this.toString());
                });
                self.chosenProjectManagers(tempPMs);
            }
            $.getJSON(self.apiurl + '/companies/all', {}, function (companies) {
                self.companies(companies);
                self.chosenCompanyId(project.companyId);
                $(companies).each(function(){
                   if (this.id == project.companyId) {
                       self.chosenCompany(this);
                   }
                });
                endLoadingIndicator();
            });
        });
    }

    self.updateProject = function() {
        if (!$("form").valid()) {
            return;
        }

        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/projects/' + self.projectId,
            data:  ko.toJSON({companyId: self.chosenCompanyId, progress: self.project().progress, name: self.projectIntro().projectName, projectManagers: self.chosenProjectManagers()})
        }).done(function(project){
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: self.apiurl + '/projects/' + project.id + '/intro',
                data: ko.toJSON(self.projectIntro)
            }).done(function(projectIntro) {
                window.location.replace("/projects/" + project.id + '/detail');
            });
        });

    };
};

var ctx = new ProjectContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
});

$(':input[type=number]').on('mousewheel',function(e){ $(this).blur();});

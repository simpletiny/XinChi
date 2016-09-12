var ProjectsContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.employeeId = $("#hidden_employee_id").val();

    self.projects = ko.observableArray([]);
    self.query = ko.observable({projectProgress: []});
    self.allProjectProgress = ["未开始", "进行中", "已结束"];

    self.searchProjects = function() {
        $(":input[value!='']").serialize();

        // fetch employee id from session
        self.query().employeeId = self.employeeId;
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/projects/search-by-employee', $.param(self.query(), true).replace(/&?[^=&]+=(&|$)/g,''), function (projects) {
            self.projects(projects);
            endLoadingIndicator();
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
    ctx.initProjectNameAutoComplete();
});


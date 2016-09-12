var ProjectsContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.financeRolls = ko.observableArray([]);
    self.query = ko.observable({projectProgress: []});
    self.projectPhases = ["未开始", "进行中", "已结束"];

    self.searchFinanceRolls = function() {
        $(":input[value!='']").serialize();
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/financial-rolls/search', $.param(self.query(), true).replace(/&?[^=&]+=(&|$)/g,''), function (financeRolls) {
            endLoadingIndicator();
            self.financeRolls(financeRolls);
        });
    }
};

var ctx = new ProjectsContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
    ctx.searchFinanceRolls();
});


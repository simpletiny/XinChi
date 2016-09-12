var FinanceRollContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.financeId = $("#hidden_finance_id").val();
    self.project = ko.observable({});
    self.projectIntro = ko.observable({});
    self.chosenCompany = ko.observable({});
    self.chosenProject = ko.observable({});
    self.chosenSales = ko.observable({});
    self.companies = ko.observableArray([{id: -1, name: '加载中...'}]);
    self.projects = ko.observableArray([{id: -1, name: '加载中...'}]);
    self.allSales = ko.observableArray([]);
    self.financeRoll = ko.observable({});

    $.getJSON(self.apiurl + '/companies/all', {}, function (companies) {
        self.companies(companies);
    });

    $.getJSON(self.apiurl + '/projects/search', {}, function (projects) {
        self.projects(projects);
    });

    $.getJSON(self.apiurl + '/employees/roles/SALES', {}, function (employees) {
        self.allSales(employees);
    });

    self.createFinanceRoll = function() {
        if (!$("form").valid()) {
            return;
        }

        self.financeRoll().projectName = $("#project").find("option:selected").text();
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/financial-rolls',
            data: ko.toJSON(self.financeRoll)
        }).done(function(financeRoll) {
            window.location.replace("/finance-rolls");
        });
    };

    if (self.financeId) {
        $.getJSON(self.apiurl + '/financial-rolls/' + self.financeId, {}, function (financeRoll) {
            $.getJSON(self.apiurl + '/companies/all', {}, function (companies) {
                self.companies(companies);
                $(companies).each(function(){
                   if (this.id == financeRoll.companyId) {
                       self.chosenCompany(this);
                   }
                });
                $.getJSON(self.apiurl + '/projects/search', {}, function (projects) {
                    self.projects(projects);
                    $(projects).each(function(){
                       if (this.id == financeRoll.projectId) {
                           self.chosenProject(this);
                       }
                    });
                    $.getJSON(self.apiurl + '/employees/roles/SALES', {}, function (employees) {
                        self.allSales(employees);
                        $(employees).each(function(){
                           if (this.employeeName == financeRoll.projectManager) {
                               self.chosenSales(this);
                           }
                        });
                        self.financeRoll(financeRoll);
                    });
                });
            });


        });
    }

    self.updateFinanceRoll = function() {
        if (!$("form").valid()) {
            return;
        }

        self.financeRoll().projectName = $("#project").find("option:selected").text();
        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/financial-rolls/' + self.financeId,
            data:  ko.toJSON(self.financeRoll)
        }).done(function(financeRoll){
            window.location.replace("/finance-rolls/" + self.financeId + '/detail');
        });
    };
};

var ctx = new FinanceRollContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
});

$(':input[type=number]').on('mousewheel',function(e){ $(this).blur();});


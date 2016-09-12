var EmployeesContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.employees = ko.observable({"total": 0, "items": []});

    self.roleMapping = {
        'ADMIN': '管理员',
        'SALES': '销售人员',
        'PM': '项目经理',
        'HR': '企业HR'
    }

    // start pagination
    self.currentPage = ko.observable(1);
    self.perPage = 10;
    self.pageNums = ko.observableArray();
    self.totalCount = ko.observable(1);
    self.startIndex = ko.computed(function () {
        return (self.currentPage() - 1) * self.perPage;
    });
    // end pagination

    self.mappedRoles = ko.computed(function(){
        var results = {};

        $(self.employees().items).each(function(employeeIdx, employee){
            var res = ''
            $(employee.roles).each(function(roleIdx, role){
                res = res + self.roleMapping[role] + ' | ';
            });

            results[employee.id] = (res + '^EOF^').replace('| ^EOF^','').replace('^EOF^', '');
        });

        return results;
    });

    self.searchEmployees = function() {
        $.getJSON(self.apiurl + '/employees', $.param({start: self.startIndex(), count: self.perPage}), function (employees) {
            self.employees(employees);

            self.totalCount(Math.ceil(employees.total / self.perPage));
            self.setPageNums(self.currentPage());
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
        self.searchEmployees();
    }
    // end pagination
};

var ctx = new EmployeesContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
    ctx.searchEmployees();
});

var EmployeeContext = function () {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.employeeId = $("#hidden_employee_id").val();
    self.employee = ko.observable({roles: []});
    self.allRoles = ['ADMIN', 'SALES', 'PM', 'HR'];

    self.changePasswordRequest = ko.observable({});

    self.roleMapping = {
        'ADMIN': '管理员',
        'SALES': '销售人员',
        'PM': '项目经理',
        'HR': '企业HR'
    };

    self.mappedRoles = ko.computed(function () {
        var res = '';
        $(self.employee().roles).each(function (idx, role) {
            res = res + self.roleMapping[role] + ' | ';
        });

        return (res + '^EOF^').replace('| ^EOF^', '').replace('^EOF^', '');
    });

    if (self.employeeId) {
        $.getJSON(self.apiurl + '/employees/' + self.employeeId, {}, function (employee) {
            self.employee(employee);
        });
    }

    self.createEmployee = function () {
        if (!$("form").valid()) {
            return;
        }

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/employees',
            data: ko.toJSON(self.employee)
        }).done(function (employee) {
            window.location.replace("/employees/" + employee.id + '/detail');
        });
    };

    self.updateEmployee = function () {
        if (!$("form").valid()) {
            return;
        }
        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/employees/' + self.employeeId,
            data: ko.toJSON(self.employee)
        }).done(function (employee) {
            window.location.replace("/employees/" + self.employeeId + '/detail');
        });
    };

    self.disableEmployee = function () {
        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/employees/' + self.employeeId + '/disable',
            data: ko.toJSON({})
        }).done(function (employee) {
            window.location.replace("/employees/" + self.employeeId + '/detail');
        }).fail(function () {
            fail_msg("操作失败");
        });
    };

    self.activateEmployee = function () {
        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/employees/' + self.employeeId + '/activate',
            data: ko.toJSON({})
        }).done(function (employee) {
            window.location.replace("/employees/" + self.employeeId + '/detail');
        }).fail(function () {
            fail_msg("操作失败");
        });
    };

    self.changePassword = function () {
        if (!$("form").valid()) {
            return;
        }

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/employees/' + self.employeeId + '/password/change',
            data: ko.toJSON(self.changePasswordRequest)
        }).done(function () {
            window.location.replace("/employees/" + self.employeeId + '/detail');
        }).fail(function (response) {
            fail_msg(response.responseJSON.message);
        });
    };
};

var ctx = new EmployeeContext();

$(document).ready(function () {
    ko.applyBindings(ctx);
});

$(':input[type=number]').on('mousewheel', function (e) {
    $(this).blur();
});

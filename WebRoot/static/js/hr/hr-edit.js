var createContactLayer;

var CompanyContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.companyId = $("#hidden_company_id").val();
    self.contactId = $("#hidden_contact_id").val();

    self.contact = ko.observable({
        employeeId: ko.observable()
    });

    self.hrRoles = ['决策者', '建议者', '执行者', '参与者'];
    self.hrStatuses = ['在职', '离职', '休假'];
    self.contactWays = ['电话', '拜访', '邮件'];
    self.allProgress = ['新增客户','联系中', '有意向', '合作中', '合作结束', '共享客户'];

    self.contactRecord = ko.observable({
        companyId: self.companyId,
        companyContactId: self.contactId,
        saleProgress:ko.observable()
    });

    self.hrEmployees = ko.observableArray([{id: -1, name: '加载中...'}]);
    self.chosenEmployeeId = ko.observable();

    $.getJSON(self.apiurl + '/employees/roles/HR', {}, function (hrEmployees) {
        self.hrEmployees(hrEmployees);
    });

    self.bindEmployee = function () {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/company-contacts/' + self.contactId + '/bind-employee/' + self.chosenEmployeeId(),
            data: ko.toJSON({})
        }).done(function () {
            success_msg("绑定平台账号成功");
            self.contact().employeeId(self.chosenEmployeeId());

            layer.close(bindEmployeeLayer);
        }).fail(function () {
            fail_msg("操作失败");
        });
    };

    $.getJSON(self.apiurl + '/company-contacts/' + self.contactId, {}, function (contact) {
        var employeeId = contact.employeeId;
        contact.employeeId = ko.observable();
        contact.employeeId(employeeId);

        self.contact(contact);

        $.getJSON(self.apiurl + '/companies/' + self.contact().companyId, {}, function (company) {
            self.contactRecord().saleProgress(company.saleProgress);
        });
    });

    self.updateHr = function() {
        if (!$("form").valid()) {
            return;
        }

        self.contact().companyId = self.companyId;
        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/company-contacts/' + self.contactId,
            data: ko.toJSON(self.contact)
        }).done(function(contact){
            console.log('created contact ' + contact.id);
            window.location.replace("/companies/" + self.companyId + '/hrs');
        });
    };

    self.createContactRecord = function() {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/company-contact-records',
            data: ko.toJSON(self.contactRecord)
        }).done(function(res) {
            success_msg("添加联系记录成功");
            layer.close(createContactRecordLayer);
        });
    };
};

var ctx = new CompanyContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
});

$('#create-contact-record-btn').on('click', function(){
    createContactRecordLayer = $.layer({
        type: 1,
        title: ['添加联系记录', ''],
        maxmin: false,
        closeBtn: [1, true],
        shadeClose: false,
        area : ['560px' , '420px'],
        offset : ['100px', ''],
        page: {
            dom: '#page-create-contact-record'
        },
        end: function() {
            console.log("Done");
        }
    });
});

$('#platform-id-btn').on('click', function(){
    bindEmployeeLayer = $.layer({
        type: 1,
        title: ['HR魔方平台账号', ''],
        maxmin: false,
        closeBtn: [1, true],
        shadeClose: false,
        area : ['320px' , '240px'],
        offset : ['100px', ''],
        page: {
            dom: '#page-platform-id'
        },
        end: function() {
            console.log("Done");
        }
    });
});

$('#edit-employee-btn').on('click', function(){
    $("#display-employee").hide();
    $("#edit-employee").show();
});

$(':input[type=number]').on('mousewheel',function(e){ $(this).blur();});

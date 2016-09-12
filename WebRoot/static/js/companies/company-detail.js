var CompanyContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.companyId = $("#hidden_company_id").val();
    self.company = ko.observable({sales: []});
    self.allSales = ko.observableArray([]);

    self.publishStatuses = ['未发布', '已发布'];

    startLoadingSimpleIndicator("加载中");
    $.getJSON(self.apiurl + '/companies/' + self.companyId, {}, function (company) {
        if (company.sales) {
            $(company.sales).each(function(idx, salesId){
                company.sales[idx] = salesId.toString();
            });
        }

        $.getJSON(self.apiurl + '/employees/roles/SALES', {}, function (employees) {
            if (employees) {
                self.allSales(employees);
                var allSalesMap = {};
                $(employees).each(function(idx, employee){
                    allSalesMap[employee.id] = employee;
                });
                if (company.sales) {
                   var salesName = '';
                   $(company.sales).each(function (idx, salesId) {
                        if (allSalesMap[salesId] && allSalesMap[salesId].employeeName) {
                            if (salesName != '') {
                                salesName = salesName + ',';
                            }
                            salesName = salesName + allSalesMap[salesId].employeeName;
                        }
                   });
                   company.salesName = salesName;
                }
            }
            self.company(company);
            endLoadingIndicator();

            $('#company-grade').rater({
                max: 5,
                value: company.grade,
                after_click	: function(ret) {
                    $('#company-grade-input').val(ret.number).change();
                }
            });
        });
    }).fail(function(reason){
        fail_msg(reason.responseJSON.message);
    });

    self.updateCompany = function() {
        if (!$("form").valid()) {
            return;
        }

        delete self.company().salesName
        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/companies/' + self.companyId,
            data:  ko.toJSON(self.company)
        }).done(function(company){
            window.location.replace("/companies/" + self.companyId + '/detail');
        });
    };
};

var ctx = new CompanyContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
});

$(':input[type=number]').on('mousewheel',function(e){ $(this).blur();});

var CompanyContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.company = ko.observable({sales: []});
    self.allSales = ko.observableArray([]);

    self.publishStatuses = ['未发布', '已发布'];

    $.getJSON(self.apiurl + '/employees/roles/SALES', {}, function (employees) {
        self.allSales(employees);
    });

    $('#company-grade').rater({
        max: 5,
        value: 1,
        after_click	: function(ret) {
            $('#company-grade-input').val(ret.number).change();
        }
    });

    self.createCompany = function() {
        if (!$("form").valid()) {
            return;
        }

        self.company().saleProgress = '新增客户';

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/companies',
            data: ko.toJSON(self.company)
        }).done(function(company){
            console.log('created company ' + company.id);
            window.location.replace("/companies/" + company.id + '/detail');
        });
    };
};

var ctx = new CompanyContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
});

$(':input[type=number]').on('mousewheel',function(e){ $(this).blur();});

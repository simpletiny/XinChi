var CompanyContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.companyId = $("#hidden_company_id").val();
    self.contact = ko.observable({});

    self.hrRoles = ['决策者', '建议者', '执行者', '参与者'];
    self.hrStatuses = ['在职', '离职', '休假'];

    self.createHr = function() {
        if (!$("form").valid()) {
            return;
        }

        self.contact().companyId = self.companyId;
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/company-contacts',
            data: ko.toJSON(self.contact)
        }).done(function(contact){
            console.log('created contact ' + contact.id);
            window.location.replace("/companies/" + self.companyId + '/hrs');
        });
    };
};

var ctx = new CompanyContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
});

$(':input[type=number]').on('mousewheel',function(e){ $(this).blur();});

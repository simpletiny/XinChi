var CompanyContactsContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.companyId = $("#hidden_company_id").val();
    self.companyContacts = ko.observableArray([]);

    self.getCompanyContacts = function() {
        $.getJSON(self.apiurl + '/company-contacts/search', {companyId: self.companyId}, function (companyContacts) {
            self.companyContacts(companyContacts);
        });
    };
};

var ctx = new CompanyContactsContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
    ctx.getCompanyContacts();
});

var CompaniesContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.companyId = $("#hidden_company_id").val();
    self.company = ko.observable({});
    self.contacts = ko.observableArray([]);
    self.contactsMap = ko.observable({});
    self.contactRecords = ko.observable({});
    self.contactRecordsMap = ko.observable({});
    self.contactRecordsMap.extend({ notify: 'always' });

    self.searchHrs = function() {
        $.getJSON(self.apiurl + '/companies/' + self.companyId + '/company-contacts', function (contacts) {
            self.contacts(contacts);

            $(contacts).each(function(idx, contact){
                self.contactsMap()[contact.id] = contact;
                $.getJSON(self.apiurl + '/company-contacts/' + contact.id + '/last-contact-record', function (record) {
                    // TODO: this is hack, might have issues
                    var temp = self.contactRecordsMap();
                    temp[contact.id] = record;
                    self.contactRecordsMap(temp);
                });
            });
        });
    };

    self.searchContactRecords = function() {
        $.getJSON(self.apiurl + '/companies/' + self.companyId + '/company-contact-records', function (records) {
            // hack fix
            $.getJSON(self.apiurl + '/companies/' + self.companyId + '/company-contacts', function (contacts) {
                $(contacts).each(function(idx, contact) {
                    self.contactsMap()[contact.id] = contact;
                    self.contactRecords(records);
                });
            });


            self.contactRecords(records);
        });
    };
};

var ctx = new CompaniesContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
    ctx.searchHrs();
    ctx.searchContactRecords();
});

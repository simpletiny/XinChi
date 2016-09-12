var CompaniesContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.companies = ko.observableArray([]);
    self.allProgress = ['新增客户','联系中', '有意向', '合作中', '合作结束', '共享客户'];
    self.query = ko.observable({saleProgress: []});
    self.allSalesMap = ko.observable({});

    self.allCompanyNames = [];
    self.allCompanyBriefNames = [];

    self.searchCompanies = function() {
        startLoadingSimpleIndicator("加载中");

        // for now, company search UI doesn't support pagination, fetch all matched records
        self.query().count = 10000;

        $.getJSON(self.apiurl + '/companies/search', $.param(self.query(), true).replace(/&?[^=&]+=(&|$)/g,''), function (results) {
            var companies = results.items;

            $.getJSON(self.apiurl + '/employees/roles/SALES', {}, function (employees) {
                $(employees).each( function(idx, employee) {
                    self.allSalesMap()[employee.id] = employee;
                });

                $(companies).each( function(idx, company) {
                    company.lastContactContent = ko.observable();
                    company.lastContactCreatedTime = ko.observable();
                    company.nextContactTime = ko.observable();
                    company.salesName = ko.observable();

                    if (company.sales) {
                        var salesName = '';
                        $(company.sales).each(function (idx, salesId) {
                            if (self.allSalesMap()[salesId] && self.allSalesMap()[salesId].employeeName) {
                                if (salesName != '') {
                                    salesName = salesName + ',';
                                }
                                salesName = salesName + self.allSalesMap()[salesId].employeeName;
                            }
                        });
                        company.salesName(salesName);
                    }

                    if (company.lastContactRecordId) {
                        $.getJSON(self.apiurl + '/company-contact-records/' + company.lastContactRecordId, {}, function (record) {
                            company.lastContactContent(record.content);
                            company.lastContactCreatedTime(record.createdTime);
                            company.nextContactTime(record.nextContactTime);
                            self.companies(self.companies());
                        });
                    }
                });
                self.companies(companies);
                endLoadingIndicator();
            });
        });
    };

    self.initCompanyNameAutoComplete = function() {
        $.getJSON(self.apiurl + '/auto-suggest/companies', {}, function (companyNames) {
            self.allCompanyNames = companyNames;

            $('#autocomplete-company-name').autocomplete({
                lookup: self.allCompanyNames,

                onSelect: function(suggestion) {
                    self.query().name = suggestion.value;
                }
            });
        });
    };

    self.initCompanyBriefNameAutoComplete = function() {
        $.getJSON(self.apiurl + '/auto-suggest/companies/brief', {}, function (companyBriefNames) {
            self.allCompanyBriefNames = companyBriefNames;

            $('#autocomplete-company-brief-name').autocomplete({
                lookup: self.allCompanyBriefNames,

                onSelect: function(suggestion) {
                    self.query().briefName = suggestion.value;
                }
            });
        });
    };
};

var ctx = new CompaniesContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
    ctx.searchCompanies();

    ctx.initCompanyNameAutoComplete();
    ctx.initCompanyBriefNameAutoComplete();
});

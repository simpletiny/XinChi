var ContractsContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.query = ko.observable({saleProgress: self.allProgress});
    self.contracts = ko.observableArray([]);
    self.companyId = $("#hidden_company_id").val();

    self.searchContracts = function() {
        startLoadingSimpleIndicator("加载中");
        $.getJSON(self.apiurl + '/companies/' + self.companyId + '/contracts', $.param(self.query(), true).replace(/&?[^=&]+=(&|$)/g,''), function (contracts) {
            endLoadingIndicator();
            self.contracts(contracts);
        });
    };
};

var ctx = new ContractsContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
    ctx.searchContracts();
});

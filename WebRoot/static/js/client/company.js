var CompanyContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.createCompany = function(){
    	window.location.href=self.apiurl+"templates/client/company-creation.jsp";
    };
};

var ctx = new CompanyContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
});


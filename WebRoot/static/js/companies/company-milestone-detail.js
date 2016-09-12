/**
 * Created by niushixing on 8/21/15.
 */
var MilestoneContext = function () {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.companyId = $("#hidden_company_id").val();
    self.milestone = ko.observable({});
    //self.milestone().offerRate = ko.observable("29.3%");

    startLoadingSimpleIndicator("加载中");
    $.getJSON(self.apiurl + '/companies/' + self.companyId+'/milestone', {}, function (milestone) {
        if(milestone){
            self.milestone(milestone);
        }
        endLoadingIndicator();
    });
};

var ctx = new MilestoneContext();

$(document).ready(function () {
    ko.applyBindings(ctx);
    console.log(ctx.companyId);
});
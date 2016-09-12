/**
 * Created by niushixing on 8/21/15.
 */
var MilestoneContext = function () {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.companyId = $("#hidden_company_id").val();
    self.milestone = ko.observable({});

    startLoadingSimpleIndicator("加载中");
    $.getJSON(self.apiurl + '/companies/' + self.companyId+'/milestone', {}, function (milestone) {
        if(milestone){
            self.milestone(milestone);
        }
        endLoadingIndicator();
    });
    self.saveMilestone = function () {
        self.milestone().companyId = self.companyId;
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/companies/milestone-create',
            data: ko.toJSON(self.milestone)
        }).done(function (milestone) {
            window.location.replace("/companies/" + milestone.companyId + '/milestone/detail');
        });
    };
};

var ctx = new MilestoneContext();

$(document).ready(function () {
    ko.applyBindings(ctx);
});
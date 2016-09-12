var ProjectContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.projectId = $("#hidden_projectId").val();
    self.candidateSummary = ko.observableArray([]);
    self.reservationSummary = ko.observableArray([]);
    self.overallSummary = ko.observableArray([]);
    self.batchDate = ko.observable();

    self.searchSummary = function() {
        $.getJSON(self.apiurl + '/projects/' + self.projectId + '/overall-summary-report', $.param({'batchDate': self.batchDate}), function (summary) {
            self.overallSummary(summary);
        });
        $.getJSON(self.apiurl + '/projects/' + self.projectId + '/candidate-summary-report', $.param({'batchDate': self.batchDate}), function (summary) {
            self.candidateSummary(summary);
        });
        $.getJSON(self.apiurl + '/projects/' + self.projectId + '/reservation-summary-report', $.param({'batchDate': self.batchDate}), function (summary) {
            self.reservationSummary(summary);
        });
    };

    self.exportResult = function() {
        window.location.href=self.apiurl + '/projects/' + self.projectId + '/report?batchDate=' + self.batchDate();
    };
};

var ctx = new ProjectContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
});

$(':input[type=number]').on('mousewheel',function(e){ $(this).blur();});

$.ajaxSettings.traditional = true;

var ProjectExecContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.projectId = $("#hidden_projectId").val();
    self.participantId = $("#hidden_participantId").val();
    self.interview = ko.observable({'offered': false});
    self.participant = ko.observable({});
    self.resume = ko.observable({});

    self.interviewStatuses = ['未参加面试', '初试通过', '初试未通过', '初试待定', '复试通过', '复试未通过', '复试待定', '终试通过', '终试未通过', '终试待定'];

    $.getJSON(self.apiurl + '/participants/' + self.participantId, {}, function (participant) {
        self.participant(participant);
        if (participant.currentOfferStatus) {
            self.interview().offered = true;
        }
        if (self.participant().resumeId) {
            $.getJSON(self.apiurl + '/resumes/' + self.participant().resumeId, {}, function (resume) {
                self.resume(resume);
            });
        }
    });

    $.getJSON(self.apiurl + '/participants/' + self.participantId + '/latest-interview-result', {}, function (latestInterviewResult) {
        self.interview(latestInterviewResult);
    }).always(function(){
        $('#star-stability').rater({
            max: 5,
            value: self.interview().stability,
            after_click	: function(ret) {
                $('#stability-input').val(ret.number).change();
            }
        });

        $('#star-communicationSkill').rater({
            max: 5,
            value: self.interview().communicationSkill,
            after_click	: function(ret) {
                $('#communicationSkill-input').val(ret.number).change();
            }
        });

        $('#star-learningCapacity').rater({
            max: 5,
            value: self.interview().learningCapacity,
            after_click	: function(ret) {
                $('#learningCapacity-input').val(ret.number).change();
            }
        });

        $('#star-professionalAbility').rater({
            max: 5,
            value: self.interview().professionalAbility,
            after_click	: function(ret) {
                $('#professionalAbility-input').val(ret.number).change();
            }
        });
    });

    self.saveInterviewResult = function() {
        console.log("interview-feedback");

        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/participants/' + self.participantId + '/interview-result',
            data: ko.toJSON(self.interview)
        }).done(function(res) {
            window.location.replace("/client-portal/projects/" + self.projectId + '/phases/interview');
        });
    };
};

var ctx = new ProjectExecContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
});



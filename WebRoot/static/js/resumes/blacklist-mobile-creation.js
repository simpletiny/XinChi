var BlacklistMobilesContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();

    self.blacklistMobile = ko.observable({});

    self.createBlacklistMobile = function() {
        self.blacklistMobile().startDate = date2iso(self.blacklistMobile().startDate);
        self.blacklistMobile().endDate = date2iso(self.blacklistMobile().endDate);

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/blacklist-mobiles',
            data: ko.toJSON(self.blacklistMobile)
        }).done(function(blacklistMobile) {
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
        });
    };
};

var ctx = new BlacklistMobilesContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
});

$(':input[type=number]').on('mousewheel',function(e){ $(this).blur();});

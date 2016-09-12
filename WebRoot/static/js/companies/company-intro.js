var CompanyIntroContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.companyId = $("#hidden_company_id").val();
    self.company = ko.observable({});
    self.companyIntro = ko.observable({});

    $.getJSON(self.apiurl + '/companies/' + self.companyId, {}, function (company) {
        self.company(company);
        self.companyIntro().companyId = company.id;
        $.getJSON(self.apiurl + '/companies/' + self.companyId + '/intro', {}, function (companyIntro) {
            self.companyIntro(companyIntro);
        });
    });

    self.createCompanyIntro = function() {
        if (!$("form").valid()) {
            return;
        }

        self.companyIntro().companyName = self.company().name;
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: self.apiurl + '/companies/' + self.companyId + '/intro',
            data: ko.toJSON(self.companyIntro)
        }).done(function(companyIntro){
            console.log('created company intro ' + companyIntro.id);
            window.location.replace("/companies/" + self.companyId + '/detail');
        });
    };
};

var ctx = new CompanyIntroContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
    $("textarea.ckeditor").ckeditor();
});

function changeRichText(path, value) {
    changeValueByPath(ctx.companyIntro(), path, value);
};

$(':input[type=number]').on('mousewheel',function(e){ $(this).blur();});

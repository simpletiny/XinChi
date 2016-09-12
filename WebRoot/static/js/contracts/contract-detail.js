var ContractContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.companyId = $("#hidden_company_id").val();
    self.contractId = $("#hidden_contract_id").val();
    self.contract = ko.observable({});

    self.allContractStatus = ['合同申请', '合同签订', '合同执行', '合同暂停', '合同结束'];
    self.allApproveStatus = ['未审批', '审批通过', '审批未通过'];

    $.getJSON(self.apiurl + '/contracts/' + self.contractId, {}, function (contract) {
        self.contract(contract);
    });

    self.updateContract = function() {
        if (!$("form").valid()) {
            return;
        }

        self.contract().companyId = self.companyId;

        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: self.apiurl + '/contracts/' +  + self.contractId,
            data: ko.toJSON(self.contract)
        }).done(function(contract){
            console.log('updated contract ' + contract.id);
            window.location.replace("/companies/" + self.companyId + "/contracts/" + contract.id + '/detail');
        });
    };

    self.initSalesNamesAutoComplete = function() {
        $.getJSON(self.apiurl + '/auto-suggest/employees/SALES', {}, function (salesNames) {
            self.allSalesNames = salesNames;

            $('#autocomplete-sales-name').autocomplete({
                lookup: self.allSalesNames,
                onSelect: function(suggestion) {
                    self.contract().customerManager = suggestion.value;
                }
            });
        });
    };
};

var ctx = new ContractContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
    $("textarea.ckeditor").ckeditor();

    ctx.initSalesNamesAutoComplete();
});


$(':input[type=number]').on('mousewheel',function(e){ $(this).blur();});


function changeRichText(path, value) {
    changeValueByPath(ctx.contract(), path, value);
};
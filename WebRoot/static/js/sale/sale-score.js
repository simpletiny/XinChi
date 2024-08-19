var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.scores = ko.observableArray([]);
	self.confirm_month = ko.observable();
	var x = new Date();
	self.confirm_month(x.Format("yyyy-MM"));
	self.refresh = function() {

		var param = $("form").serialize();
		if ($("#txt-confirm-month").val().trim() == "") {
			fail_msg("请选择月份！");
			return;
		}
		$.getJSON(self.apiurl + 'order/searchSaleScoreByParam', param, function(data) {
			self.scores(data.scores);
			$(".table").tableSum({
				except : [1, 15]
			});

			$(".rmb").formatCurrency();
		});
	};
};

var ctx = new OrderContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});

var switchType = function(rad) {
	var v = $(rad).val();
	if (v == "month") {
		$(".date-picker").attr("disabled", true);
		$(".month-picker-st").attr("disabled", false);
	} else {
		$(".date-picker").attr("disabled", false);
		$(".month-picker-st").attr("disabled", true);
	}
}
var st_page = "s";
var st_page_map = {
	"s" : "sale-score.jsp",
	"b" : "back-money-score.jsp"
}
function changePage(rad) {
	var v = $(rad).val();
	if (v == st_page)
		return;
	else
		window.location.href = ctx.apiurl + 'templates/sale/' + st_page_map[v];
}
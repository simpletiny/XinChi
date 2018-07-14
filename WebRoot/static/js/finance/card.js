var purposeLayer;
var CardContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenCards = ko.observableArray([]);
	self.createCard = function() {
		window.location.href = self.apiurl
				+ "templates/finance/card-creation.jsp";
	};

	self.cardPurposes = [ {
		data_key : 'NONE',
		data_value : '无'
	}, {
		data_key : 'TICKET',
		data_value : '票务'
	}, {
		data_key : 'OTHER',
		data_value : '其他'
	} ];

	self.cardPurposeMapping = {
		'NONE' : '无',
		'TICKET' : '票务',
		'OTHER' : '其他'
	};

	self.cards = ko.observable({
		total : 0,
		items : []
	});

	self.sumBalance = ko.observable();

	self.refresh = function() {
		$.getJSON(self.apiurl + 'finance/searchCard', {}, function(data) {
			self.cards(data.cards);
			self.sumBalance(data.sum_balance);
			$(".rmb").formatCurrency();
		});
	};
	self.search = function() {

	};

	self.resetPage = function() {

	};

	// 指定银行卡用途
	self.signPurpose = function() {
		if (self.chosenCards().length == 0) {
			fail_msg("请选择账户");
			return;
		} else if (self.chosenCards().length > 0) {
			purposeLayer = $.layer({
				type : 1,
				title : [ '指定用途', '' ],
				maxmin : false,
				closeBtn : [ 1, true ],
				shadeClose : false,
				area : [ '900px', '200px' ],
				offset : [ '', '' ],
				scrollbar : true,
				page : {
					dom : '#card-purpose'
				},
				end : function() {
					console.log("Done");
				}
			});
		}
	};
	// 执行指定用途
	self.doSign = function() {
		var purpose = $("#purpose").val();

		var card_pks = "";
		for (var i = 0; i < self.chosenCards().length; i++) {
			var data = self.chosenCards()[i];
			card_pks += data.pk + ",";
		}
		card_pks = card_pks.RTrim(",");
		startLoadingSimpleIndicator("保存中...");
		var data = "card_pks=" + card_pks + "&purpose=" + purpose;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'finance/signCardPurpose',
			data : data
		}).success(function(str) {
			layer.close(purposeLayer);
			endLoadingIndicator();
			if (str == "success") {
				self.refresh();
			} else {
				fail_msg("发生错误，请联系管理员！");
			}
		});

	};
	// 停用银行卡
	self.stopUse = function() {
		if (self.chosenCards().length == 0) {
			fail_msg("请选择账户");
			return;
		} else if (self.chosenCards().length > 0) {

			$.layer({
				area : [ 'auto', 'auto' ],
				dialog : {
					msg : '确认停用账户吗?',
					btns : 2,
					type : 4,
					btn : [ '确认', '取消' ],
					yes : function(index) {
						var card_pks = "";
						for (var i = 0; i < self.chosenCards().length; i++) {
							var data = self.chosenCards()[i];
							card_pks += data.pk + ",";
						}
						card_pks = card_pks.RTrim(",");
						startLoadingSimpleIndicator("保存中...");
						var data = "card_pks=" + card_pks;
						$.ajax({
							type : "POST",
							url : self.apiurl + 'finance/stopUseCard',
							data : data
						}).success(function(str) {
							endLoadingIndicator();
							layer.close(index);
							if (str == "success") {
								self.refresh();
							} else {
								fail_msg("发生错误，请联系管理员！");
							}
						});
					}
				}
			});

		}
	};
	self.editCard = function() {
		if (self.chosenCards().length == 0) {
			fail_msg("请选择账户");
			return;
		} else if (self.chosenCards().length > 1) {
			fail_msg("编辑只能选中一个");
			return;
		} else if (self.chosenCards().length == 1) {
			window.location.href = self.apiurl
					+ "templates/client/Card-edit.jsp?key="
					+ self.chosenCards()[0];
		}
	};

};

var ctx = new CardContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});

var legLayer;
var legEditLayer;
var AirLegContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.bases = ko.observable({
		total : 0,
		items : []
	});
	self.refresh = function() {
		startLoadingSimpleIndicator("加载中");

		var param = $("#form-search").serialize();
		param += "&page.start=" + self.startIndex() + "&page.count="
				+ self.perPage;
		$.getJSON(self.apiurl + 'ticket/searchSeasonTicketByPage', param,
				function(data) {
					self.bases(data.bases);

					self.totalCount(Math.ceil(data.page.total / self.perPage));
					self.setPageNums(self.currentPage());
					endLoadingIndicator();
				});
	};
	self.createSeasonTicket = function() {
		window.location.href = self.apiurl
				+ "templates/ticket/season-ticket-creation.jsp"
	}
	self.chosenLegs = ko.observableArray([]);

	self.deleteLeg = function() {
		if (self.chosenLegs().length < 1) {
			fail_msg("请选择！");
		} else {
			$.layer({
				area : ['auto', 'auto'],
				dialog : {
					msg : "是否要删除航段！",
					btns : 2,
					type : 4,
					btn : ['确认', '取消'],
					yes : function(index) {
						layer.close(index);
						startLoadingSimpleIndicator("删除中");
						var data = "";
						for (var i = 0; i < self.chosenLegs().length; i++) {
							data += "leg_pks=" + self.chosenLegs()[i] + "&";
						}
						$.ajax({
							type : "POST",
							url : self.apiurl + 'ticket/deleteAirLeg',
							data : data
						}).success(function(str) {
							endLoadingIndicator();
							if (str == "success") {
								self.refresh();
							} else {
								fail_msg("保存失败，联系管理员！");
							}
						});
					}
				}
			});

		}
	}
	self.switchHot = function(data, event) {
		var leg_pk = data.pk;
		var v = $(event.target).val();
		var data = "leg.pk=" + leg_pk + "&leg.hot_flg=" + v;
		$.ajax({
			type : "POST",
			url : self.apiurl + 'ticket/switchHot',
			data : data
		}).success(function(str) {
		});
	}
	self.editLeg = function() {
		if (self.chosenLegs().length < 1) {
			fail_msg("请选择！");
		} else if (self.chosenLegs().length > 1) {
			fail_msg("编辑只能选择一个！");
		} else {
			startLoadingSimpleIndicator("加载中");
			$.getJSON(self.apiurl + 'ticket/searchAirLegByPk', {
				leg_pk : self.chosenLegs()[0]
			}, function(data) {
				self.leg(data.leg);

				endLoadingIndicator();
				legEditLayer = $.layer({
					type : 1,
					title : ['新建航段', ''],
					maxmin : false,
					closeBtn : [1, true],
					shadeClose : false,
					area : ['800px', '260px'],
					offset : ['', ''],
					scrollbar : true,
					page : {
						dom : '#air-leg-edit'
					},
					end : function() {
					}
				});
			});

		}
	}

	self.doUpdate = function() {
		if (!$("#leg-edit-form").valid()) {
			return;
		}
		startLoadingSimpleIndicator("保存中");
		var data = $("#leg-edit-form").serialize();
		$.ajax({
			type : "POST",
			url : self.apiurl + 'ticket/updateAirLeg',
			data : data
		}).success(function(str) {
			endLoadingIndicator();
			if (str == "success") {
				layer.close(legEditLayer);
				self.refresh();
				$("#leg-edit-form")[0].reset();
			} else if (str == "exists") {
				fail_msg("存在相同航段！");
			} else {
				fail_msg("更新失败，联系管理员！");
			}
		});
	}

	self.cancelEdit = function() {
		layer.close(legEditLayer);
	}

	self.search = function() {

	};

	self.resetPage = function() {

	};

	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 50;
	self.pageNums = ko.observableArray();
	self.totalCount = ko.observable(1);
	self.startIndex = ko.computed(function() {
		return (self.currentPage() - 1) * self.perPage;
	});

	self.resetPage = function() {
		self.currentPage(1);
	};

	self.previousPage = function() {
		if (self.currentPage() > 1) {
			self.currentPage(self.currentPage() - 1);
			self.refreshPage();
		}
	};

	self.nextPage = function() {
		if (self.currentPage() < self.pageNums().length) {
			self.currentPage(self.currentPage() + 1);
			self.refreshPage();
		}
	};

	self.turnPage = function(pageIndex) {
		self.currentPage(pageIndex);
		self.refreshPage();
	};

	self.setPageNums = function(curPage) {
		var startPage = curPage - 4 > 0 ? curPage - 4 : 1;
		var endPage = curPage + 4 <= self.totalCount() ? curPage + 4 : self
				.totalCount();
		var pageNums = [];
		for (var i = startPage; i <= endPage; i++) {
			pageNums.push(i);
		}
		self.pageNums(pageNums);
	};

	self.refreshPage = function() {
		self.refresh();
	};
	// end pagination
};

var ctx = new AirLegContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});

var agencyLayer;
var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.companyPk = $("#client_key").val();
	self.client = ko.observable({});
	self.genders = [ '男', '女' ];
	self.clientArea = [ '哈尔滨', '齐齐哈尔', '牡丹江', '佳木斯', '大庆', '鸡西', '绥化', '呼伦贝尔',
			'伊春', '鹤岗', '双鸭山', '七台河', '黑河', '大兴安岭' ];
	self.clientType = [ '总公司', '分公司', '营业部','包桌', '经纪人', '其他' ];
	self.storeTypes = [ '未知', '门店', '写字间', '其它 ' ];
	self.mainBusinesses = [ '未知', '组团', '地接', '同业', '综合' ];
	self.backLevels = [ '未知', '立即', '及时', '拖拉', '费劲', '定期', '垃圾', '布莱' ];
	startLoadingSimpleIndicator("加载中");
	$.getJSON(self.apiurl + 'client/searchOneCompany', {
		client_pk : self.companyPk
	}, function(data) {
		if (data.client) {
			self.client(data.client);
		} else {
			fail_msg("公司不存在！");
		}
		endLoadingIndicator();
	}).fail(function(reason) {
		fail_msg(reason.responseText);
	});

	self.saveCompany = function() {
		if (!$("form").valid()) {
			return;
		}
		$.ajax({
			type : "POST",
			url : self.apiurl + 'client/updateCompany',
			data : $("form").serialize()
		}).success(
				function(str) {
					if (str == "success") {
						window.location.href = self.apiurl
								+ "templates/client/company.jsp";
					}else if(str=="exist"){
						fail_msg("存在同名财务主体！");
					}
				});
	};
	
	// 关联旅游公司相关
	self.agencies = ko.observable({
		total : 0,
		items : []
	});

	self.chooseAgency = function() {
		agencyLayer = $.layer({
			type : 1,
			title : [ '选择旅游公司', '' ],
			maxmin : false,
			closeBtn : [ 1, true ],
			shadeClose : false,
			area : [ '600px', '650px' ],
			offset : [ '50px', '' ],
			scrollbar : true,
			page : {
				dom : '#agency_pick'
			},
			end : function() {
				console.log("Done");
			}
		});
	};

	self.refresh = function() {
		var param = "agency.agency_name=" + $("#agency_full_name").val();
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;

		$.getJSON(self.apiurl + 'client/searchAgencyByPage', param, function(data) {
			self.agencies(data.agencys);
			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};
	self.searchAgency = function() {
		self.refresh();

	};
	self.pickAgency = function(name, pk) {
		$("#agency_name").val(name);
		$("#agency_pk").val(pk);
		layer.close(agencyLayer);
	};

	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 10;
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
		var endPage = curPage + 4 <= self.totalCount() ? curPage + 4 : self.totalCount();
		var pageNums = [];
		for ( var i = startPage; i <= endPage; i++) {
			pageNums.push(i);
		}
		self.pageNums(pageNums);
	};

	self.refreshPage = function() {
		self.searchAgency();

	};
	// end pagination
};

var ctx = new CompanyContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
});

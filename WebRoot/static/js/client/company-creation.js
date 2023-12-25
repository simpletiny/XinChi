var agencyLayer;
var CompanyContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.client = ko.observable({});
	self.genders = ['未知', '男', '女'];
	// self.clientArea = [ '哈尔滨', '齐齐哈尔', '牡丹江', '佳木斯', '大庆', '鸡西', '绥化',
	// '呼伦贝尔',
	// '伊春', '鹤岗', '双鸭山', '七台河', '黑河', '大兴安岭' ];
	// self.countyMapping = {
	// '哈尔滨' : [ '道里区', '南岗区', '道外区', '平房区', '松北区', '香坊区', '呼兰区', '阿城区',
	// '双城区', '哈西区', '开发区', '群力区', '尚志市', '五常市', '依兰县', '方正县', '宾县',
	// '巴彦县', '木兰县', '通河县', '延寿县' ],
	// '齐齐哈尔' : [ '齐齐哈尔' ],
	// '牡丹江' : [ '牡丹江' ],
	// '佳木斯' : [ '佳木斯' ],
	// '大庆' : [ '新村', '让胡路区', '萨尔图区', '龙凤区', '红岗区', '大同区', '肇州县', '肇源县',
	// '林甸县', '杜尔伯特县' ],
	// '鸡西' : [ '鸡西' ],
	// '绥化' : [ '绥化' ],
	// '呼伦贝尔' : [ '呼伦贝尔' ],
	// '伊春' : [ '伊春' ],
	// '鹤岗' : [ '鹤岗' ],
	// '双鸭山' : [ '双鸭山' ],
	// '黑河' : [ '黑河' ],
	// '大兴安岭' : [ '大兴安岭' ]
	// };
	// self.client().client_area = ko.observable();
	// self.client().client_county = ko.observable();
	//
	// self.client().client_area("哈尔滨");
	// self.ter = function() {
	// $("#county").empty();
	// for (var i = 0; i <
	// self.countyMapping[self.client().client_area()].length; i++) {
	// var value = self.countyMapping[self.client().client_area()][i];
	// $("#county").append(
	// "<option value='" + value + "'>" + value + "</option>");
	// }
	// };
	// self.ter();

	self.clientType = ['总公司', '分公司', '营业部', '包桌', '经纪人', '其他'];
	self.storeTypes = ['未知', '门店', '写字间', '其它 '];
	self.mainBusinesses = ['组团', '户外', '线上', '综合', '地接', '同业', '其它'];
	self.backLevels = ['未知', '立即', '及时', '拖拉', '费劲', '定期', '垃圾', '布莱'];
	self.marketLevels = ['未知', '主导级', '引领级', '普通级', '跟随级', '玩闹级'];
	self.talkLevels = ['核心', '主力', '市场', '排斥'];

	self.client().client_type = ko.observable();
	self.client().main_business = ko.observable();
	self.client().back_level = ko.observable();
	self.client().market_level = ko.observable();
	self.client().talk_level = ko.observable();
	self.client().store_type = ko.observable();

	self.client().main_business("组团");
	self.client().back_level("未知");
	self.client().market_level("普通级");
	self.client().talk_level("市场");
	self.client().store_type("未知");
	self.client().client_type("总公司");

	self.createCompany = function() {
		if (!$("form").valid()) {
			return;
		}
		$.ajax({
			type: "POST",
			url: self.apiurl + 'client/createCompany',
			data: $("form").serialize()
		}).success(function(str) {
			if (str == "success") {
				window.location.href = self.apiurl + "templates/client/company.jsp";
			} else if (str = "exist") {
				fail_msg("存在同名财务主体！");
			}
		});
	};

	// 关联旅游公司相关
	self.agencies = ko.observable({
		total: 0,
		items: []
	});

	self.chooseAgency = function() {
		agencyLayer = $.layer({
			type: 1,
			title: ['选择旅游公司', ''],
			maxmin: false,
			closeBtn: [1, true],
			shadeClose: false,
			area: ['600px', '650px'],
			offset: ['50px', ''],
			scrollbar: true,
			page: {
				dom: '#agency_pick'
			},
			end: function() {
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
		for (var i = startPage; i <= endPage; i++) {
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

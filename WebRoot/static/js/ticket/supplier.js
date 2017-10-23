var SupplierContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();
	self.chosenCompanies = ko.observableArray([]);
	self.isMapping = {
		'Y' : '合作',
		'N' : '终止合作',
	};
	self.suppliers = ko.observable({
		total : 0,
		items : []
	});
	self.supplier = ko.observable({});
	self.provices = [ '北京市', '天津市', '上海市', '重庆市', '河北省', '山西省', '辽宁省', '吉林省', '黑龙江省', '江苏省', '浙江省', '安徽省', '福建省', '江西省', '山东省', '河南省', '湖北省', '湖南省', '广东省', '海南省', '四川省', '贵州省', '云南省', '陕西省', '甘肃省',
			'青海省', '台湾省', '内蒙古自治区', '广西壮族自治区', '西藏自治区', '宁夏回族自治区', '新疆维吾尔自治区', '香港特别行政区', '澳门特别行政区' ];
	self.cityMapping = {
		'北京市' : [ '北京市' ],
		'天津市' : [ '天津市' ],
		'上海市' : [ '上海市' ],
		'重庆市' : [ '重庆市' ],
		'河北省' : [ '石家庄市', '唐山市', '秦皇岛市', '邯郸市', '邢台市', '保定市', '张家口市', '承德市', '沧州市', '廊坊市', '衡水市' ],
		'山西省' : [ '太原市', '大同市', '阳泉市', '长治市', '晋城市', '朔州市', '忻州市', '吕梁市', '晋中市', '临汾市', '运城市' ],
		'内蒙古自治区' : [ '呼和浩特市', '包头市', '乌海市', '赤峰市', '呼伦贝尔市', '通辽市', '乌兰察布市', '鄂尔多斯市', '巴彦淖尔市' ],
		'辽宁省' : [ '沈阳市', '大连市', '鞍山市', '抚顺市', '本溪市', '丹东市', '锦州市', '营口市', '阜新市', '辽阳市', '盘锦市', '铁岭市', '朝阳市', '葫芦岛市' ],
		'吉林省' : [ '长春市', '吉林市', '四平市', '辽源市', '通化市', '白山市', '白城市', '松原市' ],
		'黑龙江省' : [ '哈尔滨市', '齐齐哈尔市', '牡丹江市', '佳木斯市', '大庆市', '伊春市', '鸡西市', '鹤岗市', '双鸭山市', '七台河市', '绥化市', '黑河市' ],
		'江苏省' : [ '南京市', '无锡市', '徐州市', '常州市', '苏州市', '南通市', '连云港市', '淮安市', '盐城市', '扬州市', '镇江市', '泰州市', '宿迁市' ],
		'浙江省' : [ '杭州市', '宁波市', '温州市', '绍兴市', '湖州市', '嘉兴市', '金华市', '衢州市', '台州市', '丽水市', '舟山市' ],
		'安徽省' : [ '合肥市', '芜湖市', '蚌埠市', '淮南市', '马鞍山市', '淮北市', '铜陵市', '安庆市', '黄山市', '阜阳市', '宿州市', '滁州市', '六安市', '宣城市', '池州市', '亳州市' ],
		'福建省' : [ '福州市', '莆田市', '泉州市', '厦门市', '漳州市', '龙岩市', '三明市', '南平市', '宁德市' ],
		'江西省' : [ '南昌市', '赣州市', '宜春市', '吉安市', '上饶市', '抚州市', '九江市', '景德镇市', '萍乡市', '新余市', '鹰潭市' ],
		'山东省' : [ '济南市', '青岛市', '淄博市', '枣庄市', '东营市', '烟台市', '潍坊市', '济宁市', '泰安市', '威海市', '日照市', '滨州市', '德州市', '聊城市', '临沂市', '菏泽市', '莱芜市' ],
		'河南省' : [ '郑州市', '开封市', '洛阳市', '平顶山市', '安阳市', '鹤壁市', '新乡市', '焦作市', '濮阳市', '许昌市', '漯河市', '三门峡市', '商丘市', '周口市', '驻马店市', '南阳市', '信阳市' ],
		'湖北省' : [ '武汉市', '黄石市', '十堰市', '荆州市', '宜昌市', '襄阳市', '鄂州市', '荆门市', '黄冈市', '孝感市', '咸宁市', '随州市' ],
		'湖南省' : [ '长沙市', '株洲市', '湘潭市', '衡阳市', '邵阳市', '岳阳市', '张家界市', '益阳市', '常德市', '娄底市', '郴州市', '永州市', '怀化市' ],
		'广东省' : [ '广州市', '深圳市', '珠海市', '汕头市', '佛山市', '韶关市', '湛江市', '肇庆市', '江门市', '茂名市', '惠州市', '梅州市', '汕尾市', '河源市', '阳江市', '清远市', '东莞市', '中山市', '潮州市', '揭阳市', '云浮市' ],
		'广西壮族自治区' : [ '南宁市', '柳州市', '桂林市', '梧州市', '北海市', '崇左市', '来宾市', '贺州市', '玉林市', '百色市', '河池市', '钦州市', '防城港市', '贵港市' ],
		'海南省' : [ '海口市', '三亚市', '儋州市', '三沙市' ],
		'四川省' : [ '成都市', '绵阳市', '自贡市', '攀枝花市', '泸州市', '德阳市', '广元市', '遂宁市', '内江市', '乐山市', '资阳市', '宜宾市', '南充市', '达州市', '雅安市', '广安市', '巴中市', '眉山市' ],
		'贵州省' : [ '贵阳市', '六盘水市', '遵义市', '铜仁市', '毕节市', '安顺市' ],
		'云南省' : [ '昆明市', '昭通市', '曲靖市', '玉溪市', '普洱市', '保山市', '丽江市', '临沧市' ],
		'西藏自治区' : [ '拉萨市', '昌都市', '山南市', '日喀则市', '林芝市' ],
		'陕西省' : [ '西安市', '铜川市', '宝鸡市', '咸阳市', '渭南市', '汉中市', '安康市', '商洛市', '延安市', '榆林市' ],
		'甘肃省' : [ '兰州市', '嘉峪关市', '金昌市', '白银市', '天水市', '酒泉市', '张掖市', '武威市', '定西市', '陇南市', '平凉市', '庆阳市' ],
		'青海省' : [ '西宁市', '海东市' ],
		'宁夏回族自治区' : [ '银川市', '石嘴山市', '吴忠市', '固原市', '中卫市' ],
		'新疆维吾尔自治区' : [ '乌鲁木齐市', '克拉玛依市', '吐鲁番市', '哈密市' ]
	};

	self.supplier().supplier_provice = ko.observable();
	self.supplier().supplier_city = ko.observable();

	self.ter = function() {
		$("#city").empty();
		$("#city").append("<option value>-- 市--</option>");
		for ( var i = 0; i < self.cityMapping[self.supplier().supplier_provice()].length; i++) {
			var value = self.cityMapping[self.supplier().supplier_provice()][i];
			$("#city").append("<option value='" + value + "'>" + value + "</option>");
		}
	};

	self.createSupplier = function() {
		window.location.href = self.apiurl + "templates/ticket/supplier-creation.jsp";
	};

	self.refresh = function() {
		var param = $("form").serialize() + "&supplier.type=A";
		param += "&page.start=" + self.startIndex() + "&page.count=" + self.perPage;
		$.getJSON(self.apiurl + 'supplier/searchSupplierByPage', param, function(data) {
			self.suppliers(data.suppliers);

			self.totalCount(Math.ceil(data.page.total / self.perPage));
			self.setPageNums(self.currentPage());
		});
	};
	self.search = function() {

	};

	self.resetPage = function() {

	};

	self.editSupplier = function() {
		if (self.chosenCompanies().length == 0) {
			fail_msg("请选择公司");
			return;
		} else if (self.chosenCompanies().length > 1) {
			fail_msg("编辑只能选中一个");
			return;
		} else if (self.chosenCompanies().length == 1) {
			window.location.href = self.apiurl + "templates/ticket/supplier-edit.jsp?key=" + self.chosenCompanies()[0];
		}
	};
	// start pagination
	self.currentPage = ko.observable(1);
	self.perPage = 20;
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
		self.refresh();
	};
	// end pagination
};

var ctx = new SupplierContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});

var CardContext = function() {
	// 基于准备好的dom，初始化echarts实例
	var chart_area = echarts.init(document.getElementById('chart-area'));
	var chart_product = echarts.init(document.getElementById('chart-product'));
	var chart_sale = echarts.init(document.getElementById('chart-sale'));
	var title_area = {
		text : "地区分布",
		sub : "按收客数统计"
	};
	var title_product = {
		text : "产品分布",
		sub : "按收客数统计"
	};
	var title_sale = {
		text : "销售分布",
		sub : "按收客数统计"
	};

	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	// 获取产品经理信息
	self.users = ko.observableArray([]);
	$.getJSON(self.apiurl + 'user/searchByRole', {
		role : 'PRODUCT'
	}, function(data) {
		self.users(data.users);
	});

	self.chartTypes = [{
		name : '收客数',
		key : 'people_cnt'
	}, {
		name : '订单数',
		key : 'order_cnt'
	}, {
		name : '总分值',
		key : 'value_sum'
	}];

	var x = new Date();
	self.current_month = x.Format("yyyy-MM");
	self.areaData = ko.observableArray([]);
	self.productData = ko.observableArray([]);
	self.saleData = ko.observableArray([]);
	self.refresh = function() {
		var param = $("form").serialize();
		startLoadingSimpleIndicator("加载中...");
		$.getJSON(self.apiurl + 'data/fetchProductStatisticsData', param, function(data) {
			self.areaData(data.areaData);
			self.productData(data.productData);

			var data_area = new Array();
			var data_product = new Array();
			var data_sale = new Array();

			$(self.areaData()).each(function(idx, data) {
				var obj = new Object();
				obj.name = data.area;
				obj.value = data.sum_people;

				data_area.push(obj);
			});
			$(self.productData()).each(function(idx, data) {
				var obj = new Object();
				obj.name = data.product_name;
				obj.value = data.sum_people;

				data_product.push(obj);
			});
			$(data.saleData).each(function(idx, data) {
				var obj = new Object();
				obj.name = data.sale_name;
				obj.value = data.sum_people;
				this.score = (data.sum_people / data.order_cnt).toFixed(2);
				data_sale.push(obj);
			});
			self.saleData(data.saleData);
			self.createChart(title_area, chart_area, data_area);
			self.createChart(title_product, chart_product, data_product);
			self.createChart(title_sale, chart_sale, data_sale);

			endLoadingIndicator();
		});

	}

	self.createChart = function(title, chart, data) {
		option = {
			title : {
				text : title.text,
				subtext : title.sub,
				left : 'center'
			},
			tooltip : {
				trigger : 'item',
				formatter : '{b} : {c} ({d}%)'
			},
			legend : {
				// orient: 'vertical',
				// top: 'middle',
				bottom : 10,
				left : 'center',
				data : ['西凉', '益州', '兖州', '荆州', '幽州']
			},
			series : [{
				type : 'pie',
				radius : '65%',
				center : ['50%', '50%'],
				selectedMode : 'single',
				data : data,
				emphasis : {
					itemStyle : {
						shadowBlur : 10,
						shadowOffsetX : 0,
						shadowColor : 'rgba(0, 0, 0, 0.5)'
					}
				}
			}]
		};

		// 使用刚指定的配置项和数据显示图表。
		chart.setOption(option, true);
	}
	self.changeRangeType = function(data, event) {
		switch ($(event.target).val()) {
			case "people_cnt" :
				var data_area = new Array();
				var data_product = new Array();
				var data_sale = new Array();
				title_area.sub = "按收客数统计";
				title_product.sub = "按收客数统计";
				title_sale.sub = "按收客数统计";
				$(self.areaData()).each(function(idx, data) {
					var obj = new Object();
					obj.name = data.area;
					obj.value = data.sum_people;

					data_area.push(obj);
				});
				$(self.productData()).each(function(idx, data) {
					var obj = new Object();
					obj.name = data.product_name;
					obj.value = data.sum_people;

					data_product.push(obj);
				});
				$(self.saleData()).each(function(idx, data) {
					var obj = new Object();
					obj.name = data.sale_name;
					obj.value = data.sum_people;

					data_sale.push(obj);
				});

				self.createChart(title_area, chart_area, data_area);
				self.createChart(title_product, chart_product, data_product);
				self.createChart(title_sale, chart_sale, data_sale);
				break;
			case "order_cnt" :
				title_area.sub = "按订单数统计";
				title_product.sub = "按订单数统计";
				title_sale.sub = "按订单数统计";
				var data_area = new Array();
				var data_product = new Array();
				var data_sale = new Array();
				$(self.areaData()).each(function(idx, data) {
					var obj = new Object();
					obj.name = data.area;
					obj.value = data.order_cnt;

					data_area.push(obj);
				});
				$(self.productData()).each(function(idx, data) {
					var obj = new Object();
					obj.name = data.product_name;
					obj.value = data.order_cnt;

					data_product.push(obj);
				});
				$(self.saleData()).each(function(idx, data) {
					var obj = new Object();
					obj.name = data.sale_name;
					obj.value = data.order_cnt;

					data_sale.push(obj);
				});

				self.createChart(title_area, chart_area, data_area);
				self.createChart(title_product, chart_product, data_product);
				self.createChart(title_sale, chart_sale, data_sale);
				break;
			case "value_sum" :
				title_area.sub = "按总分值统计";
				title_product.sub = "按总分值统计";
				title_sale.sub = "按均单统计";
				var data_area = new Array();
				var data_product = new Array();
				var data_sale = new Array();
				$(self.areaData()).each(function(idx, data) {
					var obj = new Object();
					obj.name = data.area;
					obj.value = data.score;

					data_area.push(obj);
				});
				$(self.productData()).each(function(idx, data) {
					var obj = new Object();
					obj.name = data.product_name;
					obj.value = data.score;

					data_product.push(obj);
				});
				$(self.saleData()).each(function(idx, data) {
					var obj = new Object();
					obj.name = data.sale_name;
					obj.value = data.score;

					data_sale.push(obj);
				});

				self.createChart(title_area, chart_area, data_area);
				self.createChart(title_product, chart_product, data_product);
				self.createChart(title_sale, chart_sale, data_sale);
				break;

		}
	}
}
var ctx = new CardContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});
var CardContext = function() {
	// 基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById('main'));

	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	// 纵轴选项
	self.vertical = [{
		name : '订单数',
		key : 'order_cnt'
	}, {
		name : '收客数',
		key : 'people_cnt'
	}];

	// 横轴选项
	self.horizontal = [{
		name : '月',
		key : 'month'
	}, {
		name : '天',
		key : 'day'
	}, {
		name : '星期',
		key : 'week'
	}];

	// 数据类型
	self.data_type = [{
		name : '分销售',
		key : 'sale'
	}, {
		name : '全部',
		key : 'all'
	}];

	self.years = ko.observableArray();

	var x = new Date();
	var current_year = x.getFullYear();
	self.current_month = x.Format("yyyy-MM");
	for (var i = current_year; i > 2017; i--) {
		var o = new Object();
		o.name = i + "年";
		o.key = i;
		self.years().push(o);
	}

	self.changeRangeType = function(data, event) {
		switch ($(event.target).val()) {
			case "month", "week" :
				$("#sel-year").show();
				$("#txt-month").hide();
				break;
			case "day" :
				$("#sel-year").hide();
				$("#txt-month").show();
				break;
		}

	}

	self.refresh = function() {
		var param = $("form").serialize();
		var title = "";
		// var serie = new Object();
		// serie.name = '全部';
		// serie.type = 'line';
		// serie.stack = '总量';
		// serie.data = [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1];
		// serie.smooth = true;
		// series.push(serie);
		var type = $("#sel-vertical").val();
		startLoadingSimpleIndicator("加载中...");
		$.getJSON(self.apiurl + 'data/fetchOrderCountData', param, function(
				data) {
			var legend = [];
			var series = [];

			var datas = data.order_datas
			var xAxis = data.xAxis;

			for (var i = 0; i < datas.length; i++) {
				legend.push(datas[i].create_user);
				var serie = new Object();
				serie.name = datas[i].create_user;
				serie.type = 'line';
				serie.smooth = true;
				if (type == "order_cnt") {
					serie.data = datas[i].data_order_cnt;
				} else {
					serie.data = datas[i].data_people_cnt;
				}

				series.push(serie);
			}
			endLoadingIndicator();

			self.createChart(title, legend, xAxis, series);
		});

	}

	self.createChart = function(title, legend, xAxis, series) {
		option = {
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				data : legend
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			toolbox : {
				feature : {
					saveAsImage : {}
				}
			},
			xAxis : {
				type : 'category',
				boundaryGap : false,
				data : xAxis
			},
			yAxis : {
				type : 'value'
			},
			series : series
		};

		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option, true);
	}

}
var ctx = new CardContext();
$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});

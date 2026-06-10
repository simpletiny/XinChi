var OrderContext = function() {
	var self = this;
	self.apiurl = $("#hidden_apiurl").val();

	self.month = ko.observable();
	var x = new Date();
	x = new Date(x.setMonth((new Date().getMonth() - 1)));
	self.month(x.Format("yyyy-MM"));
	self.reimbursement_items = ko.observableArray(['SUM', 'X', 'H', 'B', 'K', 'I', 'J', 'T', 'Q', 'E']);
	self.human_items = ko.observableArray(['SUM', 'G', 'C', 'S']);

	self.summary = ko.observable({});
	self.main = ko.observable({});
	self.ticket = ko.observable(new Map());

	
	self.other = ko.observable({});
	self.inner = ko.observable({});
	self.reimbursement = ko.observable(new Map());
	self.p_reimbursement = ko.observable(new Map());
	self.y_reimbursement = ko.observable(new Map());
	self.i_reimbursement = ko.observable(new Map());
	//人力
	self.human = ko.observable(new Map());
	//已支付
	self.p_human = ko.observable(new Map());
	//未支付
	self.y_human = ko.observable(new Map());
	//未审批
	self.i_human = ko.observable(new Map());
	self.refresh = function() {

		//		div-summary
		//		div-main
		//		div-ticket
		//		div-human
		//		div-reimbursement
		//		div-other
		//		div-inner

		//		searchPLSummary;
		//		searchPLTicket;
		//		searchPLOther;
		//		searchPLInner;

		const date_from = $('input[name="date_from"]').val();
		const date_to = $('input[name="date_to"]').val();
		const statuses = $('input[name="statuses"]:checked').serializeArray().map(item => item.value);
		let common_data = {
			date_from: date_from,
			date_to: date_to
		}
		let main_data = {
			date_from: date_from,
			date_to: date_to,
			statuses: statuses
		}
		//		startLoadingSliceIndicator($('#div-test'));
		//		var param = $("form").serialize();
		//		$.getJSON(self.apiurl + 'accounting/searchPLSummary', param, function(data) {
		//			self.report(data.report);
		//			$(".rmb").formatCurrency();
		//		});


		//主营
		startLoadingSliceIndicator($('#div-main'));
		const p1 = $.ajax({
			type: "POST",
			url: self.apiurl + "accounting/searchPLMain",
			data: main_data,
			traditional: true
		}).success(function(data) {
			let main_report = data.main_report;
			main_report['people_count'] = main_report['adult_count'] + main_report['special_count'];
			main_report['gross_profit'] = main_report['receivable'] - main_report['air_ticket_cost'] - main_report['product_cost'] - main_report['fy'];
			if (main_report['people_count'] != 0) {
				main_report['per_gross_profit'] = main_report['gross_profit'] / main_report['people_count'];
			} else {
				main_report['per_gross_profit'] = 0;
			}

			self.main(main_report);
			$(".rmb").formatCurrency();
			endLoadingSliceIndicator($('#div-main'));
		});
		//票务
		startLoadingSliceIndicator($('#div-ticket'));
		const p2 = $.ajax({
			type: "POST",
			url: self.apiurl + "accounting/searchPLTicket",
			data: common_data,
			traditional: true
		}).success(function(data) {
			let sum_money = 0;
			let air_report = data.air_report;
			for (let key in air_report) {
				sum_money += air_report[key];
			}
			air_report['sum'] = sum_money;
			self.ticket(air_report);
			$(".rmb").formatCurrency();
			endLoadingSliceIndicator($('#div-ticket'));
		});


		//人力成本
		startLoadingSliceIndicator($('#div-human'));
		let human_data = {
			date_from: date_from,
			date_to: date_to,
			reimbursement_items: self.human_items()
		}

		const p3 = $.ajax({
			type: "POST",
			url: self.apiurl + "accounting/searchPLReimbursement",
			data: human_data,
			traditional: true
		}).success(function(data) {
			let results = new Map(self.human_items().map(item => [item, 0]));
			let p_results = new Map(self.human_items().map(item => [`P${item}`, 0]));
			let y_results = new Map(self.human_items().map(item => [`Y${item}`, 0]));
			let i_results = new Map(self.human_items().map(item => [`I${item}`, 0]));
			let sum_money = 0;
			let p_sum = 0;
			let y_sum = 0;
			let i_sum = 0;
			for (let [key, _] of results) {
				if (data.reibursements[key]) {
					results.set(key, data.reibursements[key]);
					sum_money += data.reibursements[key];
				}

				if (data.reibursements['P' + key]) {
					p_results.set('P' + key, data.reibursements['P' + key]);
					p_sum += data.reibursements['P' + key];
				}
				if (data.reibursements['Y' + key]) {
					y_results.set('Y' + key, data.reibursements['Y' + key]);
					y_sum += data.reibursements['Y' + key];
				}
				if (data.reibursements['I' + key]) {
					i_results.set('I' + key, data.reibursements['I' + key]);
					i_sum += data.reibursements['I' + key];
				}
			}

			results.set("SUM", sum_money);
			p_results.set("PSUM", p_sum);
			y_results.set("YSUM", y_sum);
			i_results.set("ISUM", i_sum);

			self.human(results);
			self.p_human(p_results);
			self.y_human(y_results);
			self.i_human(i_results);
			$(".rmb").formatCurrency();
			endLoadingSliceIndicator($('#div-human'));
		});

		//费用申请
		startLoadingSliceIndicator($('#div-reimbursement'));
		let reimbursement_data = {
			date_from: date_from,
			date_to: date_to,
			reimbursement_items: self.reimbursement_items()
		}
		const p4 = $.ajax({
			type: "POST",
			url: self.apiurl + "accounting/searchPLReimbursement",
			data: reimbursement_data,
			traditional: true
		}).success(function(data) {

			let results = new Map(self.reimbursement_items().map(item => [item, 0]));
			let p_results = new Map(self.reimbursement_items().map(item => [`P${item}`, 0]));
			let y_results = new Map(self.reimbursement_items().map(item => [`Y${item}`, 0]));
			let i_results = new Map(self.reimbursement_items().map(item => [`I${item}`, 0]));
			let sum_money = 0;
			let p_sum = 0;
			let y_sum = 0;
			let i_sum = 0;
			for (let [key, _] of results) {
				if (data.reibursements[key]) {
					results.set(key, data.reibursements[key]);
					sum_money += data.reibursements[key];
				}

				if (data.reibursements['P' + key]) {
					p_results.set('P' + key, data.reibursements['P' + key]);
					p_sum += data.reibursements['P' + key];
				}
				if (data.reibursements['Y' + key]) {
					y_results.set('Y' + key, data.reibursements['Y' + key]);
					y_sum += data.reibursements['Y' + key];
				}
				if (data.reibursements['I' + key]) {
					i_results.set('I' + key, data.reibursements['I' + key]);
					i_sum += data.reibursements['I' + key];
				}
			}

			results.set("SUM", sum_money);
			p_results.set("PSUM", p_sum);
			y_results.set("YSUM", y_sum);
			i_results.set("ISUM", i_sum);
			self.reimbursement(results);
			self.p_reimbursement(p_results);
			self.y_reimbursement(y_results);
			self.i_reimbursement(i_results);
			$(".rmb").formatCurrency();
			endLoadingSliceIndicator($('#div-reimbursement'));
		});
		startLoadingSliceIndicator($('#div-summary'));
		startLoadingSliceIndicator($('#div-other'))
		startLoadingSliceIndicator($('#div-inner'))
		//汇总
		Promise.all([p1, p2, p3, p4]).then(() => {
			let other = {};
			other.receive = self.main().other_receive;
			other.pay = self.main().other_pay;
			other.profit = other.receive - other.pay;
			self.other(other);


			let inner = {};
			inner.sys_cost = self.main().sys_cost;
			inner.sale_cost = self.main().sale_cost;
			self.inner(inner);

			let summary = {};
			summary.order_count = self.main().order_count;
			summary.people_count = self.main().people_count;
			summary.gross_profit = self.main().gross_profit;
			summary.ticket = self.ticket().sum;
			summary.human = self.human().get("SUM");
			summary.reimbursement = self.reimbursement().get("SUM");
			summary.profit = summary.gross_profit - summary.ticket - summary.human - summary.reimbursement + other.profit;
			if (summary.people_count != 0) {
				summary.per_profit = summary.profit / summary.people_count;
			} else {
				summary.per_profit = 0;
			}
			self.summary(summary);

			$(".rmb").formatCurrency();

			endLoadingSliceIndicator($('#div-summary'));
			endLoadingSliceIndicator($('#div-other'));
			endLoadingSliceIndicator($('#div-inner'));
		});
	};
};

var ctx = new OrderContext();

$(document).ready(function() {
	ko.applyBindings(ctx);
	ctx.refresh();
});

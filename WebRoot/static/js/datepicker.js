if ($('.date-picker').datetimepicker != null) {
	$('.date-picker').datetimepicker({
		format : 'Y-m-d',
		timepicker : false,
		scrollInput : false,
		defaultDate : new Date(),
		lang : 'zh'
	});
}

if ($('.datetime-picker').datetimepicker != null) {
	$('.datetime-picker').datetimepicker({
		format : 'Y-m-d H:i',
		timepicker : true,
		scrollInput : false,
		defaultDate : new Date(),
		lang : 'zh'
	});
}

if ($('.datetime-picker-same-1').datetimepicker != null) {
	$('.datetime-picker-same-1').datetimepicker({
		format : 'Y-m-d H:i',
		timepicker : true,
		scrollInput : false,
		defaultDate : new Date(),
		lang : 'zh'
	}).on('blur', function() {
		$(".datetime-picker-same-2").val($(this).val());
	});
}

if ($('.datetime-picker-same-2').datetimepicker != null) {
	$('.datetime-picker-same-2').datetimepicker({
		format : 'Y-m-d H:i',
		timepicker : true,
		scrollInput : false,
		defaultDate : new Date(),
		lang : 'zh'
	});
}

if ($('.month-picker-st').MonthPicker != null) {
	$('.month-picker-st').MonthPicker({
		Button : false,
		MonthFormat : 'yy-mm'
	});
}
function reloadDatePicker() {
	$('.date-picker').datetimepicker({
		format : 'Y-m-d',
		timepicker : false,
		scrollInput : false,
		defaultDate : new Date(),
		lang : 'zh'
	});
}
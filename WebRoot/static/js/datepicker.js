if ($('.date-picker').datetimepicker != null) {
    $('.date-picker').datetimepicker({
        format: 'Y-m-d',
        timepicker: false,
        scrollInput: false,
        defaultDate: new Date(),
        lang: 'zh'
    });
}

if ($('.datetime-picker').datetimepicker != null) {
    $('.datetime-picker').datetimepicker({
        format: 'Y-m-d H:i',
        timepicker: true,
        scrollInput: false,
        defaultDate: new Date(),
        lang: 'zh'
    });
}

$(function(){ 
	$('.month-picker-st').MonthPicker({
		 Button: false,
		 MonthFormat: 'yy-mm'
	});
});
var JobContext = function() {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.projectId = $("#hidden_project_id").val();
    self.job = ko.observable({
        interviewTimeSlots:[],
        projectId: self.projectId,
        sequenceNumber: ko.observable(100),
        degree : ko.observable(),
        workingYear : ko.observable(),
        minSalary : ko.observable(),
        maxSalary : ko.observable(),
        province:"",
        district:""
    });

    //self.allJobTypes = ['销售类','客服/售后支持','人事/行政','财务类','生活/服务类','采购/贸易类','IT技术','生产/制造/物流','实习'];
    //self.allJobTags = ['知名公司','高工资','工作稳定','交通方便','晋升快'];
    self.allJobCategorys = ['全职','兼职','实习'];
    //self.interviewTimeSlots = ['6:00','7:00','8:00','9:00','10:00','11:00','12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00'];
    //self.interviewTypes = ['现场面试'];
    self.cities = ['北京', '上海', '天津', '广州'];
    self.publishStatus = ['未发布', '已发布'];
    self.allDegree = ['博士生及以上', '研究生及以上', '本科及以上', '大专及以上', '中专、高中及以上'];
    self.allWorkingYear = ['1年以下', '1-3年', '3-5年', '5-7年', '7-10年', '10年以上'];

    $.getJSON(self.apiurl + '/projects/' + self.projectId + '/job', {}, function (job) {
        if (job) {
            //if (job.jobTypes == null) {
            //    job.jobTypes = [];
            //}
            //
            //if (job.tags == null) {
            //    job.tags = [];
            //}
            //
            //if (job.interviewTimeSlots == null) {
            //    job.interviewTimeSlots = [];
            //}

            self.job(job);
        }
    });

    self.updateJob = function() {
        if (!$("form").valid()) {
            return;
        }
        var startDate = self.job().signInStartTime.substr(0,10)
        var endDate = self.job().signInEndTime.substr(0,10)

        if(startDate != endDate){
            fail_msg("开始时段和结束时段只能为同一天！");
        } else {
            var timeSlot = self.job().signInStartTime + "-" + self.job().signInEndTime.substr(11, 16);
            if (self.job().interviewTimeSlots.indexOf(timeSlot) < 0) {
                self.job().interviewTimeSlots = []
                self.job().interviewTimeSlots[0] = timeSlot;
            }else{
                self.job().interviewTimeSlots = []
            }
        }

        self.job().province = self.job().city;
        self.job().district = self.job().city;

        if (self.job().id) {
            $.ajax({
                type: "PUT",
                contentType: "application/json",
                url: self.apiurl + '/jobs/update',
                data: ko.toJSON(self.job)
            }).done(function (job) {
                window.location.replace("/projects/" + self.projectId + '/job');
            });
        } else {
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: self.apiurl + '/jobs/create',
                data:  ko.toJSON(self.job)
            }).done(function(job){
                window.location.replace("/projects/" + self.projectId + '/job');
            });
        }
    };
};

var ctx = new JobContext();

$(document).ready(function() {
    ko.applyBindings(ctx);
});

function changeRichText(path, value) {
    changeValueByPath(ctx.job(), path, value);
};

$(':input[type=number]').on('mousewheel',function(e){ $(this).blur();});

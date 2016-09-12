var BlacklistMobilesContext = function () {
    var self = this;
    self.apiurl = $("#hidden_apiurl").val();
    self.blacklistMobiles = ko.observableArray([]);
    self.query = ko.observable({saleProgress: []});
    self.allReasons = ko.observableArray(['已入职', '已offer', '已到访', '投诉', '职位不匹配']);
    self.chosenMobiles = ko.observableArray([]);
    self.checkAll = ko.observable(false);

    // start pagination
    self.currentPage = ko.observable(1);
    self.perPage = 10;
    self.pageNums = ko.observableArray();
    self.totalCount = ko.observable(1);
    self.startIndex = ko.computed(function () {
        return (self.currentPage() - 1) * self.perPage;
    });
    // end pagination

    self.searchBlacklistMobiles = function () {
        self.query().start = self.startIndex();
        self.query().count = self.perPage;
        self.query().order = 'desc';
        self.query().orderBy = 'created_time';

        $.getJSON(self.apiurl + '/blacklist-mobiles/search', $.param(self.query(), true).replace(/&?[^=&]+=(&|$)/g, ''),
            function (blacklistMobiles) {
                self.blacklistMobiles(blacklistMobiles);

                if (blacklistMobiles) {
                    self.totalCount(Math.ceil(blacklistMobiles.total / self.perPage));
                    self.setPageNums(self.currentPage());
                    addTotalCountLabel("blacklist-total-count", blacklistMobiles.total);
                }
            }
        );
    };

    self.removeBlacklistMobiles = function () {
        $(self.chosenMobiles()).each(function (idx, mobileId) {
            $.ajax({
                type: "DELETE",
                contentType: "application/json",
                url: self.apiurl + '/blacklist-mobiles/' + mobileId,
                data: ko.toJSON(self.blacklistMobile)
            }).done(function () {
                window.location.replace('/resumes/blacklist-mobiles');
            });
        });
    };

    // start pagination
    self.resetPage = function () {
        self.currentPage(1);
    };

    self.previousPage = function () {
        if (self.currentPage() > 1) {
            self.currentPage(self.currentPage() - 1);
            self.refreshPage();
        }
    };

    self.nextPage = function () {
        if (self.currentPage() < self.pageNums().length) {
            self.currentPage(self.currentPage() + 1);
            self.refreshPage();
        }
    };

    self.turnPage = function (pageIndex) {
        self.currentPage(pageIndex);
        self.refreshPage();
    };

    self.setPageNums = function (curPage) {
        var startPage = curPage - 4 > 0 ? curPage - 4 : 1;
        var endPage = curPage + 4 <= self.totalCount() ? curPage + 4 : self.totalCount();
        var pageNums = [];
        for (var i = startPage; i <= endPage; i++) {
            pageNums.push(i);
        }
        self.pageNums(pageNums);
    };

    self.refreshPage = function () {
        self.searchBlacklistMobiles();
        self.checkAll(false);
    };
    self.chkAll = function () {
        if (self.checkAll()) {
            var choose = [];
            $(self.blacklistMobiles().items).each(function (idx, mobile) {
                choose.push(mobile.id+'');
            });
            self.chosenMobiles(choose);
        } else {
            self.chosenMobiles([]);
        }
        return true;
    }
    // end pagination
};

var ctx = new BlacklistMobilesContext();

$(document).ready(function () {
    ko.applyBindings(ctx);
    ctx.searchBlacklistMobiles();
});

$('#create-blacklist-mobile').on('click', function () {
    $.layer({
        type: 2,
        title: ['新建黑名单手机号', ''],
        maxmin: false,
        closeBtn: [1, true],
        shadeClose: false,
        area: ['600px', '400px'],
        offset: ['100px', ''],
        iframe: {src: '/resumes/create-blacklist-mobile'},
        end: function () {
            ctx.searchBlacklistMobiles();
            console.log('create blacklist mobile successfully');
            success_msg("新建黑名单手机号成功");
        }
    });
});

$('#blacklist-upload-sync-btn').fileupload({
    url: '/import-blacklist',
    done: function (e, data) {
        console.log(data.result);
        success_msg("文件上传成功!");
    },
    fail: function (e, data) {
        fail_msg("文件上传失败!");
    }
});
!function() {
	var c = window;
	var e = [1, 72, 0, 0];
	var f = e.join("-");
	var g = /,0\,[1-9]/;
	var i = -1;
	var j = "select-local";
	var k = {
		province : function() {
			var b = {};
			$
					.each(
							"北京|1|72|1,上海|2|78|1,天津|3|51035|1,重庆|4|113|1,河北|5|142,山西|6|303,河南|7|412,辽宁|8|560,吉林|9|639,黑龙江|10|698,内蒙古|11|799,江苏|12|904,山东|13|1000,安徽|14|1116,浙江|15|1158,福建|16|1303,湖北|17|1381,湖南|18|1482,广东|19|1601,广西|20|1715,江西|21|1827,四川|22|1930,海南|23|2121,贵州|24|2144,云南|25|2235,西藏|26|2951,陕西|27|2376,甘肃|28|2487,青海|29|2580,宁夏|30|2628,新疆|31|2652,港澳|52993|52994,台湾|32|2768"
									.split(","), function(a, c) {
								c = c.split("|"), b[c[0]] = {
									id : c[1],
									c : c[2],
									z : c[3] || 0
								}
							});
			return b;
		}(),
		city : function() {
			var b = {};
			$
					.each(
							{
								1 : "朝阳区|72,海淀区|2800,西城区|2801,东城区|2802,丰台区|2805,石景山区|2806,门头沟|2807,房山区|2808,通州区|2809,大兴区|2810,顺义区|2812,怀柔区|2814,密云区|2816,昌平区|2901,平谷区|2953,延庆区|3065",
								2 : "徐汇区|2813,长宁区|2815,静安区|2817,虹口区|2822,杨浦区|2823,宝山区|2824,闵行区|2825,嘉定区|2826,浦东新区|2830,青浦区|2833,松江区|2834,金山区|2835,奉贤区|2837,普陀区|2841,崇明区|2919,黄浦区|78",
								3 : "东丽区|51035,和平区|51036,河北区|51037,河东区|51038,河西区|51039,红桥区|51040,蓟州区|51041,静海区|51042,南开区|51043,滨海新区|51044,西青区|51045,武清区|51046,津南区|51047,汉沽区|51048,大港区|51049,北辰区|51050,宝坻区|51051,宁河区|51052",
								4 : "万州区|113,涪陵区|114,梁平区|115,南川区|119,潼南区|123,大足区|126,黔江区|128,武隆区|129,丰都县|130,奉节县|131,开州区|132,云阳县|133,忠县|134,巫溪县|135,巫山县|136,石柱县|137,彭水县|138,垫江县|139,酉阳县|140,秀山县|141,璧山区|48131,荣昌区|48132,铜梁区|48133,合川区|48201,巴南区|48202,北碚区|48203,江津区|48204,渝北区|48205,长寿区|48206,永川区|48207,江北区|50950,南岸区|50951,九龙坡区|50952,沙坪坝区|50953,大渡口区|50954,綦江区|50995,渝中区|51026,城口县|4164",
								5 : "石家庄市|142,邯郸市|148,邢台市|164,保定市|199,张家口市|224,承德市|239,秦皇岛市|248,唐山市|258,沧州市|264,廊坊市|274,衡水市|275",
								6 : "太原市|303,大同市|309,阳泉市|318,晋城市|325,朔州市|330,晋中市|336,忻州市|350,吕梁市|368,临汾市|379,运城市|398,长治市|3074",
								7 : "郑州市|412,开封市|420,洛阳市|427,平顶山市|438,焦作市|446,鹤壁市|454,新乡市|458,安阳市|468,濮阳市|475,许昌市|482,漯河市|489,三门峡市|495,南阳市|502,商丘市|517,周口市|527,驻马店市|538,信阳市|549,济源市|2780",
								8 : "沈阳市|560,大连市|573,鞍山市|579,抚顺市|584,本溪市|589,丹东市|593,锦州市|598,葫芦岛市|604,营口市|609,盘锦市|613,阜新市|617,辽阳市|621,朝阳市|632,铁岭市|6858",
								9 : "长春市|639,吉林市|644,四平市|651,辽源市|2992,通化市|657,白山市|664,松原市|674,白城市|681,延边朝鲜族自治州|687",
								10 : "鹤岗市|727,双鸭山市|731,鸡西市|737,大庆市|742,伊春市|753,牡丹江市|757,佳木斯市|765,七台河市|773,黑河市|776,绥化市|782,大兴安岭地区|793,哈尔滨市|698,齐齐哈尔市|712",
								11 : "呼和浩特市|799,包头市|805,乌海市|810,赤峰市|812,乌兰察布市|823,锡林郭勒盟|835,呼伦贝尔市|848,鄂尔多斯市|870,巴彦淖尔市|880,阿拉善盟|891,兴安盟|895,通辽市|902",
								12 : "南京市|904,徐州市|911,连云港市|919,淮安市|925,宿迁市|933,盐城市|939,扬州市|951,泰州市|959,南通市|965,镇江市|972,常州市|978,无锡市|984,苏州市|988",
								13 : "济宁市|2900,济南市|1000,青岛市|1007,淄博市|1016,枣庄市|1022,东营市|1025,潍坊市|1032,烟台市|1042,威海市|1053,德州市|1060,临沂市|1072,聊城市|1081,滨州市|1090,菏泽市|1099,日照市|1108,泰安市|1112",
								14 : "黄山市|1151,滁州市|1159,阜阳市|1167,亳州市|1174,宿州市|1180,池州市|1201,六安市|1206,宣城市|2971,铜陵市|1114,合肥市|1116,淮南市|1121,淮北市|1124,芜湖市|1127,蚌埠市|1132,马鞍山市|1137,安庆市|1140",
								15 : "宁波市|1158,衢州市|1273,丽水市|1280,台州市|1290,舟山市|1298,杭州市|1213,温州市|1233,嘉兴市|1243,湖州市|1250,绍兴市|1255,金华市|1262",
								16 : "福州市|1303,厦门市|1315,三明市|1317,莆田市|1329,泉州市|1332,漳州市|1341,南平市|1352,龙岩市|1362,宁德市|1370",
								17 : "孝感市|1432,黄冈市|1441,咸宁市|1458,恩施州|1466,鄂州市|1475,荆门市|1477,随州市|1479,神农架林区|3154,武汉市|1381,黄石市|1387,襄阳市|1396,十堰市|1405,荆州市|1413,宜昌市|1421,潜江市|2922,天门市|2980,仙桃市|2983",
								18 : "长沙市|1482,株洲市|1488,湘潭市|1495,衡阳市|1501,邵阳市|1511,岳阳市|1522,常德市|1530,张家界市|1540,郴州市|1544,益阳市|1555,永州市|1560,怀化市|1574,娄底市|1586,湘西州|1592",
								19 : "广州市|1601,深圳市|1607,珠海市|1609,汕头市|1611,韶关市|1617,河源市|1627,梅州市|1634,揭阳市|1709,惠州市|1643,汕尾市|1650,东莞市|1655,中山市|1657,江门市|1659,佛山市|1666,阳江市|1672,湛江市|1677,茂名市|1684,肇庆市|1690,云浮市|1698,清远市|1704,潮州市|1705",
								20 : "崇左市|3168,南宁市|1715,柳州市|1720,桂林市|1726,梧州市|1740,北海市|1746,防城港市|1749,钦州市|1753,贵港市|1757,玉林市|1761,贺州市|1792,百色市|1806,河池市|1818,来宾市|3044",
								21 : "南昌市|1827,景德镇市|1832,萍乡市|1836,新余市|1842,九江市|1845,鹰潭市|1857,上饶市|1861,宜春市|1874,抚州市|1885,吉安市|1898,赣州市|1911",
								22 : "凉山州|2103,成都市|1930,自贡市|1946,攀枝花市|1950,泸州市|1954,绵阳市|1960,德阳市|1962,广元市|1977,遂宁市|1983,内江市|1988,乐山市|1993,宜宾市|2005,广安市|2016,南充市|2022,达州市|2033,巴中市|2042,雅安市|2047,眉山市|2058,资阳市|2065,阿坝州|2070,甘孜州|2084",
								23 : "三亚市|3690,文昌市|3698,五指山市|3699,临高县|3701,澄迈县|3702,定安县|3703,屯昌县|3704,昌江县|3705,白沙县|3706,琼中县|3707,陵水县|3708,保亭县|3709,乐东县|3710,三沙市|3711,海口市|2121,琼海市|3115,万宁市|3137,东方市|3173,儋州市|3034",
								24 : "贵阳市|2144,六盘水市|2150,遵义市|2155,铜仁市|2169,毕节市|2180,安顺市|2189,黔西南布依族苗族自治州|2196,黔东南州|2205,黔南州|2222",
								25 : "迪庆州|4108,昆明市|2235,曲靖市|2247,玉溪市|2258,昭通市|2270,普洱市|2281,临沧市|2291,保山市|2298,丽江市|2304,文山州|2309,红河州|2318,西双版纳州|2332,楚雄州|2336,大理州|2347,德宏州|2360,怒江州|2366",
								26 : "阿里地区|3970,林芝市|3971,拉萨市|2951,那曲地区|3107,山南地区|3129,昌都地区|3138,日喀则地区|3144",
								27 : "延安市|2428,汉中市|2442,榆林市|2454,商洛市|2468,安康市|2476,西安市|2376,铜川市|2386,宝鸡市|2390,咸阳市|2402,渭南市|2416",
								28 : "庆阳市|2525,陇南市|2534,武威市|2544,张掖市|2549,酒泉市|2556,甘南州|2564,临夏州|2573,定西市|3080,兰州市|2487,金昌市|2492,白银市|2495,天水市|2501,嘉峪关市|2509,平凉市|2518",
								29 : "西宁市|2580,海东地区|2585,海北州|2592,黄南州|2597,海南州|2603,果洛州|2605,玉树州|2612,海西州|2620",
								30 : "银川市|2628,石嘴山市|2632,吴忠市|2637,固原市|2644,中卫市|3071",
								31 : "铁门关市|53090,五家渠市|4110,阿拉尔市|15945,图木舒克市|15946,乌鲁木齐市|2652,克拉玛依市|2654,石河子市|2656,吐鲁番地区|2658,哈密地区|2662,和田地区|2666,阿克苏地区|2675,喀什地区|2686,克孜勒苏柯尔克孜自治州|2699,巴音郭楞州|2704,昌吉州|2714,博尔塔拉州|2723,伊犁州|2727,塔城地区|2736,阿勒泰地区|2744,昆玉市|53668,北屯市|129143,可克达拉市|145492,胡杨河市|146206",
								32 : "台湾|2768",
								52993 : "香港特别行政区|52994,澳门特别行政区|52995"
							}, function(c, d) {
								var e = [];
								$.each(d.split(","), function(a, b) {
									b = b.split("|"), e.push({
										id : b[1],
										name : b[0]
									})
								}), b[c] = e
							});
			return b
		}()
	}

	console.log(k.city[2]);
}(jQuery)
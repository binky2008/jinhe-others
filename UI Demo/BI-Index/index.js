// accordion
;(function($) {

	var Accordion = function($el, multiple) {
		this.el = $el || {};
		this.multiple = multiple || false;

		var links = this.el.find('.link'), oThis = this;
		links.click( function() {
			oThis.dropdown(this);
		} );
	}

	Accordion.prototype.open = function(link) {
		// 计算高度，默认submenu的高度为 36 * 8 = 288
		var $parent = $(link.parentNode);
		var $submenu = $parent.find(".submenu");
		var liSize = $submenu.find("li").size();

		$parent.addClass("open");
		$submenu.addClass("open");
		$submenu.css("height", (++liSize * 36) + "px");			
	}

	Accordion.prototype.openFirst = function() {
		var oThis = this;
		this.el.find('.link').each(function(i, link) {
			if( !$(link.parentNode).hasClass("hidden") ) {
				oThis.open(link);
				return false;
			}
		});
	}

	Accordion.prototype.dropdown = function(link) {
		var $el = this.el,
			$li = $(link),
			$parent = $(link.parentNode);

		$el.find('li').removeClass('open');

		var $submenu = $parent.find('.submenu');

		// 已经打开的则关闭掉
		if( $submenu.hasClass("open") ) {
			$submenu.removeClass("open").css("height", "0px");
		}
		else {
			if (!this.multiple) { // 如不允许打开多个，先关闭所有已经打开的
				$el.find('.submenu').removeClass('open').css("height", "0px");
			};

			this.open(link);
		}
	}	

	$.fn.extend({
		accordion : function(multiple) {
			return new Accordion(this, false);
		}
	})

})(tssJS);

// init
;(function($) {
	var accordion = $('#ad1').accordion(false);
	accordion.openFirst();

	$("footer .switch").toggle(
		function() {
			$("header").hide();
			$("section>.left").hide();
			$("section .main").css("width", "100%");
			$("section").css("padding-bottom", "30px");
		},
		function() {
			$("header").show();
			$("section>.left").show();
			$("section .main").css("width", "83%");
			$("section").css("padding-bottom", "66px");
		}
	);

	$("header li").each(function(i, li) {
		$li = $(li);
		$li.attr("onclick", "$.openMenu(this)");
	});

	$(".submenu a").each(function(i, a) {
		$a = $(a);
		$a.attr("href", "javascript:void(0);");
		$a.attr("onclick", "$.openReport(this)");
	});

	$.extend({ 

		openMenu: function(li) {
			var $li = $(li);
			var mid = $li.attr("mid");
			if( !mid ) return;

			$(".link").each(function(i, link) {
				$link = $(link);
				if( $link.hasClass(mid) ) {
					$(link.parentNode).removeClass("hidden");
				} else {
					$(link.parentNode).addClass("hidden");
				}
			});

			$("header li").removeClass("active");
			$li.addClass("active");
			accordion.openFirst();
		},

		openReport: function(a) {
			var $a  = $(a);
			var rid = $a.attr("rid");
			if( !rid ) return;

			var id = "rp_" + rid, iframeId = "iframe_" + rid;

			$("footer ul li").removeClass("active");
			$("section .main iframe").hide();

			var $li = $("#" + id);
			if( !$li.length ) {
				var li = $.createElement("li", "", id);
				$("footer ul").appendChild(li);

				$li = $(li);
				$li.html("<span>" + $a.html() + "</span>");
				$li.find("span").click(function() {
					$.openReport(a);
				});

				// 创建一个iframe，嵌入报表
				var iframeEl = $.createElement("iframe", "", iframeId);
				$("section .main").appendChild(iframeEl);

				// TODO 换成report_portlet.html?id=rid
				$(iframeEl).attr("frameborder", 0).attr("src", "test.html");
			}

			$li.addClass("active");
			$("#" + iframeId).show();
		}
	});

})(tssJS);
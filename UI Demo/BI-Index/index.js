$(function() {

	var Accordion = function(el, multiple) {
		this.el = el || {};
		this.multiple = multiple || false;

		var links = this.el.find('.link'), oThis = this;
		links.click( function() {
			oThis.dropdown(this);
		} );
	}

	Accordion.prototype.open = function(index) {
		this.el.find('.link')[index].onclick();
	}

	Accordion.prototype.dropdown = function(link) {
		var $el = this.el,
			$li = $(link),
			$parent = $(link.parentNode);

		$el.find('li').removeClass('open');

		var submenu = $parent.find('.submenu');
		if(submenu.hasClass("open")) {
			$parent.find('.submenu').removeClass("open");
		}
		else {
			if (!this.multiple) {
				$el.find('.submenu').removeClass('open');
			};
			$parent.find('.submenu').addClass("open");
			$parent.addClass("open");
		}
		
	}	

	var accordion = new Accordion($('#ad1'), false);
	accordion.open(0);
});
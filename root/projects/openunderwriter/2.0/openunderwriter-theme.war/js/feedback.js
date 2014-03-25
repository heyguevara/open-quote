YUI().use('aui-modal', 'cookie', function(Y) {
	var reminder = Y.Cookie.get("reminder", Number);
	var now = new Date().getTime();
	if (reminder==null || now > reminder) {
		var advance=now+86400000;
		var expire=new Date();
		expire.setMonth(expire.getYear()+1);
		Y.Cookie.set("reminder", advance, {expires: expire, path: "/"});
	}
	if (reminder!=null && now > reminder) {
		var modal = new Y.Modal({
	        centered: true,
	        headerContent: '<h3>Your feedback is appreciated...</h3>',
	        bodyContent: "<p>Like all open source projects, OpenUnderwriter thrives on feedback. It \
	        	shapes the project's future direction and encourages the team of volunteers \
	        	who work to develope and maintain it.</p>\
	        	<p><a href='/web/guest/feedback'>Click here to provide your feedback</a>.</p>",
	        modal: true,
	        render: '#modal',
	        width: 500
	    }).render();
	}
	var sub=Y.one("#p_p_id_1_WAR_webformportlet_INSTANCE_7zXWkYu4cX5H_ button[type='submit']");
	if (sub!=null) {
		sub.on("mouseenter",function(event) {
			var advance=now+2592000000;
			var expire=new Date();
			expire.setMonth(expire.getYear()+1);
			Y.Cookie.set("reminder", advance, {expires: expire, path: "/"});
		});
	}
});


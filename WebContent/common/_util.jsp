<script>
	window.formatTime = function(time) {
		if (!time) {
			return ""
		}
		if (('' + time).length === 10) {
			time = parseInt(time) * 1000
		} else {
			time = +time
		}
		const d = new Date(time);
		const now = Date.now();
		const diff = (now - d) / 1000;

		if (diff < 30) {
			return '刚刚'
		} else if (diff < 3600) {
			// less 1 hour
			return Math.ceil(diff / 60) + '分钟前'
		} else if (diff < 3600 * 24) {
			return Math.ceil(diff / 3600) + '小时前'
		} else if (diff < 3600 * 24 * 2) {
			return '1天前'
		}
		let res = (d.getMonth() + 1 + '月' + d.getDate() + '日');
		if (d.getHours() !== 0 && d.getMinutes() !== 0) {
			res += d.getHours() + '时' + d.getMinutes() + '分'
		}
		return res;
	};
</script>
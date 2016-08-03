var seckill = {
	// 封装购买相关ajax的url
	URL : {
		now:function(){
			return '/seckill2/time/now';
		},
		exposer:function(seckillId){
			return '/seckill2/'+seckillId+'/exposer';
		},
		execute:function(seckillId,md5){
			return '/seckill2/'+seckillId+"/"+md5+'/execute';
		}

	},
	// 验证手机号
	validatePhone : function(phone) {
		if (phone && phone.length == 11 && !isNaN(phone)) {
			return true;
		} else {
			return false;
		}
	},
	//倒计时函数
	countdown:function(seckillId,nowTime,startTime,endTime){
		var seckillBox=$('#seckill-box');
		if (nowTime>endTime) {
			seckillBox.html("购买结束");
		}else if (nowTime<startTime) {
			seckillBox.countdown(startTime,function(event){
				var format=event.strftime('购买倒计时： %D天 %H小时 %M分 %S秒');
				seckillBox.html(format);
				//时间完成后回调时间
			}).on('finish.countdown',function(){
				seckill.handleseckill(seckillId,seckillBox);
			});
		}else{
			seckill.handleseckill(seckillId,seckillBox);
			console.log("可以购买");
		}
	},

	//处理购买逻辑
	handleseckill:function(seckillId,node){
		node.html('<button class="btn btn-primary btn-lg" id="killBtn">开始购买</button>');
		$.post(seckill.URL.exposer(seckillId),{},function(result){
			if (result&&result['success']) {
				var exposer=result['data'];
				if (exposer['exposed']) {
					var md5=exposer['md5'];
					var killUrl=seckill.URL.execute(seckillId,md5);
					console.log("killUrl: "+ killUrl);
					//绑定一次点击事件
					$('#killBtn').one('click',function(){
						//执行购买请求
						//先禁用按钮
						$(this).addClass('disabled');
						//发送购买请求
						$.post(killUrl,{},function(result){
							if (result&&result['success']) {
								var killResult=result['data'];
								var status=killResult['status'];
								var statusInfo=killResult['statusInfo'];
								//显示购买结果
								node.html('<span class="label label-success">'+statusInfo+'</span>');
							}
						});
					});
				}
				else{
					var now=exposer['now'];
					var start=exposer['start'];
					var end=exposer['end'];
					seckill.countdown(id,now,start,end);
				}
			}else{
				console.log("result: "+result);
			}
		});
	},
	// 详情页购买逻辑
	detail : {
		// 详情页初始化
		init : function(params) {
			// 手机验证和登录，计时交互
			var killPhone = $.cookie('killPhone');
			// 验证手机号
			if (!seckill.validatePhone(killPhone)) {
				// 绑定手机，控制输出
				var killPhoneModal = $('#killPhoneModal');
				killPhoneModal.modal({
					// 显示弹出层
					show : true,
					backdrop : 'static', // 禁止位置关闭
					keyboard : false
				// 关闭键盘事件
				});
				$('#killPhoneBtn')
						.click(function() {
									var inputPhone = $('#killPhoneKey').val();
									if (seckill.validatePhone(inputPhone)) {
										// 手机号写入cookie
										$.cookie('killPhone', inputPhone, {expires : 7, path:"/"});
										// 刷新页面
										window.location.reload();
									} else {
										$('#killPhoneMessage').hide().html('<labe class="label label-danger">手机号码不正确</label>').show(300);
									}
								});
			}
			//计时交互
			var startTime = params['startTime'];
			var endTime = params['endTime'];
			var seckillId = params['seckillId'];
			$.get(seckill.URL.now(),{},function(result){
				if (result&&result['success']) {
					var nowTime=result['data'];
					//时间判断
					seckill.countdown(seckillId,nowTime,startTime,endTime);
				}

			});
		}

	}
}
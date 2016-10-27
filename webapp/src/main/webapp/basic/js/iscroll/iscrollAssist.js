/**
 * 滚动辅助模块
 * 
 * @author 564539969@qq.com
 * @since 2013-07-26
 */
;
(function($, window) {

	// 模块对外提供的公共方法
	var exportsMethods = {

		/**
		 * 新建一个竖直滚动实例,并做一些处理,整合上拉下拉的功能 wrapper 要渲染滚动实例的位置 pullDownAction 下拉执行的逻辑
		 * pullUpAction 上拉执行的逻辑 opts 滚动个性化参数 pullText 拉动时不同状态要显示的文字
		 */
		initScroll : function(dataDOM, pullDownAction, pullUpAction, opts, pullText) {
			var $wrapper;
			if (dataDOM && dataDOM.wrapper) {
				$wrapper = dataDOM.wrapper;
			}
			/**
			 * 初始化变量
			 */
			var pageItemCount = opts && opts['pageItemCount'] ? opts['pageItemCount'] : 10;
			if (typeof wrapper === 'string') {
				$wrapper = $(wrapper);
			} else if (typeof wrapper === 'object') {
				$wrapper = wrapper;
			}
			var pulldownRefresh = pullText && pullText['pulldownRefresh'] ? pullText['pulldownRefresh'] : '下拉刷新';
			var pullupLoadingMore = pullText && pullText['pullupLoadingMore'] ? pullText['pullupLoadingMore'] : '上拉加载更多';
			var releaseToRefresh = pullText && pullText['releaseToRefresh'] ? pullText['releaseToRefresh'] : '松手开始刷新';
			var releaseToLoading = pullText && pullText['releaseToLoading'] ? pullText['releaseToLoading'] : '松手开始加载';
			var loading = pullText && pullText['loading'] ? pullText['loading'] : '加载中...';
			var $pullDown = $wrapper.find('.pullDown'), $pullUp = $wrapper.find('.pullUp'), pullUpOffset = 0, pullDownOffset = 0;
			var offset = 0;

			if ($pullDown.length > 0) {
				pullDownOffset = $pullDown.outerHeight();
				$pullDown.find('.pullDownLabel').html(pulldownRefresh);
			}

			if ($pullUp.length > 0) {
				pullUpOffset = $pullUp.outerHeight();
				// $pullUp.find('.pullUpLabel').html(pullupLoadingMore);
			}
			if (!offset) {
				// If we have more than 1 page of results and offset is not
				// manually defined - we set it to be the pullUpOffset.
				// 数据量超过一页且没有指定offset,则offset为上拉区块的高度(让起始的startY向上偏移上区块高度以隐藏上区块)
				offset = pullDownOffset;
			}
			var options = {
				// 让起始的startY向上偏移上区块高度以隐藏上区块
				startY : (parseInt(offset) * (-1))
			};
			$.extend(true, options, opts);
			var scrollObj = this.newVerScroll($wrapper[0], options);
			scrollObj.pullDown = $pullDown;
			scrollObj.pullUp = $pullUp;
			scrollObj.pullDownOffset = pullDownOffset;
			scrollObj.pullUpOffset = pullUpOffset;

			// 滚动的时候触发的事件
			scrollObj.on('scroll', function() {
				if ($pullDown.length > 0 && this.y >= 5 && !$pullDown.hasClass('flip')) {
					$pullDown.attr('class', 'pullDown flip text-center');
					$pullDown.find('.pullDownLabel').html(releaseToRefresh);
					this.minScrollY = 0;
				} else if ($pullDown.length > 0 && this.y < 5 && $pullDown.hasClass('flip')) {
					$pullDown.attr('class', 'pullDown text-center');
					$pullDown.find('.pullDownLabel').html(pulldownRefresh);
					this.minScrollY = -pullDownOffset;
					// this.y < this.minScrollY代表是上拉,以防下拉的时候未拉到尽头时进入上拉的逻辑中
				}
				pullActionDetect.check(0);
			});

			// 滚动结束之后触发的事件
			scrollObj.on('scrollEnd', function() {
				if ($pullDown.length > 0 && $pullDown.hasClass('flip')) {
					$pullDown.attr('class', 'pullDown loading');
					$pullDown.find('.pullDownLabel').html(loading);
					if (typeof pullDownAction === 'function') {
						pullDownAction.call(scrollObj);
					}
				} else {
					// 没有下拉到刷新位则隐藏下拉区块(必须是上滚(下拉)并且要防止正常上滚触发了scrollEnd,进入此步骤)
					if ((scrollObj.directionY == -1 || scrollObj.directionY == 0) && scrollObj.y > -pullDownOffset) {
						scrollObj.scrollTo(0, parseInt(pullDownOffset) * (-1), 0);
					}
					pullActionDetect.check(0);
				}
			});

			// In order to prevent seeing the "pull down to refresh" before the
			// iScoll is trigger - the wrapper is located at left:-9999px and
			// returned to left:0 after the iScoll is initiated
			// 把容器从9999调整回0
			setTimeout(function() {
				$wrapper.css({
					left : 0
				});
			}, 0);

			/**
			 * 滚动操作的监听器
			 * 
			 * @param obj
			 *            dom对象或者选择字符串
			 * @param option
			 *            滚动其他属性
			 * @return IScroll实例对象
			 */
			var pullActionDetect = {
				count : 0,
				limit : 10,
				check : function(count) {
					if (count) {
						// 重置监视器
						pullActionDetect.count = 0;
					}
					// Detects whether the momentum has stopped, and if it has
					// reached the end - 200px of the scroller - it trigger the
					// pullUpAction
					setTimeout(function() {
						// 离底部还有autoLoadMargin距离的时候就加载下一页,而不是等触底才加载(类似预加载)
						if (scrollObj.y <= (scrollObj.maxScrollY + (scrollObj.options.autoLoadMargin?scrollObj.options.autoLoadMargin:500)) && $pullUp && !$pullUp.hasClass('loading')) {
							$pullUp.addClass('loading').find(".pullUpLabel").html("加载中...");
							if (typeof pullUpAction === 'function') {
								pullUpAction.call(scrollObj);
							}
						} else if (pullActionDetect.count < pullActionDetect.limit) {
							// 暂不知其功能,从未触发过
							pullActionDetect.check();
							pullActionDetect.count++;
						}
					}, 200);
				}
			};

			return scrollObj;
		},
		/**
		 * 加载完数据的回调(有数据的情况)
		 */
		pullActionCallback : function(scrollObj) {
			var pullDown = scrollObj.pullDown;
			var pullUp = scrollObj.pullUp;
			if (pullDown && pullDown.hasClass('loading')) {
				pullDown.attr('class', 'pullDown text-center');
				$('.pullDownLabel', pullDown).html('下拉刷新');
				scrollObj.scrollTo(0, parseInt(scrollObj.pullDownOffset) * (-1), 0);
			} else if (pullUp && pullUp.hasClass('loading')) {
				pullUp.removeClass('loading');
			}
		},
		/**
		 * 拉到最下没有更多数据的回调
		 */
		pullActionNoMore : function(scrollObj) {
			var pullUp = scrollObj.pullUp;
			if (pullUp && pullUp.hasClass('loading')) {
				pullUp.removeClass("loading");
				pullUp.find(".pullUpLabel").html("没有更多数据");
			}
		},
		/**
		 * 创建一个竖直方向的滚动实例
		 * 
		 * @param obj
		 *            dom对象或者选择字符串
		 * @param option
		 *            滚动其他属性
		 * @return IScroll实例对象
		 */
		newVerScroll : function(dom, option) {
			var opt = {
				probeType : 1,
				tap : true,
				click : false,
				preventDefaultException : {
					tagName : /.*/
				},
				mouseWheel : true,
				scrollbars : true,
				fadeScrollbars : true,
				interactiveScrollbars : false,
				keyBindings : false,
				deceleration : 0.0002
			};
			if (option) {
				$.extend(opt, option);
			}
			var iSObj = new IScroll(dom, opt);

			// 滚动条在滚动时显示出来,滚动结束隐藏
			// V5以前版本有个参数可以设置,V5之后目前只能手动处理滚动条的显示隐藏或者可从外部传个参数进来判断
			iSObj.on("scrollEnd", function() {
				if (this.indicator1) {
					// this.indicator1.indicatorStyle['transition-duration'] =
					// '350ms';
					// this.indicator1.indicatorStyle['opacity'] = '0';
				}
			});
			iSObj.on("scrollMove", function() {
				if (this.indicator1) {
					// this.indicator1.indicatorStyle['transition-duration'] =
					// '0ms';
					// this.indicator1.indicatorStyle['opacity'] = '0.8';
				}
			});
			return iSObj;
		},
		/**
		 * 添加wrapper,scroller,上下拉元素等
		 */
		initDOM : function(dataDOM, pullDownAction, pullUpAction) {
			dataDOM.wrap('<div class="wrapper"></div>').wrap('<div class="scroller"></div>');
			if (pullDownAction) {
				var pullDown = $('<div class="pullDown text-center"><span class="pullDownLabel">下拉刷新</span></div>');
				dataDOM.before(pullDown);
			}
			if (pullUpAction) {
				var pullUp = $('<div class="pullUp text-center"><span class="pullUpLabel">没有更多数据</span></div>');
				dataDOM.after(pullUp);
			}
			// 添加无任何数据时的显示区域
			var nodata = $('<div class="nodata text-center light-grey">暂无数据！</div>');
			var wrapper = dataDOM.closest(".wrapper");
			wrapper.before(nodata);
			nodata.children("a").click(pullDownAction);
			dataDOM.wrapper = wrapper;
			dataDOM.nodata=nodata;
			return dataDOM;
		}
	};

	window.iscrollAssist = exportsMethods;

})(jQuery, window);
/*
 * jQuery File Upload User Interface Plugin 9.5.2
 * https://github.com/blueimp/jQuery-File-Upload
 *
 * Copyright 2014, xiaqiang
 * https://blueimp.net
 *
 * Licensed under the MIT license:
 * http://www.opensource.org/licenses/MIT
 */

/* jshint nomen:false */
/* global define, window */

/** 全局的上传属性可以在这里自定义配置,会覆盖初始的和jquery.fileupload-ui.js参数 **/
(function(factory) {
	'use strict';
	if (typeof define === 'function' && define.amd) {
		// Register as an anonymous AMD module:
		define([ 'jquery', 'tmpl', './jquery.fileupload-image', './jquery.fileupload-audio',
				'./jquery.fileupload-video', './jquery.fileupload-validate' ], factory);
	} else {
		// Browser globals:
		factory(window.jQuery, window.tmpl);
	}
}(function($, tmpl) {
	'use strict';

	$.widget('blueimp.fileupload', $.blueimp.fileupload, {

		options : {
			messages : {
				maxNumberOfFiles : '文件上传个数超过限制',
				acceptFileTypes : '不允许上传该文件类型',
				maxFileSize : '文件容量过大,超过上传要求',
				minFileSize : '文件容量过小,低于上传要求'
			},
			// 选择完文件后,自动上传
			autoUpload : true,
			// 使用哪个id的text/x-tmpl模板去渲染上传时文件列表html元素
			uploadTemplateId : 'template-upload',
			// 使用哪个id的text/x-tmpl模板去渲染下载时文件列表html元素
			downloadTemplateId : undefined,
			// 请求服务器后,返回的数据类型(一般返回的是下载列表的files的json)
			dataType : 'json',
			// 上传文件个数限制
			maxNumberOfFiles : undefined,
			// 单个文件最大上传容量(现在是1G左右)
			maxFileSize : 1104857600,
			// 单个文件最小上传容量
			minFileSize : undefined,
			// 图片显示缩略图的容量限制,超过该值不显示缩略图
			loadImageMaxFileSize : 10000000,
			// 重写getFilesFromResponse获取方法
			getFilesFromResponse : function(data) {
				if (data.result.obj && $.isArray(data.result.obj)) {
					return data.result.obj;
				}
				return [];
			},
			// 上传预览图最大宽度
			previewMaxWidth : 80,
			// 上传预览图最大高度
			previewMaxHeight : 80
		}

	});

}));

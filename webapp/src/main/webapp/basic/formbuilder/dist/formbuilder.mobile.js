(function() {
    rivets.binders.input = {
        publishes: true,
        routine: rivets.binders.value.routine,
        bind: function(el) {
            return $(el).bind('input.rivets', this.publish);
        },
        unbind: function(el) {
            return $(el).unbind('input.rivets');
        }
    };

    rivets.configure({
        prefix: "rv",
        adapter: {
            subscribe: function(obj, keypath, callback) {
                callback.wrapped = function(m, v) {
                    return callback(v);
                };
                return obj.on('change:' + keypath, callback.wrapped);
            },
            unsubscribe: function(obj, keypath, callback) {
                return obj.off('change:' + keypath, callback.wrapped);
            },
            read: function(obj, keypath) {
                if (keypath === "cid") {
                    return obj.cid;
                }
                return obj.get(keypath);
            },
            publish: function(obj, keypath, value) {
                if (obj.cid) {
                    return obj.set(keypath, value);
                } else {
                    return obj[keypath] = value;
                }
            }
        }
    });

}).call(this);

(function() {
    var BuilderView, EditFieldView, Formbuilder, FormbuilderCollection, FormbuilderModel, ViewFieldView, _ref, _ref1, _ref2, _ref3, _ref4,
        __hasProp = {}.hasOwnProperty,
        __extends = function(child, parent) {
            for (var key in parent) {
                if (__hasProp.call(parent, key))
                    child[key] = parent[key];
            }

            function ctor() {
                this.constructor = child;
            }
            ctor.prototype = parent.prototype;
            child.prototype = new ctor();
            child.__super__ = parent.prototype;
            return child;
        };

    FormbuilderModel = (function(_super) {
        __extends(FormbuilderModel, _super);

        function FormbuilderModel() {
            _ref = FormbuilderModel.__super__.constructor.apply(this, arguments);
            return _ref;
        }

        FormbuilderModel.prototype.sync = function() {};

        FormbuilderModel.prototype.indexInDOM = function() {
            var $wrapper,
                _this = this;
            $wrapper = $(".fb-field-wrapper").filter((function(_, el) {
                return $(el).data('cid') === _this.attributes.cid;
            }));
            return $(".fb-field-wrapper").index($wrapper);
        };

        FormbuilderModel.prototype.is_input = function() {
            return Formbuilder.inputFields[this.get(Formbuilder.options.mappings.FIELD_TYPE)] != null;
        };

        return FormbuilderModel;

    })(Backbone.DeepModel);

    FormbuilderCollection = (function(_super) {
        __extends(FormbuilderCollection, _super);

        function FormbuilderCollection() {
            _ref1 = FormbuilderCollection.__super__.constructor.apply(this, arguments);
            return _ref1;
        }

        FormbuilderCollection.prototype.initialize = function() {
            return this.on('add', this.copyCidToModel);
        };

        FormbuilderCollection.prototype.model = FormbuilderModel;

        FormbuilderCollection.prototype.comparator = function(model) {
        	//alert("comparator:" +model.attributes.label+";" + model.indexInDOM());
            return model.indexInDOM();
        };

        FormbuilderCollection.prototype.copyCidToModel = function(model) {
            return model.attributes.cid = model.cid;
        };

        return FormbuilderCollection;

    })(Backbone.Collection);

    ViewFieldView = (function(_super) {
        __extends(ViewFieldView, _super);

        function ViewFieldView() {
            _ref2 = ViewFieldView.__super__.constructor.apply(this, arguments);
            return _ref2;
        }

        ViewFieldView.prototype.className = "fb-field-wrapper";

        ViewFieldView.prototype.events = {
            'click .subtemplate-wrapper': 'focusEditView',
            'click .js-duplicate': 'duplicate',
            'click .js-clear': 'clear',
            'click .field-control-btn': 'fieldControl',
            //'click .field-control-i': 'fieldControl',
            'click .add-detailele-btn': 'addDetailEle',
            'click .fc-up': 'fieldUp', //上移
            'click .fc-down': 'fieldDown', //下移
            'click .fc-del': 'clear', //删除
            'click .fc-setting': 'fieldSetting', //设置
        };

        ViewFieldView.prototype.initialize = function(options) {
            this.parentView = options.parentView;
            this.listenTo(this.model, "change", this.render);

            $(document).on("click", function() {
                $(".field-control").addClass("hidden");
            });

            return this.listenTo(this.model, "destroy", this.remove);
        };

        //单个要素渲染方法
        ViewFieldView.prototype.render = function(e) {
            //console.log("render");
            _this = this;
            if (_this.model.get("isConnectionField")) {
                _this.$el.addClass("with-bg");
            }
            if (e) {
                //console.log(_this);
                if (e.changed.isTitle) {
                    //如果已经勾选了显示到内容，则不能勾选显示到标题
                    if (e.get("isShow")) {
                        e.set({
                            isTitle: false
                        });
                    } else {
                        //所有非明细要素，只能有1个勾选显示到标题
                        //如果已经有其他要素勾选了显示到标题，则提示是否替换
                        if ($(_this.parentView.collection.where({
                                isTitle: true
                            })).length > 1) {
                            //if(confirm("已经选择了["+$(_this.parentView.collection.where({isTitle: true}))[1].get("label")+"]要素“显示到列表标题区域”，是否替换？")){
                            if (confirm("已经选择了其他要素“显示到列表标题区域”，是否替换？")) {
                                _this.parentView.collection.remove(e, {
                                    silent: true
                                });
                                $(_this.parentView.collection.where({
                                    isTitle: true
                                })).each(function() {
                                    _this.parentView.collection.get(this).set({
                                        isTitle: false
                                    }, {
                                        silent: true
                                    });
                                });
                                _this.parentView.collection.add(e, {
                                    silent: true
                                });
                            } else {
                                e.set({
                                    isTitle: false
                                });
                            }
                        }
                    }

                    //关联任务带出来的字段，不能勾选这两个选项
                    /*
                    if (e.get("isConnectionField")) {
                        e.set({
                            isTitle: false
                        });
                        e.set({
                            isShow: false
                        });
                    }
                     * */
                }
                if (e.changed.isShow) {
                    //所有非明细要素，最多有3个勾选显示到列表内容
                    if (_this.parentView.collection.where({
                            isShow: true
                        }).length > 3) {
                        e.set({
                            isShow: false
                        });
                        alert("最多允许三个要素勾选。");
                    }

                    if (e.get("isTitle")) {
                        e.set({
                            isShow: false
                        });
                    }

                    //关联任务带出来的字段，不能勾选这两个选项
                    /*
                    if (e.get("isConnectionField")) {
                        e.set({
                            isTitle: false
                        });
                        e.set({
                            isShow: false
                        });
                    }
                     * */
                }
                //isProveEdit
                //审批可编辑逻辑处理
                if (e.changed.isProveEdit) {
                    if (e.has("pcid")) {
                        _this.parentView.collection.get(e.get("pcid")).set("isProveEdit", true);
                    }
                }
                if (e.get("field_type") == "detail") {
                    if (e.changed.isProveEdit == false) {
                        alert("取消后，整个明细的所有要素将都不可以编辑！");
                    }
                }

                if (e.changed.field_options) {
                    if (e.changed.field_options.digits) {
                        var regStr = /^(0|[1-9][0-9]*)$/;
                        if (!regStr.test(e.changed.field_options.digits)) {
                            //console.log(e.changed.field_options.digits);
                            e.set({
                                field_options: {
                                    digits: ""
                                }
                            }, {
                                silent: false
                            });
                        }
                    }
                }
            }
            this.$el.attr("data-pcid", this.model.attributes.pcid).data("pcid", this.model.attributes.pcid);
            //console.log(this.parentView.$responseFields.find("[data-pcid]"));
            this.$el.addClass(
                'response-field-' + this.model.get(Formbuilder.options.mappings.FIELD_TYPE)
            ).attr("data-cid", this.model.attributes.cid).data(
                'cid', this.model.attributes.cid
            );
            /*
      if(this.model.get(Formbuilder.options.mappings.FIELD_TYPE) == "detail"){
        //this.$el.data("childEl", this.parentView.$responseFields.find("[data-pcid='"+this.$el.attr("data-cid")+"']").clone(true,true));
      }
       * */
            //debugger;
            if (e && this.model.get(Formbuilder.options.mappings.FIELD_TYPE) == "detail") {
                //&& _this.parentView.collection.where({pcid: this.$el.attr("data-cid")}).length > 0
                this.$el.children("div").children("label").children("span").text(this.model.get("label"));
            } else {
                this.$el.html(
                    Formbuilder.templates["view/base" + (!this.model.is_input() ? '_non_input' : '')]({
                        rf: this.model
                    })
                );
            }

            /*
      if(this.model.get(Formbuilder.options.mappings.FIELD_TYPE) == "detail"){
        //console.log(this.parentView.$responseFields.find("[data-pcid='"+this.$el.attr("data-cid")+"']"));
        //console.log(this.parentView.$responseFields.find("[data-pcid]"));
        //console.log(this);
        //this.$el.data("childEl").appendTo(this.$el.find(".detail"));
      $(_this.parentView.collection.where({pcid: this.$el.attr("data-cid")})).each(function(){
        //console.log("detail info");
        //console.log(this);
        //var view = new ViewFieldView({
          //  model: this,
          //  parentView: _this.parentView
          //});
        //view.render();
        //this.trigger("change");
        
        _this.$el.find(".detail").append(
          "<div class='fb-field-wrapper response-field-"+this.get(Formbuilder.options.mappings.FIELD_TYPE)+"'>" +
        Formbuilder.templates["view/base" + (!this.is_input() ? '_non_input' : '')]({
          rf: this
        }) + 
        "</div>"
        );
    });
      }
       * */

            return this;
        };

        ViewFieldView.prototype.focusEditView = function(e) {
            //console.log(e);
            e.stopPropagation();
            return this.parentView.createAndShowEditView(this.model);
        };

        ViewFieldView.prototype.clear = function(e) {
            var cb, x,
                _this = this;
            e.preventDefault();
            e.stopPropagation();
            cb = function() {
                _this.parentView.handleFormUpdate();

                $(_this.parentView.collection.where({
                    pcid: _this.model.get("cid")
                })).each(function() {
                    this.destroy();
                });
                //console.log(_this.parentView.collection);
                return _this.model.destroy();
            };
            x = Formbuilder.options.CLEAR_FIELD_CONFIRM;
            switch (typeof x) {
                case 'string':
                    if (confirm(x)) {
                        return cb();
                    }
                    break;
                case 'function':
                    return x(cb);
                default:
                    return cb();
            }
        };
        //显示要素的下拉操作栏
        ViewFieldView.prototype.fieldControl = function(e) {
            //console.log(e);
            var _this = this,
            	$thisFieldControl = $(e.target).closest(".subtemplate-wrapper").children(".field-control");

            e.preventDefault();
            e.stopPropagation();

            if (!$(e.target).closest(".fb-field-wrapper").hasClass("editing")) {
                this.parentView.createAndShowEditView(this.model);
            }

            $thisFieldControl.toggleClass("hidden");
            $(".field-control").not($thisFieldControl).addClass("hidden");
            //$(e.target).closest(".subtemplate-wrapper").trigger("click");

            return ViewFieldView;
        };
        //上移
        ViewFieldView.prototype.fieldUp = function(e) {
            e.preventDefault();
            e.stopPropagation();

            var _this = this,
                $thisField = $(e.target).closest(".fb-field-wrapper"),
                $prevField = $thisField.prev(".fb-field-wrapper");


            $thisField.insertBefore($prevField);
            $(".field-control").addClass("hidden");
            _this.parentView.collection.sort();
            return ViewFieldView;
        };
        //下移
        ViewFieldView.prototype.fieldDown = function(e) {
            e.preventDefault();
            e.stopPropagation();

            var _this = this;

            var _this = this,
                $thisField = $(e.target).closest(".fb-field-wrapper"),
                $nextField = $thisField.next(".fb-field-wrapper");

            $thisField.insertAfter($nextField);
            $(".field-control").addClass("hidden");
            _this.parentView.collection.sort();
            return ViewFieldView;
        };
        //设置
        ViewFieldView.prototype.fieldSetting = function(e) {
            //console.log(e);
            var _this = this;

            //e.preventDefault();
            //e.stopPropagation();

            return ViewFieldView;
        };
        ViewFieldView.prototype.addDetailEle = function(e) {
        	e.stopPropagation();
        	e.preventDefault();
        	$(".fb-main").data("isDetailAdd", true);
        	
        	if (!$(e.target).closest(".fb-field-wrapper").hasClass("editing")) {
                this.parentView.createAndShowEditView(this.model);
            }
        	
        	$("a[data-target='#addField']").trigger("click");
        	this.parentView.toggleBackBtn(true);
        	
        	//console.log(this);
        	//console.log($(".fb-main").data("isDetailAdd"));
        	
        	return this;
        };

        ViewFieldView.prototype.duplicate = function() {
            var attrs;
            attrs = _.clone(this.model.attributes);
            delete attrs['id'];
            attrs['label'] += ' Copy';
            return this.parentView.createField(attrs, {
                position: this.model.indexInDOM() + 1
            });
        };

        return ViewFieldView;

    })(Backbone.View);

    EditFieldView = (function(_super) {
        __extends(EditFieldView, _super);

        function EditFieldView() {
            _ref3 = EditFieldView.__super__.constructor.apply(this, arguments);
            return _ref3;
        }

        EditFieldView.prototype.className = "edit-response-field";

        EditFieldView.prototype.events = {
            'click .js-add-option': 'addOption',
            'click .js-remove-option': 'removeOption',
            'click .js-default-updated': 'defaultUpdated',
            'input .option-label-input': 'forceRender'
        };

        EditFieldView.prototype.initialize = function(options) {
            this.parentView = options.parentView;
            return this.listenTo(this.model, "destroy", this.remove);
        };

        EditFieldView.prototype.render = function() {
            //console.log(this);
            this.$el.html(Formbuilder.templates["edit/base" + (!this.model.is_input() ? '_non_input' : '')]({
                rf: this.model
            }));
            rivets.bind(this.$el, {
                model: this.model
            });

            return this;
        };

        EditFieldView.prototype.remove = function() {
            this.parentView.editView = void 0;
            //取消下边一行代码操作。删除一个要素后，不跳转到添加要素页。
            //this.parentView.$el.find("[data-target=\"#addField\"]").click();
            return EditFieldView.__super__.remove.apply(this, arguments);
        };

        EditFieldView.prototype.addOption = function(e) {
            var $el, i, newOption, options;
            $el = $(e.currentTarget);
            i = this.$el.find('.option').index($el.closest('.option'));
            options = this.model.get(Formbuilder.options.mappings.OPTIONS) || [];
            newOption = {
                label: "",
                checked: false
            };
            if (i > -1) {
                options.splice(i + 1, 0, newOption);
            } else {
                options.push(newOption);
            }
            this.model.set(Formbuilder.options.mappings.OPTIONS, options);
            this.model.trigger("change:" + Formbuilder.options.mappings.OPTIONS);
            return this.forceRender();
        };

        EditFieldView.prototype.removeOption = function(e) {
            var $el, index, options;
            $el = $(e.currentTarget);
            index = this.$el.find(".js-remove-option").index($el);
            options = this.model.get(Formbuilder.options.mappings.OPTIONS);
            options.splice(index, 1);
            this.model.set(Formbuilder.options.mappings.OPTIONS, options);
            this.model.trigger("change:" + Formbuilder.options.mappings.OPTIONS);
            return this.forceRender();
        };

        EditFieldView.prototype.defaultUpdated = function(e) {
            var $el;
            $el = $(e.currentTarget);
            if (this.model.get(Formbuilder.options.mappings.FIELD_TYPE) !== 'checkboxes') {
                this.$el.find(".js-default-updated").not($el).attr('checked', false).trigger('change');
            }
            return this.forceRender();
        };

        EditFieldView.prototype.forceRender = function() {
            return this.model.trigger('change');
        };

        return EditFieldView;

    })(Backbone.View);

    BuilderView = (function(_super) {
        __extends(BuilderView, _super);

        function BuilderView() {
            _ref4 = BuilderView.__super__.constructor.apply(this, arguments);
            return _ref4;
        }

        BuilderView.prototype.SUBVIEWS = [];

        BuilderView.prototype.events = {
            'click .js-save-form': 'saveForm',
            'click #btn-deploy': "deployForm",
            'click .fb-tabs a': 'showTab',
            'click .fb-addfield': 'showAddTab',
            'click .fb-add-field-types a': 'addField', //点击（拖拽亦可）要素分类列表，添加一个要素
            'click .fb-back-fieldsort': 'showSortTab', //点击（拖拽亦可）要素分类列表，添加一个要素
            'mouseover .fb-add-field-types': 'lockLeftWrapper',
            'mouseout .fb-add-field-types': 'unlockLeftWrapper'
        };

        BuilderView.prototype.initialize = function(options) {
            var selector;
            selector = options.selector, this.formBuilder = options.formBuilder, this.bootstrapData = options.bootstrapData;
            if (selector != null) {
                this.setElement($(selector));
            }
            this.collection = new FormbuilderCollection;
            this.collection.bind('add', this.addOne, this);
            this.collection.bind('reset', this.reset, this);
            this.collection.bind('change', this.handleFormUpdate, this);
            this.collection.bind('destroy add reset', this.hideShowNoResponseFields, this);
            this.collection.bind('destroy', this.ensureEditViewScrolled, this);
            this.render();
            this.collection.reset(this.bootstrapData);
            return this.bindSaveEvent();
        };

        BuilderView.prototype.bindSaveEvent = function() {
            var _this = this;
            //this.formSaved = true;
            this.formSaved = false;
            this.saveFormButton = this.$el.find(".js-save-form");
            this.deployFormButton = this.$el.find("#btn-deploy");
            this.saveFormButton.text(Formbuilder.options.dict.ALL_CHANGES_SAVED);
            //this.saveFormButton.attr('disabled', true).text(Formbuilder.options.dict.ALL_CHANGES_SAVED);
            //this.deployFormButton.attr('disabled', true);
            if (!!Formbuilder.options.AUTOSAVE) {
                setInterval(function() {
                    return _this.saveForm.call(_this);
                }, 5000);
            }
            return $(window).bind('beforeunload', function() {
                if (_this.formSaved) {
                    return void 0;
                } else {
                    return Formbuilder.options.dict.UNSAVED_CHANGES;
                }
            });
        };

        BuilderView.prototype.reset = function() {
            //debugger;
            this.$responseFields.html('');
            return this.addAll();
        };

        BuilderView.prototype.render = function() {
            var subview, _i, _len, _ref5;
            this.$el.html(Formbuilder.templates['page']());
            this.$fbLeft = this.$el.find('.fb-left');
            this.$responseFields = this.$el.find('.fb-response-fields');
            this.bindWindowScrollEvent();
            this.hideShowNoResponseFields();
            _ref5 = this.SUBVIEWS;
            for (_i = 0, _len = _ref5.length; _i < _len; _i++) {
                subview = _ref5[_i];
                new subview({
                    parentView: this
                }).render();
            }
            return this;
        };

        BuilderView.prototype.bindWindowScrollEvent = function() {
            var _this = this;
            return $(window).on('scroll', function() {
                var maxMargin, newMargin;
                if (_this.$fbLeft.data('locked') === true) {
                    return;
                }
                newMargin = Math.max(0, $(window).scrollTop() - _this.$el.offset().top);
                maxMargin = _this.$responseFields.height();
                return _this.$fbLeft.css({
                    //针对移动端，取消margin-top
                    //'margin-top': Math.min(maxMargin, newMargin)
                });
            });
        };

        BuilderView.prototype.showTab = function(e) {
            var $el, first_model, target;
            $el = $(e.currentTarget);
            target = $el.data('target');
            $el.closest('li').addClass('active').siblings('li').removeClass('active');
            $(target).addClass('active').siblings('.fb-tab-pane').removeClass('active');
            if (target !== '#editField') {
                this.unlockLeftWrapper();
            }
            if (target === '#editField' && !this.editView && (first_model = this.collection.models[0])) {
                return this.createAndShowEditView(first_model);
            }
        };

        BuilderView.prototype.showAddTab = function(e) {
        	$(".fb-main").data("isDetailAdd", false);
            $("a[data-target='#addField']").trigger("click");
            
            //console.log($(".fb-main").data("isDetailAdd"));
            
            return this.toggleBackBtn(true);
        };
        BuilderView.prototype.showSortTab = function(e) {
        	$("a[data-target='#sortField']").trigger("click");
        	
        	return this.toggleBackBtn(false);
        };
        BuilderView.prototype.toggleBackBtn = function(flag) {
        	if(flag){
        		$(".form-content").removeClass("main");
        	}else{
        		if(!$(".form-content").hasClass("main")){
        			$(".form-content").addClass("main");
        		}
        	}
        	
        	return this;
        };

        BuilderView.prototype.addOne = function(responseField, _, options) {
            //debugger;
        	/*
        	 * 
        	 	var pcid = $(this).closest(".response-field-detail").data("cid");
                if (ui.item.data('field-type')) {
                    rf = context.collection.create(Formbuilder.helpers.defaultFieldAttrs(ui.item.data('field-type')), {
                        $replaceEl: ui.item
                    }).set("pcid", pcid);
                    context.createAndShowEditView(rf);
                }
        	 */
            var $replacePosition, view;
            view = new ViewFieldView({
                model: responseField,
                parentView: this
            });
            if (options.$replaceEl != null) {
                return options.$replaceEl.replaceWith(view.render().el);
            } else if ((options.position == null) || options.position === -1) {
            	if(this.editView && 
            			this.editView.model.get("field_type") == "detail" && 
            			responseField.get("field_type") != "detail" && 
            			$(".fb-main").data("isDetailAdd")){
            		var pcid = this.$responseFields.find(".response-field-detail.editing").data("cid");
            		view.model.set("pcid", pcid);
                    
            		return this.$responseFields.find(".response-field-detail.editing").find(".detail").append(view.render().el);
            	}
                return this.$responseFields.append(view.render().el);
            } else if (options.position === 0) {
                return this.$responseFields.prepend(view.render().el);
            } else if (($replacePosition = this.$responseFields.find(".fb-field-wrapper").eq(options.position))[0]) {
                return $replacePosition.before(view.render().el);
            } else {
                return this.$responseFields.append(view.render().el);
            }
            
            
        };
        
        //设置要素拖动后的操作
        BuilderView.prototype.setSortable = function() {
            var _this = this;
            if (this.$responseFields.hasClass('ui-sortable')) {
                this.$responseFields.sortable('destroy');
            }
            this.$responseFields.sortable({
                forcePlaceholderSize: true,
                placeholder: 'sortable-placeholder',
                disabled: true,
                stop: function(e, ui) {
                    //console.log(_this);
                    var rf;
                    if (ui.item.data('field-type')) {
                        //console.log(_this.collection.toJSON());
                        rf = _this.collection.create(Formbuilder.helpers.defaultFieldAttrs(ui.item.data('field-type')), {
                            $replaceEl: ui.item
                        });
                        _this.createAndShowEditView(rf);
                        //console.log(_this.collection.toJSON());
                    }
                    if (ui.item.data('field-type') == "detail") {
                        _this.setDetailSort(_this);
                        _this.setDraggable();
                    }
                    _this.collection.sort();
                    _this.handleFormUpdate();
                    return true;
                },
                update: function(e, ui) {
                    if (!ui.item.data('field-type')) {
                        return _this.ensureEditViewScrolled();
                    }
                }
            });

            this.setDetailSort(this);

            return this.setDraggable();
        };

        //设置明细拖动后的操作
        BuilderView.prototype.setDetailSort = function(context) {
            //debugger;
            context.$responseFields.find(".detail").sortable({
                forcePlaceholderSize: true,
                placeholder: 'sortable-placeholder',
                disabled: true,
                stop: function(e, ui) {
                    if (ui.item.data('field-type') != "detail") {
                        var rf;
                        var pcid = $(this).closest(".response-field-detail").data("cid");
                        if (ui.item.data('field-type')) {
                            rf = context.collection.create(Formbuilder.helpers.defaultFieldAttrs(ui.item.data('field-type')), {
                                $replaceEl: ui.item
                            }).set("pcid", pcid);
                            context.createAndShowEditView(rf);
                        }
                        context.collection.sort();
                        context.handleFormUpdate();
                        return true;
                    } else {
                        ui.item.remove();
                    }
                    //debugger;
                },
                update: function(e, ui) {
                    if (!ui.item.data('field-type')) {
                        return context.ensureEditViewScrolled();
                    }
                }
            });
            //_this.$el.find("[data-field-type]").draggable("option", "connectToSortable", ".fb-response-fields, .fb-response-fields .detail");
        }

        BuilderView.prototype.setDraggable = function() {
            var $addFieldButtons,
                _this = this;
            $addFieldButtons = this.$el.find("[data-field-type]");
            return $addFieldButtons.draggable({
                //connectToSortable: this.$responseFields,
                connectToSortable: ".fb-response-fields, .fb-response-fields .detail",
                helper: function() {
                    var $helper;
                    $helper = $("<div class='response-field-draggable-helper' />");
                    $helper.css({
                        width: _this.$responseFields.width(),
                        height: '80px',
                        position: "fixed"
                    });
                    return $helper;
                }
            });
        };

        BuilderView.prototype.addAll = function() {
            //debugger;
            var _this = this;
            _this.collection.each(this.addOne, this);

            _this.$responseFields.children("div.fb-field-wrapper.response-field-detail").each(function() {
                $(this).attr("data-cid", $(this).data("cid"));
            });

            _this.$responseFields.children("div.fb-field-wrapper").each(function() {
                if ($(this).data("pcid")) {
                    $(this).appendTo(_this.$responseFields.children("[data-cid='" + $(this).data("pcid") + "']").find(".detail"));
                }
            });
            _this.collection.sort();
            return this.setSortable();
        };

        BuilderView.prototype.hideShowNoResponseFields = function() {
            return this.$el.find(".fb-no-response-fields")[this.collection.length > 0 ? 'hide' : 'show']();
        };

        BuilderView.prototype.addField = function(e) {
            var field_type;
            field_type = $(e.currentTarget).data('field-type');
            return this.createField(Formbuilder.helpers.defaultFieldAttrs(field_type));
        };

        BuilderView.prototype.createField = function(attrs, options) {
            var rf;
            rf = this.collection.create(attrs, options);
            this.createAndShowEditView(rf);
            return this.handleFormUpdate();
        };

        BuilderView.prototype.createAndShowEditView = function(model) {
            var $newEditEl, $responseFieldEl;
            $responseFieldEl = this.$el.find(".fb-field-wrapper").filter(function() {
                $(this).data('pcid', model.pcid);
                return $(this).data('cid') === model.attributes.cid;
            });
            //点击某个要素，给该要素添加editing类，以突出选中状态
            $(".fb-response-fields").find('.fb-field-wrapper').removeClass('editing');
            $responseFieldEl.addClass('editing');
            //$responseFieldEl.addClass('editing').siblings('.fb-field-wrapper').removeClass('editing');
            if (model.get("field-type") == "detail") {
                //console.log($responseFieldEl);
                $responseFieldEl.find('.fb-field-wrapper').removeClass('editing');
            } else if (model.get("pcid")) {
                $(".fb-response-fields > .fb-field-wrapper").removeClass('editing');
            }

            if (this.editView) {
                if (this.editView.model.cid === model.cid) {
                    this.$el.find(".fb-tabs a[data-target=\"#editField\"]").click();
                    this.toggleBackBtn(true);
                    this.scrollLeftWrapper($responseFieldEl);
                    return;
                }
                this.editView.remove();
            }
            this.editView = new EditFieldView({
                model: model,
                parentView: this
            });
            $newEditEl = this.editView.render().$el;
            this.$el.find(".fb-edit-field-wrapper").html($newEditEl);
            //移动端，点击添加某个要素后，自动跳到要素排版的TAB
            this.$el.find(".fb-tabs a[data-target=\"#sortField\"]").click();
            this.toggleBackBtn(false);
            this.scrollLeftWrapper($responseFieldEl);

            //console.log(model);
            //明细、明细内、位置、附件要素，不显示这两个选项
            if (model.get("field_type") == "detail" ||
                model.get("pcid") ||
                model.get("field_type") == "position" ||
                model.get("field_type") == "selectuser" ||
                model.get("field_type") == "file") {
                $("#isShowLabel").addClass("hidden");
                $("#isTitleLabel").addClass("hidden");
            }
            //明细要素，不需要“必填”选项
            if (model.get("field_type") == "detail") {
                $("#isMustLabel").addClass("hidden");
            }

            if (model.get("field_type") == "detail") {
                this.setDetailSort(this);
            }

            return this;
        };

        BuilderView.prototype.ensureEditViewScrolled = function() {
            if (!this.editView) {
                return;
            }
            return this.scrollLeftWrapper($(".fb-field-wrapper.editing"));
        };

        BuilderView.prototype.scrollLeftWrapper = function($responseFieldEl) {
            var _this = this;
            this.unlockLeftWrapper();
            if (!$responseFieldEl[0]) {
                return;
            }
            return $.scrollWindowTo((this.$el.offset().top + $responseFieldEl.offset().top) - this.$responseFields.offset().top, 200, function() {
                return _this.lockLeftWrapper();
            });
        };

        BuilderView.prototype.lockLeftWrapper = function() {
            return this.$fbLeft.data('locked', true);
        };

        BuilderView.prototype.unlockLeftWrapper = function() {
            return this.$fbLeft.data('locked', false);
        };

        BuilderView.prototype.handleFormUpdate = function() {
            if (this.updatingBatch) {
                return;
            }
            this.formSaved = false;
            //this.deployFormButton.removeAttr('disabled');
            //return this.saveFormButton.removeAttr('disabled').text(Formbuilder.options.dict.SAVE_FORM);
            return this.saveFormButton.text(Formbuilder.options.dict.SAVE_FORM);
        };
        //表单保存方法
        BuilderView.prototype.saveForm = function(e) {
            var payload;
            if (this.formSaved) {
                return;
            }
            //必须有一个非明细内的要素的isTitle被选中
            if (this.collection.where({
                    isTitle: true,
                    pcid: undefined
                }).length < 1) {
                alert(Formbuilder.options.dict.MUST_ISTITLE);
                return;
            }

            //this.formSaved = true;
            this.formSaved = false;
            this.saveFormButton.text(Formbuilder.options.dict.ALL_CHANGES_SAVED);
            //this.saveFormButton.attr('disabled', true).text(Formbuilder.options.dict.ALL_CHANGES_SAVED);

            //debugger;
            //console.log(this.collection);
            this.collection.sort();
            payload = JSON.stringify({
                fields: this.collection.toJSON()
            });
            if (Formbuilder.options.HTTP_ENDPOINT) {
                this.doAjaxSave(payload);
            }
            return this.formBuilder.trigger('save', payload);
        };
        //表单发布方法
        BuilderView.prototype.deployForm = function(e) {
            var payload;
            //必须有一个非明细内的要素的isTitle被选中
            if (this.collection.where({
                    isTitle: true,
                    pcid: undefined
                }).length < 1) {
                alert(Formbuilder.options.dict.MUST_ISTITLE);
                return;
            }

            //如果有“单选”“多选”要素未添加选项，发布时给出提示
            if (!this.isAllHasOptions(this)) {
                alert(Formbuilder.options.dict.MUST_ALLHASOPTIONS);
                return;
            }
            

            this.collection.sort();
            payload = JSON.stringify({
                fields: this.collection.toJSON()
            });
            if (Formbuilder.options.HTTP_ENDPOINT) {
                this.doAjaxSave(payload);
            }
            return this.formBuilder.trigger('deploy', payload);
        };
        //判断表单中是否有“单选”/“多选”未添加选项，或选项全部为空
        BuilderView.prototype.isAllHasOptions = function(cObj) {
            var allHasOptions = true;

            $(cObj.collection.where({
                field_type: "checkboxes"
            })).each(function() {
                var optStrLen = 0;
                if (this.get("field_options").options.length < 1) {
                    return allHasOptions = false;
                } else {
                    $(this.get("field_options").options).each(function() {
                        optStrLen += this.label.length;
                    })
                    if (optStrLen == 0) return allHasOptions = false;
                }
            });
            if (!allHasOptions) return false;

            $(cObj.collection.where({
                field_type: "radio"
            })).each(function() {
                var optStrLen = 0;
                if (this.get("field_options").options.length < 1) {
                    return allHasOptions = false;
                } else {
                    $(this.get("field_options").options).each(function() {
                        optStrLen += this.label.length;
                    })
                    if (optStrLen == 0) return allHasOptions = false;
                }
            });
            if (!allHasOptions) return false;

            return allHasOptions;
        };
        BuilderView.prototype.doAjaxSave = function(payload) {
            var _this = this;
            return $.ajax({
                url: Formbuilder.options.HTTP_ENDPOINT,
                type: Formbuilder.options.HTTP_METHOD,
                data: payload,
                contentType: "application/json",
                success: function(data) {
                    var datum, _i, _len, _ref5;
                    _this.updatingBatch = true;
                    for (_i = 0, _len = data.length; _i < _len; _i++) {
                        datum = data[_i];
                        if ((_ref5 = _this.collection.get(datum.cid)) != null) {
                            _ref5.set({
                                id: datum.id
                            });
                        }
                        _this.collection.trigger('sync');
                    }
                    return _this.updatingBatch = void 0;
                }
            });
        };

        return BuilderView;

    })(Backbone.View);

    Formbuilder = (function() {
        Formbuilder.helpers = {
            defaultFieldAttrs: function(field_type) {
                var attrs, _base;
                attrs = {};
                //加要素属性，第一处
                //attrs[Formbuilder.options.mappings.LABEL] = '未定义';
                attrs[Formbuilder.options.mappings.LABEL] = Formbuilder.fieldype2Name(field_type);
                attrs[Formbuilder.options.mappings.FIELD_NAME] = Formbuilder.fieldype2Name(field_type);
                attrs[Formbuilder.options.mappings.FIELD_TYPE] = field_type;
                attrs[Formbuilder.options.mappings.REQUIRED] = false;
                attrs[Formbuilder.options.mappings.ISPROVEEDIT] = false;
                attrs[Formbuilder.options.mappings.ISSHOW] = false;
                attrs[Formbuilder.options.mappings.ISTITLE] = false;
                attrs[Formbuilder.options.mappings.ISCONNECTIONFIELD] = false;
                attrs['field_options'] = {};
                return (typeof(_base = Formbuilder.fields[field_type]).defaultAttributes === "function" ? _base.defaultAttributes(attrs) : void 0) || attrs;
            },
            simple_format: function(x) {
                return x != null ? x.replace(/\n/g, '<br />') : void 0;
            }
        };

        Formbuilder.options = {
            BUTTON_CLASS: 'fb-button',
            HTTP_ENDPOINT: '',
            HTTP_METHOD: 'POST',
            AUTOSAVE: false,
            CLEAR_FIELD_CONFIRM: false,
            //加要素属性，第二处
            mappings: {
                SIZE: 'field_options.size', //尺寸（要素显示尺寸）。大（large），中（medium），小（small）三个值
                UNITS: 'field_options.units', //单位（number类型要素会显示）。
                DIGITS: 'field_options.digits', //小数位数
                ISTHOUSANDTH: 'field_options.isThousandth',				//千分位格式化
                LABEL: 'label', //字段名
                FIELD_NAME: 'field_name', //字段类型中文名
                FIELD_TYPE: 'field_type', //字段类型（仅供显示，不可更改）
                REQUIRED: 'required', //是否必填
                ISPROVEEDIT: 'isProveEdit', //审批可编辑
                ISSHOW: 'isShow', //显示到列表
                ISTITLE: 'isTitle', //显示到列表标题
                ISCONNECTIONFIELD: 'isConnectionField', //是否关联任务字段
                ADMIN_ONLY: 'admin_only', //仅管理员可见（或可编辑。这个选项目前应该没用）
                OPTIONS: 'field_options.options', //要素包含的选项
                DESCRIPTION: 'field_options.description', //字段描述（一段文字，描述该字段的具体说明信息）
                INCLUDE_OTHER: 'field_options.include_other_option', //选项。当字段有多个选项时显示，如下拉选择，单选按钮组，多选按钮组
                INCLUDE_BLANK: 'field_options.include_blank_option', //是否包含“空”选项。当字段有多个选项时，可选择此项。
                INTEGER_ONLY: 'field_options.integer_only', //是否只允许整数
                INCLUDE_TIME: 'field_options.include_time', //包含时间
                MIN: 'field_options.min', //最小值。如果是数字类型字段，显示此项
                MAX: 'field_options.max', //最大值。同上
                MINLENGTH: 'field_options.minlength', //字段可输入的最小长度。
                MAXLENGTH: 'field_options.maxlength', //字段可输入的最大长度。
                LENGTH_UNITS: 'field_options.min_max_length_units' //无。（目前没有使用）
            },
            dict: {
                ALL_CHANGES_SAVED: '保存',
                SAVE_FORM: '保存',
                UNSAVED_CHANGES: '更改尚未保存，是否确认离开？',
                MUST_ISTITLE: '所有非明细内要素，必须有且只有一个选中“显示到列表标题区域”！',
                MUST_ALLHASOPTIONS: '您有部分[单选]/[多选]要素未添加选项或选项值全部为空，请先检查！'
            }
        };

        Formbuilder.fields = {};

        Formbuilder.inputFields = {};

        Formbuilder.nonInputFields = {};

        Formbuilder.registerField = function(name, opts) {
            var x, _i, _len, _ref5;
            _ref5 = ['view', 'edit'];
            for (_i = 0, _len = _ref5.length; _i < _len; _i++) {
                x = _ref5[_i];
                opts[x] = _.template(opts[x]);
            }
            opts.field_type = name;
            Formbuilder.fields[name] = opts;
            if (opts.type === 'non_input') {
                return Formbuilder.nonInputFields[name] = opts;
            } else {
                return Formbuilder.inputFields[name] = opts;
            }
        };
        //根据要素类型，映射显示名称
        Formbuilder.fieldype2Name = function(type) {
            switch (type) {
                case 'text':
                    return '单行文字';
                    break;
                case 'paragraph':
                    return '多行文字';
                    break;
                case 'date':
                    return '日期';
                    break;
                case 'time':
                    return '时间';
                    break;
                case 'datetime':
                    return '日期时间';
                    break;
                case 'radio':
                    return '单选按钮';
                    break;
                case 'checkboxes':
                    return '多选框';
                    break;
                case 'selectuser':
                    return '选择用户';
                    break;
                case 'idcard':
                    return '身份证号';
                    break;
                case 'address':
                    return '地址';
                    break;
                case 'position':
                    return '位置';
                    break;
                case 'number':
                    return '数字';
                    break;
                case 'file':
                    return '附件';
                    break;
                case 'detail':
                    return '明细';
                    break;
                case 'phone':
                    return '电话';
                    break;
                case 'video':
                    return '视频';
                    break;
                case 'dropdown':
                    return '下拉选择';
                    break;
                case 'email':
                    return '邮箱';
                    break;
                case 'website':
                    return '网址';
                    break;
                default:
                    return '';
            }
        };

        function Formbuilder(opts) {
            var args;
            if (opts == null) {
                opts = {};
            }
            _.extend(this, Backbone.Events);
            args = _.extend(opts, {
                formBuilder: this
            });
            this.mainView = new BuilderView(args);
        }

        return Formbuilder;

    })();

    window.Formbuilder = Formbuilder;

    if (typeof module !== "undefined" && module !== null) {
        module.exports = Formbuilder;
    } else {
        window.Formbuilder = Formbuilder;
    }

}).call(this);

(function() {
    Formbuilder.registerField('text', {
        order: 0,
        view: "<input type='text' placeholder=\'请输入内容\' class='rf-size-<%= rf.get(Formbuilder.options.mappings.SIZE) %>' disabled/>",
        edit: "<%= Formbuilder.templates['edit/size']() %>\n<%= Formbuilder.templates['edit/min_max_length']() %>",
        addButton: "单行文字",
        defaultAttributes: function(attrs) {
            attrs.field_options.size = 'small';
            return attrs;
        }
    });

}).call(this);

(function() {
    Formbuilder.registerField('paragraph', {
        order: 5,
        view: "<textarea placeholder=\'请输入内容\' class='rf-size-<%= rf.get(Formbuilder.options.mappings.SIZE) %>' disabled></textarea>",
        edit: "<%= Formbuilder.templates['edit/size']() %>\n<%= Formbuilder.templates['edit/min_max_length']() %>",
        addButton: "多行文字",
        defaultAttributes: function(attrs) {
            attrs.field_options.size = 'small';
            return attrs;
        }
    });

}).call(this);

(function() {
    Formbuilder.registerField('date', {
        order: 10,
        //view: "<div class='input-line'>\n <span class='year'>\n    <input type=\"text\" />\n    <label>年</label>\n  </span>\n <span class='above-line'>-</span>\n\n <span class='month'>\n    <input type=\"text\" />\n    <label>月</label>\n  </span>\n\n  <span class='above-line'>-</span>\n\n  <span class='day'>\n    <input type=\"text\" />\n    <label>日</label>\n  </span>\n\n </div>",
        view: "<input type=\'text\' disabled><i class=\'fa fa-angle-right\'></i>",
        edit: "<%= Formbuilder.templates['edit/include_time']() %>",
        addButton: "日期"
    });

}).call(this);

(function() {
    return;
    Formbuilder.registerField('time', {
        order: 12,
        //view: "<div class='input-line'>\n  <span class='hours'>\n    <input type=\"text\" />\n    <label>小时</label>\n  </span>\n\n  <span class='above-line'>:</span>\n\n  <span class='minutes'>\n    <input type=\"text\" />\n    <label>分钟</label>\n  </span>\n\n  <span class='above-line'>:</span>\n\n  <span class='seconds'>\n    <input type=\"text\" />\n    <label>秒</label>\n  </span>\n\n  <span class='am_pm'>\n    <select>\n      <option>上午</option>\n      <option>下午</option>\n    </select>\n  </span>\n</div>",
        view: "<input type=\'text\' disabled><i class=\'fa fa-angle-right\'></i>",
        edit: "",
        addButton: "时间"
    });

}).call(this);

(function() {
    return;
    Formbuilder.registerField('datetime', {
        order: 14,
        //view: "<div class='input-line'>\n  <span class='hours'>\n    <input type=\"text\" />\n    <label>小时</label>\n  </span>\n\n  <span class='above-line'>:</span>\n\n  <span class='minutes'>\n    <input type=\"text\" />\n    <label>分钟</label>\n  </span>\n\n  <span class='above-line'>:</span>\n\n  <span class='seconds'>\n    <input type=\"text\" />\n    <label>秒</label>\n  </span>\n\n  <span class='am_pm'>\n    <select>\n      <option>上午</option>\n      <option>下午</option>\n    </select>\n  </span>\n</div>",
        view: "<input type=\'text\' disabled><i class=\'fa fa-angle-right\'></i>",
        edit: "",
        addButton: "日期时间"
    });

}).call(this);

(function() {
    Formbuilder.registerField('radio', {
        order: 15,
        //view: "<% for (i in (rf.get(Formbuilder.options.mappings.OPTIONS) || [])) { %>\n  <div>\n    <label class='fb-option'>\n      <input type='radio' <%= rf.get(Formbuilder.options.mappings.OPTIONS)[i].checked && 'checked' %> onclick=\"javascript: return false;\" />\n      <%= rf.get(Formbuilder.options.mappings.OPTIONS)[i].label %>\n    </label>\n  </div>\n<% } %>\n\n<% if (rf.get(Formbuilder.options.mappings.INCLUDE_OTHER)) { %>\n  <div class='other-option'>\n    <label class='fb-option'>\n      <input type='radio' />\n      Other\n    </label>\n\n    <input type='text' />\n  </div>\n<% } %>",
        view: "<input type=\'text\' disabled><i class=\'fa fa-angle-right\'></i>",
        edit: "<%= Formbuilder.templates['edit/options']({ includeOther: true }) %>",
        addButton: "单选",
        defaultAttributes: function(attrs) {
            attrs.field_options.options = [{
                label: "",
                checked: false
            }, {
                label: "",
                checked: false
            }];
            return attrs;
        }
    });

}).call(this);

(function() {
    Formbuilder.registerField('checkboxes', {
        order: 20,
        //view: "<% for (i in (rf.get(Formbuilder.options.mappings.OPTIONS) || [])) { %>\n  <div>\n    <label class='fb-option'>\n      <input type='checkbox' <%= rf.get(Formbuilder.options.mappings.OPTIONS)[i].checked && 'checked' %> onclick=\"javascript: return false;\" />\n      <%= rf.get(Formbuilder.options.mappings.OPTIONS)[i].label %>\n    </label>\n  </div>\n<% } %>\n\n<% if (rf.get(Formbuilder.options.mappings.INCLUDE_OTHER)) { %>\n  <div class='other-option'>\n    <label class='fb-option'>\n      <input type='checkbox' />\n      Other\n    </label>\n\n    <input type='text' />\n  </div>\n<% } %>",
        view: "<input type=\'text\' disabled><i class=\'fa fa-angle-right\'></i>",
        edit: "<%= Formbuilder.templates['edit/options']({ includeOther: true }) %>",
        addButton: "多选",
        defaultAttributes: function(attrs) {
            attrs.field_options.options = [{
                label: "",
                checked: false
            }, {
                label: "",
                checked: false
            }];
            return attrs;
        }
    });

}).call(this);

(function() {
    return; //暂时屏蔽
    Formbuilder.registerField('selectuser', {
        order: 25,
        view: "<input type=\'text\' disabled><i class=\'fa fa-angle-right\'></i>",
        edit: "",
        addButton: "选择用户"
    });

}).call(this);

(function() {
    Formbuilder.registerField('idcard', {
        order: 30,
        view: "<input type=\'text\' placeholder=\'请输入身份证号\' disabled>",
        edit: "",
        addButton: "身份证号"
    });

}).call(this);

(function() {
    return;
    Formbuilder.registerField('address', {
        order: 35,
        view: "<div class='input-line'>\n  <span class='street'>\n    <input type='text' disabled/>\n    <label>详细地址</label>\n  </span>\n</div>\n\n<div class='input-line'>\n  <span class='city'>\n    <input type='text' disabled/>\n    <label>市/县</label>\n  </span>\n\n  <span class='state'>\n    <input type='text' disabled/>\n    <label>省</label>\n  </span>\n</div>\n\n<div class='input-line'>\n  <span class='zip'>\n    <input type='text' disabled/>\n    <label>邮编</label>\n  </span>\n\n  <span class='country'>\n    <select><option> - 选择 - </option></select>\n    <label>国家</label>\n  </span>\n</div>",
        edit: "",
        addButton: "地址"
    });

}).call(this);

(function() {
    Formbuilder.registerField('position', {
        order: 40,
        view: "<input type=\'text\' disabled><i class=\'fa fa-angle-right\'></i>",
        edit: "",
        addButton: "位置"
    });

}).call(this);

(function() {
    Formbuilder.registerField('number', {
        order: 45,
        //view: "<input type='text' placeholder=\'请输入数字\' />\n<% if (units = rf.get(Formbuilder.options.mappings.UNITS)) { %>\n  <%= units %>\n<% } %>",
        view: "<input type='text' placeholder=\'请输入数字\' disabled/>",
        //edit: "<%= Formbuilder.templates['edit/min_max']() %>\n<%= Formbuilder.templates['edit/units']() %>\n<%= Formbuilder.templates['edit/integer_only']() %>",
        edit: "<%= Formbuilder.templates['edit/digits']() %> \n <%= Formbuilder.templates['edit/isThousandth']() %>",
        addButton: "数字"
    });
}).call(this);

(function() {
    Formbuilder.registerField('file', {
        order: 50,
        view: "<input type='text' disabled/>\n<i class=\'fa fa-angle-right\'></i>",
        edit: "",
        addButton: "附件"
    });

}).call(this);

(function() {
    Formbuilder.registerField('detail', {
        order: 55,
        view: "<div class='detail'>\n</div><div class='add-detailele-btn'>增加明细要素</div>",
        edit: "",
        addButton: "明细"
    });

}).call(this);

(function() {
    Formbuilder.registerField('phone', {
        order: 60,
        view: "<input type='text' placeholder=\'请输入电话号码\' disabled/>\n<% if (units = rf.get(Formbuilder.options.mappings.UNITS)) { %>\n  <%= units %>\n<% } %>",
        //edit: "<%= Formbuilder.templates['edit/min_max']() %>\n<%= Formbuilder.templates['edit/units']() %>\n<%= Formbuilder.templates['edit/integer_only']() %>",
        edit: "",
        addButton: "电话"
    });
}).call(this);

(function() {
    return;
    Formbuilder.registerField('video', {
        order: 55,
        view: "<div class='video'>\n <i class=\'fa fa-play-circle-o\'>\n</i> \n </div>",
        edit: "",
        addButton: "视频"
    });

}).call(this);

(function() {
    return;
    Formbuilder.registerField('dropdown', {
        order: 24,
        view: "<select>\n  <% if (rf.get(Formbuilder.options.mappings.INCLUDE_BLANK)) { %>\n    <option value=''></option>\n  <% } %>\n\n  <% for (i in (rf.get(Formbuilder.options.mappings.OPTIONS) || [])) { %>\n    <option <%= rf.get(Formbuilder.options.mappings.OPTIONS)[i].checked && 'selected' %>>\n      <%= rf.get(Formbuilder.options.mappings.OPTIONS)[i].label %>\n    </option>\n  <% } %>\n</select>",
        edit: "<%= Formbuilder.templates['edit/options']({ includeBlank: true }) %>",
        addButton: "下拉选择",
        defaultAttributes: function(attrs) {
            attrs.field_options.options = [{
                label: "",
                checked: false
            }, {
                label: "",
                checked: false
            }];
            attrs.field_options.include_blank_option = false;
            return attrs;
        }
    });

}).call(this);

(function() {
    return;
    Formbuilder.registerField('email', {
        order: 40,
        view: "<input type='text' class='rf-size-<%= rf.get(Formbuilder.options.mappings.SIZE) %>' placeholder=\'请输入邮箱\' disabled/>",
        edit: "",
        addButton: "邮箱"
    });

}).call(this);

(function() {


}).call(this);

(function() {
    return;
    Formbuilder.registerField('section_break', {
        order: 0,
        type: 'non_input',
        view: "<label class='section-name'><%= rf.get(Formbuilder.options.mappings.LABEL) %></label>\n<p><%= rf.get(Formbuilder.options.mappings.DESCRIPTION) %></p>",
        edit: "<div class='fb-edit-section-header'>标题</div>\n<input type='text' data-rv-input='model.<%= Formbuilder.options.mappings.LABEL %>' disabled/>\n<textarea data-rv-input='model.<%= Formbuilder.options.mappings.DESCRIPTION %>'\n  placeholder='提示文字' disabled></textarea>",
        addButton: "<span class='symbol'><span class='fa fa-minus'></span></span> Section Break"
    });

}).call(this);

(function() {
    return;
    Formbuilder.registerField('website', {
        order: 35,
        view: "<input type='text' placeholder='http://' disabled/>",
        edit: "",
        addButton: "网址"
    });

}).call(this);

this["Formbuilder"] = this["Formbuilder"] || {};
this["Formbuilder"]["templates"] = this["Formbuilder"]["templates"] || {};

this["Formbuilder"]["templates"]["edit/base"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p +=
            ((__t = (Formbuilder.templates['edit/base_header']())) == null ? '' : __t) +
            '\n' +
            ((__t = (Formbuilder.templates['edit/common']())) == null ? '' : __t) +
            '\n' +
            ((__t = (Formbuilder.fields[rf.get(Formbuilder.options.mappings.FIELD_TYPE)].edit({
                rf: rf
            }))) == null ? '' : __t) +
            '\n';

    }
    return __p
};


this["Formbuilder"]["templates"]["edit/base_header"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class=\'form-field-box form-text clearfix\'>\n ' +
            //' <span data-rv-text="model.' +
            //((__t = ( Formbuilder.options.mappings.LABEL )) == null ? '' : __t) +
            //'"></span>\n '+
        	'<label>要素类型：</label>'+
            '<span class=\'field-type disabled\' data-rv-text=\'model.' +
            ((__t = (Formbuilder.options.mappings.FIELD_NAME)) == null ? '' : __t) +
            '\'></span>\n </div>';
        /*
         * */

    }
    return __p
};

this["Formbuilder"]["templates"]["edit/base_non_input"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p +=
            ((__t = (Formbuilder.templates['edit/base_header']())) == null ? '' : __t) +
            '\n' +
            ((__t = (Formbuilder.fields[rf.get(Formbuilder.options.mappings.FIELD_TYPE)].edit({
                rf: rf
            }))) == null ? '' : __t) +
            '\n';

    }
    return __p
};

this["Formbuilder"]["templates"]["edit/checkboxes"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div id=\'isMustLabel\' class=\'form-field-box form-check clearfix\'>\n'+
        	'<label>必填</label>'+
        	'<span class=\'my-checkbox\'><input class=\'field-checkbox js-switch\' type=\'checkbox\' data-rv-checked=\'model.' +
            ((__t = (Formbuilder.options.mappings.REQUIRED)) == null ? '' : __t) +
            '\' /><span class=\'text\'></span></span></div>' +
            //添加要素属性，第三处
            '<div class=\'form-field-box form-check clearfix\'><label>审批时可修改</label><span class=\'my-checkbox\'><input class=\'field-checkbox js-switch\' type=\'checkbox\' data-rv-checked=\'model.' + ((__t = (Formbuilder.options.mappings.ISPROVEEDIT)) == null ? '' : __t) + '\' /><span class=\'text\'></span></span></div>' +
            '<div class=\'form-field-box form-check clearfix\' id=\'isShowLabel\'><label>显示到列表内容区域</label><span class=\'my-checkbox\'><input class=\'field-checkbox js-switch\' type=\'checkbox\' data-rv-checked=\'model.' + ((__t = (Formbuilder.options.mappings.ISSHOW)) == null ? '' : __t) + '\' /><span class=\'text\'></span></span></div>' +
            '<div class=\'form-field-box form-check clearfix\' id=\'isTitleLabel\'><label>显示到列表标题区域</label><span class=\'my-checkbox\'><input class=\'field-checkbox js-switch\' type=\'checkbox\' data-rv-checked=\'model.' + ((__t = (Formbuilder.options.mappings.ISTITLE)) == null ? '' : __t) + '\' /><span class=\'text\'></span></span></div>' +
            '<div class=\'form-field-box form-check clearfix hidden\'><label>是否关联任务字段</label><span class=\'my-checkbox\'><input class=\'field-checkbox js-switch\' type=\'checkbox\' data-rv-checked=\'model.' + ((__t = (Formbuilder.options.mappings.ISCONNECTIONFIELD)) == null ? '' : __t) + '\' /><span class=\'text\'></span></span></div>' +
            '\n<!-- <label>\n  <input type=\'checkbox\' data-rv-checked=\'model.' +
            ((__t = (Formbuilder.options.mappings.ADMIN_ONLY)) == null ? '' : __t) +
            '\' />\n  Admin only\n</label -->';

    }
    return __p
};

//显示到列表选项
this["Formbuilder"]["templates"]["edit/show_in_list"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<label>\n  <input type=\'checkbox\' data-rv-checked=\'model.' +
            ((__t = (Formbuilder.options.mappings.INCLUDE_TIME)) == null ? '' : __t) +
            '\' />\n  包含时间\n</label>\n';
    }
    return __p
};

/*
 * 修改前，留作备用
this["Formbuilder"]["templates"]["edit/common"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class=\'fb-edit-section-header\'>标题</div>\n\n'+
        '<div class=\'fb-common-wrapper\'>\n  <div class=\'fb-label-description\'>\n    ' +
            ((__t = (Formbuilder.templates['edit/label_description']())) == null ? '' : __t) +
            '\n  </div>\n  <div class=\'fb-common-checkboxes\'>\n    ' +
            ((__t = (Formbuilder.templates['edit/checkboxes']())) == null ? '' : __t) +
            '\n  </div>\n  <div class=\'fb-clear\'></div>\n</div>\n';

    }
    return __p
};
 * */
this["Formbuilder"]["templates"]["edit/common"] = function(obj) {
	obj || (obj = {});
	var __t, __p = '',
	__e = _.escape;
	with(obj) {
		__p += '<div class=\'form-field-box form-text clearfix\'>\n    ' +
		'<label>标题：</label>'+
		((__t = (Formbuilder.templates['edit/label_description']())) == null ? '' : __t) +
		'\n  </div>\n <div class=\'form-field-box seperator\'></div>' +
		((__t = (Formbuilder.templates['edit/checkboxes']())) == null ? '' : __t) +
		'\n';
		
	}
	return __p
};



//屏蔽
this["Formbuilder"]["templates"]["edit/integer_only"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        /*
        __p += '<div class=\'fb-edit-section-header\'>仅整数</div>\n<label>\n  <input type=\'checkbox\' data-rv-checked=\'model.' +
        ((__t = ( Formbuilder.options.mappings.INTEGER_ONLY )) == null ? '' : __t) +
        '\' />\n  只允许整数\n</label>\n';
         * */

    }
    return __p
};

//包含时间
this["Formbuilder"]["templates"]["edit/include_time"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class="form-field-box seperator"></div>' +
        	'<div class=\'form-field-box form-check clearfix\'><label>包含时间</label>\n<span class=\'my-checkbox\'><input class=\'field-checkbox\' type=\'checkbox\' data-rv-checked=\'model.' +
            ((__t = (Formbuilder.options.mappings.INCLUDE_TIME)) == null ? '' : __t) +
            '\' /><span class=\'text\'></span></span>\n</div>';
    }
    return __p
};

this["Formbuilder"]["templates"]["edit/label_description"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<input type=\'text\' maxlength=\'10\' data-rv-input=\'model.' +
            ((__t = (Formbuilder.options.mappings.LABEL)) == null ? '' : __t) +
            '\' />\n<textarea class=\'hidden\' data-rv-input=\'model.' +
            ((__t = (Formbuilder.options.mappings.DESCRIPTION)) == null ? '' : __t) +
            '\'\n  placeholder=\'描述文字\'></textarea>';

    }
    return __p
};

this["Formbuilder"]["templates"]["edit/min_max"] = function(obj) {
    return;
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class=\'fb-edit-section-header\'>最小 / 最大</div>\n\n最小\n<input type="text" data-rv-input="model.' +
            ((__t = (Formbuilder.options.mappings.MIN)) == null ? '' : __t) +
            '" style="width: 30px" />\n\n&nbsp;&nbsp;\n\n最大\n<input type="text" data-rv-input="model.' +
            ((__t = (Formbuilder.options.mappings.MAX)) == null ? '' : __t) +
            '" style="width: 30px" />\n';

    }
    return __p
};

this["Formbuilder"]["templates"]["edit/min_max_length"] = function(obj) {
    return;
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class=\'fb-edit-section-header\'>长度范围</div>\n\n最小\n<input type="text" data-rv-input="model.' +
            ((__t = (Formbuilder.options.mappings.MINLENGTH)) == null ? '' : __t) +
            '" style="width: 30px" />\n\n&nbsp;&nbsp;\n\n最大\n<input type="text" data-rv-input="model.' +
            ((__t = (Formbuilder.options.mappings.MAXLENGTH)) == null ? '' : __t) +
            '" style="width: 30px" />\n\n&nbsp;&nbsp;\n\n<select style=\'display: none;\' data-rv-value="model.' +
            ((__t = (Formbuilder.options.mappings.LENGTH_UNITS)) == null ? '' : __t) +
            '" style="width: auto;">\n  <option value="characters">characters</option>\n  <option value="words">words</option>\n</select>\n';

    }
    return __p
};

this["Formbuilder"]["templates"]["edit/options"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape,
        __j = Array.prototype.join;

    function print() {
        __p += __j.call(arguments, '')
    }
    with(obj) {
        __p += '<div class="form-field-box seperator"></div><div class=\'form-field-box form-option-edit clearfix\'>'+
        	'<div class=\'top clearfix\'><label>选项</label>\n</div>\n';
        if (typeof includeBlank !== 'undefined') {;
            __p += '\n  <label>\n    <input type=\'checkbox\' data-rv-checked=\'model.' +
                ((__t = (Formbuilder.options.mappings.INCLUDE_BLANK)) == null ? '' : __t) +
                '\' />\n    包含空行\n  </label>\n';
        };
        __p += '\n<div class=\'content\'>\n'+
        	'<div class=\'form-field-box form-option clearfix\' data-rv-each-option=\'model.' +
            ((__t = (Formbuilder.options.mappings.OPTIONS)) == null ? '' : __t) +
            '\'>\n  <span class=\'my-checkbox\'><input type="checkbox" class=\'option-check js-default-updated\' data-rv-checked="option:checked" />\n<span class=\'text\'></span></span>  <input type="text" data-rv-input="option:label" class=\'option-text option-label-input\' />\n '+
            '<div class=\'option-btn\'><button class="js-add-option ' +
            ((__t = (Formbuilder.options.BUTTON_CLASS)) == null ? '' : __t) +
            '" title="增加选项"><img src="basic/img/iconfont-plus-orange.png" alt="加"></button>\n  <button class="js-remove-option ' +
            ((__t = (Formbuilder.options.BUTTON_CLASS)) == null ? '' : __t) +
            '" title="删除选项"><img src="basic/img/iconfont-minus-orange.png" alt="减"></button>\n</div>\n</div>\n</div>\n</div>'+
            '<div class="form-field-box seperator"></div>';
        if (typeof includeOther !== 'undefined') {
            //屏蔽”包含‘其他’“功能
            /*
            __p += '\n  <label>\n    <input type=\'checkbox\' data-rv-checked=\'model.' +
            ((__t = ( Formbuilder.options.mappings.INCLUDE_OTHER )) == null ? '' : __t) +
            '\' />\n    包含“其他”\n  </label>\n';
             * */
        };
        //屏蔽增加选项功能
        /*
        __p += '\n\n<div class=\'fb-bottom-add\'>\n  <a class="js-add-option ' +
        ((__t = ( Formbuilder.options.BUTTON_CLASS )) == null ? '' : __t) +
        '">增加选项</a>\n</div>\n';
         * */

    }
    return __p
};

this["Formbuilder"]["templates"]["edit/size"] = function(obj) {
    return;
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class=\'fb-edit-section-header\'>尺寸</div>\n<select data-rv-value="model.' +
            ((__t = (Formbuilder.options.mappings.SIZE)) == null ? '' : __t) +
            '">\n  <option value="small">小</option>\n  <option value="medium">中</option>\n  <option value="large">大</option>\n</select>\n';

    }
    return __p
};

this["Formbuilder"]["templates"]["edit/units"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class=\'fb-edit-section-header\'>单位</div>\n<input type="text" data-rv-input="model.' +
            ((__t = (Formbuilder.options.mappings.UNITS)) == null ? '' : __t) +
            '" />\n';

    }
    return __p
};

//小数位数
this["Formbuilder"]["templates"]["edit/digits"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class="form-field-box seperator"></div>'+
        	'<div class=\'form-field-box form-text clearfix\'><label>小数位数</label>\n<input id=\'digitsVal\' type="text" data-rv-input="model.' +
            ((__t = (Formbuilder.options.mappings.DIGITS)) == null ? '' : __t) +
            '" /></div>\n';

    }
    return __p
};

//是否千分位格式化
this["Formbuilder"]["templates"]["edit/isThousandth"] = function(obj) {
	obj || (obj = {});
	var __t, __p = '', __e = _.escape;
	with (obj) {
		__p += '<div class=\'fb-edit-section-header\'>千分位</div>\n<label>\n  <input type=\'checkbox\' data-rv-checked=\'model.' +
		((__t = ( Formbuilder.options.mappings.ISTHOUSANDTH )) == null ? '' : __t) +
		'\' />\n  千分位格式化\n</label>\n';
		
	}
	return __p
};

this["Formbuilder"]["templates"]["page"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p +=
            ((__t = (Formbuilder.templates['partials/save_button']())) == null ? '' : __t) +
            '\n' +
            //((__t = ( Formbuilder.templates['partials/left_side']() )) == null ? '' : __t) +
            //'\n' +
            ((__t = (Formbuilder.templates['partials/right_side']())) == null ? '' : __t) +
            //'\n '+
            //((__t = ( Formbuilder.templates['partials/right_side_2']() )) == null ? '' : __t) +
            '\n<div class=\'fb-clear\'></div>';

    }
    return __p
};

//要素排序
this["Formbuilder"]["templates"]["partials/sort_field"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class=\'fb-tab-pane no-padding active\' id=\'sortField\'>' +
            ((__t = (Formbuilder.templates['partials/left_side']())) == null ? '' : __t) +
            '</div>\n';

    }
    return __p
};

//添加要素
this["Formbuilder"]["templates"]["partials/add_field"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape,
        __j = Array.prototype.join;

    function print() {
        __p += __j.call(arguments, '')
    }
    with(obj) {
        __p += '<div class=\'fb-tab-pane\' id=\'addField\'>\n  <div class=\'fb-add-field-types\'>\n    <div class=\'section\'>\n      ';
        _.each(_.sortBy(Formbuilder.inputFields, 'order'), function(f) {;
            __p += '\n        <a data-field-type="' +
                ((__t = (f.field_type)) == null ? '' : __t) +
                '" class="' +
                ((__t = (Formbuilder.options.BUTTON_CLASS)) == null ? '' : __t) +
                '">\n          ' +
                ((__t = (f.addButton)) == null ? '' : __t) +
                '\n        </a>\n      ';
        });;
        __p += '\n    </div>\n\n    <div class=\'section\'>\n      ';
        _.each(_.sortBy(Formbuilder.nonInputFields, 'order'), function(f) {;
            __p += '\n        <a data-field-type="' +
                ((__t = (f.field_type)) == null ? '' : __t) +
                '" class="' +
                ((__t = (Formbuilder.options.BUTTON_CLASS)) == null ? '' : __t) +
                '">\n          ' +
                ((__t = (f.addButton)) == null ? '' : __t) +
                '\n        </a>\n      ';
        });
        __p += '\n    </div>\n  </div>\n';
        //__p += '<div class="fb-back-fieldsort"><button>返回</button></div>';
        __p += '</div>\n';

    }
    return __p
};

//编辑要素
this["Formbuilder"]["templates"]["partials/edit_field"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class=\'fb-tab-pane no-padding\' id=\'editField\'>\n  <div class=\'fb-edit-field-wrapper common-form\'></div>\n';
        //__p += '<div class="fb-back-fieldsort"><button>返回</button></div>';
        __p += '</div>\n';

    }
    return __p
};
//任务设置
this["Formbuilder"]["templates"]["partials/edit_form"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class=\'fb-tab-pane\' id=\'editForm\'>\n <div class=\'fb-right-2-container\'>\n  <div class=\'approval-setting\'>\n ' +
            '<div class=\'form-field clearfix\'><div class=\'field-label\'>任务说明：</div><textarea class=\'field-val\' id=\'templateDescription\'></textarea></div>' +
            //'<div class=\'form-field clearfix\'><input class=\'field-checkbox\' id=\'templateIsEdit\' type=\'checkbox\'/ ><div class=\'field-label\'>应用直接进入新增</div></div>'+
            //'<div class=\'form-field clearfix\'><input class=\'field-checkbox\' id=\'templateSaveIsEdit\' type=\'checkbox\'/ checked=\'true\'><div class=\'field-label\'>保存后可编辑</div></div>'+
            '<div class=\'form-field clearfix\'><input class=\'field-checkbox js-switch\' id=\'isStartAssign\' type=\'checkbox\'/ ><div class=\'field-label\'>动态选择审批人</div></div>' +
            '<div class=\'form-field clearfix\'><input class=\'field-checkbox js-switch\' id=\'notifyType\' type=\'checkbox\'/ ><div class=\'field-label\'>是否选择传阅人</div></div>' +
            '<div class=\'form-field clearfix\'><div class=\'field-label\'>选择图标：</div><div class=\'icon-box\' id=\'templateIcon\'></div></div>' +
            '<div class=\'form-field clearfix hidden\' id=\'field_relatedTemp\'><div class=\'field-label\'>关联任务：</div><div class=\'search-select\'><input class=\'field-val\' id=\'relateTemplate\' type=\'input\' disabled/></div></div>' + //<span class=\'fa fa-th-list\' data-toggle=\'modal\' data-target=\'#sltTmpModal\'></span>
            '<div class=\'form-field clearfix\' id=\'field_tempUser\'><div class=\'field-label no-float\'>任务使用人：</div><div id=\'tpltPermPerson\' class=\'person-list without-arrow\'><ul class=\'list-style-none-h\'><li class=\'add-item\' id=\'tpltPermSetBtn\' data-toggle=\'modal\' data-target=\'#templateModal\'><div><img src="basic/img/group_member_add.png" alt="增加"></div></li></ul></div></div>' +
            '<div id=\'aproPermPersonDiv\' class=\'form-field clearfix\'><div class=\'field-label no-float\'>审批人：</div><div id=\'aproPermPerson\' class=\'person-list\'><ul class=\'list-style-none-h\'><li class=\'add-item\' id=\'aproPermSetBtn\' data-toggle=\'modal\' data-target=\'#templateModal\'><div><img src="basic/img/group_member_add.png" alt="增加"></div></li></ul></div></div>' +
            //'<div class=\'form-field clearfix\'><div class=\'field-label\'>数据权限：</div><button class=\'btn btn-orange form-val-set\' id=\'dataPermSetBtn\' data-toggle=\'modal\' data-target=\'#templateModal\'>设置</button></div>'+
            //'<div class=\'form-field clearfix\'><div class=\'field-label\'>版本：</div><span id=\'templateVersion\'></span></div>'+
            '</div>\n </div>\n</div>\n';

    }
    return __p
};

//右侧区域（可选要素列表，要素设置）
this["Formbuilder"]["templates"]["partials/right_side"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class=\'fb-right\'>\n ' +
            '<div class=\'fb-right-container\'>\n  <ul class=\'fb-tabs header hidden\'>\n    ' +
            '<li class=\'active\'><a data-target=\'#sortField\'>要素排版</a></li>\n    ' +
            '<li><a data-target=\'#addField\'>要素</a></li>\n    ' +
            '<li><a data-target=\'#editField\'>要素设置</a></li>\n    ' +
            //'<li><a data-target=\'#editForm\'>任务设置</a></li>\n  '+
            '<li class=\'pull-right hidden\'>' +
            '<div class=\'fb-save-wrapper\'>\n  <button id=\'btn-save\' class=\'js-save-form ' +
            ((__t = (Formbuilder.options.BUTTON_CLASS)) == null ? '' : __t) +
            '\'></button>\n' +
            '<button id=\'btn-deploy\' class=\'fb-button deploy\'>发布</button>' +
            '</div>\n' +
            '</li>\n  ' +
            '</ul>\n\n  <div class=\'fb-tab-content\'>\n    ' +
            ((__t = (Formbuilder.templates['partials/sort_field']())) == null ? '' : __t) +
            '\n    ' +
            ((__t = (Formbuilder.templates['partials/add_field']())) == null ? '' : __t) +
            '\n    ' +
            ((__t = (Formbuilder.templates['partials/edit_field']())) == null ? '' : __t) +
            //'\n    ' +
            //((__t = ( Formbuilder.templates['partials/edit_form']() )) == null ? '' : __t) +
            '\n  </div>\n</div>\n</div>\n';

    }
    return __p
};

//左侧要素布局区域（原右侧区域）
this["Formbuilder"]["templates"]["partials/left_side"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class=\'fb-left\'>\n ' +
            //'<div class=\'header\'>\n<input class=\'template-name\' id=\'templateName\' type=\'text\' placeholder=\'请在这里输入任务名称\'>\n </div>\n '+
            '<div class=\'fb-left-container\'>\n   ' +
            '<div class=\'fb-no-response-fields\'>请添加要素</div>\n ' +
            '<div class=\'fb-response-fields\'></div>\n' +
            '<div class=\'fb-addfield\'><img src="basic/img/iconfont-plus-orange.png"></div>' +
            '</div>\n</div>\n';

    }
    return __p
};

//新增右侧区域，用于“审批设置”
this["Formbuilder"]["templates"]["partials/right_side_2"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        /*
        __p += '<div class=\'fb-right-2\'>\n <div class=\'header text-center\'>\n任务设置：</div>\n <div class=\'fb-right-2-container\'>\n  <div class=\'approval-setting\'>\n '+
          '<div class=\'form-field clearfix\'><div class=\'field-label\'>任务说明：</div><textarea class=\'field-val\' id=\'templateDescription\'></textarea></div>' +
          '<div class=\'form-field clearfix\'><div class=\'field-label\'>图标：</div><div class=\'icon-box\' id=\'templateIcon\'></div></div>' +
          '<div class=\'form-field clearfix\'><div class=\'field-label\'>是否可编辑：</div><input id=\'templateIsEdit\' type=\'checkbox\'/ checked=\'true\'></div>'+
          '<div class=\'form-field clearfix\'><div class=\'field-label\'>关联任务：</div><div class=\'search-select\'><input class=\'field-val\' id=\'relateTemplate\' type=\'input\'/><span class=\'fa fa-th-list\' data-toggle=\'modal\' data-target=\'#sltTmpModal\'></span></div></div>'+
          '<div class=\'form-field clearfix\'><div class=\'field-label\'>任务权限：</div><button class=\'btn btn-orange form-val-set\' id=\'tpltPermSetBtn\' data-toggle=\'modal\' data-target=\'#templateModal\'>设置</button></div>'+
          '<div class=\'form-field clearfix\'><div class=\'field-label\'>审批权限：</div><button class=\'btn btn-orange form-val-set\' id=\'aproPermSetBtn\' data-toggle=\'modal\' data-target=\'#templateModal\'>设置</button></div>'+
          '<div class=\'form-field clearfix\'><div class=\'field-label\'>数据权限：</div><button class=\'btn btn-orange form-val-set\' id=\'dataPermSetBtn\' data-toggle=\'modal\' data-target=\'#templateModal\'>设置</button></div>'+
          '<div class=\'form-field clearfix\'><div class=\'field-label\'>版本：</div><span id=\'templateVersion\'></span></div>'+
          '</div>\n <div class=\'fb-save-wrapper\'>\n  <button class=\'js-save-form ' +
          ((__t = ( Formbuilder.options.BUTTON_CLASS )) == null ? '' : __t) +
          '\'></button>\n'+
          '<button id=\'btn-deploy\' class=\'fb-button deploy\'>发布</button>'
          '</div>\n </div>\n</div>\n';
         * */
    }
    return __p
};

//保存按钮移至右下方
this["Formbuilder"]["templates"]["partials/save_button"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        /*
        __p += '<div class=\'fb-save-wrapper\'>\n  <button class=\'js-save-form ' +
        ((__t = ( Formbuilder.options.BUTTON_CLASS )) == null ? '' : __t) +
        '\'></button>\n</div>';
         * */
    }
    return __p
};

this["Formbuilder"]["templates"]["view/base"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class=\'subtemplate-wrapper clearfix\'>\n  <div class=\'cover hidden\'></div>\n  ' +
            ((__t = (Formbuilder.templates['view/label']({
                rf: rf
            }))) == null ? '' : __t) +
            '\n\n  ' +
            ((__t = (Formbuilder.fields[rf.get(Formbuilder.options.mappings.FIELD_TYPE)].view({
                rf: rf
            }))) == null ? '' : __t) +
            '\n\n  ' +
            ((__t = (Formbuilder.templates['view/description']({
                rf: rf
            }))) == null ? '' : __t) +
            '\n  ' +
            //((__t = ( Formbuilder.templates['view/duplicate_remove']({rf: rf}) )) == null ? '' : __t) +
            '\n  ' +
            ((__t = (Formbuilder.templates['view/field_control_btn']({
                rf: rf
            }))) == null ? '' : __t) +
            '<div class=\'field-control hidden\'>' +
            '<ul class=\'list-style-none-h\'>' +
            '<li class=\'fc-up\'>上移</li><li class=\'fc-down\'>下移</li><li class=\'fc-del\'>删除</li><li class=\'fc-setting\'>设置</li>' +
            '</ul>'
        '</div>' +
        '\n</div>\n';

    }
    return __p
};

this["Formbuilder"]["templates"]["view/base_non_input"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '';

    }
    return __p
};

this["Formbuilder"]["templates"]["view/description"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<span class=\'help-block\'>\n  ' +
            ((__t = (Formbuilder.helpers.simple_format(rf.get(Formbuilder.options.mappings.DESCRIPTION)))) == null ? '' : __t) +
            '\n</span>\n';

    }
    return __p
};

this["Formbuilder"]["templates"]["view/duplicate_remove"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<div class=\'actions-wrapper\'>\n  <a class="js-duplicate ' +
            ((__t = (Formbuilder.options.BUTTON_CLASS)) == null ? '' : __t) +
            '" title="复制"><i class=\'fa fa-plus-circle\'></i></a>\n  <a class="js-clear ' +
            ((__t = (Formbuilder.options.BUTTON_CLASS)) == null ? '' : __t) +
            '" title="删除"><i class=\'fa fa-minus-circle\'></i></a>\n</div>';

    }
    return __p
};

//控制弹出字段控制栏的按钮
this["Formbuilder"]["templates"]["view/field_control_btn"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape;
    with(obj) {
        __p += '<button class=\'field-control-btn\'><img class=\'orange\' src="basic/img/iconfont-gengduo-orange.png"><img class=\'grey\' src="basic/img/iconfont-gengduo-grey.png"></button>';

    }
    return __p
};

this["Formbuilder"]["templates"]["view/label"] = function(obj) {
    obj || (obj = {});
    var __t, __p = '',
        __e = _.escape,
        __j = Array.prototype.join;

    function print() {
        __p += __j.call(arguments, '')
    }
    with(obj) {
        __p += '<label>\n  <span>' +
            ((__t = (Formbuilder.helpers.simple_format(rf.get(Formbuilder.options.mappings.LABEL)))) == null ? '' : __t + '：') +
            '\n  ';
        if (rf.get(Formbuilder.options.mappings.REQUIRED)) {;
            __p += '\n    <abbr class=\'hidden\' title=\'required\'>*</abbr>\n  ';
        };
        __p += '\n</label>\n';

    }
    return __p
};

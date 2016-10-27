package com.xplatform.base.form.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.xplatform.base.form.dao.AppFormFieldDao;
import com.xplatform.base.form.entity.AppFormField;
import com.xplatform.base.form.entity.AppFormTable;
import com.xplatform.base.form.service.AppFormFieldService;
import com.xplatform.base.form.service.AppFormTableService;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.service.CommonService;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.PinyinUtil;
import com.xplatform.base.platform.common.def.BusinessConst;

@Service("appFormFieldService")
public class AppFormFieldServiceImpl extends BaseServiceImpl<AppFormField> implements AppFormFieldService {

	@Resource
	private AppFormFieldDao appFormFieldDao;
	@Resource
	private CommonService commonService;
	@Resource
	private AppFormTableService appFormTableService;

	@Resource
	public void setBaseDao(AppFormFieldDao appFormFieldDao) {
		super.setBaseDao(appFormFieldDao);
	}

	@Override
	public void saveAppFormField(JSONObject jsonObject,String code,String mainFlowCode) throws Exception {
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("fields"));
		if (jsonArray.size() > 0) {
			
			String mainTableName = "t_auto_"+code;
			//通过主表的tableName得到主表的AppFormTable对象
			AppFormTable mainTable = this.appFormTableService.queryAppFormTable(mainTableName);
			//判断是否首次发布
			boolean isFirstDeploy = true;  //设定为首次发布

			String isAddress = "";
			//存放明细表的parentId
			String detailParentId = "";
			String detailIsProveEdit = "";
			//所有的字表的code
			String allSonTableCode = "";
			//所有的字表的code
			String allSonTableName = "";
			String sonCode = "";
			//存放新的子表的名字（倒序排列）
			Map<String,String> map = new HashMap<String,String>(); 
			//存放数据库中子表的表名
			Map<String,String> allSonTableMap = new HashMap<String,String>(); 
			//存放数据库中子表的列名
			Map<String,String> allFieldMap = new HashMap<String,String>(); 

			// 获取formId里上个版本正在使用的 控件
			String tableId = "";
			if(mainTable != null){
				tableId = mainTable.getId();
				isFirstDeploy = false;
			}
			// 获取formId里的所有使用或者未使用的控件
			List<AppFormField> allAFFList = this.getAllAFFList(code);
			if(allAFFList.size()>0){
				for(AppFormField appFF : allAFFList){
					allFieldMap.put(appFF.getCode(), appFF.getCode());
				}
			}
			List<AppFormField> lastAFFList = this.getAFFListByTableId(code,tableId);
			// 用于保存当前正要使用的控件
			List<AppFormField> newAFFList = new ArrayList<AppFormField>();
			// 用于存放上次版本的控件里当前版本用不到的控件
			List<AppFormField> noUpdate = new ArrayList<AppFormField>();
			//子表存
			List<List> sonFieldList = new ArrayList<List>();
			//用于存放所有新增或者修改的明细的table对象
			List<AppFormTable> newSonTableObj = new ArrayList<AppFormTable>();
			
			//加入有明细，则用来保存明细数组
			List<JSONArray> detailList = new ArrayList<JSONArray>();
			
			//--------------------------------------强哥你需要的三个AppFormTableList-----------------------------------------------------------
			//获取已经存在并且上次正在使用的子表
			List<AppFormTable> lastFormTable = this.appFormTableService.getIsUseAFTList(mainTableName);
			//存放本次正在更新的子表list
			List<AppFormTable> newFormTableList = new ArrayList<AppFormTable>();
			//存放本次与上次正在使用的比较，得出没有更新的子表的list
			List<AppFormTable> UpateFormTableList = new ArrayList<AppFormTable>();
			
			//存放本次正在更新的子表list
			List<AppFormTable> thisNewFormTable = new ArrayList<AppFormTable>();
			//存放本次与上次正在使用的比较，得出没有更新的子表的list
			List<AppFormTable> thisUpdateTable = new ArrayList<AppFormTable>();
			//--------------------------------------到此结束-----------------------------------------------------------

			if (lastAFFList.size() > 0) {
				// field是修改逻辑，也就是说formId对应之前的数据库中存有数据
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject affJson = (JSONObject) jsonArray.get(i);
					if (affJson.getString("field_type").equals("address")) {
						isAddress = affJson.getString("cid");
						List<AppFormField> myAddress = this.getAddress(code, isAddress);
						if (myAddress.size() == 3) {
							for (AppFormField address : myAddress) {
								if(affJson.getBoolean("isShow")){
									address.setIsShow(1);
								}else{
									address.setIsShow(0);
								}
								if(affJson.getBoolean("isTitle")){
									address.setIsTitle(1);
								}else{
									address.setIsTitle(0);
								}
								if(affJson.getBoolean("isProveEdit")){
									address.setIsProveEdit(1);
								}else{
									address.setIsProveEdit(0);
								}
								if(affJson.getBoolean("required")){
									address.setNotNull(1);
								}else{
									address.setNotNull(0);
								}
								newAFFList.add(address);
							}
						} else {
							String str[] = { "省份", "市县", "详细地址" };
							for (int j = 0; j < str.length; j++) {
								AppFormField appFFAdd = new AppFormField();
								appFFAdd.setName(str[j]);

								String isTrueCode = "";
								if("省份".equals(str[j])){
									isTrueCode = "province";
								}else if("市县".equals(str[j])){
									isTrueCode = "city";
								}else{
									isTrueCode = "detailAddress";
								}
								
								//设置地址控件的基本
								appFFAdd = setAddressAttr(appFFAdd,allFieldMap,affJson,code,isTrueCode);
								newAFFList.add(appFFAdd);

							}
						}
					} else if (affJson.getString("field_type").equals("detail")) {
						String sonTabbleCode = PinyinUtil.converterToFirstSpell(affJson.getString("label"));
						sonTabbleCode = "t_auto_" + code + "_" + sonTabbleCode;
						String parentId = affJson.getString("cid");
						List<AppFormTable> isExistTable = this.appFormTableService.queryAppFormTableList(mainTableName);
						//将所有存在的字表存入sonTableMap
						for(AppFormTable sonTable : isExistTable){
							allSonTableMap.put(sonTable.getTableName(), sonTable.getTableName());
						}
						//根据parentId和formId判断明细是否存在
						AppFormTable isExistMX = this.appFormTableService.getAppFTByCid(code,parentId);
						if(isExistMX != null){
							//当前明细存在
							map.put(isExistMX.getTableName(), isExistMX.getTableName());
							if(allSonTableName == ""){
								allSonTableName = isExistMX.getTableName();
							}else{
								allSonTableName = allSonTableName + "," + isExistMX.getTableName();
							}
							if(allSonTableCode == ""){
								allSonTableCode = isExistMX.getTableName();
							}else{
								allSonTableCode = allSonTableCode+","+isExistMX.getTableName();
							}
							//sonTableMap.put(isExistMX.getTableName(), isExistMX.getTableName());
						} else {
							// 判断字表的code是不是一样
							for (int flag = 0; flag < 1; flag++) {
								if (allSonTableMap.containsKey(sonTabbleCode)) {
									String exist = allSonTableMap.get(sonTabbleCode);
									String number = exist.replaceAll("\\D+", "");
									if (StringUtil.isNotEmpty(number)) {
										int newNumner = Integer.parseInt(number) + 1;
										sonTabbleCode = sonTabbleCode.replaceAll("\\d+", "");
										sonTabbleCode = sonTabbleCode + newNumner;
										flag = -1;
									} else {
										sonTabbleCode = sonTabbleCode + "1";
										flag = -1;
									}

								} else {
									allSonTableMap.put(sonTabbleCode, sonTabbleCode);
									map.put(sonTabbleCode, sonTabbleCode);
									if(allSonTableName == ""){
										allSonTableName = sonTabbleCode;
									}else{
										allSonTableName = allSonTableName + "," +sonTabbleCode;
									}
									if(allSonTableCode == ""){
										allSonTableCode = sonTabbleCode;
									}else{
										allSonTableCode = allSonTableCode+","+sonTabbleCode;
									}
								}
							}
						}
						detailList.add(JSONArray.fromObject(affJson.get("fields")));
						if (detailParentId == "") {
							detailParentId = affJson.getString("cid");
							// sonCode =
						} else {
							detailParentId = detailParentId + "," + affJson.getString("cid");
						}
						if(affJson.getBoolean("isProveEdit")){
							if(detailIsProveEdit == ""){
								detailIsProveEdit = "1";
							}else{
								detailIsProveEdit = detailIsProveEdit+","+"1";
							}
						}else{
							if(detailIsProveEdit == ""){
								detailIsProveEdit = "0";
							}else{
								detailIsProveEdit = detailIsProveEdit+","+"0";
							}
						}
					} else {
						AppFormField appFormField = new AppFormField();
						//设置基本属性
						appFormField = setBasicAttr(appFormField,affJson,code);
						// appFormField.setCode(PinyinUtil.converterToFirstSpell(affJson.getString("label")));
						
						AppFormField oldEntity = this.getAppFF(code, appFormField.getMyCid());
						if (oldEntity != null) {
							MyBeanUtils.copyBeanNotNull2Bean(appFormField, oldEntity);
							newAFFList.add(oldEntity);
						} else {
							//判断code是否是唯一的
							String label = "";
							if(StringUtil.isEmpty(affJson.optString("label"))){
								label = "控件";
							}else{
								label = affJson.getString("label").replaceAll(" ", "");
							}
							label = label.replaceAll("\\pP|\\pS", "");
							String isTrueCode =PinyinUtil.converterToFirstSpell(label);
							
							for (int flag = 0; flag < 1; flag++) {
								if (allFieldMap.containsKey(isTrueCode)) {
									String exist = allFieldMap.get(isTrueCode);
									String number = exist.replaceAll("\\D+", "");
									if (StringUtil.isNotEmpty(number)) {
										int newNumner = Integer.parseInt(number) + 1;
										isTrueCode = isTrueCode.replaceAll("\\d+", "");
										isTrueCode = isTrueCode + newNumner;
										flag = -1;
									} else {
										isTrueCode = isTrueCode + "1";
										flag = -1;
									}
									//sonTabbleCode = sonTabbleCode + "1";
									//map.put(sonTabbleCode, sonTabbleCode);
								} else {
									allFieldMap.put(isTrueCode, isTrueCode);
								}
							}
							
							if(appFormField.getIsConnectionField() == 0){
								appFormField.setCode(isTrueCode);
							}else{
								AppFormField oldEntity1 = this.getAppFF(mainFlowCode, appFormField.getMyCid());
								if(oldEntity1 != null){
									appFormField.setCode(oldEntity1.getCode());
									appFormField.setFormCode(code);
								}
							}
							newAFFList.add(appFormField);
						}

					}
				}
				// 判断控件数组里是否含有地址控件
				/*
				 * if (StringUtil.isNotEmpty(isAddress)) { // 如果含有地址控件 String
				 * str[] = { "省份", "市县", "详细地址" }; for (int i = 0; i <
				 * str.length; i++) { AppFormField appFormField = new
				 * AppFormField(); appFormField.setName(str[i]);
				 * appFormField.setCode
				 * (PinyinUtil.converterToFirstSpell(str[i]));
				 * appFormField.setType("String"); appFormField.setLength(300);
				 * 
				 * // appFormField.setTableId(""); 暂时不写
				 * appFormField.setParentId(isAddress);
				 * appFormField.setFormId(formId); appFormField.setIsOverdue(1);
				 * // 暂时写死 appFormField.setIsShow(1); // 暂时写死
				 * appFormField.setNotNull(0); newAFFList.add(appFormField); } }
				 */

				// 判断明细是否存在，如果存在则将明细里边的field解析出来存入newAFFList
				if (StringUtil.isNotEmpty(detailParentId)) {
					String str[] = detailParentId.split(",");
					String isProveEditStr[] = detailIsProveEdit.split(",");
					String allSonTableCodeStr[] = allSonTableCode.split(",");
					for (int i = 0; i < str.length; i++) {
						List<AppFormField> sonTableList = new ArrayList<AppFormField>();
						for (int j = 0; j < detailList.get(i).size(); j++) {
							JSONObject affJson = (JSONObject) detailList.get(i).get(j);
							// 判断控件数组里是否含有地址控件
							if (affJson.getString("field_type").equals("address")) {
								isAddress = affJson.getString("cid");
								List<AppFormField> myAddress = this.getAddress(code, isAddress);
								if (myAddress.size() == 3) {
									for (AppFormField address : myAddress) {
										if(affJson.getBoolean("isShow")){
											address.setIsShow(1);
										}else{
											address.setIsShow(0);
										}
										if(affJson.getBoolean("isTitle")){
											address.setIsTitle(1);
										}else{
											address.setIsTitle(0);
										}
										if("1".equals(isProveEditStr[i])){
											if(affJson.getBoolean("isProveEdit")){
												address.setIsProveEdit(1);
											}else{
												address.setIsProveEdit(0);
											}
										}else{
											address.setIsProveEdit(0);
										}
										
										if(affJson.getBoolean("required")){
											address.setNotNull(1);
										}else{
											address.setNotNull(0);
										}
										sonTableList.add(address);
									}
								} else {
									String strAdd[] = { "省份", "市县", "详细地址" };
									for (int k = 0; k < strAdd.length; k++) {
										AppFormField appFFAdd = new AppFormField();
										appFFAdd.setName(strAdd[k]);

										String isTrueCode = "";
										if("省份".equals(strAdd[k])){
											isTrueCode = "province";
										}else if("市县".equals(strAdd[k])){
											isTrueCode = "city";
										}else{
											isTrueCode = "detailAddress";
										}
										
										//设置地址控件的基本
										appFFAdd = setAddressAttr(appFFAdd,allFieldMap,affJson,code,isTrueCode);
										if("1".equals(isProveEditStr[i])){
											if(affJson.getBoolean("isProveEdit")){
												appFFAdd.setIsProveEdit(1);
											}else{
												appFFAdd.setIsProveEdit(0);
											}
										}else{
											appFFAdd.setIsProveEdit(0);
										}
										sonTableList.add(appFFAdd);
									}
								}
							} else {
								AppFormField appFormField = new AppFormField();
								appFormField.setTableName(allSonTableCodeStr[i]);
								//设置field基本属性
								appFormField = setBasicAttr(appFormField,affJson,code);
								if("1".equals(isProveEditStr[i])){
									if(affJson.getBoolean("isProveEdit")){
										appFormField.setIsProveEdit(1);
									}else{
										appFormField.setIsProveEdit(0);
									}
								}else{
									appFormField.setIsProveEdit(0);
								}
								AppFormField oldEntity = this.getAppFF(code, appFormField.getMyCid());
								if (oldEntity != null) {
									appFormField.setId(oldEntity.getId());
									MyBeanUtils.copyBeanNotNull2Bean(appFormField, oldEntity);
									sonTableList.add(oldEntity);
								} else {
									//appFormField.setCode(PinyinUtil.converterToFirstSpell(affJson.getString("label")));
									String label = "";
									if(StringUtil.isEmpty(affJson.optString("label"))){
										label = "控件";
									}else{
										label = affJson.getString("label").replaceAll(" ", "");
									}
									label = label.replaceAll("\\pP|\\pS", "");
									String isTrueCode =PinyinUtil.converterToFirstSpell(label);
									
									for (int flag = 0; flag < 1; flag++) {
										if (allFieldMap.containsKey(isTrueCode)) {
											String exist = allFieldMap.get(isTrueCode);
											String number = exist.replaceAll("\\D+", "");
											if (StringUtil.isNotEmpty(number)) {
												int newNumner = Integer.parseInt(number) + 1;
												isTrueCode = isTrueCode.replaceAll("\\d+", "");
												isTrueCode = isTrueCode + newNumner;
												flag = -1;
											} else {
												isTrueCode = isTrueCode + "1";
												flag = -1;
											}
											//sonTabbleCode = sonTabbleCode + "1";
											//map.put(sonTabbleCode, sonTabbleCode);
										} else {
											appFormField.setCode(isTrueCode);
											allFieldMap.put(isTrueCode, isTrueCode);
										}
									}
																
									sonTableList.add(appFormField);
									
								}

							}

						}
						sonFieldList.add(sonTableList);
					}

				}

				boolean flag = false; // 判断是否修改
				for (int i = 0; i < newAFFList.size(); i++) {
					for (int j = 0; j < lastAFFList.size(); j++) {
						if(newAFFList.get(i).getId() != null){
							if (newAFFList.get(i).getId().equals(lastAFFList.get(j).getId())) {
								// userId当前存在，则删除掉isExistedList里边的元素
								flag = true;
								noUpdate.add(lastAFFList.get(j));
								lastAFFList.remove(j);
								if (lastAFFList.size() == 0) {
									break;
								}
							}
						}
						
					}
				}
				// 检查code与数据库中是否有相同，有则让它不同
/*				for (int i = 0; i < newAFFList.size(); i++) {
					for (int j = 0; j < allAFFList.size(); j++) {
						if (allAFFList.get(j).getCode().equals(newAFFList.get(i).getCode())) {
							String number = allAFFList.get(j).getCode().replaceAll("\\D+", "");
							if (StringUtil.isNotEmpty(number)) {
								int newNumner = Integer.parseInt(number) + 1;
								newAFFList.get(i).setCode(newAFFList.get(i).getCode().replaceAll("\\d+", ""));
								newAFFList.get(i).setCode(newAFFList.get(i).getCode() + newNumner);
								j = -1;
							} else {
								newAFFList.get(i).setCode(newAFFList.get(i).getCode() + "1");
								j = -1;
							}

						}
					}
				}*/

				// 检查当前list中有没有相同的code
/*				for (int i = 0; i < newAFFList.size() - 1; i++) { // 最多做n-1趟排序
					for (int j = 0; j < newAFFList.size() - i - 1; j++) {

						if (newAFFList.get(j).getCode().equals(newAFFList.get(j + 1).getCode())) {
							String number = newAFFList.get(j).getCode().replaceAll("\\D+", "");
							if (StringUtil.isNotEmpty(number)) {
								int newNumner = Integer.parseInt(number) + 1;
								newAFFList.get(i).setCode(newAFFList.get(i).getCode() + newNumner);
							} else {
								newAFFList.get(i).setCode(newAFFList.get(i).getCode() + "1");
							}
						}

					}
				}*/

				// 第一步，先删除改动的Field
				if (flag) {
					for (AppFormField aFF : noUpdate) {
						this.appFormFieldDao.delete(aFF);
					}
				}
				// 第二步，将新改动中已不存在的Field状态置为0
				for (AppFormField aFF : lastAFFList) {
					aFF.setIsOverdue(1);
					aFF.setIsShow(0);
					this.appFormFieldDao.updateEntitie(aFF);
				}

				//设置mainTable的字表字段以及类型
				String subTables = "";
				if(StringUtil.isNotEmpty(allSonTableName)){
					String allSonTableNameStr[] = allSonTableName.split(",");
					 for (String key : allSonTableNameStr) {
						 AppFormTable sonTable = this.appFormTableService.queryAppFormTable(key);
						 if(sonTable != null){
							 newFormTableList.add(sonTable);
						 }else{
							 sonTable = new AppFormTable();
							 //sonTable.setFormId(formId);
							 sonTable.setFormCode(code);
							 sonTable.setIsOverdue(0);
							 sonTable.setMainTable(mainTableName);
							 sonTable.setTableType(3);
							 sonTable.setTableName(key);
							 newFormTableList.add(sonTable);
						 }
						 if(subTables == ""){
							 subTables = key;
						 }else{
							 subTables = subTables+","+key;
						 }
					 }
				}
			
				 if(StringUtil.isNotEmpty(subTables)){
				 mainTable.setSubTables(subTables);
				 mainTable.setTableType(2);
				 }
				 //更新mainTable
				 this.appFormTableService.update(mainTable);
				//更新主表field
				for(AppFormField newMainAFF : newAFFList){
					newMainAFF.setTableId(mainTable.getId());
					newMainAFF.setTableName(mainTable.getTableName());
					this.save(newMainAFF);
				}
				//this.appFormFieldDao.batchSave(newAFFList);
				
				//--------------------------------------判断子表是否修改--------------------------------------------------------
				boolean sonflag = false; // 判断是否修改
				for (int i = 0; i < newFormTableList.size(); i++) {
					for (int j = 0; j < lastFormTable.size(); j++) {
						if(newFormTableList.get(i).getId() != null){
							if (newFormTableList.get(i).getId().equals(lastFormTable.get(j).getId())) {
								// userId当前存在，则删除掉isExistedList里边的元素
								sonflag = true;
								UpateFormTableList.add(lastFormTable.get(j));
								lastFormTable.remove(j);
								if (lastFormTable.size() == 0) {
									break;
								}
							}
						}
					}
				}
				
				// 第一步，先删除改动的Field
/*				if (sonflag) {
					for (AppFormTable aFT : UpateFormTableList) {
						this.appFormTableService.delete(aFT);
					}
				}*/

				// 第二步，将新改动中已不存在的AppFormTable状态置为0
				for (AppFormTable aFT : lastFormTable) {
					List<AppFormField> noUpdateSonFormFieldList = this.getAFFListByTableId(code, aFT.getId());
					for(AppFormField noUpdateSonFormField : noUpdateSonFormFieldList){
						noUpdateSonFormField.setIsShow(0);
						noUpdateSonFormField.setIsOverdue(1);
						this.appFormFieldDao.updateEntitie(noUpdateSonFormField);
					}
					aFT.setIsOverdue(1);
					this.appFormTableService.update(aFT);
				}
				//保存子表
				   String sonTableCid[] = detailParentId.split(",");
				   String isProveEditStr[] = detailIsProveEdit.split(",");
				   for(int i = 0;i<newFormTableList.size();i++){
					   AppFormTable sonTable = newFormTableList.get(i);
					   //sonTable.setFormId(formId);
					   sonTable.setFormCode(code);
					   sonTable.setMainTable(mainTableName);
					   sonTable.setTableName(newFormTableList.get(i).getTableName());
					   sonTable.setIsOverdue(0);
					   sonTable.setTableType(3);
					   sonTable.setCId(sonTableCid[i]);
					   sonTable.setIsProveEdit(Integer.parseInt(isProveEditStr[i]));
					   String oldTableId = sonTable.getId();
//					   AppFormTable testSonTable = this.appFormTableService.get(sonTable.getId());
					   if(StringUtil.isNotEmpty(sonTable.getId())){
						   thisUpdateTable.add(sonTable);
						   this.appFormTableService.update(sonTable);
					   }else{
						   thisNewFormTable.add(sonTable);
						   String sonPk = (String)this.appFormTableService.save(sonTable);
					   }
					   newSonTableObj.add(sonTable);
					   List<AppFormField> oneSonList = this.getAFFListByTableId(code, sonTable.getId());
					   List<AppFormField> deleteSonList = new ArrayList<AppFormField>();
					   List<AppFormField> newSonList = new ArrayList<AppFormField>();
					   boolean sonFieldFlag = false;
					   List<AppFormField> oneInSonFieldList = new ArrayList<AppFormField>();
					   for(int j = 0;j<sonFieldList.size();j++){
						   AppFormField appFormField = (AppFormField)sonFieldList.get(j).get(0);
						   if(sonTable.getTableName().equals(appFormField.getTableName())){
							   oneInSonFieldList = sonFieldList.get(j);
						   }
					   }
					   for(int j = 0;j<oneInSonFieldList.size();j++){
						   AppFormField appFormField = oneInSonFieldList.get(j);
						   if(appFormField.getId() != null){
							   for(int l=0;l<oneSonList.size();l++){
								   //如果子表的field里有重复的field
								   if(appFormField.getId().equals(oneSonList.get(l).getId())){
									   sonFieldFlag = true;
									   deleteSonList.add(oneSonList.get(l));
									   oneSonList.remove(l);
									   if (oneSonList.size() == 0) {
											break;
										}
								   }
							   }
						   }
						   appFormField.setTableId(sonTable.getId());
						   appFormField.setTableName(newFormTableList.get(i).getTableName());
						   newSonList.add(appFormField);
						   //this.save(appFormField);
					   }
					   if(sonFieldFlag){
						   for(AppFormField deleteSonAFF : deleteSonList){
							   this.delete(deleteSonAFF);
						   }
					   }
					   for(AppFormField noUpdateSonAFF : oneSonList){
						   noUpdateSonAFF.setIsOverdue(1);
						   noUpdateSonAFF.setIsShow(0);
						   this.update(noUpdateSonAFF);
					   }
					   this.batchSave(newSonList);
				   }
				   
					
				

			}else{
				//field是新增，也就是说formId对应的表里没有数据--------------------------------------------------------------------------------------新增逻辑--------------------------------------------------------------------
				Map<String,String> newFieldCode = new HashMap<String,String>();
				
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject affJson = (JSONObject) jsonArray.get(i);
					if (affJson.getString("field_type").equals("address")) {
						isAddress = affJson.getString("cid");
						String str[] = { "省份", "市县", "详细地址" };
						for (int j = 0; j < str.length; j++) {
							AppFormField appFFAdd = new AppFormField();
							appFFAdd.setName(str[j]);
							
							String isTrueCode = "";
							if("省份".equals(str[j])){
								isTrueCode = "province";
							}else if("市县".equals(str[j])){
								isTrueCode = "city";
							}else{
								isTrueCode = "detailAddress";
							}
							//设置地址控件的基本
							appFFAdd = setAddressAttr(appFFAdd,newFieldCode,affJson,code,isTrueCode);
							newAFFList.add(appFFAdd);
						}
					}/*else if(affJson.getString("field_type").equals("position")){

						isAddress = affJson.getString("cid");
						String str[] = { "地址", "经纬度"};
						for (int j = 0; j < str.length; j++) {
							AppFormField appFFAdd = new AppFormField();
							appFFAdd.setName(str[j]);
							
							String isTrueCode = "";
							if("地址".equals(str[j])){
								isTrueCode = "mapAddress";
							}else if("经纬度".equals(str[j])){
								isTrueCode = "latiAndLongi"; //经纬度
							}
							for (int flag = 0; flag < 1; flag++) {
								if (newFieldCode.containsKey(isTrueCode)) {
									String exist = newFieldCode.get(isTrueCode);
									String number = exist.replaceAll("\\D+", "");
									if (StringUtil.isNotEmpty(number)) {
										int newNumner = Integer.parseInt(number) + 1;
										isTrueCode = isTrueCode.replaceAll("\\d+", "");
										isTrueCode = isTrueCode + newNumner;
										flag = -1;
									} else {
										isTrueCode = isTrueCode + "1";
										flag = -1;
									}
									//sonTabbleCode = sonTabbleCode + "1";
									//map.put(sonTabbleCode, sonTabbleCode);
								} else {
									newFieldCode.put(isTrueCode, isTrueCode);
								}
							}
							//设置地址控件的基本
							appFFAdd.setCode(isTrueCode);
							appFFAdd = setBasicAttr(appFFAdd,affJson,code);
							newAFFList.add(appFFAdd);
						}
					
					} */else if (affJson.getString("field_type").equals("detail")) {
						String sonTabbleCode = PinyinUtil.converterToFirstSpell(affJson.getString("label"));
						sonTabbleCode = "t_auto_" + code + "_" + sonTabbleCode;
						// 判断字表的code是不是一样
						for (int flag = 0; flag < 1; flag++) {
							if (map.containsKey(sonTabbleCode)) {
								String exist = map.get(sonTabbleCode);
								String number = exist.replaceAll("\\D+", "");
								if (StringUtil.isNotEmpty(number)) {
									int newNumner = Integer.parseInt(number) + 1;
									sonTabbleCode = sonTabbleCode + newNumner;
									flag = -1;
								} else {
									sonTabbleCode = sonTabbleCode + "1";
									flag = -1;
								}
								//sonTabbleCode = sonTabbleCode + "1";
								//map.put(sonTabbleCode, sonTabbleCode);
							} else {
								map.put(sonTabbleCode, sonTabbleCode);
								if(allSonTableName == ""){
									allSonTableName = sonTabbleCode;
								}else{
									allSonTableName = allSonTableName + "," +sonTabbleCode;
								}
							}
						}
						detailList.add(JSONArray.fromObject(affJson.get("fields")));
						if (detailParentId == "") {
							detailParentId = affJson.getString("cid");
							// sonCode =
						} else {
							detailParentId = detailParentId + "," + affJson.getString("cid");
						}
						if(affJson.getBoolean("isProveEdit")){
							if(detailIsProveEdit == ""){
								detailIsProveEdit = "1";
							}else{
								detailIsProveEdit = detailIsProveEdit+","+"1";
							}
						}else{
							if(detailIsProveEdit == ""){
								detailIsProveEdit = "0";
							}else{
								detailIsProveEdit = detailIsProveEdit+","+"0";
							}
						}
					} else {
						AppFormField appFormField = new AppFormField();
					
						//设置field基本属性
						appFormField = setBasicAttr(appFormField,affJson,code);
						String label = "";
						if(StringUtil.isEmpty(affJson.optString("label"))){
							label = "控件";
						}else{
							label = affJson.getString("label").replaceAll(" ", "");
						}
						label = label.replaceAll("\\pP|\\pS", "");
						String isTrueCode =PinyinUtil.converterToFirstSpell(label);
						for (int flag = 0; flag < 1; flag++) {
							if (newFieldCode.containsKey(isTrueCode)) {
								String exist = newFieldCode.get(isTrueCode);
								String number = exist.replaceAll("\\D+", "");
								if (StringUtil.isNotEmpty(number)) {
									int newNumner = Integer.parseInt(number) + 1;
									isTrueCode = isTrueCode.replaceAll("\\d+", "");
									isTrueCode = isTrueCode + newNumner;
									flag = -1;
								} else {
									isTrueCode = isTrueCode + "1";
									flag = -1;
								}
								//sonTabbleCode = sonTabbleCode + "1";
								//map.put(sonTabbleCode, sonTabbleCode);
							} else {
								newFieldCode.put(isTrueCode, isTrueCode);
							}
						}
						if(appFormField.getIsConnectionField() == 0){
							appFormField.setCode(isTrueCode);
						}else{
							AppFormField oldEntity1 = this.getAppFF(mainFlowCode, appFormField.getMyCid());
							if(oldEntity1 != null){
								appFormField.setCode(oldEntity1.getCode());
								appFormField.setFormCode(code);
							}
						}
						
						newAFFList.add(appFormField);
						
					}
				}

				// 判断明细是否存在，如果存在则将明细里边的field解析出来存入newAFFList
				if (StringUtil.isNotEmpty(detailParentId)) {
					String str[] = detailParentId.split(",");
					String isProveEditStr[] = detailIsProveEdit.split(",");
					for (int i = 0; i < str.length; i++) {
						Map<String,String> sonTableFieldsMap = new HashMap<String,String>();
						List<AppFormField> sonTable = new ArrayList<AppFormField>();
						for (int j = 0; j < detailList.get(i).size(); j++) {
							JSONObject affJson = (JSONObject) detailList.get(i).get(j);
							// 判断控件数组里是否含有地址控件
							if (affJson.getString("field_type").equals("address")) {
								isAddress = affJson.getString("cid");
								String strAdd[] = { "省份", "市县", "详细地址" };
								for (int k = 0; k < strAdd.length; k++) {
									AppFormField appFFAdd = new AppFormField();
									appFFAdd.setName(strAdd[k]);
									
									String isTrueCode = "";
									if("省份".equals(strAdd[k])){
										isTrueCode = "province";
									}else if("市县".equals(strAdd[k])){
										isTrueCode = "city";
									}else{
										isTrueCode = "detailAddress";
									}
									
									//设置地址控件的基本
									appFFAdd = setAddressAttr(appFFAdd,sonTableFieldsMap,affJson,code,isTrueCode);
									if("1".equals(isProveEditStr[i])){
										if(affJson.getBoolean("isProveEdit")){
											appFFAdd.setIsProveEdit(1);
										}else{
											appFFAdd.setIsProveEdit(0);
										}
									}else{
										appFFAdd.setIsProveEdit(0);
									}
									sonTable.add(appFFAdd);
								}
							} else {
								AppFormField appFormField = new AppFormField();
								
								//设置field基本属性
								appFormField = setBasicAttr(appFormField,affJson,code);
								if("1".equals(isProveEditStr[i])){
									if(affJson.getBoolean("isProveEdit")){
										appFormField.setIsProveEdit(1);
									}else{
										appFormField.setIsProveEdit(0);
									}
								}else{
									appFormField.setIsProveEdit(0);
								}
								String label = "";
								if(StringUtil.isEmpty(affJson.optString("label"))){
									label = "控件";
								}else{
									label = affJson.getString("label").replaceAll(" ", "");
								}
								label = label.replaceAll("\\pP|\\pS", "");
								String isTrueCode =PinyinUtil.converterToFirstSpell(label);
								//String isTrueCode = PinyinUtil.converterToFirstSpell(affJson.getString("label"));
								for (int flag = 0; flag < 1; flag++) {
									if (sonTableFieldsMap.containsKey(isTrueCode)) {
										String exist = sonTableFieldsMap.get(isTrueCode);
										String number = exist.replaceAll("\\D+", "");
										if (StringUtil.isNotEmpty(number)) {
											int newNumner = Integer.parseInt(number) + 1;
											isTrueCode = isTrueCode.replaceAll("\\d+", "");
											isTrueCode = isTrueCode + newNumner;
											flag = -1;
										} else {
											isTrueCode = isTrueCode + "1";
											flag = -1;
										}
										//sonTabbleCode = sonTabbleCode + "1";
										//map.put(sonTabbleCode, sonTabbleCode);
									} else {
										appFormField.setCode(isTrueCode);
										sonTableFieldsMap.put(isTrueCode, isTrueCode);
									}
								}


							sonTable.add(appFormField);
							}

						}
						sonFieldList.add(sonTable);
					}

				}
				
				//保存主表
				String subTables = "";
				AppFormTable mainAppFormTable = new AppFormTable();
				//mainAppFormTable.setFormId(formId);
				mainAppFormTable.setFormCode(code);
				mainAppFormTable.setIsOverdue(0);
				mainAppFormTable.setTableName(mainTableName);
				
				 /*for (String key : map.keySet()) {
					 if(subTables == ""){
						 subTables = map.get(key);
					 }else{
						 subTables = subTables+","+map.get(key);
					 }
				 }*/
			   subTables = allSonTableName;
			   if(StringUtil.isNotEmpty(subTables)){
				   //mainAppFormTable.setMainTable(mainTableName);
				   mainAppFormTable.setTableType(2);
				   mainAppFormTable.setSubTables(subTables);
			   }else{
				   mainAppFormTable.setTableType(1);
			   }
			   String mainPk = (String)this.appFormTableService.save(mainAppFormTable);
			   mainTable = mainAppFormTable;
			   for(AppFormField aFF : newAFFList){
				   aFF.setTableId(mainPk);
				   aFF.setTableName(mainTableName);
				   this.save(aFF);
			   }
			   
			   //保存从表
			   String sonTableName[] = subTables.split(",");
			   String sonTableCid[] = detailParentId.split(",");
			   String isProveEditStr[] = detailIsProveEdit.split(",");
			   for(int i = 0;i<sonFieldList.size();i++){
				   AppFormTable sonTable = new AppFormTable();
				   //sonTable.setFormId(formId);
				   sonTable.setFormCode(code);
				   sonTable.setMainTable(mainTableName);
				   sonTable.setTableName(sonTableName[i]);
				   sonTable.setIsOverdue(0);
				   sonTable.setTableType(3);
				   sonTable.setCId(sonTableCid[i]);
				   sonTable.setIsProveEdit(Integer.parseInt(isProveEditStr[i]));
				   String sonPk = (String)this.appFormTableService.save(sonTable);
				   newSonTableObj.add(sonTable);
				   for(int j = 0;j<sonFieldList.get(i).size();j++){
					   AppFormField appFormField = (AppFormField)sonFieldList.get(i).get(j);
					   appFormField.setTableId(sonPk);
					   appFormField.setTableName(sonTableName[i]);
					   this.save(appFormField);
				   }
			   }
			   
			}
			
			// 生成物理表逻辑
			if (isFirstDeploy) {
				// 新增主表
				appFormTableService.generatePhysicalTable(mainTable, BusinessConst.TableType_main);
				// 新增的从表
				for (AppFormTable table : newSonTableObj) {
					appFormTableService.generatePhysicalTable(table, BusinessConst.TableType_sub);
				}
			} else {
				// 修改主表
				appFormTableService.updatePhysicalTable(mainTable);
				// 新增的从表
				for (AppFormTable table : thisNewFormTable) {
					appFormTableService.generatePhysicalTable(table, BusinessConst.TableType_sub);
				}
				// 修改的从表
				for (AppFormTable table : thisUpdateTable) {
					appFormTableService.updatePhysicalTable(table);
				}
			}
			//this.batchSave(newAFFList);
		}
	}
	
	
	//设置field基本属性
	public AppFormField setBasicAttr(AppFormField appFormField,JSONObject affJson,String code){
		//appFormField.setType(affJson.getString("field_type"));
		//appFormField.setIsConnectionField(affJson.getInt("isConnectionField"));
		appFormField.setcType(affJson.getString("field_type"));
		if(affJson.getBoolean("isShow")){
			appFormField.setIsShow(1);
		}else{
			appFormField.setIsShow(0);
		}
		if(affJson.getBoolean("isTitle")){
			appFormField.setIsTitle(1);
			appFormField.setIsShow(0);
		}else{
			appFormField.setIsTitle(0);
		}
		if(affJson.optBoolean("isConnectionField")){
			if(affJson.getBoolean("isConnectionField")){
				appFormField.setIsConnectionField(1);
				appFormField.setIsDB(0);
//				appFormField.setIsShow(0);
//				appFormField.setIsTitle(0);
			}else{
				//如果是file类型，则设置不生成表的列
				if("file".equals(appFormField.getcType())){
					appFormField.setIsConnectionField(0);
					appFormField.setIsDB(0);
					appFormField.setIsShow(0);
				}else{
					appFormField.setIsDB(1);
					appFormField.setIsConnectionField(0);
				}
			}
		}else{
			//如果是file类型，则设置不生成表的列
			if("file".equals(appFormField.getcType())){
				appFormField.setIsConnectionField(0);
				appFormField.setIsDB(0);
				appFormField.setIsShow(0);
			}else{
				appFormField.setIsDB(1);
				appFormField.setIsConnectionField(0);
			}
		}
		
		//如果是number类型，则设置小数位
		if("number".equals(appFormField.getcType())){
			JSONObject numberObject = JSONObject.fromObject(affJson.get("field_options"));
			if(!(numberObject.isEmpty())){
				if(StringUtil.isNotEmpty(numberObject.getString("digits"))){
					appFormField.setScale(Integer.parseInt(numberObject.getString("digits")));
				}else{
					appFormField.setScale(0);
				}
			}else{
				appFormField.setScale(0);
			}
		}
		//判断date是否包含时分秒
		if("date".equals(appFormField.getcType())){
			JSONObject dataObject = JSONObject.fromObject(affJson.get("field_options"));
			if(!(dataObject.isEmpty())){
				if(dataObject.getBoolean("include_time")){
					appFormField.setDateType(1);	
				}else{
					appFormField.setDateType(0);
				}
			}else{
				appFormField.setDateType(0);
			}
		}

		if(StringUtil.isEmpty(affJson.optString("label"))){
			appFormField.setName("");
		}else{
			appFormField.setName(affJson.getString("label"));
		}
		
		appFormField = getLength(appFormField);
		appFormField.setMyCid(affJson.getString("cid"));
		appFormField.setParentId(affJson.getString("cid"));
		appFormField.setFormCode(code);
		appFormField.setIsOverdue(0);
		
		// 暂时写死
		//appFormField.setNotNull(0);

		if(affJson.getBoolean("isProveEdit")){
			appFormField.setIsProveEdit(1);
		}else{
			appFormField.setIsProveEdit(0);
		}
		if(affJson.getBoolean("required")){
			appFormField.setNotNull(1);
		}else{
			appFormField.setNotNull(0);
		}
		
		return appFormField;
	}
	
	public AppFormField setAddressAttr(AppFormField appFFAdd,Map<String,String> allFieldMap,JSONObject affJson,String code,String isTrueCode){
		appFFAdd.setFieldKey(isTrueCode);
		for (int flag = 0; flag < 1; flag++) {
			if (allFieldMap.containsKey(isTrueCode)) {
				String exist = allFieldMap.get(isTrueCode);
				String number = exist.replaceAll("\\D+", "");
				if (StringUtil.isNotEmpty(number)) {
					int newNumner = Integer.parseInt(number) + 1;
					isTrueCode = isTrueCode.replaceAll("\\d+", "");
					isTrueCode = isTrueCode + newNumner;
					flag = -1;
				} else {
					isTrueCode = isTrueCode + "1";
					flag = -1;
				}
				// sonTabbleCode = sonTabbleCode + "1";
				// map.put(sonTabbleCode,
				// sonTabbleCode);
			} else {
				allFieldMap.put(isTrueCode, isTrueCode);
			}
		}
		appFFAdd.setCode(isTrueCode);
		appFFAdd.setcType("address");
		appFFAdd.setType("String");
		appFFAdd.setLength(300);

		// appFormField.setTableId(""); 暂时不写
		appFFAdd.setParentId(affJson.getString("cid"));
		//appFFAdd.setFormId(formId);
		appFFAdd.setFormCode(code);
		appFFAdd.setIsOverdue(0);
		// 暂时写死
		//appFFAdd.setNotNull(0);
		if(affJson.getBoolean("isShow")){
			appFFAdd.setIsShow(1);
		}else{
			appFFAdd.setIsShow(0);
		}
		if(affJson.getBoolean("isTitle")){
			appFFAdd.setIsTitle(1);
		}else{
			appFFAdd.setIsTitle(0);
		}
		if(affJson.getBoolean("isProveEdit")){
			appFFAdd.setIsProveEdit(1);
		}else{
			appFFAdd.setIsProveEdit(0);
		}
		if(affJson.getBoolean("required")){
			appFFAdd.setNotNull(1);
		}else{
			appFFAdd.setNotNull(0);
		}
		
		return appFFAdd;
	}

	@Override
	public List<Map<String,Object>> queryAFFList(String formCode) throws BusinessException {
		String sql = "SELECT id,name,code,length,scale,type,tableId,parentId,formId,notNull,isShow from t_app_form_field where formCode=? and isShow=1 and isOverdue=0";
		return this.appFormFieldDao.findForJdbc(sql, formCode);
	}
	
	/**
	 * 根据类型的不同设置不同的length
	 * @param type
	 * @return
	 */
	private AppFormField getLength(AppFormField appFormField){
		if("text".equals(appFormField.getcType())){
			appFormField.setType("String");
			appFormField.setLength(300);
		}else if("paragraph".equals(appFormField.getcType())){
			appFormField.setType("Text");
			//appFormField.setLength(1000);
		}else if("checkboxes".equals(appFormField.getcType())){
			appFormField.setType("String");
			appFormField.setLength(100);
		}else if("radio".equals(appFormField.getcType())){
			appFormField.setType("String");
			appFormField.setLength(100);
		}else if("date".equals(appFormField.getcType())){
			appFormField.setType("Date");
			//appFormField.setLength(100);
		}else if("dropdown".equals(appFormField.getcType())){
			appFormField.setType("String");
			appFormField.setLength(100);
		}else if("time".equals(appFormField.getcType())){
			appFormField.setType("String");
			appFormField.setLength(20);
		}else if("number".equals(appFormField.getcType())){
			appFormField.setType("BigDecimal");
			appFormField.setLength(20);
			//appFormField.setScale(2);
		}else if("website".equals(appFormField.getcType())){
			appFormField.setType("String");
			appFormField.setLength(1000);
		}else if("email".equals(appFormField.getcType())){
			appFormField.setType("String");
			appFormField.setLength(100);
		}else if("price".equals(appFormField.getcType())){
			appFormField.setType("BigDecimal");
			appFormField.setLength(100);
			appFormField.setScale(2);
		}else if("address".equals(appFormField.getcType())){
			appFormField.setType("String");
			appFormField.setLength(200);
		}else if("detail".equals(appFormField.getcType())){
			appFormField.setType("String");
			appFormField.setLength(2000);
		}else if("phone".equals(appFormField.getcType())){
			appFormField.setType("String");
			appFormField.setLength(15);
		}else if("selectuser".equals(appFormField.getcType())){
			appFormField.setType("String");
			appFormField.setLength(4000);
		}else if("idcard".equals(appFormField.getcType())){
			appFormField.setType("String");
			appFormField.setLength(18);
		}else if("position".equals(appFormField.getcType())){
			appFormField.setType("String");
			appFormField.setLength(100);
		}
		
		return appFormField;
	}

	@Override
	public List<AppFormField> getAFFListByTableId(String formCode,String tableId) throws BusinessException {
		String hql = "from AppFormField where tableId=? and formCode=? and isOverdue = 0";
		//String hql = "select * from AppFormField where formId=? and tableId=? and isShow=1 and isOverdue=1";
		//String hql = "from AppFormField where formId='"+formId+"' and tableId='"+tableId+"' and isShow=1 and isOverdue=1";
		//List<AppFormField> hahalist = this.findByQueryString(hql);
		return this.appFormFieldDao.findHql(hql, tableId,formCode);
		//return hahalist;
	}
	
	@Override
	public AppFormField queryTitleField(String formCode) {
		String hql = "FROM AppFormField WHERE formCode=? AND isOverdue = 0 AND isTitle=1";
		return this.appFormFieldDao.findUniqueByHql(hql, formCode);
	}

	@Override
	public List<AppFormField> getAllAFFList(String formCode) throws BusinessException {
		String hql = "from AppFormField where formCode=?";
		return this.appFormFieldDao.findHql(hql, formCode);
	}

	public void saveFieldAndTable(List<AppFormField> aFFList,Integer tableType){
		
	}

	@Override
	public AppFormField getAppFF(String formCode, String cid) {
		String hql = "from AppFormField where formCode=? and myCid=?";
		return this.appFormFieldDao.findUniqueByHql(hql, formCode,cid);
	}
	
	@Override
	public AppFormField getAppFFByPaId(String formCode, String parentId) {
		String hql = "from AppFormField where formCode=? and parentId=?";
		return this.appFormFieldDao.findUniqueByHql(hql, formCode,parentId);
	}

	public List<AppFormField> getAddress(String formCode, String parentId) {
		String hql = "from AppFormField where formCode=? and parentId=?";
		return this.appFormFieldDao.findHql(hql, formCode,parentId);
	}
	
	@Override
	public List<AppFormField> getAFFList(String formCode) throws BusinessException {
		String hql = "from AppFormField where formCode=? and isShow=1 and isOverdue=0";
		return this.appFormFieldDao.findHql(hql, formCode);
	}
	
	@Override
	public Map<String, AppFormField> queryAppFormFieldMap(String tableName) {
		List<AppFormField> list = this.findByProperty("tableName", tableName);
		list = parseFilterFields(list);
		Map<String, AppFormField> map = new HashMap<String, AppFormField>();
		if (list != null && list.size() > 0) {
			for (AppFormField po : list) {
				if (po.getIsConnectionField() == 0) {
					// 非关联字段,自身字段才加入
					map.put(po.getCode(), po);
				}
			}
		}
		return map;
	}
	/**
	 * 将数据库的field集合转化为物理表的field集合
	 * @param fields
	 * @return
	 */
	@Override
	public List<AppFormField> queryAppFormFieldBySelf(String tableName) {
		List<AppFormField> list = this.findByProperty("tableName", tableName);
		if (list != null && list.size() > 0) {
			Iterator<AppFormField> it = list.iterator();
			while (it.hasNext()) {
				AppFormField field = it.next();
				if (field.getIsConnectionField() == 0) {
					// 非关联字段,自身字段才加入
					it.remove();
				}
			}
		}
		return list;
	}

	@Override
	public List<AppFormField> queryAppFormField(String tableId) {
		return this.findByProperty("tableId", tableId);
	}

	@Override
	public List<AppFormField> getNotIsOverdueAFFList(String formCode, String tableId) {
		String hql = "from AppFormField where formCode=? and tableId=? and isShow=1 and isOverdue=0";
		return this.appFormFieldDao.findHql(hql, formCode,tableId);
	}
	
	@Override
	public List<AppFormField> queryUsedFileAppFormField(String tableName) {
		String hql = "FROM AppFormField WHERE tableName=? and isOverdue=0 and cType='file'";
		return this.appFormFieldDao.findHql(hql, tableName);
	}

	@Override
	public List<Map<String, Object>> queryDisableFieldList(String formCode) {
		String hql1 = "SELECT DISTINCT parentId AS cid,cType FROM t_app_form_field WHERE formCode = ? AND tableId IN (SELECT id FROM t_app_form_table WHERE formCode = ? AND isOverdue = 0) AND isProveEdit = 0 AND isOverdue=0";
		String hql2 = "SELECT cid,'detail' AS cType FROM t_app_form_table WHERE formCode=? AND tableType=? AND isOverdue=0 AND isProveEdit = 0";
		List<Map<String, Object>> list1 = this.appFormFieldDao.findForJdbc(hql1, formCode, formCode);
		List<Map<String, Object>> list2 = this.appFormFieldDao.findForJdbc(hql2, formCode, BusinessConst.TableType_sub);
		list1.addAll(list2);
		return list1;
	}
	
	
	@Override
	public List<AppFormField> parseFilterFields(List<AppFormField> fields) {
		return parseFilterFields(fields, true);
	}
	
	@Override
	public List<AppFormField> parseFilterFields(List<AppFormField> fields, Boolean isRemove) {
		// 特殊处理的字段
		Iterator<AppFormField> it = fields.iterator();
		List<AppFormField> addAppFormFields = new ArrayList<AppFormField>();
		while (it.hasNext()) {
			AppFormField field = it.next();
			Boolean isRemoved=false;
			if (isRemove && field.getIsDB().equals(0)) {
				// 说明不生成数据库字段
				it.remove();
				isRemoved=true;
			}
			
			// 位置控件
			if ("position".equals(field.getcType())) {
				// 再添加全地址和经纬度字段
				AppFormField mapAddressField = new AppFormField();
				mapAddressField.setCode(field.getCode() + BusinessConst.mapaddress);// 控件code+固定名称作为物理表字段名
				mapAddressField.setType("String");
				mapAddressField.setName("位置名称");
				mapAddressField.setNotNull(0);
				mapAddressField.setLength(300);
				mapAddressField.setIsConnectionField(field.getIsConnectionField());
				mapAddressField.setIsProveEdit(field.getIsProveEdit());
				mapAddressField.setcType(field.getcType());
				addAppFormFields.add(mapAddressField);
				AppFormField lonAndLatField = new AppFormField();
				lonAndLatField.setCode(field.getCode() + BusinessConst.lonandlat);
				lonAndLatField.setType("String");
				lonAndLatField.setName("位置经纬度");
				lonAndLatField.setNotNull(0);
				lonAndLatField.setLength(100);
				lonAndLatField.setIsConnectionField(field.getIsConnectionField());
				lonAndLatField.setIsProveEdit(field.getIsProveEdit());
				lonAndLatField.setcType(field.getcType());
				addAppFormFields.add(lonAndLatField);
				// 移除掉之前的字段
				if (isRemove && !isRemoved) {
					it.remove();
				}
			}
		}
		fields.addAll(addAppFormFields);
		return (List<AppFormField>) fields;
	}
}

/**
 * 收集页面XML
 *
 * @param scope：收集的范围
 * 若scope=null，则收集页面所有元素；
 * 若scope="prp"，则只收集name="prp:xx/xxx/xx"的页面元素
 */
function prePareXmlData(scope) {
	var xmlData = "<?xml version='1.0' encoding='UTF-8'?><data>"
			+ prePareXmlDataStand($(document),scope) + "</data>";
	//将&nbsp;转换成空格
	xmlData = xmlData.replace(/&nbsp;/g," "); 
    //将<br>转换成换行符"\r\n"
	xmlData = xmlData.replace(/&lt;br&gt;/g,"\r\n");
	// 节点名称转小写
	xmlData = xmlData.replace(/<([^\?][^<>]*?)?[A-Z][^<>]*?>/g, function(str){
		return str.toLowerCase();
	});	
	// 将转码过的换行符替换回来（打印PDF时使用）
	xmlData = xmlData.replace(/&amp;#xD;&amp;#xA;/g,"&#xD;&#xA;");
	
	return xmlData;
}



/** XML收集新方法，稳定后搬出去* */
/**
 * @param scope：收集的范围
 * 若scope=null，则收集页面所有元素；
 * 若scope="prp"，则只收集name="prp:xx/xxx/xx"的页面元素
 */
function prePareXmlDataStand(obj, scope,trimflag) {

	var xmlData = "<data></data>";
	var xmlObj = $(xmlData);
	/*效率偏低 DOM元素过多时IE8下会报脚本运行缓慢提示 简化过滤逻辑
	var scopeObj = obj.find(":text,:hidden,:password,span,textarea");
	if(scope!=null && $.trim(scope)!="")
		scopeObj=scopeObj.filter("[name^='"+$.trim(scope)+":']");
	
	scopeObj.not("[name*='|']")
			.filter("[name*='/'],[label='xml']").each(function() {
		prePareXmlDataSub(xmlObj, $(this));
	});
	*/
	//简化开始
	var scopeObj = obj.find(":text,:hidden,:password,span,textarea");
	if(scope!=null && $.trim(scope)!="")
		scopeObj=scopeObj.filter("[name^='"+$.trim(scope)+":']");
	scopeObj.not("[name*='|']").filter("[name*='/']").each(function() {
		prePareXmlDataSub(xmlObj, $(this),trimflag);
	});
	//简化结束
	return xmlObj.html();

}

function prePareXmlDataSub(xmlObj, oTag,trimFlag) {
	
	var name = oTag.attr("name").toLowerCase().split(":");
	var isTextArea = false; //该变量 针对保存申请书时,textarea中不能保存换行符的问题
	if(oTag[0].tagName == 'TEXTAREA'){
	   	isTextArea = true;
	}
	var strNames;
	if(name.length==1)
		strNames= name[0].split("/");
	else
		strNames= name[1].split("/");

	var isXml = oTag.attr("label");
	
	var xmlValue = getTagValue(oTag);
	if (isXml == "xml"&& (xmlValue==null||xmlValue=="")){//排除收集的xml为空的情况
		return;
	}
	
	for ( var i = 0; i < strNames.length; i++) {
		var strName = strNames[i];

		var objName = "";
		var index = 0;
		var attrName = "";
		if (strName.indexOf("[") >= 0 && strName.indexOf("]") >= 0) {// 多重节点
			index = strName.substring(strName.indexOf("[") + 1, strName
					.indexOf("]"));
			if ($.isNumeric(index))
				index = parseInt(index,10);
			// alert(xmlObj.html());
			//alert(index);
			objName = strName.substring(0, strName.indexOf("["));
		}

		if (strName.indexOf("@") > 0) {
			attrName = strName.substring(strName.indexOf("@") + 1);
		}

		if (strName.indexOf("@") == -1 && strName.indexOf("[") == -1)
			objName = strName;
		if (xmlObj.children(objName).length == index + 1) {// 如果该节点存在
			xmlObj = (xmlObj.children(objName)).eq(index);
		} else {
			while(xmlObj.children(objName).length < index + 1){
				var newXmlObjStr = $("<" + objName + ">" + "</" + objName + ">");
				xmlObj.append(newXmlObjStr);
			}
			xmlObj = xmlObj.children(objName).eq(index);
		}

		if (attrName != "") {
			xmlValue = xmlscc(xmlValue);
			xmlObj.attr(attrName, xmlValue);
		}

		if (attrName == "" && i == (strNames.length - 1)) {
			if (isXml == "xml"){
				xmlValue = xmlValue.replace(/<!\[CDATA\[(.*?)\]\]>/ig, "$1");
				xmlValue = xmlValue.replace(/&nbsp;/g,"&amp;nbsp;");
				xmlObj.empty();				
				xmlObj.append($(xmlValue));
			}
			else{
				// 针对textarea 问题 添加的if逻辑判断
				if(isTextArea){
					if(!!trimFlag && trimFlag==true){
						xmlValue=$.trim(xmlValue);
					}
					var strValues = "";
					var editXmlValue = "";
					strValues = xmlValue.split("\n");
					var len = strValues.length;
					if(len>1 && xmlValue!=""){
						for(var j =  0 ; j < len; j++){
							editXmlValue += '<br>'+ strValues[j];
						}
						editXmlValue= editXmlValue.substring(4);
						//editXmlValue=xmlValue;
					}else {
						editXmlValue=xmlValue;
					}
					xmlObj.text(editXmlValue); 
				}
				else
					xmlObj.text(xmlValue);
			}
		}
	}

	xmlData = xmlObj.text();

}

/**
 * 获得标签的值
 */
function getTagValue(oTag)
{
	try
	{
		if(oTag.is("span"))
		{
			
			return irisTrim(oTag.text());
		}
		else
		{
			return oTag.val();
		}
	}
	catch(ex)
	{
		return "";
	}
}

/**
 * 申请书中申请代码的输入控制
 * 
 * @param obj
 */
function subjectControl(obj) 
{
	if(obj.value == null)
		obj.value="";
	var oldValue = obj.value;
	
	var forbid = function() {
		var keyCode = getEvent().keyCode;
		var regex = /^[\da-zA-Z]*$/;
		
		if(keyCode==8 && !obj.value.match(regex)){	// 按删除键并且不是由字母和数字组成则清空
			obj.value="";
			oldValue="";
		}
		else if(keyCode!=13 && keyCode!=38 && keyCode!=40){// 除了按上、下、回车，输入后如果文本框中不是字母和数字则保持原值
			if(!obj.value.match(regex))
				obj.value=oldValue;
		}
	};
	
	obj.onblur = function() {
		var regex = /^[\da-zA-Z]+(\..+)?$/;
		if(obj.value!="" && !obj.value.match(regex))
			obj.value=oldValue;
	};
	
	obj.onkeypress = function() {
		forbid();
	};
	obj.onkeyup = function() {
		forbid();
	};
}

/**
 * 将xml字符串转成jquery对象
 * 
 * @param str
 */
function xmlStrToObj(str){
	var xml;
	
	if($.browser.msie & parseInt($.browser.version) < 9){
		xml= new ActiveXObject("Microsoft.XMLDOM");
		xml.async = false;
		xml.loadXML(str);
		xml = $(xml);
	} else {
		str = str.replace(/<!\[CDATA\[(.*?)\]\]>/ig, "$1");
		xml= $(str);
	}
	return xml;
}

/**
 * 压力测试专用方法
 */
function enterTest(){
	var href=ctx + "/proposal/enter-prp";
	$("#mainForm").attr("action",href);
	$("#mainForm").submit();
}

/**
 * 清空文本框及隐藏域的值
 * @param id
 */
function clearValue(id){
	$("#"+id + ",[name$='/"+id+"_code']" + ",[name$='/"+id+"_name']").val("");
}


/*
 * 金额转换大小写
 * */

function lw(str,str1){
var num=Math.round(parseFloat($("#"+str1).val())*10000 * parseFloat(10000)*10000)/(10000*10000);
var num1 = chineseNumber(num);
$("#"+str).val(num1);
}


function chineseNumber(num)
{
if (isNaN(num) || num > Math.pow(10, 12)) return "";
if(num==0){return "零";} 

var cn = "零壹贰叁肆伍陆柒捌玖";
var unit = new Array("拾佰千", "分角");
var unit1= new Array("万亿", "");
var numArray = num.toString().split(".");
var start = new Array(numArray[0].length-1, 2);

function toChinese(num, index)
{
var num = num.replace(/\d/g, function ($1)
{
return cn.charAt($1)+unit[index].charAt(start--%4 ? start%4 : -1);
});
return num;
}

for (var i=0; i<numArray.length; i++)
{
var tmp = "";
for (var j=0; j*4<numArray[i].length; j++)
{
var strIndex = numArray[i].length-(j+1)*4;
var str = numArray[i].substring(strIndex, strIndex+4);
var start = i ? 2 : str.length-1;
var tmp1 = toChinese(str, i);
tmp1 = tmp1.replace(/(零.)+/g, "零").replace(/零+$/, "");
tmp1 = tmp1.replace(/^壹拾/, "拾");
tmp = (tmp1+unit1[i].charAt(j-1)) + tmp;
}
numArray[i] = tmp;
}

numArray[1] = numArray[1] ? numArray[1] : "";
numArray[0] = numArray[0] ? numArray[0]+"元" : numArray[0], numArray[1] = numArray[1].replace(/^零+/, "");
numArray[1] = numArray[1].match(/分/) ? numArray[1] : numArray[1];
return numArray[0]+numArray[1];
}



/**
 * 数字显示函数，适用于可输入四位小数或二位少数
 * number:值
 * maxIntLength:整数部分最大长度,
 * maxDecimalLength:小数部分最大长度,
 * displayDecimal:小数部分显示出的长度
 */
function formatNumberAuto(number,maxIntLength,maxDecimalLength,displayDecimal) {
	if(number.indexOf(".")>=0){//如果是输入值中带有小数点则检查小数部分长度是否达到了最低显示的长度，如果没有则补0
		var integral="";
		var decimal="";
		if (number.indexOf(".")==0&&number.length>1){
			integral="0";
			decimal=number.substring(1,number.length);
		}else if (number.indexOf(".")>0){
			integral=number.substring(0,number.indexOf("."));
			decimal=number.substring(number.indexOf(".")+1,number.length);
		}
		var f=decimal.substring(displayDecimal,maxDecimalLength);
	    //小数四位，如果后两位为00，就去掉
		if(f=="00"){
			decimal=decimal.substring(0, displayDecimal);
		}else{
			decimal=decimal.substring(0, maxDecimalLength);
		}
		number=integral+"."+decimal;
	}else if (number!=""&&displayDecimal>0){//输入的是整数则补上小数部分，全部为0
		number=number+".";
		for (var i=0;i<displayDecimal;i++){
			number=number+"0";
		}
	}
	return number;
};
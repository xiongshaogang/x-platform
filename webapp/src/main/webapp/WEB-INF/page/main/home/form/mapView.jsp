<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="http://cache.amap.com/lbs/static/main.css?v=1.0" />
<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=dfb5bc6490068bd08fe0aafeaf1c2827"></script>

<style type="text/css">
#container {
	height: 100%;
}

.panel {
	background-color: #ddf;
	color: #333;
	border: 1px solid silver;
	box-shadow: 3px 4px 3px 0px silver;
	position: absolute;
	top: 10px;
	right: 10px;
	border-radius: 5px;
	overflow: hidden;
	line-height: 20px;
}

#mapSearch {
	width: 250px;
	height: 25px;
	border: 0;
}
</style>
<div id="container" tabindex="0"></div>
<input type="hidden" id="lonAndLat" />
<input type="hidden" id="mapAddress" />
<div class='panel'>
	<input id='mapSearch' placeholder='请输入地点关键词'></input>
	<div id='message'></div>
</div>
<script type="text/javascript">
	var defaultLonAndLat = [ 113.325879, 23.120159 ]; //默认打开地图显示的经纬度(广州高德置地秋广场)
	var jsonObj = ${params};
	if (jsonObj && jsonObj.lonAndLat) {
		defaultLonAndLat = jsonObj.lonAndLat.split(",");
	}
	//初始化加载地图时，若center及level属性缺省，地图默认显示用户当前城市范围
	var map = new AMap.Map('container', {
		resizeEnable : true,
		zoom : 17,
		center : defaultLonAndLat
	});
	//初始化infoWindow
	var infoWindow = new AMap.InfoWindow({offset:new AMap.Pixel(5,-20)});
	infoWindow.setContent(jsonObj.mapAddress);
	infoWindow.open(map, defaultLonAndLat);
	//地图中添加地图操作ToolBar插件
	map.plugin([ 'AMap.ToolBar' ], function() {
		//设置地位标记为自定义标记
		var toolBar = new AMap.ToolBar();
		map.addControl(toolBar);
	});

	/*     AMap.plugin('AMap.Geocoder',function(){
	 var geocoder = new AMap.Geocoder({
	 city: ""//城市，默认：“全国”
	 });
	 var marker = new AMap.Marker({
	 map:map,
	 bubble:true
	 })
	 map.on('click',function(e){
	 marker.setPosition(e.lnglat);
	 console.info(e.lnglat);
	 geocoder.getAddress(e.lnglat,function(status,result){
	 if(status=='complete'){
	 document.getElementById('input').value = result.regeocode.formattedAddress
	 }
	 })
	 });
	
	 input.onchange = function(e){
	 debugger;
	 var address = input.value;
	 geocoder.getLocation(address,function(status,result){
	 console.info(result);
	 if(status=='complete'&&result.geocodes.length){
	 marker.setPosition(result.geocodes[0].location);
	 map.setCenter(marker.getPosition())
	 message.innerHTML = ''
	 }else{
	 message.innerHTML = '无法获取位置';
	 }
	 })
	 }  

	 }); */

	AMap.plugin([ 'AMap.Autocomplete', 'AMap.PlaceSearch', 'AMap.Geocoder' ], function() {
		 var hideObject = null; 
	   	 var markerArr = null;
		var geocoder = new AMap.Geocoder({
			city : ""//城市，默认：“全国”
		});
		var marker = new AMap.Marker({
			map : map,
			bubble : true,
			icon : 'basic/img/loc_marker.png'
		})

		map.on('click', function(e) {
			marker.setPosition(e.lnglat);
			geocoder.getAddress(e.lnglat, function(status, result) {
				if(hideObject != null){
		      		 hideObject.show();
		      	 }
				if (status == 'complete') {
					//document.getElementById('mapSearch').value = result.regeocode.formattedAddress;
					document.getElementById("lonAndLat").value = e.lnglat.lng + "," + e.lnglat.lat;
					document.getElementById("mapAddress").value = result.regeocode.formattedAddress;
					marker.content = result.regeocode.formattedAddress;
					infoWindow.setContent(result.regeocode.formattedAddress);
					infoWindow.open(map, e.lnglat);
				}
			});
		});

		var autoOptions = {
			city : "", //城市，默认全国
			input : "mapSearch"//使用联想输入的input的id
		};
		autocomplete = new AMap.Autocomplete(autoOptions);
		var placeSearch = new AMap.PlaceSearch({
			city : ''
		})
		AMap.event.addListener(autocomplete, "select", function(e) {
			geocoder.getAddress(e.poi.location, function(status, result) {
				if (status == 'complete') {
					document.getElementById("lonAndLat").value = e.poi.location.lng + "," + e.poi.location.lat;
					document.getElementById("mapAddress").value = result.regeocode.formattedAddress;
				}
			});
			placeSearch.search(e.poi.district + e.poi.name, function(status, result) {
				marker.setPosition(e.poi.location);
				map.setCenter(marker.getPosition());
				infoWindow.setContent(e.poi.district + e.poi.name);
				/* var Pixel = new AMap.Pixel(5,-20);
				infoWindow.setOffset(Pixel); */
				infoWindow.open(map, e.poi.location);
				message.innerHTML = '';
				if (markerArr != null) {
					map.remove(markerArr);
					markerArr = null;
				}

				if (result.poiList != null && result.poiList != "" && typeof (result.poiList) != "undefined") {
					markerArr = new Array();
					for (var i = 0, marker1; i < result.poiList.pois.length; i++) {
						marker1 = new AMap.Marker({
							position : result.poiList.pois[i].location,
							icon : 'basic/img/marker.png',
							map : map
						});
						marker1.content = e.poi.district + result.poiList.pois[i].address + result.poiList.pois[i].name;
						//给Marker绑定单击事件
						marker1.on('click', markerClick);
						markerArr[i] = marker1;
					}
					map.setFitView();
				}
			})

		});

		function markerClick(e) {
			if (hideObject != null) {
				hideObject.show();
			}
			marker.setPosition(e.target.getPosition());
			infoWindow.setContent(e.target.content);
			infoWindow.open(map, e.target.getPosition());
			hideObject = e.target;
			e.target.hide();
		}
	});
</script>

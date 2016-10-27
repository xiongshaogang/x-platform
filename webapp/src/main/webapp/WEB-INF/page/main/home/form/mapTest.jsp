<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="http://cache.amap.com/lbs/static/main.css?v=1.0"/>
<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=dfb5bc6490068bd08fe0aafeaf1c2827"></script>
    
  <style type="text/css">
      body,html,#container{
        height: 100%;
        margin: 0px
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
      #input{
        width: 250px;
        height: 25px;
        border: 0;
      }
    </style>
<div id="container" tabindex="0"></div>
   <div class ='panel'>
     <input id='input' value='点击地图显示地址/输入地址显示位置' onfocus='this.value=""'></input>
    <div id = 'message'></div>
   </div>
<script type="text/javascript">

    //初始化地图对象，加载地图
    ////初始化加载地图时，若center及level属性缺省，地图默认显示用户当前城市范围
      var map = new AMap.Map('container',{
            resizeEnable: true,
            zoom: 13,
            center: [113.325879,23.120159] 
    });
    //地图中添加地图操作ToolBar插件
    map.plugin(['AMap.ToolBar'], function() {
        //设置地位标记为自定义标记
        var toolBar = new AMap.ToolBar();
        map.addControl(toolBar);
    });
    
    AMap.plugin(['AMap.Autocomplete','AMap.PlaceSearch','AMap.Geocoder'],function(){
    	
    	 var hideObject = null; 
    	 var markerArr = null;
    	 var geocoder = new AMap.Geocoder({
             city: ""//城市，默认：“全国”
         });
         var marker = new AMap.Marker({
             map:map,
             bubble:true,
             icon : 'basic/img/loc_marker.png'
         })
         var infoWindow = new AMap.InfoWindow({offset:new AMap.Pixel(5,-20)});
         map.on('click',function(e){
        	 if(hideObject != null){
        		 hideObject.show();
        	 }
             marker.setPosition(e.lnglat);
             geocoder.getAddress(e.lnglat,function(status,result){
               if(status=='complete'){
            	  marker.content = result.regeocode.formattedAddress;
                  document.getElementById('input').value = result.regeocode.formattedAddress;
                  infoWindow.setContent(result.regeocode.formattedAddress);
                  infoWindow.open(map, e.lnglat);
               }
             });
         });
    	
        var autoOptions = {
          city: "", //城市，默认全国
          input: "input"//使用联想输入的input的id
        };
        autocomplete= new AMap.Autocomplete(autoOptions);
        
        var placeSearch = new AMap.PlaceSearch({
              city:''/* ,
              map:map */
        })
          AMap.event.addListener(autocomplete, "select", function(e){
        	console.info(e);
        	document.getElementById("input").value=e.poi.district+e.poi.name;
            placeSearch.search(e.poi.district+e.poi.name,function(status, result){
            	 if(result.poiList != null && result.poiList!="" && typeof(result.poiList) != "undefined"){
            		 marker.setPosition(result.poiList.pois[0].location);
  	                 map.setCenter(marker.getPosition());
  	                 infoWindow.setContent(e.poi.district+e.poi.name);
  	                 infoWindow.open(map, result.poiList.pois[0].location);
  	                 message.innerHTML = '';
            	 }else{
            		 if(e.poi.location != null && e.poi.location!="" && typeof(e.poi.location) != "undefined"){
      	            	marker.setPosition(e.poi.location);
      	                map.setCenter(marker.getPosition());
      	                infoWindow.setContent(e.poi.district+e.poi.name);
      	                /* var Pixel = new AMap.Pixel(5,-20);
      	                infoWindow.setOffset(Pixel); */
      	                infoWindow.open(map, e.poi.location);
      	                message.innerHTML = '';
                  	}else{
                  		alert('抱歉，找不到该地址');
                  	}
            	 }
                //debugger;
                if(markerArr != null){
                	map.remove(markerArr);
            		markerArr = null;
            	}

                if(result.poiList != null && result.poiList!="" && typeof(result.poiList) != "undefined"){
                	markerArr = new Array();
                	for(var i= 0,marker1;i<result.poiList.pois.length;i++){
                	    marker1=new AMap.Marker({
                	          position:result.poiList.pois[i].location,
                	          icon : 'basic/img/marker.png',
                	          map:map
                	    });
                	    marker1.content=e.poi.district+result.poiList.pois[i].address+result.poiList.pois[i].name;
                	    //给Marker绑定单击事件
                	    marker1.on('click', markerClick);
                	    markerArr[i] = marker1;
                	    if(i == 0){
                	    	marker1.hide();
                	    	hideObject = marker1;
                	    }
                	}
                	map.setFitView();
                }
            }) 
        });  
        function markerClick(e){
        	if(hideObject != null){
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

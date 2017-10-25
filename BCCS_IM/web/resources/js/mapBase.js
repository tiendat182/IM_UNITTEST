/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function VTHashTable() {
    var hashList = [];
    this.put = function (key, object) {
        if (this.findObject(key) != null) {
            this.removeObject(key);
        }
        var hash = new VTHash(key, object);
        hashList.push(hash);
    }
    this.getList = function () {
        return hashList;
    }
    this.getLength = function () {
        return hashList.length;
    }
    this.get = function (i) {
        return hashList[i];
    }
    this.findObject = function (_key) {
        for (var i = 0; i < hashList.length; i++) {
            if (hashList[i].getKey() == _key) {
                return hashList[i].getObject();
            }
        }
        return null;
    }
    this.getAllObject = function () {
        var objectList = [];
        for (var i = 0; i < hashList.length; i++) {
            var object = hashList[i].getObject();
            for (var j = 0; j < object.length; j++) {
                objectList.push(object[j])
            }
        }
        return objectList;
    }
    this.findIndex = function (_key) {
        for (var i = 0; i < hashList.length; i++) {
            if (hashList[i].getKey() == _key) {
                return i;
            }
        }
        return -1;
    }
    this.removeObject = function (key) {
        var index = this.findIndex(key);
        if (index != -1) {
            hashList.splice(index, 1)
        }
    }
    this.destroy = function () {
        var length = hashList.length;
        for (var i = 0; i < length; i++) {
            hashList.pop();
        }
    }

}
function VTHash(_key, _object) {
    var key = _key;
    var object = _object;
    this.getKey = function () {
        return key;
    }
    this.getObject = function () {
        return object;
    }
}

/*
 * MarkerManagerFactory
 *
 */
MarkerManagerFactory = function () {
    var markerManagerHashTable = new VTHashTable();
    this.getLayer = function (markerType) {
        return markerManagerHashTable.findObject(markerType);
    }
    this.addLayer = function (markerManagerType, specificMarkerManager) {

        markerManagerHashTable.put(markerManagerType, specificMarkerManager);
    }
    this.getLayerList = function () {
        return markerManagerHashTable.getList();
    }

}

Constant = {};
Constant.mapObj = {};
Constant.mapObj.station = 1;
Constant.mapObj.marker = 0;
Constant.mapObj.polyline = 2;
Constant.mapObj.polyline2 = 3;
Constant.mapObj.circle = 4;
/*
 * LayerManager
 * Quan ly cac Layer tren map
 */
LayersManager = function () {
    var markerManagerFactory = new MarkerManagerFactory();
    this.addLayer = function (layerName, layer) {
        markerManagerFactory.addLayer(layerName, layer);
    }
    this.getLayerFactory = function () {
        return markerManagerFactory;
    }
    /*
     * Them cac doi tuong toi cac layer
     */
    this.addMapObjs = function (mapObjs) {

        for (var i = 0; i < mapObjs.length; i++) {

            var layer = markerManagerFactory.getLayer(mapObjs[i].markerType);
            if (layer != null) {
                try {
                    layer.addObjToLayer(mapObjs[i])
                }
                catch (ex) {
                    console.log(ex);
                }
            }
        }
    }

    this.addMapObj = function (mapObj) {
        var layer = markerManagerFactory.getLayer(mapObj.markerType);
        if (layer != null) {
            try {
                layer.addObjToLayer(mapObj)
            }
            catch (ex) {
                console.log(ex);
            }
        }
    }
    /*
     * Xoa bo het tat ca cac doi tuong trong Layer co cung type
     */
    this.clearObjByType = function (objType) {
        var layers = markerManagerFactory.getLayerList();
        for (var i = 0; i < layers.length; i++) {
            layers[i].getObject().clearObjByType(objType)
        }
    }
    /*
     * Xoa bo het tat ca cac doi tuong trong Layer
     */
    this.clear = function () {
        var layers = markerManagerFactory.getLayerList();
        for (var i = 0; i < layers.length; i++) {
            layers[i].getObject().clear()
        }
    };

    this.show = function () {

    };
    this.hide = function () {

    }
    this.add = function () {

    }
}
/*
 * LayerManager: Quan ly cac Layer chung nhu them Layer, xoa doi tuong....]
 * //TODO add them thong tin su kien quan ly ben trong
 */
function LayerManager(map, layersManager) {
    var mapObjs = [];
    /*
     * Them doi tuong toi LayerFactory de MapManager co the tao ra khi biet kieu
     */
    this.addToFactory = function (markerManagerType) {
        layersManager.addLayer(markerManagerType, this)
    }
    /*
     * Add them 1 danh sach cac doi tuong toi Layer
     */
    this.addObjsToLayer = function (mapObjsData) {
        for (var i = 0; i < mapObjsData.length; i++) {
            try {
                this.addObjToLayer(mapObjsData[i]);
            }
            catch (ex) {
                console.log("ex: ", ex)
            }
        }
        return mapObjs;
    }
    /*
     * Xoa cac doi tuong trong Layer neu co kieu Type thich hop
     */
    this.clearObjByType = function (objType) {
        for (var i = mapObjs.length - 1; i >= 0; i--) {
            if (mapObjs[i].objType == objType) {
                mapObjs[i].setMap(null);
                mapObjs.splice(i, 1);
            }
        }
    }
    /*
     * Them mot doi tuong cu the toi Layer
     */
    this.addObjToLayer = function (mapObjData) {

    }
    /*
     * Lay danh sach tat ca cac doi tuong tren nen Layer
     */
    this.getObjs = function () {
        return mapObjs;
    }
    /*
     * Xoa tat ca cac doi tuong tren nen Layer
     */
    this.clear = function () {
        for (var i = 0; i < mapObjs.length; i++) {
            mapObjs[i].setMap(null);
        }
        mapObjs = [];
        viettel.Events.trigger(this, "clear");
    }
    /*
     * Xoa tat ca cac doi tuong theo type tren nen Layer
     */
    this.clearObjByType = function (objType) {
//        console.log('objType: ' + objType);
        for (var i = mapObjs.length - 1; i >= 0; i--) {
            console.log(mapObjs[i].mapObj.objectType);
            if (mapObjs[i].mapObj.objectType == objType) {
                mapObjs[i].setMap(null);
                mapObjs.splice(i, 1);
            }
        }
        //mapObjs = [];
        //viettel.Events.trigger(this, "clear");
    }
    this.registerEvent = function () {
    }
}

//Ve polyline khong danh so
Polyline2Manager.prototype = new LayerManager();
Polyline2Manager.prototype.constructor = Polyline2Manager;
function Polyline2Manager(map, layersManager) {
    LayerManager.call(this, map, layersManager);
    this.addObjToLayer = function (mapObj) {
        var path = Util.convertFromStrToGeo(mapObj.geometry);
        var polyline = new viettel.Polyline(
            {
                path: path,
                strokeWeight: 2,
                strokeColor: mapObj.color,
                map: map
            });
        polyline.mapObj = mapObj;
        this.getObjs().push(polyline);
        return polyline;
    }
//    this.clear = function(){
//        var mapObjs = this.getObjs();
//
//        for(var i = 0; i < mapObjs.length; i++){
//            mapObjs[i].drawer.remove();
//        }
//        mapObjs = [];
//
//    }

}

/*
 * Layer nay quan ly chung cac doi tuong Polyline. Hien tai chua tao ham ve, de ve chi can override ham addObjToLayer
 */
PolylineManager.prototype = new LayerManager();
PolylineManager.prototype.constructor = PolylineManager;
function PolylineManager(map, layersManager) {
    LayerManager.call(this, map, layersManager);
    this.addObjToLayer = function (mapObj) {
        var path = Util.convertFromStrToGeo(mapObj.geometry);
        var polyline = new viettel.Polyline(
            {
                path: path,
                strokeWeight: 2,
                strokeColor: mapObj.color,
                map: map
            });
        var drawer = new VectorDirectionDrawer(polyline);
        drawer.active();
        polyline.mapObj = mapObj;
        this.getObjs().push(polyline);
        return polyline;
    }
    this.clear = function () {
        var mapObjs = this.getObjs();

        for (var i = 0; i < mapObjs.length; i++) {
            mapObjs[i].drawer.remove();
        }
        mapObjs = [];

    }

}

function VectorDirectionDrawer(vector) {
    vector.drawer = this;
    var isDrawDirection = false;
    //var that = this;
    var markers = [];
    this.active = function () {
        if (isDrawDirection) {
            return;
        }
        isDrawDirection = true;
        drawPolylineNumber();
    }
    this.deactive = function () {
        if (!isDrawDirection) {
            return;
        }
        isDrawDirection = false;
        removeMarkers();
    }
    this.remove = function () {
        this.deactive();
        vector.setMap(null);
    }
    this.getVector = function () {
        return vector
    }
    var drawPolylineNumber = function () {
        var path = vector.getPath();
        for (var i = 0; i < path.getLength(); i++) {

            var point = path.getAt(i);
            //console.log("point: ", point.lat(), " ", point.lng())
            createMarker(point, i + 1);
        }
    }
    var removeMarkers = function () {
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(null);
        }
        markers = [];
    }
    var createMarker = function (position, index) {
        var size = new viettel.Size(30, 30);
        var img = new viettel.MarkerImage("/MSM/share/images/icons/marker_point.png", null, null, null, size);
        var marker = new viettel.LabelMarker({
            position: position,
            map: vector.getMap(),
            draggable: true,
            icon: img,
            labelContent: index + "",
            labelClass: "markerlabel",
            labelVisible: true,
            labelAnchor: new viettel.Point(9, 23)
        });
        viettel.Events.addListener(marker, "click", function (evt) {
            //viettel.Events.trigger(map, "clearLayers");
            //showInfoWindow(marker.mapObj, map);
            //console.log("Vao click vector");
            var point = vector.getPath().getAt(index - 1);
            var geometry = point.lat() + "," + point.lng();
            var shortInfo = vector.mapObj.lstShortInfo[index - 1];
            //console.log("geometry: ", geometry);
            //console.log("shortInfo: ", shortInfo);
            VectorInfowindow.open(geometry, shortInfo, vector.getMap());
        });
        markers.push(marker);
    }
}

/*
 * MarkerManager: layer chinh cho viec khoi tao quan ly cac doi tuong Marker
 */
MarkerManager.prototype = new LayerManager();
MarkerManager.prototype.constructor = MarkerManager;
function MarkerManager(map, layersManager) {
    LayerManager.call(this, map, layersManager);
    this.addObjToLayer = function (mapObj) {

        var latLng = Util.convertFromStrToGeo(mapObj.geometry);
        var marker = new viettel.Marker(
            {
                position: latLng,
                //icon: {
                //  url:mapObj.iconUrl,
                //   size: new viettel.Size(20,20)
                // },
                icon: new viettel.MarkerImage(mapObj.iconUrl, null, null, null, new viettel.Size(30, 30)),
                map: map
            });
        marker.mapObj = mapObj;
        this.getObjs().push(marker);
        this.registerEvent(marker);
        return marker;
    }
    this.registerEvent = function (marker) {
        viettel.Events.addListener(marker, "click", function (evt) {
            //viettel.Events.trigger(map, "clearLayers");
            //showInfoWindow(marker.mapObj, map);
            MapObjInfowindow.open(marker.mapObj, map);
        })
        viettel.Events.addListener(marker, "rightclick", function (evt) {
        })
    }
}

/*
 * StationManager: Mo rong tu MarkerManager nhiem vu quan ly doi tuong tram
 */
StationManager.prototype = new MarkerManager();
StationManager.prototype.constructor = StationManager;
function StationManager(map, layersManager) {
    MarkerManager.call(this, map, layersManager);
    this.addToFactory(Constant.mapObj.station);
    var addObjToLayer = this.addObjToLayer;
    this.addObjToLayer = function (mapObj) {
        var marker = addObjToLayer.call(this, mapObj);
        this.registerEvent();
    }
}
/*
 * StationMarker: Ham demo khoi tao. Neu ung dung can mo rong doi tuong Marker thi lam nhu vi du duoi, hien tai trong vi du StationMarker khong
 * lam gi them so voi Marker.
 */
StationMarker.prototype = new viettel.Marker();
StationMarker.prototype.constructor = StationMarker;
function StationMarker(markerOption) {
    viettel.Marker.call(this, markerOption);
}

/*
 * 29/03/2013 LinhVM
 * CircleManager: layer cho viec quan ly doi tuong circle
 */
CircleManager.prototype = new LayerManager();
CircleManager.prototype.constructor = CircleManager;
function CircleManager(map, layersManager) {
    LayerManager.call(this, map, layersManager);
    this.addObjToLayer = function (mapObj) {
        var arrCenter = new viettel.LatLng(mapObj.geoX, mapObj.geoY);
//        var center = new viettel.LatLng(21.02993,105.774801);
        var circleOptions = {
//                        strokeColor: "#821B8B",
            strokeColor: mapObj.color,
            strokeOpacity: 0.7,
            strokeWeight: 2,
//                        fillColor: "#8A0829",
            fillColor: mapObj.color,
            fillOpacity: 0.7,
            map: map,
            center: arrCenter,
            radius: mapObj.radius,
            clickable: true
        };

        var circle = new viettel.Circle(circleOptions);
        circle.mapObj = mapObj;

        this.getObjs().push(circle);
        this.registerEvent(circle);
        return circle;
    }
    this.registerEvent = function (marker) {
        viettel.Events.addListener(marker, "click", function (evt) {
            //viettel.Events.trigger(map, "clearLayers");
            //showInfoWindow(marker.mapObj, map);
            MapObjInfowindow.open(marker.mapObj, map);
        })
        viettel.Events.addListener(marker, "rightclick", function (evt) {
        })
    }
}

/*
 * MapManager: Doi tuong quan ly chinh cua ban do voi nhiem vu quan ly map, cac layer tren map
 */
function MapManager(mapDiv, userProfile, mapOptions) {
    var layersManager = new LayersManager();
    var that = this;
    var map;
    var measureTool;
    var objAdminPolygon;
    /*
     * Them cac doi tuong MapObj tren layer cua ban do
     * @param mapObjs: Array(MapObj)
     */
    this.addMapObjs = function (mapObjs) {
        if (mapObjs.length <= 1) {
            this.addMapObj(mapObjs[0]);
            return;
        }
        fitBoundMapObjs(mapObjs);
        layersManager.addMapObjs(mapObjs);
    }

    this.addMapObj = function (mapObj) {
        fitBoundMapObj(mapObj);
        layersManager.addMapObj(mapObj);
    }
    /*
     * zoom toi vung cu the duoc tra ve tu ma code
     * @param code: String Ma hanh chinh
     * @param callback: function(status, result){}  Ham callback duoc tra ve khi ham complete. Neu ham ko duoc goi thi mac dinh zoom toi vung
     * hanh chinh
     */
    this.zoomToUserFeature = function (code, callback) {
        console.log("start zoomToUserFeature");
        console.log(code);
        var service = new viettel.AdministrationService();
        //LinhVM: Thay doi theo server moi:
        service.setReturnType(viettel.AdminReturnType.BOUND | viettel.AdminReturnType.PATH);
        //-end of LinhVM
        var level = getAdminLevel(code);
        service.setLevel(level);
        service.getFeatureFromCode(code, function (status, result) {
            if (callback != null) {
                callback(status, result);
                console.log("end zoomToUserFeature");
            }
            else {
                try {
                    map.fitBounds(GeoUtil.getBoundFromPaths(result.paths));

                }
                catch (err) {

                }
            }
        })
    }
    /*
     * Zoom fit tat ca cac vung bao quanh diem va thiet lap thong tin du lieu ben trong infowindow
     * @param mapObj: MapObj Du lieu doi tuong cua MapObj
     */
    this.zoomToMapObj = function (mapObj) {
        /*var infoWindow = new viettel.InfoWindow();
         var position = Util.convertFromStrToLatLng(mapObj.geometry);
         infoWindow.setContent(Util.convertMarkerDataToHtml(mapObj.strLst, mapObj.img));
         infoWindow.setPosition(position);
         infoWindow.open(map);*/
        //LinhVM:
        //addMapObj(mapObj)
        //--End of LinhVM
        //showInfoWindow(mapObj, map);
        this.addMapObj(mapObj);
        MapObjInfowindow.open(mapObj, map);
    }
    /*
     * Clear het layer ben tren Map
     */
    this.clear = function () {
        layersManager.clear();
        viettel.Events.trigger(map, "clearLayers")
    }
    this.clearObjByType = function (objType) {
        layersManager.clearObjByType(objType);
        viettel.Events.trigger(map, "clearLayersByType")
    }
    this.getMap = function () {
        return map;
    }
    this.zoomToArea = function (code) {
        that.zoomToUserFeature(code, zoomToFeature);
    }
    this.zoomToAreaPaint = function (code) {
        console.log("23423");
        that.zoomToUserFeature(code, zoomToFeaturePaint);
        console.log("1111");
    }
    var clearPolygonObj = function () {
        // Xoa vung hanh chinh da ve truoc do
        console.log(objAdminPolygon);
        if (objAdminPolygon != null) {
            objAdminPolygon.setMap(null);
            objAdminPolygon = null;
        }
    }

    var zoomToFeature = function (status, result) {
        clearPolygonObj();
        if (result != null && result.items[0].bound != '' && result.items[0].bound != null) {
            map.fitBounds(result.items[0].bound);
            var pt = map.getCenter();
            if (marker != null) {
                marker.setMap(null);
                (marker) == null;
            }
            marker = new viettel.Marker({
                position: pt,
                map: map,
                draggable: true
            });
            marker.setDraggable(true);
            marker.setClickable(true);
            objAdminPolygon = new viettel.Polygon({
                paths: result.items[0].paths,
                strokeColor: "#0000FF",
                strokeOpacity: 0.3,
                strokeWeight: 0.5,
                fillColor: "#FF0000",
                fillOpacity: 0.1,
                clickable: false,
                map: map

            });
        }


    }

    var zoomToFeaturePaint = function (status, result) {
        clearPolygonObj();
        if (result != null && result.items[0].bound != '' && result.items[0].bound != null) {
            objAdminPolygon = new viettel.Polygon({
                paths: result.items[0].paths,
                strokeColor: "#0000FF",
                strokeOpacity: 0.3,
                strokeWeight: 0.5,
                fillColor: "#FF0000",
                fillOpacity: 0.1,
                clickable: false,
                map: map

            });
        }


    }

    /*
     * Ham mac dinh neu muon map co vi tri default
     * @param mapOption: MapOptions dung luu tru cac tuy chon cho Map
     */
    this.initDefaultMap = function (mapOption) {
        map = new viettel.Map(mapDiv, mapOption);
        initLayers();
    }
    /*
     * fit het vung bound chua tat ca cac doi tuong
     */
    var fitBoundMapObjs = function (mapObjs) {
        try {
            var points = [];
            for (var i = 0; i < mapObjs.length; i++) {
                var geo = Util.convertFromStrToGeo(mapObjs[i].geometry);
                points = points.concat(geo);
            }
            //console.log("points: ", points);
            GeoUtil.fitBounds(map, points)
            //map.fitBounds(GeoUtil.getBoundFromPoints(points));
        }
        catch (ex) {
            console.log(ex);
        }

    }
    var fitBoundMapObj = function (mapObj) {
        try {

            var geo = Util.convertFromStrToGeo(mapObj.geometry);

            map.setZoom(12)
            //GeoUtil.fitBounds(map, geo)
            map.setZoom(GeoUtil.fitBounds(map, geo))

            //map.fitBounds(GeoUtil.getBoundFromPoints(points));
        }
        catch (ex) {

        }

    }
    /*
     * Ham lay ra danh sach muc hanh chinh tu code
     * @Note: Ham nay se duoc bo di khi hoan thien phia server
     */
    //LinhVM: sua lai ham nay theo server moi
    var getAdminLevel = function (code) {
        var lengthCode = code.length;
        switch (lengthCode) {
            case 2:
                return viettel.AdminLevelType.PROVINCE;
            case 3:
                return viettel.AdminLevelType.DISTRICT;
            case 5:
                return viettel.AdminLevelType.COMMUNE;
            default:
                return -1;
        }
    }

    //    var getAdminLevel = function(code){
    //        var lengthCode = code.length;
    //        switch(lengthCode){
    //            case 2:
    //                return 1;
    //            case 3:
    //                return 2;
    //            case 5:
    //                return 3;
    //            default:
    //                return 0;
    //        }
    //    }

    /*
     * Khoi tao map, va layer lien quan
     */
    //LinhVM: sua lai ham nay theo server moi
    var initMapZoomToFeature = function (status, result) {
        if (status == viettel.AdminStatus.OK) {
            map.fitBounds(result.items[0].bound);
            //map.fitBounds(GeoUtil.getBoundFromSwNe(result.items[0].bound.sw, result.items[0].bound.ne));
        }

        initLayers();
    }

    //    var  initMapZoomToFeature = function(status, result){
    //        map = new viettel.Map(mapDiv, mapOptions);
    //        try{
    //            map.fitBounds(GeoUtil.getBoundFromPaths(result.paths) );
    //        }
    //        catch(err){
    //        }
    //        initLayers();
    //    }
    /*
     * khoi tao cac Layer va kich hoat su kien khi layer da duoc tao thanh cong
     */
    var initLayers = function () {
        //init Layer
        var stationManager = new StationManager(map, layersManager);
        var otherManager = new MarkerManager(map, layersManager);
        otherManager.addToFactory(Constant.mapObj.marker);
        var polylineManager = new PolylineManager(map, layersManager);
        polylineManager.addToFactory(Constant.mapObj.polyline);
        var polyline2Manager = new Polyline2Manager(map, layersManager);
        polyline2Manager.addToFactory(Constant.mapObj.polyline2);
        //LinhVM
        var circleManager = new CircleManager(map, layersManager);
        circleManager.addToFactory(Constant.mapObj.circle);
        //triger
        viettel.Events.trigger(that, "loaded");
        //init event map
        measureTool = new MeasureTool(map);
        measureTool.init();
    }
    /*
     * Ham khoi tao dau tien
     */
    var init = function () {
        //LinhVM: comment cho nay
        map = new viettel.Map(mapDiv, mapOptions);
        that.zoomToUserFeature(userProfile.areaCode, initMapZoomToFeature);
        //map = new viettel.Map(mapDiv, mapOptions);
        //initLayers();
    }

    init();
}
/*
 * MeasureTool: Dung de khoi tao tool do khoang cach
 * @param map: Map
 */
function MeasureTool(map) {
    var polylineEdit;
    var that = this;
    /*
     * Dung de khoi tao doi tuong contextmenu moi khi co su kien chuot phai xuat hien
     */
    this.init = function () {
        viettel.Events.addListener(map, "rightclick", function (evt) {
            // Neu doi tong polyline dang duoc ve thi clear man hinh ve di
            if (polylineEdit != null) {
                clearMeasureLayer();
            }
            // Thiet lap contextmenu
            var context = new ContextMenuForMap(map, evt.pixel);
            context.setToolbox(that);
        })
    }
    /*
     * Dung de kich hoat cac su kien ve tren nen ban do
     */
    this.draw = function () {
        enableMeasureForm(true);
        var polylineOptions = {
            strokeColor: "#0000FF",
            strokeOpacity: 0.4,
            strokeWeight: 2,
            clickable: true,
            map: map
        };
        polylineEdit = new viettel.Polyline(polylineOptions);
        polylineEdit.setDrawing(true);
        viettel.Events.addListener(polylineEdit, "endDraw", function (evt) {
            updateMeasureForm(this);
        });
        viettel.Events.addListener(polylineEdit, "endEdit", function (evt) {
            updateMeasureForm(this);
        })
    }
    /*
     * update thong tin chieu dai khoang cach trong man hinh Measure Form
     */
    var updateMeasureForm = function (polylineEdit) {
        var polylineLength = Math.round(viettel.GeometryUtil.getLength(polylineEdit.getPath().getArray()));
        document.getElementById("total_distance").innerHTML = polylineLength + " m";
    }
    /*
     * Dung de an hien measure Form. Neu flag la true thi show, neu flag la false thi hide
     */
    var enableMeasureForm = function (flag) {
        if (flag) {
            document.getElementById("measure_form").style.visibility = "visible";
            document.getElementById("measureFormCloser").onclick = function () {
                clearMeasureLayer();
            }
        }
        else {
            document.getElementById("measure_form").style.visibility = "hidden";
        }
    }
    /*
     *  Xoa het man hinh MeasureForm va doi tuong polyline, huy toan bo su kien cua tool nay tren nen ban do
     */
    var clearMeasureLayer = function () {
        polylineEdit.setMap(null);
        polylineEdit = null;
        document.getElementById("total_distance").innerHTML = "";
        enableMeasureForm(false);
    }
}
/*
 * GeoUtil: Tien ich xu ly cac doi tuong Geometry
 */
GeoUtil = {
    /*
     * Lay danh sach cac points from multipolygon
     * @param polysPaths: ArraY(polygonPath) Mang path cua cac polygon
     */
    getPointsFromMultiPolygon: function (polysPaths) {
        var points = [];
        for (var k = 0; k < polysPaths.length; k++) {
            var polyPaths = polysPaths[k];
            for (var i = 0; i < polyPaths.length; i++) {
                var path = polyPaths[i];
                for (var j = 0; j < path.length; j++) {
                    points.push(path[j]);
                }
            }
        }
        return points;
    },

    /* LinhVM:
     * convert dữ liệu bound trên server thành đối tượng bound phía client
     * @param sw, ne
     */
    //    getBoundFromSwNe: function(sw, ne){
    //    return new viettel.LatLngBounds(new viettel.LatLng(sw.y, sw.x), new viettel.LatLng(ne.y, ne.x))
    //    },

    /*
     * Tinh toan ra bounds chua tat ca cac points
     * @param points: Array<viette.LatLng>
     */
    getBoundFromPoints: function (points) {
        var bounds = new viettel.LatLngBounds();
        for (var i = 0; i < points.length; i++) {
            bounds = bounds.extend(points[i]);
        }
        return bounds;
    },
    fitBounds: function (map, points) {
        if (points instanceof viettel.LatLng) {
            map.setCenter(points)
        }
        else if (points instanceof Array && points.length <= 1) {
            map.setCenter(points[0])
        }
        else {
            map.fitBounds(this.getBoundFromPoints(points));
        }
    },

    /*
     * getBoundFromPaths: Tinh toan ra bouns tu paths cua multipolygon
     * @param polygonsPath: ArraY(polygonPath) Mang path cua cac polygon
     */
    getBoundFromPaths: function (polygonsPath) {
        var points = this.getPointsFromMultiPolygon(polygonsPath);
        return this.getBoundFromPoints(points);
    }
}

Util = {
    convertMarkerDataToHtml: function (strLst, imgSrc, mapObj) {
        var htmlContent = "";
        for (var i = 0; i < strLst.length; i++) {
            htmlContent += strLst[i] + "<br/>";
        }

        if (mapObj != null) {
            htmlContent += "<a href='#' onclick=page.showDetailInfoFromMap()> <b>Xem chi tiết</b> </a>";
            htmlContent += "<br/>";
        }

        if (imgSrc != null) {
            htmlContent += "<img src=" + imgSrc + " onclick=showFullImage('" + imgSrc + "') style='width:100px; height:100px;cursor: pointer; ' id='smallMarkerImage'/>"
        }
        //        htmlContent = mapObj.htmlBasicInfo;
        return htmlContent;
    },
    convertFromStrToLatLng: function (str, joiner) {
        var array = this.splitPoints(str, joiner);
        if (array.length != 2) {
            return null;
        }
        else {
            return new viettel.LatLng(array[0], array[1]);
        }
    },
    convertFromLatLngToStr: function (position, joiner) {
        return position.lat() + joiner + position.lng()
    },
    convertFromStrToGeo: function (str, joinerPoint, joinerPath) {
        var array = Util.splitPoints(str, joinerPath);
        var path = [];
        for (var i = 0; i < array.length; i++) {
            path.push(Util.convertFromStrToLatLng(array[i], joinerPoint))
        }
        return path;
    },
    convertFromGeoToStr: function (path, joinerPoint, joinerPath) {
        var str = "";
        var joiner = "";
        for (var i = 0; i < path.getLength(); i++) {
            str += joiner + Util.convertFromLatLngToStr(path.getAt(i), joinerPoint);
            joiner = joinerPath;
        }
        return str;
    },
    splitPoints: function (str, joiner) {
        str = str.toString();
        str.replace(" ", "");
        return str.split(joiner);
    }
}
/*
 * showFullImage: KHi click vao anh trong infowindow se goi toi ham nay
 * @param src: String Thong tin ve duong dan anh
 */
function showFullImage(src) {
    document.getElementById("marker_image_icon").src = src;
    document.getElementById("marker_image_form").style.visibility = "visible";
    document.getElementById("MarkerImageFormCloser").onclick = function () {
        document.getElementById("marker_image_form").style.visibility = "hidden";
    }
}

function showInfoWindow(mapObj, map) {
    var infoWindow = new viettel.InfoWindow();
    var position = Util.convertFromStrToGeo(mapObj.geometry);
    //infoWindow.setContent(Util.convertMarkerDataToHtml(mapObj.shortInfo, mapObj.imgUrl, mapObj));
    infoWindow.setContent(mapObj.basicInfo);
    infoWindow.setPosition(position);
    infoWindow.open(map);
    map.setCenter(position);
    viettel.Events.addListenerOnce(map, "clearLayers", function () {
        infoWindow.close();
    })
}

MapObjInfowindow = {
    infoWindow: null,
    open: function (mapObj, map) {
        if (this.infoWindow != null) {
            this.infoWindow.close();
        }
        MapObjInfowindow.mapObj = mapObj;
        this.infoWindow = new viettel.InfoWindow();
        var position = Util.convertFromStrToGeo(mapObj.geometry);
        //this.infoWindow.setContent(Util.convertMarkerDataToHtml(mapObj.shortInfo, mapObj.imgUrl,mapObj));
        this.infoWindow.setContent(mapObj.basicInfo);
        console.log('mapObj.basicInfo: ' + mapObj.basicInfo);
        this.infoWindow.setPosition(position);
        this.infoWindow.open(map);
        map.setCenter(position);
        viettel.Events.addListenerOnce(map, "clearLayers", function () {
            MapObjInfowindow.infoWindow.close();
        })
    }

}

VectorInfowindow = {
    infoWindow: null,
    open: function (geometry, shortInfo, map) {
        if (this.infoWindow != null) {
            this.infoWindow.close();
        }
        this.infoWindow = new viettel.InfoWindow();
        var position = Util.convertFromStrToGeo(geometry);
        this.infoWindow.setContent(Util.convertMarkerDataToHtml(shortInfo, null, null));
        this.infoWindow.setPosition(position);
        this.infoWindow.open(map);
        map.setCenter(position);
        viettel.Events.addListenerOnce(map, "clearLayers", function () {
            VectorInfowindow.infoWindow.close();
        })
    }

}



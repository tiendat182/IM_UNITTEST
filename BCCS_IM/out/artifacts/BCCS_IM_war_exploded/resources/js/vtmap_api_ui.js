/***************************************************/
/******** Ho tro mot so lop UI hien thi ************/
/***************************************************/

var VTMAP_VIEWROAD_ZOOMLEVEL        = 14;
var current_geocode_ui = null;
var current_geoprocessing_ui = null;
var current_routing_ui = null;
var map_box = null;

// Mot so ham tien ich
var isIE = isIEBrowser();
function isIEBrowser() {
    // Detect if the browser is IE or not.
    // If it is not IE, we assume that the browser is NS.
    var IE = document.all?true:false;
    return IE;
}
function objectToString(o) {
    var parse = function(_o) {
        var a = [], t;
        var isIE = isIEBrowser();
        for(var p in _o) {
            var hasOwnProp = false;
            if (isIE)
                    hasOwnProp = Object.prototype.hasOwnProperty.call(_o, p);
            else
                    hasOwnProp = _o.hasOwnProperty(p);
            if (hasOwnProp) {
                t = _o["'" + p + "'"];
                if (t && typeof t == "object")
                    a[a.length]= p + ":{ " + arguments.callee(t).join(", ") + "}";
                else {
                    if (t==undefined)
                        a[a.length] = [ p + ": undefined"];
                    else if (t==null)
                        a[a.length] = [ p + ": null"];
                    else if (t==NaN)
                        a[a.length] = [ p + ": NaN"];
                    else if (typeof t == "string")
                        a[a.length] = [ p+ ": \"" + t.toString() + "\"" ];
                    else if (t.toString!=null)
                        a[a.length] = [ p+ ": " + t.toString()];
                    else
                        a[a.length] = [ p+ ": unknown"];
                }
            }
        }
        return a;
    }

    return "{" + parse(o).join(", ") + "}";
}
function isObject(obj){
    return (obj instanceof Object);
}
function isStringEmpty(str) {
    return (str==null || str==undefined || str.length==null || str.length==undefined || str.length<=0);
}
function createSourceIcon() {
    var size = new viettel.Size(34, 35);
    return new viettel.MarkerImage("images/start_point.png", null, null, null, size);
}
function createTargetIcon() {
    var size = new viettel.Size(34, 35);
    return new viettel.MarkerImage("images/destination_point.png", null, null, null, size);
}
function createBlinkIcon() {
    var size = new viettel.Size(11, 11);
    var offset = new viettel.Point(size.width/2, size.height/2);
    return new viettel.MarkerImage("images/dot_blink.gif", null, null, offset, size);
}
function createPointIcon() {
    var size = new viettel.Size(32, 32);
    return new viettel.MarkerImage("images/marker_point.png", null, null, null, size);
}
function createBlueSearchIcon() {
    var size = new viettel.Size(26, 33);
    return new viettel.MarkerImage("images/marker_blue.png", null, null, null, size);
}
function createRedSearchIcon() {
    var size = new viettel.Size(26, 33);
    return new viettel.MarkerImage("images/marker_red.png", null, null, null, size);
}
function createMarker(map, position, image) {
    var marker = new viettel.Marker ({
        map: map,
        position: position,
        icon: image
    });
    return marker;
}
function createSearchMarker(map, position, image, text) {
    var marker = new viettel.LabelMarker ({
        map: map,
        position: position,
        icon: image,
        labelContent: text,
        labelAnchor: new viettel.Point(13, 30),
        labelClass: "search_label_marker" // the CSS class for the label
    });
    return marker;
}

function getObjectFromID(id) {
    var obj = null;
    if (document.layers) {
        obj = document.layers[id];
    } else if (document.all) {
        obj = document.all[id];
    } else if (document.getElementById) {
        obj = document.getElementById(id);
    }
    return obj;
}
function setDivContent(objDiv, content) {
    if (objDiv!=null) {
        objDiv.innerHTML = content;
    }
}
function openInfoBox(map, position, content) {
    closeInfoBox();
    
    map_box = new viettel.InfoWindow({
        position:position,
        content:content,
        maxWidth:350
    });
    
    map_box.open(map);
    viettel.Events.addListener(map_box, "closeclick", function() {
        //map_box = null;
    });
}
function closeInfoBox() {
    if (isObject(map_box)) {
        map_box.close();
    }
    map_box = null;
}
var LEFT_MENU_WIDTH = 370;
function zoomFitBoundary(map, boundary) {
    if (isObject(map) && isObject(boundary)) {
        if (boundary.isEmpty()) {
            var pt = boundary.getCenter();
            if (isObject(pt)) {
                map.setCenter(pt);
                map.setZoom(VTMAP_VIEWROAD_ZOOMLEVEL);
            }
        }
        else {
            // Hieu chinh do co menu trai
            var sw = boundary.getSouthWest();
            var ne = boundary.getNorthEast();
            // var pt1 = map.fromLatLngToContainerPixel(sw);
            // var pt2 = map.fromLatLngToContainerPixel(ne);
			// var dx = LEFT_MENU_WIDTH*Math.abs(ne.lng() - sw.lng())/(pt2.x - pt1.x);
            // var ll = new viettel.LatLng(sw.lat(), sw.lng() - dx);
            // boundary = boundary.extend(ll);
            map.fitBounds(boundary);
        }
    }
}
function roundNumber(number, decimal) {
    var fixValue = parseFloat(Math.pow(10, decimal));
    var retValue = parseInt(Math.round(number * fixValue)) / fixValue;
    return retValue;
}
function getDistanceStr(distance) {
    var str = "";
    if (distance>=10000) {
        str = roundNumber(distance/1000, 1);
        str += " km";
    }
    else if (distance>=1000) {
        str = roundNumber(distance/1000, 2);
        str += " km";
    }
    else {
        str = roundNumber(distance, 0);
        str += " m";
    }
    return str;
}

// Lop ho tro hien thi ket qua tim kiem geocode
function VTGeocodeUI(_map, divResultID) {
    var map = _map;
    var objDivResult = getObjectFromID(divResultID);
    var markers = null;

    var geocode = new viettel.GeoService();
    var iconRedSearch = createRedSearchIcon();
    var iconBlueSearch = createBlueSearchIcon();
    var geocode_result = null;
    
    var prepareSearchHandler = null;
    var searchCompleteHander = null;
    var itemClickHandler = null;
    var itemFromHereHandler = null;
    var itemToHereHandler = null;
    var itemZoomInHandler = null;
    var itemSearchAroundHandler = null;

    var search_text = "";
    var currentPage = 1;
    var that = this;

    var getLimit = function() {
        return 10;
    }
    var getOffset = function() {
        return (currentPage-1)*getLimit();
    }
    
    this.setPrepareSearchHandler = function(handler) {
        prepareSearchHandler = handler;
    }
    this.setSearchCompleteHander = function(handler) {
        searchCompleteHander = handler;
    }
    this.setItemClickHandler = function(handler) {
        itemClickHandler = handler;
    }
    this.setItemFromHereHandler = function(handler) {
        itemFromHereHandler = handler;
    }
    this.setItemToHereHandler = function(handler) {
        itemToHereHandler = handler;
    }
    this.setItemZoomInHandler = function(handler) {
        itemZoomInHandler = handler;
    }
    this.setItemSearchAroundHandler = function(handler) {
        itemSearchAroundHandler = handler;
    }
    
    this.setPage = function(page) {
        currentPage = page;
    }
    
    this.onResultPage = function(page) {
        this.setPage(page);
        this.research();
    }

    this.clear = function() {
        geocode_result = null;
        setHTMLResult("");
        clearMarkers();
    }
    
    var _search = function(searchText) {
        closeInfoBox();
        geocode_result = null;
		search_text = searchText;
        if (prepareSearchHandler!=null)
            prepareSearchHandler();
        setHTMLResult("<div align='center'><img src='images/waiting.gif' border=0 /><br>Đang thực hiện tìm kiếm theo tên, xin vui lòng chờ trong giây lát...</div>");
        clearMarkers();
        geocode.setOffset(getOffset());
        geocode.setLimit(getLimit());
        geocode.getLocations(encodeURIComponent(searchText), function(result, status) {
            switch (status) {
                case viettel.GeoServiceStatus.OK:
                    {
                        geocode_result = result;
                        setHTMLResult(getResultHTML());
                        updateToMap();
                        zoomToSearchBoundary();
                    }
                    break;
                case viettel.GeoServiceStatus.ZERO_RESULTS:
                    setHTMLResult("Không tìm thấy đối tượng nào. Xin bạn vui lòng xem lại thông tin đầu vào!");
                    break;
                case viettel.GeoServiceStatus.INVALID_REQUEST:
                    setHTMLResult("Thông tin đầu vào không đúng!");
                    break;
                default:
                    setHTMLResult("Không tìm thấy kết quả nào. Xin xem lại thông tin đầu vào!");
                    break;
            }
            if (searchCompleteHander!=null)
                searchCompleteHander(status);
        });
    }
    
    this.search = function(searchText) {
        currentPage = 1;
        current_geocode_ui = this;
        _search(searchText);
    }
    
    this.research = function() {
        if (!isStringEmpty(search_text)) {
            current_geocode_ui = this;
            _search(search_text);
        }
        else {
            setHTMLResult("Thông tin đầu vào không đúng. Xin xem lại thông tin đầu vào!");
            if (searchCompleteHander!=null)
                searchCompleteHander(viettel.GeoServiceStatus.INVALID_REQUEST);
        }
    }
    
    var setHTMLResult = function(content) {
        setDivContent(objDivResult, content);
    }
    
    var addMarker = function(marker) {
        if (markers==null)
            markers = new Array();
        markers.push(marker);
    }
    
    var clearMarkers = function() {
        if (!isObject(markers) || !isObject(map)) return;
        for (var i=0; i<markers.length; i++) {
            markers[i].setMap(null);
            markers[i] = null;
        }
        markers = null;
    }
    
    var getBoundary = function() {
        if (!isObject(geocode_result) || geocode_result.total<0)
            return null;
        var items = geocode_result.items;
        if (!isObject(items)) return null;
        
        var boundary = new viettel.LatLngBounds();
        var nPt = items.length;
        for (var i=0; i<nPt; i++)
            boundary = boundary.extend(items[i].location);
        return boundary;
    }

    var zoomToSearchBoundary = function() {
        var boundary = getBoundary();
        zoomFitBoundary(map, boundary);
    }
    
    var getResultHTML = function() {       
        var htmlReport = "";
        if (!isObject(geocode_result) || geocode_result.total<0)
            return htmlReport;
        
        current_geocode_ui = that;
        var offset = getOffset();
        var limit = getLimit();
        var items = geocode_result.items;
        var itemCount = items.length;
        var total = geocode_result.total;

        htmlReport += '<div class="search_summary">Kết quả từ ' + (offset+1) + ' đến ' + (offset + itemCount) + ' trong tổng số ' + total + ' cho <b>\'' + search_text + '\'</b></div>';
        for (var i=0; i<itemCount; i++) {
            var item = items[i];
            htmlReport += '<div id="" class="search_item">';
            
            if (item.type==viettel.GeoObjType.ROAD) {
                htmlReport += '<div class="search_blue_icon">' + (offset+i+1) + '</div> ';
                htmlReport += '<div class="search_itemname" onclick="current_geocode_ui.onItemClick(' + i + ');">' + item.name + '</div>';
            } 
            else {
                htmlReport += '<div class="search_red_icon">' + (offset+i+1) + '</div> ';
                htmlReport += '<div class="search_itemname" onclick="current_geocode_ui.onItemClick(' + i + ');">' + item.name + '</div>';
            }
			htmlReport += '<div class="search_itemaddr"><b>Địa chỉ</b>: ' + (isStringEmpty(item.address)?"N/A":item.address) + '</div>';
            
            htmlReport += '<div class="search_itemlink">';
            htmlReport += '<a class="text_link" href="javascript:current_geocode_ui.onItemFromHere(' + i + ');">Từ đây</a>';
            htmlReport += ' <a class="text_link" href="javascript:current_geocode_ui.onItemToHere(' + i + ');">Đến đây</a>';
            htmlReport += ' <a class="text_link" href="javascript:current_geocode_ui.onItemZoomIn(' + i + ');">Phóng to</a>';
            htmlReport += ' <a class="text_link" href="javascript:current_geocode_ui.onItemSearchAround(' + i + ');">Tìm xung quanh</a>';
            htmlReport += '</div>';
            
            htmlReport += '</div>';
        }
        
        var pageLink = getPageLink(total, offset, limit, currentPage);
        if (!isStringEmpty(pageLink)) {
            htmlReport += pageLink;
        }
        
        return htmlReport;
    }
    
    var getPageLink = function(total, offset, limit, curPage) {
        if (total<=limit)
            return "";

        var pageLink = '<div class="search_result_page">';
        var pageNum = parseInt(total/limit);
        if (total>pageNum*limit)
            pageNum += 1;

        if (curPage>1) {
            pageLink += "<a href='javascript:current_geocode_ui.onResultPage(1);'>Đầu</a> ";
        }
        else {
            pageLink += "Đầu ";
        }
        
        var offsetPage = 4;
        var startPage = curPage - offsetPage;
        var endPage = curPage + offsetPage;
        if (startPage<1) {
            startPage = 1;
            endPage = startPage +2*offsetPage;
        } 
        if (endPage>pageNum){
            endPage = pageNum;
            startPage = endPage - 2*offsetPage;
            if (startPage<1) startPage = 1;
        }
        
        for (var i=startPage; i<=endPage; i++) {
            if (i!=curPage)
                pageLink += "<a href='javascript:current_geocode_ui.onResultPage(" + i + ");'>" + i + "</a> ";
            else
                pageLink += i + " ";
        }

        if (curPage<pageNum) {
            pageLink += "<a href='javascript:current_geocode_ui.onResultPage(" + pageNum + ");'>Cuối</a> ";
        }
        else {
            pageLink += "Cuối ";
        }
        pageLink += "</div>";
        
        return pageLink;
    }
    
    var onItemMapClick = function(numberLabel) {
        closeInfoBox();
        var idx = parseInt(numberLabel) - getOffset() - 1;
        var item = getItem(idx);
        if (!isObject(item)) return;

        if (isObject(map)) {
            openInfoBox(map, item.location, getItemHTML(idx));
        }
    }
    
    var addClickEventToMarker = function(marker) {
        viettel.Events.addListener(marker, 'click', function(evt){
            onItemMapClick(marker.getLabelContent());
        });
    }
    
    var updateToMap = function() {
        if (!isObject(geocode_result) || geocode_result.total<=0)
            return;

        var items = geocode_result.items;
        clearMarkers();
        var offset = getOffset();
        var itemCount = items.length;
        for (var i=0; i<itemCount; i++) {
            var item = items[i];
            var marker = null;
            if (item.type==viettel.GeoObjType.ROAD)
                marker = createSearchMarker(map, item.location, iconBlueSearch, (offset + i + 1));
            else
                marker = createSearchMarker(map, item.location, iconRedSearch, (offset + i + 1));
            addClickEventToMarker(marker);
            addMarker(marker);
        }
    }
    
    var getItem = function(idx) {
        if (!isObject(geocode_result) || geocode_result.total<=0)
            return null;
        return geocode_result.items[idx];
    }
    var getItemHTML = function(idx) {
        var item = getItem(idx);

        var htmlReport = "";
        if (!isObject(item))
            return htmlReport;
        htmlReport += '<div class="search_itemname">' + item.name + '</div>';        
        htmlReport += '<div class="line_seperator"></div>';

		var imageLink = item.getImageLink(true);
		if (imageLink!=null) {
			// Hien thi anh
			htmlReport += '<div>';
			htmlReport += '<div style="width:122px; float:left; display:inline-block; padding:5px;">';
			htmlReport += '<a href="' + item.getImageLink(false) + '" target="_blank">';
			htmlReport += '<img src="' + imageLink + '" width="112px" height="150px">';
			htmlReport += '</a>';
			htmlReport += '</div>';
			htmlReport += '<div style="margin-left:122px; height:160px;">';
		}
		htmlReport += '<div class="search_itemaddr"><b>Địa chỉ</b>: ' + (isStringEmpty(item.address)?"N/A":item.address) + '</div>';
		htmlReport += '<div class="search_itememail"><b>Email</b>: ' + (isStringEmpty(item.email)?"N/A":item.email) + '</div>';
        htmlReport += '<div class="search_itemweb"><b>Web</b>: ' + (isStringEmpty(item.website)?"N/A":item.website) + '</div>';
		htmlReport += '<div class="search_itemphone"><b>SĐT</b>: ' + (isStringEmpty(item.phone)?"N/A":item.phone) + '</div>';
		htmlReport += '<div class="search_itemphone"><b>Fax</b>: ' + (isStringEmpty(item.fax)?"N/A":item.fax) + '</div>';
		if (imageLink!=null) {
			htmlReport += '</div>';
			htmlReport += '</div>';
		}

		htmlReport += '<div class="line_seperator"></div>';
        htmlReport += '<div class="search_infobox_itemlink">';
        htmlReport += '<a class="text_link" href="javascript:current_geocode_ui.onItemFromHere(' + idx + ');">Từ đây</a>';
        htmlReport += ' <a class="text_link" href="javascript:current_geocode_ui.onItemToHere(' + idx + ');">Đến đây</a>';
        htmlReport += ' <a class="text_link" href="javascript:current_geocode_ui.onItemZoomIn(' + idx + ');">Phóng to</a>';
        htmlReport += ' <a class="text_link" href="javascript:current_geocode_ui.onItemSearchAround(' + idx + ');">Tìm xung quanh</a>';
        htmlReport += '</div>';

        return htmlReport;
    }
    
    this.onItemClick = function(idx) {
        closeInfoBox();
        var item = getItem(idx);
        if (!isObject(item)) return;
        
        if (isObject(itemClickHandler))
            itemClickHandler(item);
        else if (isObject(map)) {
            map.panTo(item.location);
            openInfoBox(map, item.location, getItemHTML(idx));
        }
    }
    
    this.onItemFromHere = function(idx) {
        closeInfoBox();
        var item = getItem(idx);
        if (!isObject(item)) return;
        
        if (isObject(itemFromHereHandler))
            itemFromHereHandler(item);
    }

    this.onItemToHere = function(idx) {
        closeInfoBox();
        var item = getItem(idx);
        if (!isObject(item)) return;
        
        if (isObject(itemToHereHandler))
            itemToHereHandler(item);
    }
    
    this.onItemZoomIn = function(idx) {
        closeInfoBox();
        var item = getItem(idx);
        if (!isObject(item)) return;
        if (isObject(itemZoomInHandler)) {
            itemZoomInHandler(item);
        }
        else if (isObject(map)) {
            map.setCenter(item.location);
            var zoom = map.getZoom();
            if (zoom<VTMAP_VIEWROAD_ZOOMLEVEL)
                zoom = VTMAP_VIEWROAD_ZOOMLEVEL;
            else
                zoom = zoom + 1;
            map.setZoom(zoom);
            openInfoBox(map, item.location, getItemHTML(idx));
        }
    }
    
    this.onItemSearchAround = function(idx) {
        closeInfoBox();
        var item = getItem(idx);
        if (!isObject(item)) return;
        
        if (isObject(itemSearchAroundHandler))
            itemSearchAroundHandler(item);
    }
}

// Lop ho tro hien thi ket qua tim kiem geoprocessing
var arrPOINames = new Array(
    'Tất cả',
    "Trạm xăng",
	"Dịch vụ ô tô",
	"ATM",
	"Ngân hàng",
	"Giáo dục",
	"Chăm sóc sức khỏe",
	"Vui chơi, giải trí",
	"Chính phủ",
	"Thể thao, thư giãn",
	"Dịch vụ công cộng",
	"Tòa nhà",
    "Mua sắm",
    "Phong cảnh",
	"Địa điểm giao thông",
	"Ủy ban nhân dân"
);
function VTGeoprocessUI(_map, divResultID) {
    var map = _map;
    var objDivResult = getObjectFromID(divResultID);
    var baseMarker = null;
    var markers = null;

    var geoprocess = new viettel.GeoService();
    var iconRedSearch = createRedSearchIcon();
    var iconBlueSearch = createBlueSearchIcon();
    var iconBasePoint = createPointIcon();
    var geoprocess_result = null;
    
    var prepareSearchHandler = null;
    var searchCompleteHander = null;
    var itemClickHandler = null;
    var itemFromHereHandler = null;
    var itemToHereHandler = null;
    var itemZoomInHandler = null;
    var itemSearchAroundHandler = null;
    
    var pt = null;
    var name = null;
    var types = new Array(viettel.GeoObjType.ALL);
    var radius = 5000;
    var currentPage = 1;
    
    var that = this;

    var getLimit = function() {
        return 10;
    }
    var getOffset = function() {
        return (currentPage-1)*getLimit();
    }
    
    this.setPrepareSearchHandler = function(handler) {
        prepareSearchHandler = handler;
    }
    this.setSearchCompleteHander = function(handler) {
        searchCompleteHander = handler;
    }
    this.setItemClickHandler = function(handler) {
        itemClickHandler = handler;
    }
    this.setItemFromHereHandler = function(handler) {
        itemFromHereHandler = handler;
    }
    this.setItemToHereHandler = function(handler) {
        itemToHereHandler = handler;
    }
    this.setItemZoomInHandler = function(handler) {
        itemZoomInHandler = handler;
    }
    this.setItemSearchAroundHandler = function(handler) {
        itemSearchAroundHandler = handler;
    }
    
    this.setPage = function(page) {
        currentPage = page;
    }
    
    this.onResultPage = function(page) {
        this.setPage(page);
        this.research();
    }

    this.clear = function() {
        geoprocess_result = null;    
        pt = null;
        name = null;
        types = new Array(viettel.GeoObjType.ALL);
        radius = 5000;
        currentPage = 1;

        setHTMLResult("");
        clearMarkers();
    }
    
    var getHTMLComboLayer = function() {
        var htmlCombo = "";
        htmlCombo += '<input type="radio" id="geoprocess_poi_all_id" name="geoprocess_poi_all" value="0" checked="checked" onclick="current_geoprocessing_ui.onPOIAllTypeChange();" />Tất cả &nbsp;&nbsp;<input type="radio" name="geoprocess_poi_all" value="1" onclick="current_geoprocessing_ui.onPOIAllTypeChange();" /> Tùy chọn<p>';
        htmlCombo += '<select id="geoprocess_poi_type_id" size="5" style="width:200px" multiple disabled>';
        var length = arrPOINames.length;
        for (var i=1; i<length; i++) {
            htmlCombo += '<option value="' + i + '" >' + arrPOINames[i] + '</option>';
        }
        htmlCombo += '</select>';
        return htmlCombo;
    }
    
    var getHTMLForm = function(_pt, _name) {
        pt = _pt;
        name = _name;

        if (isStringEmpty(name) && isObject(pt))
            name = pt.toString();
        
        var htmlForm = "";
        htmlForm += '<div class="geoprocess_form_title">Tìm POIs gần \'' + name + '\'</div>';
        htmlForm += '<div class="line_seperator"></div>';
        htmlForm += '<div class = "geoprocess_form_input">';
        htmlForm += '<table border=0 cellSpacing=0 cellPadding=0 width=100%>';
		htmlForm += '<tr>';
        htmlForm += '<td valign=top align=right width=150 style="padding-top: 4px;">Nhập chữ:</td>';
        htmlForm += '<td>' + '<input id="geoprocess_search_text" type="text" style="width:200px"></input>' + '</td>';
        htmlForm += '</tr>';
        htmlForm += '<tr>';
        htmlForm += '<td valign=top align=right width=150 style="padding-top: 4px;">Kiểu:</td>';
        htmlForm += '<td>' + getHTMLComboLayer() + '</td>';
        htmlForm += '</tr>';
        htmlForm += '<tr>';
        htmlForm += '<td align=right>Khoảng cách:</td>';
        htmlForm += '<td><input type="number" id="geoprocess_radius_input_id" value="5000" maxlength=8 style="width:170px;" onkeypress="var key = event.charCode; var code = event.keyCode; if ((key>=48 && key<=57) || key==46 || code==8 || code==46) {return true;}; return false;" /> (m)</td>';
        htmlForm += '</tr>';
        htmlForm += '<tr>';
        htmlForm += '<td colspan="2"><div class="line_seperator"></div></td>';
        htmlForm += '</tr>';
        htmlForm += '<tr>';
        htmlForm += '<td colspan=2><div class = "geoprocess_form_button"><input type="button" value="Tìm" style="width:80px;" onclick="current_geoprocessing_ui.onNearestSearchFormSearch();" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="s1" value="Đóng" style="width:80px;" onclick="current_geoprocessing_ui.onNearestSearchFormClose();"/></div></td>';
        htmlForm += '</tr>';
        htmlForm += '</table>';
        htmlForm += '</div>';

        return htmlForm;
    }
    
    this.openNearestSearchForm = function(_pt, _name, comleteHandler) {
        onComleteHandler = comleteHandler;
        current_geoprocessing_ui = that;
        pt = _pt;
        name = _name;
        if (isObject(map) && isObject(pt)) {
            removeBaseMarker();
            baseMarker = createMarker(map, pt, iconBasePoint);
            openInfoBox(map, pt, getHTMLForm(pt, name));
        }
    }
    
    this.onNearestSearchFormClose = function() {
        closeInfoBox();
    }
    
    this.onPOIAllTypeChange = function() {
        var objPOIAll = getObjectFromID('geoprocess_poi_all_id');
        var objPOIType = getObjectFromID('geoprocess_poi_type_id');
        if (objPOIAll.checked) {
            objPOIType.disabled = true;
        }
        else {
            objPOIType.disabled = false;
        }
    }
    
    var getPOITypesFromFormSearch = function() {
        var types = new Array();
        
        var objPOIAll = getObjectFromID('geoprocess_poi_all_id');
        var objPOITypes = getObjectFromID('geoprocess_poi_type_id');
        if (!objPOIAll.checked) {
            for (i=0; i<objPOITypes.options.length; i++) {
                if (objPOITypes.options[i].selected) {
                    types.push(VTUtil.parseInteger(objPOITypes.options[i].value));
                }
            }
        }
        
        if (types.length==0)
            types.push(0);
        
        return types;
    }
    
    this.onNearestSearchFormSearch = function() {
        var objInput = getObjectFromID('geoprocess_radius_input_id');
        radius = parseInt(objInput.value)*1.0;
		var searchtext = getObjectFromID('geoprocess_search_text').value;
		if(searchtext != null) {
			searchtext = encodeURIComponent(searchtext);
		}
        types = getPOITypesFromFormSearch();
        
        closeInfoBox();
        
        this.search(pt, types, radius, searchtext);
    }
    
    var _search = function(_pt, _types, _radius, _searchtext) {
        closeInfoBox();
        geoprocess_result = null;
        
        pt = _pt;
        types = _types;
        radius = _radius;
		searchtext = _searchtext;
        if (prepareSearchHandler!=null)
            prepareSearchHandler();
        setHTMLResult("<div align='center'><img src='images/waiting.gif' border=0 /><br>Đang thực hiện tìm kiếm xung quanh, xin vui lòng chờ trong giây lát...</div>");
        clearMarkers();
        geoprocess.setOffset(getOffset());
        geoprocess.setLimit(getLimit());
        geoprocess.getLocations(pt, radius, types, searchtext, function(result, status) {
            switch (status) {
                case viettel.GeoServiceStatus.OK:
                    {
                        geoprocess_result = result;
                        setHTMLResult(getResultHTML());
                        updateToMap();
                        zoomToSearchBoundary();
                    }
                    break;
                case viettel.GeoServiceStatus.ZERO_RESULTS:
                    setHTMLResult("Không tìm thấy đối tượng nào. Xin bạn vui lòng xem lại thông tin đầu vào");
                    break;
                case viettel.GeoServiceStatus.INVALID_REQUEST:
                    setHTMLResult("Thông tin đầu vào không đúng!");
                    break;
                default:
                    setHTMLResult("Không tìm thấy kết quả nào. Xin xem lại thông tin đầu vào!");
                    break;
            }
            if (searchCompleteHander!=null)
                searchCompleteHander(status);
        });
    }
    
    this.search = function(_pt, _types, _radius, _searchtext) {
        currentPage = 1;
        current_geoprocessing_ui = this;
        _search(_pt, _types, _radius, _searchtext);
    }
    
    this.research = function() {
        if (isObject(pt)) {
            current_geoprocessing_ui = this;
            _search(pt, types, radius);
        }
        else {
            setHTMLResult("Thông tin đầu vào không đúng. Xin xem lại thông tin đầu vào!");
            if (searchCompleteHander!=null)
                searchCompleteHander(viettel.GeoServiceStatus.INVALID_REQUEST);
        }
    }
    
    var setHTMLResult = function(content) {
        setDivContent(objDivResult, content);
    }
    
    var addMarker = function(marker) {
        if (markers==null)
            markers = new Array();
        markers.push(marker);
    }
    
    var removeBaseMarker = function() {
        if (isObject(baseMarker))
            baseMarker.setMap(null);
        baseMarker = null;
    }
    
    var clearMarkers = function() {
        removeBaseMarker();
        if (!isObject(markers) || !isObject(map)) return;
        for (var i=0; i<markers.length; i++) {
            markers[i].setMap(null);
            markers[i] = null;
        }
        markers = null;
    }
    
    var getBoundary = function() {
        if (!isObject(geoprocess_result) || geoprocess_result.total<0)
            return null;
        var items = geoprocess_result.items;
        if (!isObject(items)) return null;
        
        var boundary = new viettel.LatLngBounds();
        var nPt = items.length;
        for (var i=0; i<nPt; i++)
            boundary = boundary.extend(items[i].location);
        return boundary;
    }
    
    var zoomToSearchBoundary = function() {
        var boundary = getBoundary();
        zoomFitBoundary(map, boundary);
    }
    
    var getResultHTML = function() {       
        var htmlReport = "";
        if (!isObject(geoprocess_result) || geoprocess_result.total<0)
            return htmlReport;
        
        current_geoprocessing_ui = that;
        var offset = getOffset();
        var limit = getLimit();
        var items = geoprocess_result.items;
        var itemCount = items.length;
        var total = geoprocess_result.total;

        htmlReport += '<div class="search_summary">Kết quả từ ' + (offset+1) + ' đến ' + (offset + itemCount) + ' trong tổng số ' + total + ' đối tượng xung quanh <b>\'' + name + '\'</b></div>';
        for (var i=0; i<itemCount; i++) {
            var item = items[i];
            htmlReport += '<div id="" class="search_item">';

            if (item.type==viettel.GeoObjType.ROAD) {
                htmlReport += '<div class="search_blue_icon">' + (offset+i+1) + '</div> ';
                htmlReport += '<div class="search_distance_float">' + getDistanceStr(viettel.GeometryUtil.getDistanceBetween(pt, item.location)) + '</div>';
                htmlReport += '<div class="search_itemname" onclick="current_geoprocessing_ui.onItemClick(' + i + ');">' + item.name + '</div>';
            } 
            else {
                htmlReport += '<div class="search_red_icon">' + (offset+i+1) + '</div> ';
                htmlReport += '<div class="search_distance_float">' + getDistanceStr(viettel.GeometryUtil.getDistanceBetween(pt, item.location)) + '</div>';
                htmlReport += '<div class="search_itemname" onclick="current_geoprocessing_ui.onItemClick(' + i + ');">' + item.name + '</div>';
            }
            htmlReport += '<div class="search_itemaddr"><b>Địa chỉ</b>: ' + (isStringEmpty(item.address)?"N/A":item.address) + '</div>';
            
            htmlReport += '<div class="search_itemlink">';
            htmlReport += '<a class="text_link" href="javascript:current_geoprocessing_ui.onItemFromHere(' + i + ');">Từ đây</a>';
            htmlReport += ' <a class="text_link" href="javascript:current_geoprocessing_ui.onItemToHere(' + i + ');">Đến đây</a>';
            htmlReport += ' <a class="text_link" href="javascript:current_geoprocessing_ui.onItemZoomIn(' + i + ');">Phóng to</a>';
            htmlReport += ' <a class="text_link" href="javascript:current_geoprocessing_ui.onItemSearchAround(' + i + ');">Tìm xung quanh</a>';
            htmlReport += '</div>';
            
            htmlReport += '</div>';
        }
        
        var pageLink = getPageLink(total, offset, limit, currentPage);
        if (!isStringEmpty(pageLink)) {
            htmlReport += pageLink;
        }
        
        return htmlReport;
    }
    
    var getPageLink = function(total, offset, limit, curPage) {
        if (total<=limit)
            return "";

        var pageLink = '<div class="search_result_page">';
        var pageNum = parseInt(total/limit);
        if (total>pageNum*limit)
            pageNum += 1;

        if (curPage>1) {
            pageLink += "<a href='javascript:current_geoprocessing_ui.onResultPage(1);'>Đầu</a> ";
        }
        else {
            pageLink += "Đầu ";
        }
        
        var offsetPage = 4;
        var startPage = curPage - offsetPage;
        var endPage = curPage + offsetPage;
        if (startPage<1) {
            startPage = 1;
            endPage = startPage +2*offsetPage;
        } 
        if (endPage>pageNum){
            endPage = pageNum;
            startPage = endPage - 2*offsetPage;
            if (startPage<1) startPage = 1;
        }
        
        for (var i=startPage; i<=endPage; i++) {
            if (i!=curPage)
                pageLink += "<a href='javascript:current_geoprocessing_ui.onResultPage(" + i + ");'>" + i + "</a> ";
            else
                pageLink += i + " ";
        }

        if (curPage<pageNum) {
            pageLink += "<a href='javascript:current_geoprocessing_ui.onResultPage(" + pageNum + ");'>Cuối</a> ";
        }
        else {
            pageLink += "Cuối ";
        }
        pageLink += "</div>";
        
        return pageLink;
    }
    
    var onItemMapClick = function(numberLabel) {
        closeInfoBox();
        var idx = parseInt(numberLabel) - getOffset() - 1;
        var item = getItem(idx);
        if (!isObject(item)) return;

        if (isObject(map)) {
            openInfoBox(map, item.location, getItemHTML(idx));
        }
    }
    
    var addClickEventToMarker = function(marker) {
        viettel.Events.addListener(marker, 'click', function(evt){
            onItemMapClick(marker.getLabelContent());
        });
    }
    
    var updateToMap = function() {
        if (!isObject(geoprocess_result) || geoprocess_result.total<=0)
            return;

        var items = geoprocess_result.items;
        clearMarkers();
        baseMarker = createMarker(map, pt, iconBasePoint);
        var offset = getOffset();
        var itemCount = items.length;
        for (var i=0; i<itemCount; i++) {
            var item = items[i];
            var marker = null;
            if (item.type==viettel.GeoObjType.ROAD)
                marker = createSearchMarker(map, item.location, iconBlueSearch, (offset + i + 1));
            else
                marker = createSearchMarker(map, item.location, iconRedSearch, (offset + i + 1));
            addClickEventToMarker(marker);
            addMarker(marker);
        }
    }
    
    var getItem = function(idx) {
        if (!isObject(geoprocess_result) || geoprocess_result.total<=0)
            return null;
        return geoprocess_result.items[idx];
    }
    var getItemHTML = function(idx) {
        var item = getItem(idx);

        var htmlReport = "";
        if (!isObject(item))
            return htmlReport;
        htmlReport += '<div class="search_itemname">' + item.name + '</div>';        
        htmlReport += '<div class="line_seperator"></div>';

		var imageLink = item.getImageLink(true);
		if (imageLink!=null) {
			// Hien thi anh
			htmlReport += '<div>';
			htmlReport += '<div style="width:122px; float:left; display:inline-block; padding:5px;">';
			htmlReport += '<a href="' + item.getImageLink(false) + '" target="_blank">';
			htmlReport += '<img src="' + imageLink + '" width="112px" height="150px">';
			htmlReport += '</a>';
			htmlReport += '</div>';
			htmlReport += '<div style="margin-left:122px; height:160px;">';
		}
		htmlReport += '<div class="search_itemaddr"><b>Địa chỉ</b>: ' + (isStringEmpty(item.address)?"N/A":item.address) + '</div>';
		htmlReport += '<div class="search_itememail"><b>Email</b>: ' + (isStringEmpty(item.email)?"N/A":item.email) + '</div>';
        htmlReport += '<div class="search_itemweb"><b>Web</b>: ' + (isStringEmpty(item.website)?"N/A":item.website) + '</div>';
		htmlReport += '<div class="search_itemphone"><b>SĐT</b>: ' + (isStringEmpty(item.phone)?"N/A":item.phone) + '</div>';
		htmlReport += '<div class="search_itemphone"><b>Fax</b>: ' + (isStringEmpty(item.fax)?"N/A":item.fax) + '</div>';
		if (imageLink!=null) {
			htmlReport += '</div>';
			htmlReport += '</div>';
		}
        htmlReport += '<div class="line_seperator"></div>';
        htmlReport += '<div class="search_infobox_itemlink">';
        htmlReport += '<a class="text_link" href="javascript:current_geoprocessing_ui.onItemFromHere(' + idx + ');">Từ đây</a>';
        htmlReport += ' <a class="text_link" href="javascript:current_geoprocessing_ui.onItemToHere(' + idx + ');">Đến đây</a>';
        htmlReport += ' <a class="text_link" href="javascript:current_geoprocessing_ui.onItemZoomIn(' + idx + ');">Phóng to</a>';
        htmlReport += ' <a class="text_link" href="javascript:current_geoprocessing_ui.onItemSearchAround(' + idx + ');">Tìm xung quanh</a>';
        htmlReport += '</div>';

        return htmlReport;
    }
    
    this.onItemClick = function(idx) {
        closeInfoBox();
        var item = getItem(idx);
        if (!isObject(item)) return;
        
        if (isObject(itemClickHandler))
            itemClickHandler(item);
        else if (isObject(map)) {
            map.panTo(item.location);
            openInfoBox(map, item.location, getItemHTML(idx));
        }
    }
    
    this.onItemFromHere = function(idx) {
        closeInfoBox();
        var item = getItem(idx);
        if (!isObject(item)) return;
        
        if (isObject(itemFromHereHandler))
            itemFromHereHandler(item);
    }

    this.onItemToHere = function(idx) {
        closeInfoBox();
        var item = getItem(idx);
        if (!isObject(item)) return;
        
        if (isObject(itemToHereHandler))
            itemToHereHandler(item);
    }
    
    this.onItemZoomIn = function(idx) {
        closeInfoBox();
        var item = getItem(idx);
        if (!isObject(item)) return;
        
        if (isObject(itemZoomInHandler))
            itemZoomInHandler(item);
        else if (isObject(map)) {
            map.setCenter(item.location);
            var zoom = map.getZoom();
            if (zoom<VTMAP_VIEWROAD_ZOOMLEVEL)
                zoom = VTMAP_VIEWROAD_ZOOMLEVEL;
            else
                zoom = zoom + 1;
            map.setZoom(zoom);
            openInfoBox(map, item.location, getItemHTML(idx));
        }
    }
    
    this.onItemSearchAround = function(idx) {
        closeInfoBox();
        var item = getItem(idx);
        if (!isObject(item)) return;
        
        if (isObject(itemSearchAroundHandler))
            itemSearchAroundHandler(item);
    }
}

function VTRouteUI(_map, divMainPanelId, divAlternativePanelId, travelModeName, inputSourceID, inputTargetID) {
    var map = _map;
	var routingRender = null;
    var objInputSource = $('#' + inputSourceID);
    var objInputTarget = $('#' + inputTargetID);
    var that = this;
    
    var geoprocess = new viettel.GeoService();
    var sourceIcon = createSourceIcon();
    var targetIcon = createTargetIcon();
    var sourceMarker = null;
    var targetMarker = null;
    var sourcePoint = null;
    var sourceName = "";
    var targetPoint = null;
    var targetName = "";

    var removeMarker = function(marker) {
        if (isObject(marker)) {
            marker.setMap(null);
        }
        marker = null;
    }
    
    var clearMarkers = function() {
        removeMarker(sourceMarker);
        removeMarker(targetMarker);
    }

    this.clear = function() {
		routingRender.clear();
        clearInput();
        clearMarkers();
    }
    
    var getSourceName = function() {
        if (!isStringEmpty(sourceName))
            return sourceName;
        else if (isObject(sourcePoint))
            return sourcePoint.toString();
        return "";
    }

    var getTargetName = function() {
        if (!isStringEmpty(targetName))
            return targetName;
        else if (isObject(targetPoint))
            return targetPoint.toString();
        return "";
    }
    
    var isRouteSet = function() {
        return (isObject(sourcePoint) && isObject(targetPoint));
    }
    
    var updateInputs = function() {
        if (isObject(objInputSource))
            objInputSource.val(getSourceName());
        if (isObject(objInputTarget))
            objInputTarget.val(getTargetName());
        closeInfoBox();
    }
    
    var clearInput = function() {
        sourcePoint = null;
        sourceName = "";
        targetPoint = null;
        targetName = "";

        if (isObject(objInputSource))
            objInputSource.val("");
        if (isObject(objInputTarget))
            objInputTarget.val("");
        closeInfoBox();
    }
    
    this.setSource = function(_sourcePoint, _sourceName) {
        sourcePoint = _sourcePoint;
        if (!isStringEmpty(_sourceName))
            sourceName = _sourceName;
        else
            sourceName = "";
        if (isStringEmpty(sourceName)  && isObject(sourcePoint)) {
            geoprocess.getAddress(sourcePoint, function(result, status) {
                if (status==viettel.GeoServiceStatus.OK) {
                    sourceName = result.items[0].name;
                    updateInputs();
                }
            });
        }
        
        removeMarker(sourceMarker);
        sourceMarker = new viettel.Marker({
            map: map,
            position: sourcePoint,
            icon: sourceIcon,
            draggable: true,
            clickable: true
        });
        var that = this;
        viettel.Events.addListenerOnce(sourceMarker, 'dragend', function(evt) {
            that.setSource(evt.latLng);
        });
        
        updateInputs();
        
        if (isRouteSet())
            this.findRoute();
    }
    
    this.setTarget = function(_targetPoint, _targetName) {
        targetPoint = _targetPoint;
        if (!isStringEmpty(_targetName))
            targetName = _targetName;
        else
            targetName = "";
        if (isStringEmpty(targetName) && isObject(targetPoint)) {
            geoprocess.getAddress(targetPoint, function(result, status) {               
                if (status==viettel.GeoServiceStatus.OK) {
                    targetName = result.items[0].name;
                    updateInputs();
                }
            });
        }
        
        removeMarker(targetMarker);
        targetMarker = new viettel.Marker({
            map: map,
            position: targetPoint,
            icon: targetIcon,
            draggable: true,
            clickable: true
        });
        var that = this;
        viettel.Events.addListenerOnce(targetMarker, 'dragend', function(evt) {
            that.setTarget(evt.latLng);
        });
        
        updateInputs();
        
        if (isRouteSet())
            this.findRoute();
    }

    this.findRoute = function() {
        if (isRouteSet()) {
			var request = {
				origin: sourcePoint,
				destination: targetPoint
			};
			routingRender.setRoutingRequest(request);
        }
        else if (isObject(findRouteCompleteHandler))
            findRouteCompleteHandler(viettel.RoutingStatus.INVALID_REQUEST);
    }
	
	this.initRouting = function() {
		routingRender = new viettel.RoutingRender({
			map: map,
			mainPanel: document.getElementById(divMainPanelId),
			hideMarker:false,
			alternativePanel: document.getElementById(divAlternativePanelId),
			showAlternativeRoute: true,
			enableWayPoints: true,
			travelMode: parseInt($('input[name="' + travelModeName + '"]:checked').val()),
			draggable: true
		});
		
		$('input[name="' + travelModeName + '"]').change(function(){
			var travelModeValue = parseInt($('input[name="' + travelModeName + '"]:checked').val());
			routingRender.setOptions({travelMode: travelModeValue});
		});
		
		routingRender.onRoutingCompleteCallback(function(status) {
			clearMarkers();
		});
	}
}
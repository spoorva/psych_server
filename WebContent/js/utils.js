function clone(obj) {
    if (null == obj || "object" != typeof obj) return obj;
    var copy = obj.constructor();
    for (var attr in obj) {
        if (obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
    }
    return copy;
}

function JSONSearch(searchObject, jsonObject){
	
	var keys = [];
	var xPath = '//*';
	
	for (var param in searchObject) {
		//console.log(param + " " + locationParams[param]);
		if(searchObject[param] != ''){
			if(param.toLowerCase().indexOf("id") != -1){
				xPath += '[' + param + '="' + searchObject[param] + '"]'
			}
			else{
				xPath += '[contains(' + param + ',' + '"' + searchObject[param] + '"' + ')]';
			}
			
			keys.push(param);
		}
	}
	
	if(keys.length > 0) {
		var found = JSON.search(jsonObject, xPath);
		
		return found;
	}
	else {
		return jsonObject;
	}
}
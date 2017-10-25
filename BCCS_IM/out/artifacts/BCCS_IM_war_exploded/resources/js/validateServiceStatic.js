/**
 *
 * Validate form ADSL
 * @param object
 * @returns {string}
 */

function isValidFraction(fraction) {

    var arr = fraction.split("/");
    var numerator = parseInt(arr[0], 10);
    var denomirator = parseInt(arr[1], 10);
    if (numerator >= denomirator) {
        return false;
    }

    return true;
}

function is_empty(str) {
    if ((str.trim()).length == 0) {
        return true;
    }
    return false;
}


function validateADSL(object) {
    var message = "";
    console.log(object.resourceConnector);
    var resourceConnector = object.resourceConnector;
    if (!is_empty(resourceConnector)) {
        if (!isValidFraction(resourceConnector)) {
            message += "Tài nguyên hộp cáp không đủ \n";
        }
    }
    console.log(object.resourceRootCable);
    var resourceRootCable = object.resourceRootCable;
    if (!is_empty(resourceRootCable)) {
        if (!isValidFraction(resourceRootCable)) {
            message += "Tài nguyên cáp gốc không đủ \n";
        }
    }
    console.log(object.resourceDevice);
    var resourceDevice = object.resourceDevice;
    if (!is_empty(resourceDevice)) {
        if (!isValidFraction(resourceDevice)) {
            message += "Tài nguyên port (DSLAM) không đủ \n";
        }
    }
    console.log(message);
    return message;
}

function validatePSTN(object) {
    var message = "";
    var resourceConnector = object.resourceConnector;
    if (!is_empty(resourceConnector.val())) {
        if (!isValidFraction(resourceConnector.val())) {
            message += "Tài nguyên hộp cáp không đủ \n";
        }
    }
    var resourceRootCable = object.resourceRootCable;
    if (!is_empty(resourceRootCable.val())) {
        if (!isValidFraction(resourceRootCable.val())) {
            message += "Tài nguyên cáp gốc không đủ \n";
        }
    }
    var resourceDevice = object.resourceDevice;
    if (!is_empty(resourceDevice.val())) {
        if (!isValidFraction(resourceDevice.val())) {
            message += "Tài nguyên port DLU không đủ \n";
        }
    }
    return message;
}

export class ApiCalls {

    static server_path = "http://192.168.0.157";
    static ws_server_path = "http://192.168.0.157:7474";

    constructor() {
        }

    // User-API
    static login(username, password) {
        return fetch(ApiCalls.server_path + "/login", {
            method: 'POST',
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: "username="+username+"&password="+password
        }).then((response) => response.json()).then((json) => ApiCalls._parse_json(json));
    }

    static get_my_user(token) {
        return fetch(ApiCalls.server_path + "/get_my_user", {
            method: 'POST',
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: "token="+token
        }).then((response) => response.json()).then((json) => ApiCalls._parse_json(json));
    }

    static register(username, password) {

    }

    //RentBike API

    static add_rent_bike(rentbikeplace) {
        return fetch(ApiCalls.server_path + "/add_new_rbp", {
            method: 'POST',
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: "token=" + global.token + "&street=" + rentbikeplace.street + "&total=" + rentbikeplace.numberOfBikes + "&available=" + rentbikeplace.numberOfAvailable + "&active=" + rentbikeplace.active
        }).then((response) => response.json()).then((json) => ApiCalls._parse_json(json));
    }

    static delete_rent_bike(rentbikeplace) {
        return fetch(ApiCalls.server_path + "/delete_rbp", {
            method: 'DELETE',
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: "token="+global.token+"&street="+rentbikeplace.street
        }).then((response) => response.json()).then((json) => ApiCalls._parse_json(json));
    }

    static edit_rent_bike(rentbikeplace) {
        return fetch(ApiCalls.server_path + "/edit_rbp", {
            method: 'PUT',
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: "token="+global.token+"&street="+rentbikeplace.street+"&total="+rentbikeplace.numberOfBikes+"&available="+rentbikeplace.numberOfAvailable+"&active="+rentbikeplace.active
        }).then((response) => response.json()).then((json) => ApiCalls._parse_json(json));
    }

    static merge_rent_bike(rentbikeplacelist) {
        return fetch(ApiCalls.server_path + "/merge", {
            method: 'POST',
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: "list="+JSON.stringify(rentbikeplacelist)
        }).then((response) => response.json()).then((json) => ApiCalls._parse_json(json));
    }


    // rental api
    static do_rent(rentbikeplace) {

    }

    static do_unrent(rentbikeplace) {

    }


    static _parse_json(json) {
        try {
            if (json["status"].localeCompare("Success") !== 0) {
                alert(json["reason"]);
                return null;
            }
            return json;
        }
        catch(Exception) {
            console.log("Unhandled exception");
            return null;
        }
    }

}
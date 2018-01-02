export class ApiCalls {

    static server_path = "http://192.168.0.20";

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
        //return fetch(ApiCalls.server_path + "/add_rent_bike")
    }

    static delete_rent_bike(rentbikeplace) {

    }

    static edit_rent_bike(rentbikeplace) {

    }

    static merge_rent_bike(rentbikeplacelist) {

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
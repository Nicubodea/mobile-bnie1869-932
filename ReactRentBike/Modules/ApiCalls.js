export class ApiCalls {

    static server_path = "http://192.168.100.21";

    constructor() {
        }

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
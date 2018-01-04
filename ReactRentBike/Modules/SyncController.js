import React from 'react';
import {ApiCalls} from "./ApiCalls";

export class SyncController extends React.Component {
    constructor(props) {
        super(props);
        this.ws = null;
    }

    networkStateHasChanged(connected) {

        global.devicestate = connected ? "online" : "offline";
        console.log("device state is now " + global.devicestate);

        if(connected) {
            console.log("recreating web socket");

            this.new_web_socket();

            console.log("merging results");
            if(global.is_list_loaded === true) {
                this.merge();
            }
        }
    }

    merge() {
        console.log("[sync controller] merge called");
        ApiCalls.merge_rent_bike(global.rentbikeplaces).then(result => this.merge_result(result));
    }

    new_web_socket() {
        let ws = new WebSocket(ApiCalls.ws_server_path);

        ws.onopen = () => {
        };

        ws.onmessage = (e) => {
            let json = JSON.parse(e.data);

            let rentbikeplace = {street: json.street, numberOfBikes:json.total, numberOfAvailable:json.available, active:json.active};

            console.log(JSON.stringify(json));

            if(json.state.localeCompare("created") === 0) {
                this.element_created(rentbikeplace)
            }
            else if(json.state.localeCompare("deleted") === 0) {
                this.element_deleted(rentbikeplace)
            }
            else if(json.state.localeCompare("edited") === 0) {
                this.element_modified(rentbikeplace, true)
            }
            else
            {
                console.log("Unhandled command received")
            }

        };
        this.ws = ws;
    }

    /*
    After a connection to the server is re-established, we merge our modifications and the server returns what was new since we disconnected based on the merge
     */
    merge_result(result) {

        let final = JSON.parse(result['result']);
        for(let i=0; i<final.length; i++)
        {
            let rentbikeplace = {street: final[i]['street'], numberOfBikes: final[i]['total'], numberOfAvailable: final[i]['available'], active: ""}
            if(final[i]['active'] == 1) {
                rentbikeplace['active'] = "Active";
            }
            else
            {
                rentbikeplace['active'] = "Inactive";
            }
            if(final[i]['state'].localeCompare('created') === 0) {
                this.element_created(rentbikeplace);
            }
            else if(final[i]['state'].localeCompare('deleted') === 0) {
                this.element_deleted(rentbikeplace);
            }
            else if(final[i]['state'].localeCompare('edited') === 0) {
                this.element_modified(rentbikeplace, true);
            }
        }
    }

    /*
    Server just signaled us that someone created a new element, we should add it to the list
     */
    element_created(element) {

        console.log("Create element " + JSON.stringify(element));
        for(let i=0; i<global.rentbikeplaces.length; i++)
        {
            if(global.rentbikeplaces[i].street.localeCompare(element.street) === 0) {
                // already added, probably a notification after we called the API

                // we don't want to propagate the creation again
                global.rentbikeplaces[i].state = "";
                return;
            }
        }

        global.rentbikeplaces.push(element);
        global.vieewlist.update_callback();
        // add this to the cache also
        global.sync.addOne(element.street, element);
    }

    /*
    Server just signaled us that someone deleted an element, we should remove it from the list
     */
    element_deleted(element) {
        console.log("Delete element " + JSON.stringify(element));
        for(let i =0;i<global.rentbikeplaces.length;i++){
            if(global.rentbikeplaces[i].street.localeCompare(element.street)===0){
                global.rentbikeplaces.splice(i, 1);
            }
        }

        global.sync.removeOne(element.street);

        global.vieewlist.update_callback();

    }

    /*
    Server signaled us that someone modified an element from the list, modify in the local list as well
     */
    element_modified(element, called_from_serv) {
        console.log("Modify element " + JSON.stringify(element));
        if(called_from_serv === true)
        {
            element.state = "";
        }

        for(let i =0;i<global.rentbikeplaces.length;i++){
            if(global.rentbikeplaces[i].street.localeCompare(element.street)===0){
                global.rentbikeplaces[i].numberOfBikes = element.numberOfBikes;
                global.rentbikeplaces[i].numberOfAvailable = element.numberOfAvailable;
                global.rentbikeplaces[i].active = element.active;
                global.rentbikeplaces[i].state = element.state;
            }
        }

        global.sync.editOne(element.street, element);

        global.vieewlist.update_callback();

    }



}
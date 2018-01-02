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
            ApiCalls.merge_rent_bike(global.rentbikeplaces).then(result => this.merge_result(result));


        }
    }

    new_web_socket() {
        let ws = new WebSocket('ws://192.168.0.20:7474');

        ws.onopen = () => {
        };

        ws.onmessage = (e) => {
            let json = JSON.parse(e.data);

            if(json.command.localeCompare("created") === 0) {
                this.element_created(json.payload)
            }
            else if(json.command.localeCompare("deleted") === 0) {
                this.element_deleted(json.payload)
            }
            else if(json.command.localeCompare("modified") === 0) {
                this.element_modified(json.payload)
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

    }

    /*
    Server just signaled us that someone created a new element, we should add it to the list
     */
    element_created(payload) {

    }

    /*
    Server just signaled us that someone deleted an element, we should remove it from the list
     */
    element_deleted(payload) {

    }

    /*
    Server signaled us that someone modified an element from the list, modify in the local list as well
     */
    element_modified(payload) {

    }



}
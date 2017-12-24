<?php
/**
 * Created by PhpStorm.
 * User: nbodea
 * Date: 12/24/2017
 * Time: 2:05 AM
 */

class Observer
{
    public $ip;
    private $port;

    public function __construct($ip)
    {
        $this->ip = $ip;
        $this->port = 7447;
    }

    public function notify() {
        /*
         * Creates a new socket to $ip on designated port and send a json with the new data, then close the socket
         *
         */

        $socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);

        // if connection cannot be made we assume the observer is offline and will be deleted.
        $result = socket_connect($socket, $this->ip, $this->port);
        if ($result === false) {
            throw new Exception("The observer is no longer available, will delete the observer from list.");
        }

        $data = null;

        // call DAO to get $data...
        //
        //

        socket_write($socket, json_encode($data), strlen(json_encode($data)));

        socket_close($socket);
    }
}
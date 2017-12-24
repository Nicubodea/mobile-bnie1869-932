<?php
/**
 * Created by PhpStorm.
 * User: nbodea
 * Date: 12/24/2017
 * Time: 12:10 AM
 */

class Notifier
{

    private $observers;

    public static function Instance() {
        static $inst = null;
        if($inst == null)
        {
            $inst = new Notifier();
        }
        return $inst;
    }

    private function __construct()
    {
        $this->observers = [];
    }

    public function new_observer($observer)
    {
        foreach($this->observers as $observer_this)
        {
            if($observer->ip == $observer_this->ip)
            {
                return;
            }
        }
        array_push($this->observers, $observer);
    }

    public function notify_all()
    {
        $index = 0;
        foreach($this->observers as $observer) {
            try {
                $observer->notify();
            }
            catch(Exception $e) {
                $this->observers = array_splice($this->observers, $index, 1);
                $index -= 1;
            }
            $index += 1;
        }
    }

}
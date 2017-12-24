<?php
/**
 * Created by PhpStorm.
 * User: nbodea
 * Date: 12/24/2017
 * Time: 12:08 AM
 */

require_once ("./db_manager.php");
class UserDAO
{
    private $db;

    public static function Instance() {

        static $inst = null;
        if($inst == null)
        {
            $inst = new UserDAO();
        }
        return null;
    }

    private function __construct()
    {
        $this->db = DBUtils::Instance();
    }
}
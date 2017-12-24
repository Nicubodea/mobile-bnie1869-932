<?php

use Firebase\JWT\JWT;

require_once("../vendor/autoload.php");

class JWTService
{
    private $key;
    public function __construct()
    {
        $this->key = "o parola foarte secure";
    }

    public function validateToken($token)
    {
        $arr = array("HS512");

        $token = JWT::decode($token,
            $this->key,
            $arr);

        return json_decode(json_encode($token), true);

    }

    public function createToken($role, $username)
    {
        $data = ["name"=>$username, "role"=>$role, "timestamp" => time()];

        $jwt = JWT::encode(
            $data,      //Data to be encoded in the JWT
            $this->key, // The signing key
            'HS512'     // Algorithm used to sign the token, see https://tools.ietf.org/html/draft-ietf-jose-json-web-algorithms-40#section-3
        );

        return $jwt;
    }
}

<?php
    require('MulticraftAPI.php');
    $api_key = $_GET["api"];
    $panel_url = $_GET["url"];
    $username = $_GET["username"];
    $password = $_GET["password"];
    $new_api_key = base64_decode($api_key);
    $api = new MulticraftAPI($panel_url, 'admin', $new_api_key);

    $email = $username . "@kingdom.com";

    print_r($api->createUser($username, $email, $password));
?>
<?php
    require('MulticraftAPI.php');
    $api_key = $_GET["api"];
    $panel_url = $_GET["url"];
    $server_id = $_GET["server_id"];
    $new_api_key = base64_decode($api_key);
    $api = new MulticraftAPI($panel_url, 'admin', $new_api_key);

    print_r($api->startServer($server_id));
?>
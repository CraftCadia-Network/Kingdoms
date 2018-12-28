<?php
    require('MulticraftAPI.php');
    $api_key = $_GET["api"];
    $panel_url = $_GET["url"];
    $serverId = $_GET["server_id"];
    $server_memory = $_GET['memory'];
    $new_api_key = base64_decode($api_key);
    $api = new MulticraftAPI($panel_url, 'admin', $new_api_key);

    print_r($api->updateServer($serverId, array(
        'memory'
    ),
    array(
        $server_memory
    )
));
?>
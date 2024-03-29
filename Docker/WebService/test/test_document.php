<?php

require_once "send_data.php";

echo "Start anonymization\n";
$ret=sendData("http://127.0.0.1:8111/schedule.php",
[ "input" => json_encode([
    "docid" => "DOCID1",
    "document" => base64_encode("Vreau să-mi cumpăr o mașină. Pe urmă vreau să merg la mare. Când ajung acolo vreau să merg la plajă."),
    "type" => "txt"
])]);

echo "Result: ".var_export($ret,true)."\n";

$data=json_decode($ret,true);
$id=$data['id'];

while(true){
    $ret=file_get_contents("http://127.0.0.1:8111/getResult.php?input=".urlencode(json_encode(["id"=>$id])));
    var_dump($ret);
    $json=json_decode($ret,true);
    if( 
        isset($json['status']) && $json['status']=="OK" &&
        isset($json['result']) && $json['result']=="DONE" &&
        isset($json['document']) 
    ){
        file_put_contents("test1.out.txt",base64_decode($json['document']));
        break;
    }
        
    sleep(1);
}

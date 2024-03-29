<?php

require_once "lib/lib.php";

$in=get_input(["id"]);

$pathDone="${TASK_DIR_DONE}${in['id']}";
$pathNew="${TASK_DIR_NEW}${in['id']}";
$pathPrio="${TASK_DIR_NEW_PRIO}${in['id']}";

$task=false;
if(is_file($pathDone) && filesize($pathDone)>0){
	$task=@json_decode(@file_get_contents($pathDone),true);
}else if(is_file($pathNew)){
	$task=@json_decode(@file_get_contents($pathNew),true);
}else if(is_file($pathPrio)){
	$task=@json_decode(@file_get_contents($pathPrio),true);
}else{
	die(json_encode(["status"=>"ERROR", "message"=>"E200 Invalid ID"]));
}

if(!is_array($task) || !isset($task['status'])){
	die(json_encode(["status"=>"ERROR", "message"=>"E201 Invalid task content"]));
}

if($task['status']==="DONE"){
        $ret=["status" => "OK", "result" => $task['status'], "document" => $task['output'], "version" => $task['version']];
        if(isset($task['outputann']))$ret['outputann']=$task['outputann'];
	echo json_encode($ret);
}else if($task['status']==="SCHEDULED" || $task['status']==="RUNNING"){
	echo json_encode(["status" => "OK", "result" => $task['status'], "document" => ""]);
}else if($task['status']==="ERROR"){
	die(json_encode(["status"=>"ERROR", "message"=>"E203 Error processing document: ".$task['message']]));
}else{
	die(json_encode(["status"=>"ERROR", "message"=>"E202 Invalid status"]));
}

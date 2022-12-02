# APITable PHP SDK
[apitable(apitable.com)](https://apitable.com) The PHP SDK, giving you the ability to extend your datasheet with ease.  
[![Mit License](https://img.shields.io/badge/License-MIT-blue.svg)](https://www.mit-license.org/)

## Environment

php 7.0+

## Installation

```shell
composer require ApiTable/ApiTable
```



## Get api_token

Visit the workbench of ApiTable, click on the personal avatar in the lower left corner, and enter [My Setting > Developer]. click generate token (binding email required for first use).

## Use
```php
<?php
require_once './vendor/autoload.php';
use ApiTable\ApiTable;

echo '<pre>';

ApiTable::auth('your api token');

$all = ApiTable::datasheet('your dstId')->record()->all([
    "fieldKey" => 'id'
]);
var_dump(json_encode($all->getData()->getRecords()));

$page = ApiTable::datasheet('your dstId')->get(['pageNum' => 2, 'pageSize' => 2]);
var_dump(json_encode($page->getData()->getRecords()));

$attach = ApiTable::datasheet('your dstId')->upload(__DIR__.'/image.png');
var_dump($attach);

$insertArr = [
    [
        'fields' => ['ID' => 88],
    ],
    [
        'fields' => ['ID' => 99],
    ]
];
$insert = ApiTable::datasheet('your dstId')->record()->add($insertArr, 'name');
var_dump('insert message ' . $insert->getMessage());

$insertRecords = $insert->getData()->getRecords();
$updateArr = [
    [
        'recordId' => $insertRecords[0]['recordId'],
        'fields' => ['ID' => 100],
    ],
    [
        'recordId' => $insertRecords[1]['recordId'],
        'fields' => ['ID' => 101],
    ]
];
$update = ApiTable::datasheet('your dstId')->record()->update($updateArr, 'name');
var_dump('update message ' . $update->getMessage());

$delete = ApiTable::datasheet('your dstId')->record()->del([$insertRecords[0]['recordId'], $insertRecords[1]['recordId'],]);
var_dump('delete message ' . $delete->getMessage());
echo '</pre>';
```

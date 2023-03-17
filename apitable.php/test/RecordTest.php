<?php

use ApiTable\ApiTable;
use PHPUnit\Framework\TestCase;

final class RecordTest extends TestCase
{
    public function testAdd()
    {
        ApiTable::auth(getenv('TOKEN'), getenv('DOMAIN'));
        $all = ApiTable::datasheet(getenv('DATASHEET_ID'))::record()->add([
            [
                'fields' => [getenv('NUMBER_FIELD_NAME') => 88],
            ],
            [
                'fields' => [getenv('NUMBER_FIELD_NAME') => 99],
            ]
        ]);
        error_log("RecordTest::testAdd: " . json_encode($all));
        self::assertSame(200, $all->getCode());
    }

    public function testAll()
    {
        ApiTable::auth(getenv('TOKEN'), getenv('DOMAIN'));
        $all = ApiTable::datasheet(getenv('DATASHEET_ID'))::record()->all([
            "fieldKey" => 'id'
        ]);
        error_log("RecordTest::testAll: " . json_encode($all));
        self::assertSame(200, $all->getCode());
    }

    public function testGet()
    {
        ApiTable::auth(getenv('TOKEN'), getenv('DOMAIN'));
        $all = ApiTable::datasheet(getenv('DATASHEET_ID'))::record()->get([
            "fieldKey" => 'id'
        ]);
        error_log("RecordTest::testGet: " . json_encode($all));
        self::assertSame(200, $all->getCode());
    }

    public function testUpdate()
    {
        ApiTable::auth(getenv('TOKEN'), getenv('DOMAIN'));
        $all = ApiTable::datasheet(getenv('DATASHEET_ID'))::record()->get([
            "fieldKey" => 'name',
            "pageSize" => 2,
            "pageNum" => 1
        ]);
        $result = ApiTable::datasheet(getenv('DATASHEET_ID'))::record()->update([
            [
                'recordId' => $all->getData()->getRecords()[0]['recordId'],
                'fields' => [getenv('NUMBER_FIELD_NAME') => 88],
            ],
            [
                'recordId' => $all->getData()->getRecords()[1]['recordId'],
                'fields' => [getenv('NUMBER_FIELD_NAME') => 99],
            ]
        ]);
        error_log("RecordTest::testUpdate: " . json_encode($result));
        self::assertSame(200, $result->getCode());
    }

    public function testDel()
    {
        ApiTable::auth(getenv('TOKEN'), getenv('DOMAIN'));
        $all = ApiTable::datasheet(getenv('DATASHEET_ID'))::record()->get([
            "fieldKey" => 'name',
            "pageSize" => 2,
            "pageNum" => 1
        ]);
        $result = ApiTable::datasheet(getenv('DATASHEET_ID'))::record()->del([
            $all->getData()->getRecords()[0]['recordId'], $all->getData()->getRecords()[1]['recordId']
        ]);
        error_log("RecordTest::testDel: " . json_encode($result));
        self::assertSame(200, $result->getCode());
    }
}

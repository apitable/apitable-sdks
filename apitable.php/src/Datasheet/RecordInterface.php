<?php
namespace ApiTable\Datasheet;

use ApiTable\Common\Response;

/**
 * Interface Datasheet Record Interface
 * @package ApiTable
 */
interface RecordInterface {
    /**
     * get all records
     * @param array $params parameters
     * @return Response
     */
    public function all($params = []);

    /**
     * obtain records based on query condition
     * @param array $params
     * @return Response
     */
    public function get($params = []);

    /**
     * obtain the specified data according to the recordId.
     * @param array $recordIds
     * @param string $fieldKey
     * @return Response
     */
    public function find($recordIds = [], $fieldKey = 'name');

    /**
     * update record
     * @param $records
     * @param string $fieldKey
     * @return Response
     */
    public function update($records, $fieldKey = 'name');

    /**
     * add record
     * @param $records
     * @param string $fieldKey
     * @return Response
     */
    public function add($records, $fieldKey = 'name');

    /**
     * delete record
     * @param array $recordIds record id
     * @return Response
     */
    public function del($recordIds);
}
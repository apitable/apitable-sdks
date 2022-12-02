<?php
namespace ApiTable\Common;

/**
 * Class Record
 * @package ApiTable\Common
 */
class RecordModel {
    /**
     * @var int $pageNum current page
     */
    protected $pageNum;
    /**
     * @var int $pageSize current page size， corresponding to count($records)
     */
    protected $pageSize;
    /**
     * @var array $records record array
     */
    protected $records;
    /**
     * @var int $total total number of records
     */
    protected $total;
    /**
     * @param int $pageNum
     * @return RecordModel
     */
    public function setPageNum($pageNum)
    {
        $this->pageNum = $pageNum;
        return $this;
    }

    /**
     * @param int $pageSize
     * @return RecordModel
     */
    public function setPageSize($pageSize)
    {
        $this->pageSize = $pageSize;
        return $this;
    }

    /**
     * @param array $records
     * @return RecordModel
     */
    public function setRecords($records)
    {
        $this->records = $records;
        return $this;
    }

    /**
     * @param int $total
     * @return RecordModel
     */
    public function setTotal($total)
    {
        $this->total = $total;
        return $this;
    }

    /**
     * @return int
     */
    public function getPageNum()
    {
        return $this->pageNum;
    }

    /**
     * @return int
     */
    public function getPageSize()
    {
        return $this->pageSize;
    }

    /**
     * @return array
     */
    public function getRecords()
    {
        return $this->records;
    }

    /**
     * @return int
     */
    public function getTotal()
    {
        return $this->total;
    }

    /**
     * @param $data
     * @return RecordModel
     */
    public static function init($data)
    {
        $record = new RecordModel();
        if (isset($data['pageSize'])) {
            $record->pageSize = $data['pageSize'];
        }
        if (isset($data['pageNum'])) {
            $record->pageNum = $data['pageNum'];
        }
        if (isset($data['total'])) {
            $record->total = $data['total'];
        }
        if (isset($data['records'])) {
            $record->records = $data['records'];
        }
        return $record;
    }
}
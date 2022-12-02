<?php
namespace ApiTable\Datasheet;

use ApiTable\Common\Request;


class Record implements RecordInterface {
    // constant
    const DEFAULT_MAX_PAGE_SIZE = 2;
    const DEFAULT_PAGE_NUM = 1;
    // request url
    protected static $recordPath= '/fusion/v1/datasheets/%s/records';

    protected $datasheetId;
    /**
     * @var Request
     */
    private $request;

    public function __construct($datasheetId)
    {
        $this->datasheetId = $datasheetId;
        $this->request = new Request();
    }

    public function all($params = [])
    {
        $params['pageSize'] = self::DEFAULT_MAX_PAGE_SIZE;
        $params['pageNum'] = self::DEFAULT_PAGE_NUM;
        $result = $this->request->params($params)->url(sprintf(self::$recordPath, $this->datasheetId))->send();
        $data = $result->getData();
        if (!$result->isSuccess()) {
            return $result;
        }
        $total = $data->getTotal();
        echo sprintf("[first request]pageSize: %d, recordCount: %d, total: %d\n ", $data->getPageSize(), count($data->getRecords()), $data->getTotal());
	    // calculate the total number of cycles
	    if ($total > self::DEFAULT_MAX_PAGE_SIZE) {
            $times = intval(ceil(floatval($total / self::DEFAULT_MAX_PAGE_SIZE)));
            echo "total number of cycles: {$times}\n";
            for($i = 2; $i <= $times; $i++)  {
                $params['pageNum'] = $i;
                $tmp = $this->request->params($params)->url(sprintf(self::$recordPath, $this->datasheetId))->send();
                if (!$tmp->isSuccess()) {
                    return $tmp;
                }
                echo sprintf("[the no {$i} request]pageSize: %d, recordCount: %d, total: %d\n ", $tmp->getData()->getPageSize(), count($tmp->getData()->getRecords()), $tmp->getData()->getTotal());
                $data->setRecords(array_merge($data->getRecords(), $tmp->getData()->getRecords()));
            }
	    }
	    $data->setPageSize(null)->setPageNum(null)->setTotal(null);
        return $result->setData($data);
    }

    public function get($params = [])
    {
        return $this->request->params($params)->url(sprintf(self::$recordPath, $this->datasheetId))->send();
    }

    public function find($recordIds = [], $fieldKey = 'name')
    {
        return $this->request->params(['recordIds' => $recordIds, 'fieldKey' => $fieldKey])
            ->url(sprintf(self::$recordPath, $this->datasheetId))
            ->send();
    }

    public function update($records, $fieldKey = 'name')
    {
        return $this->request->method(Request::PATCH)->headers(Request::DEFAULT_JSON_CONTENT_TYPE)
            ->url(sprintf(self::$recordPath, $this->datasheetId))
            ->params(json_encode(['records' => $records, 'fieldKey' => $fieldKey]))
            ->send();
    }

    public function add($records, $fieldKey = 'name')
    {
        return $this->request->method(Request::POST)->headers(Request::DEFAULT_JSON_CONTENT_TYPE)
            ->url(sprintf(self::$recordPath, $this->datasheetId))
            ->params(json_encode(['records' => $records, 'fieldKey' => $fieldKey]))
            ->send();
    }

    public function del($recordIds)
    {
        return $this->request->method(Request::DELETE)->params(['recordIds' => $recordIds])
            ->url(sprintf(self::$recordPath, $this->datasheetId))->send();
    }
}
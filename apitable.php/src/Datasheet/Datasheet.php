<?php

namespace ApiTable\Datasheet;

use ApiTable\Common\Request;
use CURLFile;

class Datasheet implements DatasheetInterface
{

    // request url
    protected static $attachPath = '/fusion/v1/datasheets/%s/attachments';

    protected static $datasheetId;
    /**
     * @var Request
     */
    private $request;

    public function __construct($datasheetId)
    {
        self::$datasheetId = $datasheetId;
        $this->request = new Request();
    }

    public static function record()
    {
        return new Record(self::$datasheetId);
    }

    public function upload($file)
    {
        $request = new Request();
        $fileName = basename($file);
        $fields = [
            'file' => new CurlFile($fileName, mime_content_type($fileName), $fileName)
        ];
        return $request->method(Request::POST)
            ->url(sprintf(self::$attachPath, $this->datasheetId))
            ->params($fields)->send();
    }
}
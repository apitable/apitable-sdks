<?php
namespace ApiTable\Datasheet;

use ApiTable\Common\Response;

/**
 * Interface DatasheetInterface
 * @package ApiTable
 */
interface DatasheetInterface {

    /**
     * upload file
     * @param string $file file path
     * @return Response
     */
    public function upload($file);
}
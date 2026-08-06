<?php
namespace ApiTable;

use ApiTable\Datasheet\Datasheet;

/**
 * Class ApiTable
 * SDK entry file
 */
class ApiTable {
    protected static $apiToken;
    protected static $fieldKey;
    protected static $host;
    protected static $requestTimeout;

    /**
     * @param string $apiToken  (required) your API token, for authentication.
     * @param string $fieldKey  (optional）globally specifies the key of the query and returned field. use column names by default. when value is 'id', field id will be used as the query and return. (Using id can avoid code failure caused by column name modification)
     * @param string $host (optional）destination server address, default: https://aitable.ai
     * @param int $requestTimeout (optional）request expiration time, default 10s.
     */
    public static function auth($apiToken, $host = 'https://aitable.ai', $fieldKey = 'name', $requestTimeout = 10) {
        self::$apiToken = $apiToken;
        self::$requestTimeout = $requestTimeout;
        self::$host = $host;
        self::$fieldKey = $fieldKey;
    }

    /**
     * @param string $datasheetId datasheet id
     * @return Datasheet datasheet instance
     */
    public static function datasheet($datasheetId) {
        return new Datasheet($datasheetId);
    }

    /**
     * @return string
     */
    public static function getApiToken()
    {
        return self::$apiToken;
    }

    /**
     * @return string
     */
    public static function getFieldKey()
    {
        return self::$fieldKey;
    }

    /**
     * @return string
     */
    public static function getHost()
    {
        return self::$host;
    }

    /**
     * @return int
     */
    public static function getRequestTimeout()
    {
        return self::$requestTimeout;
    }


}
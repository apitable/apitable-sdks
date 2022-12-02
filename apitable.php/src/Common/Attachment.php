<?php
namespace ApiTable\Common;

/**
 * Class Attachment
 * @package ApiTable\Common
 */
class Attachment {
    /**
     * @var string $token attachment token
     */
    protected $token;
    /**
     * @var string $mimeType attachment mimeType
     */
    protected $mimeType;
    /**
     * @var int $size attachment size
     */
    protected $size;
    /**
     * @var int $height When the attachment is a picture, the height of the picture.
     */
    protected $height;
    /**
     * @var int $width When the attachment is a picture, the width of the picture.
     */
    protected $width;
    /**
     * @var string $name attachment name
     */
    protected $name;
    /**
     * @var string $url attachment download path
     */
    protected $url;
    /**
     * @var string $preview attachment thumbnail
     */
    protected $preview;

    /**
     * @return string
     */
    public function getToken()
    {
        return $this->token;
    }

    /**
     * @return string
     */
    public function getMimeType()
    {
        return $this->mimeType;
    }

    /**
     * @return int
     */
    public function getSize()
    {
        return $this->size;
    }

    /**
     * @return int
     */
    public function getHeight()
    {
        return $this->height;
    }

    /**
     * @return int
     */
    public function getWidth()
    {
        return $this->width;
    }

    /**
     * @return string
     */
    public function getName()
    {
        return $this->name;
    }

    /**
     * @return string
     */
    public function getUrl()
    {
        return $this->url;
    }

    /**
     * @return string
     */
    public function getPreview()
    {
        return $this->preview;
    }

    /**
     * @param $data
     * @return Attachment
     */
    public static function init($data)
    {
        $attach = new Attachment();
        if (isset($data['token'])) {
            $attach->token = $data['token'];
        }
        if (isset($data['mimeType'])) {
            $attach->mimeType = $data['mimeType'];
        }
        if (isset($data['size'])) {
            $attach->size = $data['size'];
        }
        if (isset($data['height'])) {
            $attach->height = $data['height'];
        }
        if (isset($data['width'])) {
            $attach->width = $data['width'];
        }
        if (isset($data['name'])) {
            $attach->name = $data['name'];
        }
        if (isset($data['url'])) {
            $attach->url = $data['url'];
        }
        if (isset($data['preview'])) {
            $attach->preview = $data['preview'];
        }
        return $attach;
    }
}
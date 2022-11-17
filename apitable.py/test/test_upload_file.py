import os
import time
import unittest
import warnings

from apitable import Apitable

from . import TEST_API_BASE, TEST_API_TOKEN, TEST_TABLE


class TestUploadFile(unittest.TestCase):

    def setUp(self):
        warnings.simplefilter('ignore', ResourceWarning)
        self.apitable = Apitable(TEST_API_TOKEN)
        self.apitable.set_api_base(TEST_API_BASE)
        self.dst = self.apitable.datasheet(TEST_TABLE)

    def test_upload_file(self):
        test_url = "https://i0.wp.com/apitable.com/wp-content/uploads/2022/11/IT.png"
        test_file = self.dst.upload_attachment(test_url)
        self.assertIsNotNone(test_file.get("token"))

        time.sleep(2)

        test_local_file = "test.png"
        filepath = os.path.join(os.path.dirname(__file__), test_local_file)
        test_file = self.dst.upload_file(filepath)
        self.assertEqual(test_file.get("mimeType"), "image/png")


if __name__ == "__main__":
    unittest.main()

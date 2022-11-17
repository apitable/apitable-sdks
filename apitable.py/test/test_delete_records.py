import time
import unittest
import warnings

from apitable import Apitable

from . import TEST_API_BASE, TEST_API_TOKEN, TEST_TABLE


class TestDeleteRecords(unittest.TestCase):

    def setUp(self):
        warnings.simplefilter('ignore', ResourceWarning)
        apitable = Apitable(TEST_API_TOKEN)
        apitable.set_api_base(TEST_API_BASE)
        self.dst = apitable.datasheet(TEST_TABLE)

    def test_record_delete(self):
        record = self.dst.records.get(title="apitable")
        time.sleep(1 / 5)
        r = record.delete()
        self.assertTrue(r)

        time.sleep(1 / 5)
        record_id = "recnotexist"
        with self.assertRaises(Exception) as e:
            self.dst.delete_records([record_id])
        self.assertIsNotNone(e)

    def tearDown(self):
        time.sleep(1)
        self.dst.records.create({"title": "apitable"})


if __name__ == "__main__":
    unittest.main()

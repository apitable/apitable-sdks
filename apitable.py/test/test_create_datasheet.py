import unittest
import warnings
from apitable import Apitable
from . import TEST_API_BASE, TEST_API_TOKEN, TEST_SPACE_ID


class TestCreateFields(unittest.TestCase):
    """Apitable Python SDK create datasheet test class
        - Datasheet Creation SDK Test
    """

    def setUp(self):
        """Create a datasheet test class object initial hook：

        """
        warnings.simplefilter('ignore', ResourceWarning)
        apitable = Apitable(TEST_API_TOKEN)
        apitable.set_api_base(TEST_API_BASE)
        self.spc = apitable.space(TEST_SPACE_ID)

    def test_datasheet_create(self):
        """Datasheet Creation SDK Test

        """
        req_data = {'name': 'table_name'}
        self.datasheet = self.spc.datasheets.create(req_data)
        self.assertIsNotNone(self.datasheet.id)

    def tearDown(self):
        """Create a Datasheet test class object destroy hook：

        """


if __name__ == '__main__':
    unittest.main()


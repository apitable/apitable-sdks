import unittest
import warnings
from apitable import Apitable
from . import TEST_TABLE, TEST_API_BASE, TEST_API_TOKEN, TEST_SPACE_ID


class TestDeleteFields(unittest.TestCase):
    """Apitable Python SDK Field delete test class
        - Field delete SDK test
    """

    def setUp(self):
        """Create a field test class object initial hook:
        - Warnings related to resource usage are ignored
        - Initialize the Apitable object
        - Get action sheet
        - add fields
        """
        warnings.simplefilter('ignore', ResourceWarning)
        apitable = Apitable(TEST_API_TOKEN)
        apitable.set_api_base(TEST_API_BASE)
        self.dst = apitable.space(TEST_SPACE_ID).datasheet(TEST_TABLE)
        req_data = {'type': 'SingleText', 'name': 'title', 'property': {'defaultValue': 'hello apitable'}}
        self.field = self.dst.fields.create(req_data)

    def test_field_delete(self):
        """Field delete SDK test:

        """
        is_true = self.dst.fields.delete(self.field.id)
        self.assertTrue(is_true)


if __name__ == '__main__':
    unittest.main()

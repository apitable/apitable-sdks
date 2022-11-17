import unittest
import warnings
from apitable import Apitable
from . import TEST_TABLE, TEST_API_BASE, TEST_API_TOKEN, TEST_SPACE_ID


class TestCreateFields(unittest.TestCase):
    """Apitable Python SDK create field test class
        - Field Creation SDK Test
    """

    def setUp(self):
        """Create a field test class object initial hook:
            - Warnings related to resource usage are ignored
            - Initialize the Apitable object
            - Get action sheet
        """
        warnings.simplefilter('ignore', ResourceWarning)
        apitable = Apitable(TEST_API_TOKEN)
        apitable.set_api_base(TEST_API_BASE)
        self.dst = apitable.space(TEST_SPACE_ID).datasheet(TEST_TABLE)

    def test_field_create(self):
        """Field Creation SDK Test

        """
        req_data = {'type': 'SingleText', 'name': 'title', 'property': {'defaultValue': 'hello apitable'}}
        self.field = self.dst.fields.create(req_data)
        self.assertIsNotNone(self.field.id)
        self.assertEqual(self.field.name, 'title')

    def tearDown(self):
        """Create a field test class object to destroy the hook:
            - Delete test created fields
        """
        self.dst.fields.delete(self.field.id)


if __name__ == '__main__':
    unittest.main()

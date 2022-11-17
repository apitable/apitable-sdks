import unittest
import warnings

from apitable import Apitable
from . import TEST_TABLE, TEST_API_BASE, TEST_API_TOKEN


class TestGetViews(unittest.TestCase):

    def setUp(self):
        warnings.simplefilter('ignore', ResourceWarning)
        apitable = Apitable(TEST_API_TOKEN)
        apitable.set_api_base(TEST_API_BASE)
        self.dst = apitable.datasheet(TEST_TABLE)

    def test_views_all(self):
        views = self.dst.views.all()
        self.assertIsInstance(views, list)
        first_view = views[0]
        self.assertEqual(first_view.name, 'Grid View')
        self.assertEqual(first_view.type, 'Grid')


if __name__ == '__main__':
    unittest.main()

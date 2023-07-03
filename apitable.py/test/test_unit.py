import unittest
import warnings

from apitable import Apitable
from apitable.unit import Member
from test import TOKEN, DOMAIN, SPACE_ID


class TestUnit(unittest.TestCase):
    def setUp(self):
        """
        Unit test setup.
        """
        warnings.simplefilter("ignore", ResourceWarning)
        self._apitable = Apitable(TOKEN)
        self._apitable.set_api_base(DOMAIN)

    def test_update_member(self):
        member = self._apitable.space(SPACE_ID).member().update()
        self.assertIsInstance(member, Member)
        self.assertIn(first_node.type, ["Datasheet", "Folder", "Mirror"])

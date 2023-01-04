import time
import unittest
import warnings
from apitable import Apitable
from apitable.types import EmbedLinkThemeEnum
from . import DOMAIN, TOKEN, SPACE_ID, DATASHEET_ID


class TestEmbedLinkCRD(unittest.TestCase):
    def setUp(self):
        warnings.simplefilter("ignore", ResourceWarning)
        apitable = Apitable(TOKEN)
        apitable.set_api_base(DOMAIN)
        self.spc = apitable.space(SPACE_ID)

    def test_embed_link_crd(self):
        datasheet = self.spc.datasheet(DATASHEET_ID)
        newEmbedLink = datasheet.create_embed_link(
            {
                "paylod": {
                    "primarySideBar": {"collapsed": False},
                    "viewControl": {
                        "viewId": "viwBBc0PMN3WB",
                        "tabBar": False,
                        "toolBar": {
                            "basicTools": False,
                            "shareBtn": False,
                            "widgetBtn": False,
                            "apiBtn": False,
                            "formBtn": False,
                            "historyBtn": False,
                            "robotBtn": False,
                        },
                        "collapsed": False,
                    },
                    "bannerLogo": True,
                    "permissionType": "readOnly",
                },
                "theme": EmbedLinkThemeEnum.Light,
            }
        )
        self.assertIsNotNone(newEmbedLink.linkId)
        time.sleep(0.5)
        embedLinks = datasheet.get_embed_links()
        self.assertEqual(len(embedLinks), 1)
        time.sleep(0.5)
        isDeleted = datasheet.delete_embed_link(newEmbedLink.linkId)
        self.assertTrue(isDeleted)
        time.sleep(0.5)
        embedLinks = datasheet.get_embed_links()
        self.assertEqual(len(embedLinks), 0)

    def tearDown(self):
        """Create an embed link test class object destroy hook:"""


if __name__ == "__main__":
    unittest.main()

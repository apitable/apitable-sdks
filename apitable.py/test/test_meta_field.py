import unittest

from apitable.types.field import MetaField


class TestMetaField(unittest.TestCase):

    def test_mapped_type_without_property(self):
        field = MetaField(id="fld1", name="n", type="SingleText")
        self.assertIsNone(field.property)

    def test_mapped_type_with_property(self):
        field = MetaField.model_validate({
            "id": "fld1",
            "name": "n",
            "type": "SingleText",
            "property": {"defaultValue": "x"},
        })
        self.assertEqual(field.property.defaultValue, "x")

    def test_mapped_type_property_none(self):
        field = MetaField(id="fld1", name="n", type="SingleText", property=None)
        self.assertIsNone(field.property)

    def test_unmapped_type(self):
        field = MetaField(id="fld1", name="n", type="URL")
        self.assertIsNone(field.property)

    def test_magic_lookup_nested(self):
        field = MetaField.model_validate({
            "id": "fld1",
            "name": "lookup",
            "type": "MagicLookUp",
            "property": {
                "relatedLinkFieldId": "lf",
                "targetFieldId": "tf",
                "rollupFunction": "SUM",
            },
        })
        self.assertEqual(field.property.rollupFunction.value, "SUM")


if __name__ == "__main__":
    unittest.main()

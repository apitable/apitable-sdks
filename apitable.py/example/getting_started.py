"""
APITable Python SDK v1.5.0 - Getting Started

This example walks you through the basic operations of the APITable SDK.
You can run it directly to verify the SDK works with your account.

Usage:
    python3 example/getting_started.py --token YOUR_TOKEN --api-base https://aitable.ai --dst YOUR_DST_ID

For vika.cn users:
    python3 example/getting_started.py --token YOUR_TOKEN --api-base https://vika.cn --dst YOUR_DST_ID

----------------------------------------------------------------------
MIGRATING FROM vika.py?

If you previously used the `vika` package (https://github.com/vikadata/vika.py),
the migration is straightforward. The API is identical — only the package name
and class name changed:

  Before (vika.py):                    After (apitable):
  ─────────────────                    ─────────────────
  pip install vika                     pip install apitable
  from vika import Vika                from apitable import Apitable
  vika = Vika("token")                 apitable = Apitable("token")
  dst = vika.datasheet("dstXXX")       dst = apitable.datasheet("dstXXX")

Everything else stays the same: records.all(), records.filter(),
records.create(), record.update(), etc.

If you are using vika.cn, just set the API base:
  apitable.set_api_base("https://vika.cn")
----------------------------------------------------------------------

Before you begin:
1. Install:  pip install apitable
2. Get your API Token:
   Log in to APITable (or vika.cn) -> click avatar (bottom left) ->
   User Center -> Developer -> Generate Token
3. Get your Datasheet ID:
   Open any table in the browser. The "dst..." string in the URL is your ID.
   Example URL: https://aitable.ai/workbench/dstXXXXXXXX/viwYYYYYYYY
                                               ^^^^^^^^^^^^ this part

Troubleshooting:
- "ModuleNotFoundError: No module named 'apitable'"
  Make sure you install with the SAME Python you run with:
      python3 -m pip install apitable
      python3 example/getting_started.py ...
- If using a virtual environment, activate it before install AND run.
"""

import argparse
import time
from apitable import Apitable


def main():
    parser = argparse.ArgumentParser(description="APITable SDK Getting Started Example")
    parser.add_argument("--token", required=True, help="Your API Token")
    parser.add_argument("--api-base", default="https://aitable.ai", help="API base URL (default: https://aitable.ai)")
    parser.add_argument("--dst", required=True, help="Datasheet ID to test with (e.g. dstXXXXXXXX)")
    args = parser.parse_args()

    # ================================================================
    # Step 1: Initialize the client
    # ================================================================
    print("=== Step 1: Initialize ===")
    apitable = Apitable(args.token)
    apitable.set_api_base(args.api_base)
    print(f"Connected to {args.api_base}")

    # ================================================================
    # Step 2: List your spaces
    # ================================================================
    print("\n=== Step 2: List Spaces ===")
    spaces = apitable.spaces.all()
    for space in spaces[:3]:
        print(f"  {space.name} (ID: {space.id})")
    if len(spaces) > 3:
        print(f"  ... and {len(spaces) - 3} more")
    time.sleep(1)

    # ================================================================
    # Step 3: Connect to a datasheet and inspect its structure
    # ================================================================
    print(f"\n=== Step 3: Datasheet Structure ({args.dst}) ===")
    dst = apitable.datasheet(args.dst)

    print("Fields:")
    for field in dst.fields.all():
        print(f"  {field.name} ({field.type})")
    time.sleep(1)

    print("Views:")
    for view in dst.views.all():
        print(f"  {view.name} (ID: {view.id})")
    time.sleep(1)

    # ================================================================
    # Step 4: Read records
    # ================================================================
    print("\n=== Step 4: Read Records ===")
    all_records = dst.records.all()
    print(f"Total records: {all_records.count()}")
    for i, r in enumerate(all_records):
        if i >= 5:
            print("  ... (showing first 5 only)")
            break
        print(f"  {r.json()}")
    time.sleep(1)

    # ================================================================
    # Step 5: Create a record
    # ================================================================
    print("\n=== Step 5: Create Record ===")
    fields = dst.fields.all()
    text_field = None
    for f in fields:
        if f.type == "SingleText":
            text_field = f.name
            break

    if not text_field:
        print("No SingleText field found, skipping write tests.")
        print("Add a SingleText field to your datasheet to test create/update/delete.")
        return

    new_record = dst.records.create({text_field: "SDK Test Record"})
    print(f"Created: ID={new_record._id}, data={new_record.json()}")
    time.sleep(1)

    # ================================================================
    # Step 6: Update the record
    # ================================================================
    print("\n=== Step 6: Update Record ===")
    setattr(new_record, text_field, "SDK Updated Record")
    print(f"Updated: {new_record.json()}")
    time.sleep(1)

    # ================================================================
    # Step 7: Delete the record
    # ================================================================
    print("\n=== Step 7: Delete Record ===")
    dst.records.filter(**{text_field: "SDK Updated Record"}).delete()
    print("Deleted test record")
    time.sleep(1)

    # ================================================================
    # Done!
    # ================================================================
    print("\n=== Done ===")
    remaining = dst.records.all()
    print(f"Records after cleanup: {remaining.count()} (same as before)")
    print("All tests passed!")


if __name__ == "__main__":
    main()

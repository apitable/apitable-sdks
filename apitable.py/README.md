# APITable.py

[![Latest Version](https://shields.mitmproxy.org/pypi/v/apitable.svg)](https://pypi.python.org/pypi/apitable)

It is the official package for the Fusion API of APITable, and provides an API similar to the Django ORM style.

# Requirements

python3.6 +

If you want to call apitable rest api, you need get the API Token first: visit the workbench of APITable, click the avatar in the lower left corner, and go to User Center > Developer Configuration. Click to generate Token (you need to bind your email for the first time).

# Installation

```shell
pip install --upgrade apitable
```

# Getting started

## Example

```python
from apitable import Apitable

apitable = Apitable("your api_token")

dst = apitable.datasheet("your datasheet id")
# When the URL is used as a parameter, the datasheet id is automatically resolved, ignoring the view id.
# dst = apitable.datasheet("https://apitable.com/workbench/dstxxxxxxxx/viwxxxxxxxx")

# Create a record
record = dst.records.create({"title": "new record from Python SDK"})
print(record.title)

# Create records in bulk
records = dst.records.bulk_create([
    {"title": "new record from Python SDK"},
    {"title": "new record from Python SDK2"}
])

# Update a single field
record.title = "new title"
print(record.title)
# "new title"

# Update multiple fields
record.update({
    "title": "new title",
    "other_field": "new value",
})

# Batch update multiple records
records = dst.records.bulk_update([
    {"recordId": "recxxxxx1", "fields":{"title": "new record.title from Python SDK"}},
    {"recordId": "recxxxxx2", "fields":{"title": "new record.title from Python SDK2"}},
])

# Attachment field update
my_file = dst.upload_file( <local or network file path>)
record.files = [my_file]

# Filter record
songs = dst_songs.records.filter(artist="faye wong")
for song in songs:
    print(song.title)

# Batch update a batch of records
dst_tasks.records.filter(title=None).update(status="Pending")

# Get a single record
book = dst_book.records.get(ISBN="9787506341271")
print(book.title)

# Convert the record object to json
record.json()

# Delete a batch of records that match the filter criteria
dst.records.filter(title=None).delete()

# Get fields
for field in apitable.datasheet("dstId").fields.all():
  print(field.name)

# Get the fields of the specified view, hidden fields will not be returned
for field in apitable.datasheet("dstId").fields.all(viewId="viewId"):
  print(field.name)

# Get views
for view in apitable.datasheet("dstId").views.all():
  print(view.name)

```

## Field mapping

Using the field name of the datasheet directly as a variable may not conform to the variable specification. So you have to fall back to using fieldId as the key, making the code less readable.

In order to solve this problem, the Python SDK provides the function of field mapping.

| Bug title\!                       | Bug status |
| --------------------------------- | ---------- |
| The page crashes after logging in | Waiting    |

```python
dst = apitable.datasheet("dstt3KGCKtp11fgK0t",field_key_map={
  "title": "Bug title!",
  "state": "Bug status",
})

record = dst.records.get()
print(record.title)
# "The page crashes after logging in"
print(record.state)
# "Waiting"
record.state="Done"
```

Keep the use of field id as the key

```python
bug = apitable.datasheet("dstn2lEFltyGHe2j86", field_key="id")
row = bug.records.get(flddpSLHEzDPQ="The page crashes after logging in")
row.flddpSLHEzDPQ = "The page crashes after logging in"
row.update({
    "flddpSLHEzDPQ": "The page crashes after logging in",
    "fldwvNDf9teD2": "Done"
})
```

When specifying `field_key="id"`, specify the corresponding key value of `field_key_map` should be `fieldId`

```python
bug = apitable.datasheet("dstn2lEFltyGHe2j86", field_key="id", field_key_map={
    "title": "flddpSLHEzDPQ",
    "state": "fldwvNDf9teD2",
})
```

# Documentation

## records

`dst.records` manage records in datasheets.

| Method           | Parameter             | Type        | Description                                                                                                                                                                    | Example                                                                                                        |
| ---------------- | --------------------- | ----------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------------------------------------- |
| create           | dict                  | Record      | Create a single record                                                                                                                                                         | `dst.records.create({"title":"new title"})`                                                                    |
| bulk_create      | dict[]                | Record[]    | Create multiple records in batches                                                                                                                                             | `dst.records.bulk_create([{"title":"new record1"},{"title":"new record2"}])`                                   |
| bulk_update      | dict[]                | Record[]    | Batch update multiple records                                                                                                                                                  | `dst.records.bulk_update([{"recordId": "recxxxxx1", "fields":{"title": "new record.title from Python SDK"}}])` |
| all              | \*\*kwargs            | QuerySet    | Return the record collection, you can pass parameters to customize the return content                                                                                          | `dst.records.all()`                                                                                            |
| get              | \*\*kwargs            | Record      | single record                                                                                                                                                                  | `dst.records.get(title="new title")`                                                                           |
| get_or_create    | (defaults,\*\*kwargs) | Record,bool | Query the corresponding record through kwargs. If it does not exist, create a new record in combination with defaults. The returned bool indicates whether it is a new record. | `dst.records.get_or_create(title="new title",defaults={"status":"pending"})`                                   |
| update_or_create | (defaults,\*\*kwargs) | Record,bool | Query the corresponding record through kwargs, and update the record with defaults. Create if it does not exist (same as get_or_create)                                        | `dst.records.update_or_create(title="new title",defaults={"status":"done"})`                                   |
| filter           | \*\*kwargs            | QuerySet    | Filter a batch of records                                                                                                                                                      | `dst.records.filter(title="new title")`                                                                        |

## QuerySet

Chained calls are possible. For example, `qs = dst.records.all()` returns a batch of queried datasets.

| Method | Parameter | Type     | Description                              | Example                                                  |
| ------ | --------- | -------- | ---------------------------------------- | -------------------------------------------------------- |
| filter | \*\*dict  | QuerySet | Filter out a batch of records            | `qs.filter(title="new title")`                           |
| all    | /         | QuerySet | Returns a copy of the current record set | `qs.filter(title="new title").all()`                     |
| get    | \*\*dict  | Record   | Single record                            | `qs.get(title="new title")`                              |
| count  | /         | int      | Total number of records                  | `qs.filter(title="new title").count()`                   |
| last   | /         | Record   | Last record                              | `qs.filter(title="new title").last()`                    |
| first  | /         | Record   | First record                             | `qs.filter(title="new title").first()`                   |
| update | \*\*dict  | Record   | Number of records successfully updated   | `qs.filter(title="new title").update(title="new title")` |
| delete | /         | bool     | Whether the deletion is successful       | `qs.filter(title="new title").delete()`                  |

## Record

The QuerySet queried out is a collection of Records. A single Record can obtain the value of the specified field through `record.fieldname`.

**Please try to avoid the same name of the field name and the method property reserved by Record, the field with the same name in the table will be obscured. If it does exist, use the field mapping configuration**

| Method/Attribute | Parameter | Type | Description                                                              | Example         |
| ---------------- | --------- | ---- | ------------------------------------------------------------------------ | --------------- |
| json             | /         | dict | Returns all field values of the current record                           | `record.json()` |
| \_id             | /         | str  | \_id is a reserved attribute, returns the recordId of the current record | `record._id`    |

## Field

The mapping relationship between Field and Python data structure. The data with empty cells in the datasheet is always null, and the records returned by the API will not contain fields with null values.

| Field Type       | Data type           |
| ---------------- | ------------------- |
| SingleText       | str                 |
| Text             | str                 |
| SingleSelect     | str                 |
| MultiSelect      | str[]               |
| URL              | str                 |
| Phone            | str                 |
| Email            | str                 |
| Number           | number              |
| Currency         | number              |
| Percent          | number              |
| AutoNumber       | number              |
| DateTime         | number              |
| CreatedTime      | number              |
| LastModifiedTime | number              |
| Attachment       | attachment object[] |
| Member           | unit object[]       |
| Checkbox         | bool                |
| Rating           | int                 |
| CreatedBy        | unit object         |
| LastModifiedBy   | unit object         |
| MagicLink        | str[]               |
| MagicLookUp      | any[]               |
| Formula          | str / bool          |

## all

all method will automatically handle paging loading all resources

_When the paging-related parameters (pageNum, pageSize) are passed in, the SDK will no longer automatically load all records, and only return the specified page data_。

> Try to avoid using the dst.records.all method without parameters to get all the data.
> The API can obtain a maximum of 1000 pieces of data per request. If your data volume is too large, it is close to the limit of 50000. In the case of no parameters, calling all will serially request the API 50 times. Not only is it very slow, but it consumes API request credit.

_Return the records of the specified pagination_

```python
dst.records.all(pageNum=3)
```

_Use with views_

Specifying the view id returns the same data as in the view.

```python
dst.records.all(viewId="viwxxxxxx")
```

_Filter data using formulas_

```python
dst.records.all(filterByFormula='{title}="hello"')
```

| Parameter       | Type               | Description                                                                                                 | Example                               |
| --------------- | ------------------ | ----------------------------------------------------------------------------------------------------------- | ------------------------------------- |
| viewId          | str                | View ID. The request will return the filtered/sorted results in the view                                    |                                       |
| pageNum         | int                | Default 1                                                                                                   |                                       |
| pageSize        | int                | Default 100, maximum 1000                                                                                   |                                       |
| sort            | dict[]             | Specify the sorting field, which overrides the view sorting conditions                                      | `[{ field: 'field1', order: 'asc' }]` |
| recordIds       | str[]              | Returns the recordset with the specified recordId                                                           | `['recordId1', 'recordId2']`          |
| fields          | str[]              | Only the specified fields will be returned                                                                  |                                       |
| filterByFormula | str                | Returns matching records using formulas as filter criteria                                                  |                                       |
| maxRecords      | int                | Limit the number of records returned, the default is 5000                                                   |                                       |
| cellFormat      | 'json' or 'string' | Defaults to 'json', when specified as 'string' all values will be automatically converted to string format. |                                       |
| fieldKey        | 'name' or 'id'     | Specifies the field query and returned key. The column name 'name' is used by default.                      |                                       |

See: [Tutorial getting started with formulas](https://help.apitable.com/docs/guide/tutorial-getting-started-with-formulas)

## FAQ

### Can I get the field type (meta) information of the datasheet?

Can be get through the fields/views API.

### Can option be created automatically?

If you write to a non-existent option, the option will be created automatically

### How many records can a single datasheet?

5w records.

### Can more records be processed per request?

10 records. In the future, we will adjust the size of this limit according to the actual situation.

### Can more records be fetched per request?

The current maximum is 1000 records. In the future, we will adjust the size of this limit according to the actual situation.

# Development and test

Create a new `.env`, the content can refer to `.env.example`.

```shell
cp .env .env.example
```

Then modify the test code in the `test/` folder for testing.

```shell
pipenv install --pre
pipenv shell
python -m unittest test
```

import setuptools

with open("README.md", "r") as fh:
    long_description = fh.read()

with open(".version", "r") as fh:
    version_number = fh.read()

setuptools.setup(
    name="apitable",
    version=version_number,
    author="apitable",
    author_email="dev@apitable.com",
    description="Apitable Python SDK",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://github.com/apitable/apitable-sdks",
    packages=[
        "apitable", "apitable.datasheet", "apitable.types", "apitable.node", "apitable.space", "apitable.unit"
    ],
    classifiers=[
        "Programming Language :: Python :: 3",
        "License :: OSI Approved :: MIT License",
        "Operating System :: OS Independent",
    ],
    python_requires=">=3.6",
    install_requires=["requests<=2.31.0", "pydantic==1.7", "environs<=9.5.0"],
)
from setuptools import find_packages, setup

setup(
    name="nhtsa-recall-client",
    version="0.1.0",
    description="Unified NHTSA recall API client (Python).",
    long_description=open("README.md", encoding="utf-8").read(),
    long_description_content_type="text/markdown",
    author="Wal33D",
    license="MIT",
    packages=find_packages(include=["python", "python.*"]),
    python_requires=">=3.8",
)

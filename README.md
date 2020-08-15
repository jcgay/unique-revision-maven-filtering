# unique-revision-maven-filtering

This Maven extension aims to replace `${revision}` version in POM when installing / deploying artifacts. You can then depend on such artifacts without breaking dependency resolution.

## Installation

Get [unique-revision-maven-filtering](https://dl.bintray.com/jcgay/maven/fr/jcgay/maven/extension/unique-revision-maven-filtering/1.3/unique-revision-maven-filtering-1.3.jar) and copy it in `%M2_HOME%/lib/ext` folder (where `%M2_HOME` targets your local Maven installation).

*or*

Use the new [core extensions configuration mechanism](http://takari.io/2015/03/19/core-extensions.html) by creating a `${maven.multiModuleProjectDirectory}/.mvn/extensions.xml` file with:

```
<?xml version="1.0" encoding="UTF-8"?>
<extensions>
    <extension>
      <groupId>fr.jcgay.maven.extension</groupId>
      <artifactId>unique-revision-maven-filtering</artifactId>
      <version>1.3</version>
    </extension>
</extensions>
```

## Usage

Run you build as usual, if your are using [continuous delivery friendly version](https://maven.apache.org/docs/3.2.1/release-notes.html) with the `${revision}` property it will be automatically replaced by its resolved value when installing or deploying artifacts.

# Build status
[![Build Status](https://travis-ci.org/jcgay/unique-revision-maven-filtering.svg?branch=master)](https://travis-ci.org/jcgay/unique-revision-maven-filtering)
[![Coverage Status](https://coveralls.io/repos/jcgay/unique-revision-maven-filtering/badge.svg?branch=master)](https://coveralls.io/r/jcgay/unique-revision-maven-filtering?branch=master)

# Release

    mvn -B release:prepare release:perform

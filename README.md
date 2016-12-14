[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.javaslang/javaslang-gwt/badge.png)](https://maven-badges.herokuapp.com/maven-central/io.javaslang/javaslang-gwt)
[![Build Status](https://travis-ci.org/javaslang/javaslang-gwt.png)](https://travis-ci.org/javaslang/javaslang-gwt)
[![Coverage Status](https://codecov.io/github/javaslang/javaslang-gwt/coverage.png?branch=master)](https://codecov.io/github/javaslang/javaslang-gwt?branch=master)
[![Sputnik](https://sputnik.ci/conf/badge)](https://sputnik.ci/app#/builds/javaslang/javaslang-gwt)
[![Gitter Chat](https://badges.gitter.im/Join%20Chat.png)](https://gitter.im/javaslang/javaslang)

# GWT support for Javaslang

### Using Javaslang in GWT maven projects

* Add the following maven dependency to your project:

```
<dependency>
    <groupId>io.javaslang</groupId>
    <artifactId>javaslang-gwt</artifactId>
    <version>{javaslang-current-version}</version>
</dependency>
```

* Inherit the `Javaslang` module in your GWT module's descriptor file:

```
<module>
    <!-- ... -->
    <inherits name="Javaslang"/>
    <!-- ... -->
</module>
```

* Use the Javaslang APIs in your code.

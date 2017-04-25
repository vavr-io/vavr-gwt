[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.vavr/vavr-gwt/badge.png)](https://maven-badges.herokuapp.com/maven-central/io.vavr/vavr-gwt)
[![Build Status](https://travis-ci.org/vavr-io/vavr-gwt.png)](https://travis-ci.org/vavr-io/vavr-gwt)
[![Coverage Status](https://codecov.io/github/vavr-io/vavr-gwt/coverage.png?branch=master)](https://codecov.io/github/vavr-io/vavr-gwt?branch=master)
[![Sputnik](https://sputnik.ci/conf/badge)](https://sputnik.ci/app#/builds/vavr-io/vavr-gwt)
[![Gitter Chat](https://badges.gitter.im/Join%20Chat.png)](https://gitter.im/vavr-io/vavr)

# GWT support for Vavr

### Using Vavr in GWT maven projects

* Add the following maven dependency to your project:

```
<dependency>
    <groupId>io.vavr</groupId>
    <artifactId>vavr-gwt</artifactId>
    <version>{vavr-current-version}</version>
</dependency>
```

* Inherit the `Vavr` module in your GWT module's descriptor file:

```
<module>
    <!-- ... -->
    <inherits name="Vavr"/>
    <!-- ... -->
</module>
```

* Use the Vavr APIs in your code.

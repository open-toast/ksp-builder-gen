
# Builder Generator

[![CircleCI](https://circleci.com/gh/open-toast/ksp-builder-gen.svg?style=svg)](https://circleci.com/gh/open-toast/ksp-builder-gen)
[![Maven Central](https://img.shields.io/maven-central/v/com.toasttab.ksp.builder/ksp-builder-gen-processor)](https://search.maven.org/artifact/com.toasttab.ksp.builder/ksp-builder-gen-processor)


This is a [KSP](https://github.com/google/ksp) processor that generates builders for Kotlin classes. It is intended to provide just enough functionality to help migrate away from Immutables and ditch KAPT annotation processing.

## Usage

See the [integration-tests](integration-tests) subproject for a working setup.

### Gradle Setup

You have to bring in the KSP plugin

```kotlin
plugins {
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
}
```

and add the processor as a `ksp` dependency and the annotations as an `implementation` dependency:

```kotlin
dependencies {
    implementation("com.toasttab.ksp.builder:ksp-builder-gen-annotations:${version}")
    ksp("com.toasttab.ksp.builder:ksp-builder-gen-processor:${version}")
}
```

### Code Generation

Unlike Immutables, you start with a simple Kotlin _data-ish_ class. A more precise definition of a _data-ish_ class is a class whose primary constructor's parameters are all backed by public properties. To generate a builder for a class, annotate the class with `@GenerateBuilder`.

```kotlin
@GenerateBuilder
class User(
    val name: String,
    val email: String?
)
```

The generated code will look like this

```kotlin
public class UserBuilder() {
    private var name: String? = null
    private var email: String? = null

    public constructor(o: User) : this() {
        this.name = o.name
        this.email = o.email
    }

    public fun name(name: String): UserBuilder {
        this.name = name
        return this
    }

    public fun email(email: String?): UserBuilder {
        this.email = email
        return this
    }

    public fun build(): User = User(name!!, email)
}
```

### Collections

For basic collections (`Collection`, `List`, `Set`, `Map`), convenience mutators will be generated. For example,

```kotlin
@GenerateBuilder
class Container(
    val map: Map<String, Long>,
    val list: List<String>
)
```

will yield

```kotlin
public class ContainerBuilder() {
    public putMap(k: String, v: Long): ContainerBuilder // { ... }
    public putAllMap(Map<String, Long> map): ContainerBuilder // { ... }
    public addList(o: String): ContainerBuilder // { ... }
    public addAllList(Iterable<String>): ContainerBuilder // { ... }
}
```

### Builder name

The name of the builder class is `"${className}Builder"` by default. It is customizable via the `name` annotation attribute.

### Defaults

Defaults are supported via a custom annotation. Unfortunately, the code generator does not have access to parameters' default values, but it knows whether a default exists. For the sake of consistency, if the `@Default` annotation is present, the property must also have a Kotlin default.

```kotlin
@GenerateBuilder
class User(
    @GenerateBuilder.Default("true")
    val active = true
)
```

The `@Default` annotation is not supported for collection properties. You can do really bad things if you put complex expressions into the annotation; so don't.

### Deprecation

For callsites written in Kotlin, it is typically desirable to use the constructor directly instead of calling the builder. Generated builders can be marked `@Deprecated` via the `deprecated` attribute.

## Migration from Immutables

* Convert the Immutables interface spec to a concrete Kotlin class, add `@GenerateBuilder`, and adapt existing callsites to the new builder.
* Add `deprecated = true` to the `@GenerateBuilder` annotation when all callsites are converted to Kotlin.
* ?
* Profit

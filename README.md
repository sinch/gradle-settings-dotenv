
### Gradle dotenv plugin for settings & projects

A plugin providing the possibility of using `.env` files not only for Gradle projects, but also for settings etc.,
with a KISS approach. Allows transparent usage and mixing of both regular env, `.env` and `gradle.properties`.
Designed and currently tested with Gradle 8.x, but probably compatible with many earlier and later releases as well.

Written 100% in Kotlin. Has an extensive set of tests.

#### Usage

For example `.env` file:
```dotenv
VARIABLE=baz9001
VARIABLE2=allyourbase
```

a matching `settings.gradle.kts` consumer would be e.g.
```kotlin
println(dotenv["VARIABLE"]) // direct access

val VARIABLE: String by dotenv // access via a dedicated Kotlin property delegate
println(VARIABLE)

// if the default var merging options are in force, this is also OK
val VARIABLE2: String by settings // property delegate for properties from `gradle.properties` which got `.env` merged in
println(VARIABLE2)
```

and a matching `build.gradle.kts` consumer would be e.g.
```kotlin
println(dotenv["VARIABLE"]) // direct access

val VARIABLE: String by dotenv // access via a dedicated Kotlin property delegate
println(VARIABLE)

// if the default var merging options are in force, this is also OK
val VARIABLE2: String by project // property delegate for properties from `gradle.properties` which got `.env` merged in
println(VARIABLE2)
```

#### Additional features

By default, this plugin merges both the system env vars and dotenv into both settings and project extra properties.
See [configuration](#configuration) on how to disable this behaviour if it's unneeded or causes problems.
The order of precedence (bottom is merged over and overwrites what is above) is:

 1. `gradle.properties`
 2. env variables
 3. dotenv variables

#### Input format

Because `.env` doesn't have a strictly formalized format and many variations do exist in the wild, this parser
implements only the simplest and most common subset, that is:

 1. completely blank lines and lines starting exactly with a `#` are ignored
 2. non-comment lines are assignments that are required to have a non-blank key followed by `=`
 3. all characters apart from the above are treated verbatim and are preserved
    (including whitespace, quotes and `=` other than the first one in the line)

No interpolation is done. No prefixes or keywords are handled. Direct multiline variables are not supported.
Escape sequences etc. are not supported. The only limit for the key is to be non-blank, all other characters are valid
(including leading/trailing whitespace, which is preserved verbatim). Values can be blank.

For further reference, see the rules described in `UnitTests` test class.

The rationale for such parsing rules is taken from the earliest `.env` document existing to my knowledge, that is
[Foreman `man` file](https://github.com/ddollar/foreman/commit/9193a675a3e53739f412d4e493ab74594d1e826c)
([with expanded wording later](https://github.com/ddollar/foreman/commit/3b8fec463d9966c5dc119c67084780849a219cd7)
as well)- which explicitly states this and only this:

> The file should be key/value pairs separated by `=`, with one key/value pair per line.

and

> If a `.env` file exists in the project directory, the default environment will be read from it.
> This file should contain key/value pairs, separated by `=`, with one key/value pair per line.
> FOO=bar
> BAZ=qux

All further extensions make the format more error-prone and arguably less suited for the original usage
(i.e. supplying simple text config variables).
The exceptions made here for omitting blank lines and comments are intended to increase the readability and are commonly
handled by other parsers anyway. As a result, any file conforming to this particular subset should be fully compatible
with all other `.env` file parsers (but not necessarily the other way around).

#### Configuration

The following `gradle.properties` are used:

* `dotenv.mergeEnvIntoSettingsProperties=[true|false]` ; default is `true`
  merges the env vars into Gradle properties visible in `settings.gradle.*` if set

* `dotenv.mergeIntoSettingsProperties=[true|false]` ; default is `true`
  merges the dotenv vars into Gradle properties visible in `settings.gradle.*` if set

* `dotenv.mergeEnvIntoProjectProperties=[true|false]` ; default is `true`
  merges the env vars into Gradle properties visible in `build.gradle.*` if set

* `dotenv.mergeIntoProjectProperties=[true|false]` ; default is `true`
  merges the dotenv vars into Gradle properties visible in `build.gradle.*` if set

* `dotenv.filename=[filesystem filename]` ; default is `.env`
  allows to consume dotenv from a file with custom name or path

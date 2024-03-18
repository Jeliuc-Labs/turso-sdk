# Turso Platform SDK

Turso Platform SDK is a Kotlin library for developing applications using Turso Platform API. 

The SDK covers all Platform API resources and provides statically typed classes.

Learn more about the [Turso](https://turso.tech/about-us).

Checkout what **libSQL** can do for you at [https://turso.tech/libsql](https://turso.tech/libsql).

## Reference Turso SDK in your project

### Define version variables 

Add package versions in `gradle.properties`

Example:

```
ktorVersion=2.3.6
dateTimeVersion=0.6.0-RC.2
logbackVersion=1.5.1
tursoSdkVersion=0.1.11 // currently published version
```

### Add dependencies

Add dependencies to `build.gradle.kts`

```Kotlin
// define version variables
val ktorVersion: String by project
val dateTimeVersion: String by project
val logbackVersion: String by project
val tursoSdkVersion: String by project

// add maven central to repositories
repositories {
    // other repositories
    mavenCentral()
}

dependencies {
    // ...
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:$dateTimeVersion")
    implementation ("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.jeliuc:turso-sdk-jvm:$tursoSdkVersion")
    // ...
}
```

## Create Turso auth token

Generate auth token. And copy it somewhere.

```Bash
turso auth api-tokens mint quickstart
```

See official [documentation](https://docs.turso.tech/api-reference/quickstart).

## Use client 

### Create TursoClient.

```Kotlin
val token = "you-api-access-token"

val client = TursoClient.using(CIO.create(), token)
```

### Make your first call 

Example: listing databases for an organization.

```Kotlin
val organization = "myorg"
val databases = client.databases.list(organization)
```

Example: creating new database.

```Kotlin
val createDatabase = CreateDatabase("databaseName", "databaseGroup")

val databaseCreationResponse = client.databases().create(organization, createDatabase)
```

### Free resources

Don't forget to close the client to free resources when you are done.

```Kotlin
client.close()
```

**For full list of examples, check tests.**

## Future Plans

* [x] Use `kotlinx.datetime` for date and time handling
* [ ] Transform into a multiplatform library (KMP) - and support JVM, JS, and Native
* [ ] HRANA3 protocol implementation (WIP)
* [ ] libSQL JDBC driver (WIP)

## Contribution
If you've found an error in this sample, please file an issue. <br>
Feel free to help out by sending a pull request :heart:.

[Code of Conduct](https://github.com/Jeliuc-Labs/turso-sdk/blob/main/CODE_OF_CONDUCT.md)

## Disclaimer

Turso and it's logo are trademarks of **CHISELSTRIKE INC**. 
All other trademarks are the property of their respective owners.

## License

```
Copyright 2024 Jeliuc.com S.R.L.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0.txt

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

# Turso Platform SDK

Turso Platform SDK is a Kotlin library for developing applications using Turso Platform API. 

The SDK covers all Platform API resources and provides statically typed classes.

Learn more about the [Turso](https://turso.tech/about-us).

Checkout what **libSQL** can do for you at [https://turso.tech/libsql](https://turso.tech/libsql).

## Getting Started

### Install gradle dependencies

```kts
TODO()
```

### Create and instance of the client

```kotlin

val token = "your-api-key" 

val client = TursoClient.using(CIO, token)
// Interact with the client.
// And close it when you are done.
client.close()

// Optionally you can provide some optional parameters
val client = TursoClient.using(
    CIO,
    token,
    baseUri = "https://custom-api-url.com",
    maxRetries = 5
)
```

### Create your first request

```kotlin
runBlocking {
    val organizations: List<Organization> = client.organizations.list()
}

organizations.forEach { org ->
    println("${org.slug} ${org.name}") 
}
```

### Resources

* [x] Organizations
  * [x] Members
  * [x] Invites
* [x] Groups
* [x] Databases
* [x] Locations
* [x] Audit Logs
* [x] API Tokens


## Future Plans

* [ ] 
* [ ] Transform into a multiplatform library (KMP) - and support JVM, JS, and Native

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
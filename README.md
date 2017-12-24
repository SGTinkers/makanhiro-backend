# MakanHiro Backend

For sql file please download from the discord #backend server

**Status**: In development

**TODO**: 

 - [x] getPost
 - [x] getPostById
 - [x] getPostByLocationId
 - [x] getPostsByUserId
 - [x] getUserSubscribedPosts
 - [x] createPost
 - [x] editPost
 - [x] deletePost
 - [x] subscribeToPost
 - [x] unsubscribeFromPost
 - [x] subscribeToLocation
 - [x] unsubscribeFromLocation
 - [x] auth **(Please contact me if the graph API I used is the wrong one as I am not using a library)**
 - [ ] image upload
 - [ ] notifications
    
## Technical Specs
**Database**: MySQL

**Language**: Kotlin/Java. Feel free to use Java if you are more comfortable with Java.

**Web Framework**: [ktor.io](http://ktor.io/index.html)

**Auth** : Facebook login

## Initial Setup
**Recommended IDE** : IntelliJ

[setup](https://www.jetbrains.com/help/idea/gradle.html) Check this webpage to setup project for IntelliJ

 Any further queries please contact [BudiNverse](https://github.com/BudiNverse)

## Framework/Libraries used

    compile "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version"
    compile "io.ktor:ktor-server-core:$ktor_version"
    compile "io.ktor:ktor-server-netty:$ktor_version"
    compile "io.ktor:ktor-gson:$ktor_version"
    compile "ch.qos.logback:logback-classic:1.2.1"
    compile group: 'mysql', name: 'mysql-connector-java', version: '6.0.6'

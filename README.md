# MakanHiro Backend

For sql file please download from the discord #backend server

**Status**: In development

**TODO**: 

 - [x] getPost
 - [x] getPostById
 - [x] getPostByLocationId
 - [x] getPostsByUserId
 - [ ] getUserSubscribedPosts
 - [ ] createPost
 - [ ] editPost
 - [ ] deletePost
 - [ ] subscribeToPost
 - [ ] unsubscribeFromPost
 - [ ] subscribeToLocation
 - [ ] unsubscribeFromLocation
 - [ ] auth
 - [ ] notifications
    

## Initial Setup
**Recommended IDE** : IntelliJ
![setup](https://www.jetbrains.com/help/idea/gradle.html) Check this webpage to setup project for IntelliJ

 Any further queries please contact [BudiNverse](https://github.com/BudiNverse)

## Framework/Libraries used

    compile "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version"
    compile "io.ktor:ktor-server-core:$ktor_version"
    compile "io.ktor:ktor-server-netty:$ktor_version"
    compile "io.ktor:ktor-gson:$ktor_version"
    compile "ch.qos.logback:logback-classic:1.2.1"
    compile group: 'mysql', name: 'mysql-connector-java', version: '6.0.6'

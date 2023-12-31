# 🦾 omegalib
A simple-to use, lightweight, and fast Java library providing a set of tools for the development of Minecraft Paper plugins. 

# 📦 Features


# 📭 Installation
Setup takes only about 3 minutes. omegalib is hosted on Jitpack. You'll need to add the Jitpack repository if you don't have it already, and add omegalib as a dependency.

### Step 1
Download and install the Java Plugin for omegalib on the Spigot Page (wip).

### Step 2 (Maven):
Add the following to your pom.xml file:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.omegaladon</groupId>
        <artifactId>omegalib</artifactId>
        <version>-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### Step 2 (Gradle)
Add the following to your build.gradle file:
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.omegaladon:omegalib:-SNAPSHOT'
}
```

# 📚 Documentation
Full documentation for this library can be found in the [Wiki](https://github.com/omegaladon/omegalib/wiki).

# 📝 License
This project is licensed under the MIT License - see the [LICENSE](https://github.com/omegaladon/omegalib/blob/main/LICENSE) file for details.

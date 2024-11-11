== Installation of WAF Mobile SDK to Maven Repository ==

1. Unzip the `WafMobileSdk.zip` to a build directory
2. cd to build directory and run `gradle publishToMavenLocal` to install to a local maven repository
3. Artifact will be installed as "com:amazonaws.waf:waf-mobile-sdk:{VERSION}"
4. Include the dependency in build.gradle
   ```
   implementation 'com.amazonaws.waf:waf-mobile-sdk:{VERSION}'
   ```

Note: build.gradle can also be modified to publish to a remote internal Maven repository.

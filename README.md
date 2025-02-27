# Deploy

Library now available at Maven Central: `ro.flexbiz.util:commons:1.0.1`

To deploy:
1. Set `MAVEN_GPG_PASSPHRASE` environment variable with the pgp passphrase 
2. Create `C:\Users\danut\.m2\settings.xml` with:

```
<settings>
  <servers>
    <server>
      <id>central</id>
      <username>[maven central generated token username]</username>
	  <password>[maven central generated token password]</password>
    </server>
  </servers>
</settings>
```

3. Run `mvn clean deploy`

# Install locally

Run the following Maven goal to build the jar:

`source:jar install`
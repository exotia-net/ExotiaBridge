# ExotiaBridge
### Compilation
```xml
mvn clean package
```

# API docs
### Installation (maven)
```xml
<repository>
    <id>exotia-repository-releases</id>
    <name>Exotia.net maven repository</name>
    <url>https://repository.exotia.net/releases</url>
</repository>

<dependency>
    <groupId>net.exotia.bridge</groupId>
    <artifactId>api</artifactId>
    <version>1.0.1</version>
</dependency>
```
### Installation (gradle)
```kotlin
maven {
    url = uri("https://repository.exotia.net/<repository>")
}

implementation("net.exotia.bridge:api:1.0.1")
```

### For the integration to work properly, you need to add a dependency in the plugin.yml file
```yaml
depend:
  - ExotiaBridge
```
### Creating an instance
```java
ExotiaBridgeInstance exotiaBridgeInstance = ExotiaBridgeProvider.getProvider();

ApiUserService userService = exotiaBridgeInstance.getUserService();
```

### Example
```java
public final class EconomyPlugin extends JavaPlugin {
    private final Injector injector = OkaeriInjector.create();
    private ApiUserService userService;

    @Override
    public void onEnable() {
        this.injector.registerInjectable(this.injector);
        this.injector.registerInjectable(this);
        this.userService = ExotiaBridgeProvider.getProvider().getUserService();
        this.injector.registerInjectable(this.userService);
        this.getServer().getPluginManager().registerEvents(this.injector.createInstance(PlayerJoinListener.class), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

public class PlayerJoinListener implements Listener{
    @Inject private ApiUserService userService;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ApiUser user = this.userService.getUser(player.getUniqueId());
        if (user == null) return;
        player.sendMessage("Twoje saldo: " + user.getBalance());
    }
}
```
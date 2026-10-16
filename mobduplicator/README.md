# mobduplicator

Mob drops are replaced with random items, and each stack is multiplied by a random amount. Use `/mobduplicator multiplier <min> <max>` to change the range in game.

## Versions

Versions are set in `gradle.properties`, and `fabric.mod.json` reads them at build time.

| Setting | Value |
|---|---|
| Minecraft | 26.3 |
| Fabric Loader | 0.19.5 |
| Fabric API | 0.163.0 |
| Java | 25 |

The jar is named `mobduplicator-<version>.jar`. Releases use the tag format `mobduplicator-v<version>+<minecraft version>`, for example `mobduplicator-v1.0.0+26.3`.

When Minecraft updates, change `minecraft_version`, `loader_version`, `loom_version` and `fabric_api_version` in `gradle.properties`, then check the mixin still applies by running `./gradlew runClient`.

## Setup

For setup instructions, please see the [Fabric Documentation page](https://docs.fabricmc.net/develop/getting-started/creating-a-project#setting-up) related to the IDE that you are using.

## License

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.

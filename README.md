# stupid minecraft mods

A collection of Minecraft Fabric mods.

Current Minecraft version: **26.3** (Fabric Loader 0.19.5, Fabric API 0.163.0).

| Mod | Description |
|---|---|
| [template](template/) | Starter mod to copy when making a new one. |
| [mobduplicator](mobduplicator/) | Mob drops become random items, each multiplied by a random amount. Change the range with `/mobduplicator multiplier <min> <max>`. |

Each mod lives in its own folder, named after its mod ID.

## Getting started

```bash
git clone https://github.com/RividuPesara/Stupid-minecraft-mods
cd Stupid-minecraft-mods
```

You need JDK 25. The easiest way is the dev container below, which sets it up for you.

### Using the dev container

1. Install [Docker](https://www.docker.com/) and the **Dev Containers** extension for VS Code.
2. Open the repo folder in VS Code.
3. Press `Ctrl+Shift+P` and run **Dev Containers: Reopen in Container**. The first build takes a few minutes.
4. In the terminal inside the container, check Java with `java -version`. It should show 25.
5. Build a mod with `cd <mod-folder> && ./gradlew build`.

The container is for building. To test the game, copy the built jar into your Minecraft `mods` folder.

If you don't want Docker, install JDK 25 another way, for example with SDKMAN on Linux or macOS.

## Adding a mod

Pick a mod ID first. Use lowercase letters, digits and hyphens only, for example `mob-something`. It must not match an existing mod. Use it everywhere below in place of `my-new-mod`.

From the repo root, run:

```bash
./new-mod.sh my-new-mod
```

It copies `template/` into `my-new-mod/` and makes the renames for you. It won't overwrite an existing folder, and it rejects invalid mod IDs.

## File structure

```
my-new-mod/
├── build.gradle            <- build setup, leave as is
├── settings.gradle         <- mod ID as rootProject.name
├── gradle.properties       <- mod version, group and Minecraft versions
├── README.md               <- describe your mod here
└── src/main/
    ├── java/com/example/mynewmod/
    │   └── MyNewMod.java   <- startup code, register items, mobs and commands here
    └── resources/
        ├── fabric.mod.json <- mod ID, name, author and description
        └── assets/my-new-mod/
            └── icon.png    <- mod icon shown in the game
```


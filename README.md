# Pathomatic

Pathomatic is an intelligent pathfinding bot for Minecraft. It navigates any terrain, breaks and places blocks, and automates complex tasks with precision — from mining and farming to building and exploration.

## Features

- Pathfinding to any location — `/pathomatic go <x> <y> <z>`
- Automated mining — `/pathomatic mine <block>`
- Farming — `/pathomatic farm`
- Building from schematics — `/pathomatic build`
- Exploration — `/pathomatic explore`
- And many more commands...

## Loaders

Pathomatic supports all major mod loaders:

- **NeoForge** (1.21.1)
- **Forge** (1.21.1)
- **Fabric** (1.21.1)
- **Quilt** (1.21.1)

## Usage

Commands are prefixed with `/pathomatic`:

```
/pathomatic go 100 64 200
/pathomatic mine diamond_ore
/pathomatic help
```

## Building

```bash
git clone https://github.com/shadvls/pathomatic.git
cd pathomatic
./gradlew build
```

The built JARs will be in `build/libs/` and `build/dist/`.

## License

LGPL-3.0

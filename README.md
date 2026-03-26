<h1 align="center">Baritone Command Hub</h1>

<div align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.20.1-brightgreen.svg" alt="Minecraft 1.20.1">
  <img src="https://img.shields.io/badge/Modloader-Forge-orange.svg" alt="Forge Loader">
  <img src="https://img.shields.io/badge/Baritone-Integration-blue.svg" alt="Baritone">
</div>

<br>

**Baritone Command Hub** is a lightweight, GUI-driven overlay for Minecraft 1.20.1 (Forge) designed to streamline your interactions with the [Baritone API](https://github.com/cabaletta/baritone). It provides a sleek and intuitive multi-line command interface, persistent command history, and a robust Favorite Command Manager, removing the hassle of typing long configuration strings or memorizing complex syntaxes in the standard game chat.

## ✨ Features

- **Multi-Line Command Input:** The GUI features an expansive, multi-line edit box supporting thousands of characters, text-wrapping, and easy editing. Your multi-line inputs are seamlessly collapsed and dispatched to Baritone accurately.
- **Robust Command History:** Automatically saves up to 100 of your most recently executed commands. Easily navigate back and forward through your history utilizing the <kbd>Page Up</kbd> and <kbd>Page Down</kbd> keys.
- **Favorite Command Manager:** Do you frequently use specific mining parameters or build schematics? Save your commands with a quick click and manage them seamlessly in the dedicated "Command Manager" screen.
- **Lightweight Overlay:** Built natively on Forge Event Bus, this mod integrates cleanly without heavy performance overhead.
- **Localized UI:** Clean, intuitive UI components engineered for swift usability (Currently natively supporting English labeling).
- **Execution & Navigation shortcuts**: Integrated keyboard support out-of-the-box.


## 🧰 Prerequisites

To run this mod, ensure you have:
- **Minecraft Java Edition** `1.20.1`
- **Forge Mod Loader** (Compatible with `1.20.1` builds)
- **Baritone** loaded into your client.

## 📥 Installation

1. Install Minecraft Forge for `1.20.1`.
2. Download the packaged `.jar` file of the **Baritone Command Hub**.
3. Move the downloaded `.jar` file into your `.minecraft/mods` directory.
4. Ensure the Baritone `.jar` is also present inside the `mods` directory (if not embedded globally).
5. Launch the game!

## 🚀 Usage

- Open the Baritone GUI interface in-game (usually mapped to a specified keybind in your Controls menu).
- Use the **multi-line text box** to draft extended commands such as continuous pathing or schematic building.
- Click **Execute** to run the command directly through the Baritone command manager.
- Press **Page Up / Page Down** while inside the text field to swiftly retrieve entries from your command history.
- Click **Save Command** to persist a commonly used setup to your favorites.
- Click **Manage Commands** to easily browse, execute, or delete saved commands.

## 🛠️ Building from Source

This project uses Gradle. To build the mod yourself:

```sh
# Clone the repository
git clone https://github.com/your-username/baritone-command-hub.git

# Navigate to the workspace directory
cd baritone-command-hub

# Build the Forge project
gradlew build
```
Once the build is complete, you will locate the compiled `.jar` file residing in the `build/libs` directory.


## 📝 License

This project is open-source. Feel free to fork, expand, and redistribute!
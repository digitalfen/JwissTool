# JwissTool - Prototype

JwissTool is a Java application designed to serve as a flexible launcher for plugins and customizable with addons. This tool allows you to extend its functionality and integrate various features through a plugin-based architecture and addon system.

## Overview

JwissTool is built to provide a robust platform for managing and executing plugins. It supports custom addons that can enhance or modify the functionality of the core application, making it a versatile tool for developers and users who need a customizable launcher.

## Features

- **Plugin Launcher**: Easily load and manage plugins through a simple interface.
- **Customizable Addons**: Extend the capabilities of JwissTool with addons that you can create or download.
- **Flexible Architecture**: Designed to be modular and adaptable to various use cases.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven (for building the project)

### Installation

1. **Clone the Repository**

   ```
   git clone https://github.com/yourusername/jwisstool.git
   cd jwisstool
   ```

2. **Build the Project**

   Ensure you have Maven installed, then build the project with:

   ```
   mvn clean install
   ```

   This will compile the project and package it into a JAR file.

3. **Run JwissTool**

   Navigate to the `target` directory and run the JAR file:

   ```
   java -jar jwisstool-1.0.0.jar
   ```

   Replace `1.0.0` with the actual version number of the JAR file.

### Configuration

JwissTool requires a configuration file to specify how plugins and addons are loaded. Place your `global.conf` file in the `config` directory next to the JAR file. The `config` directory should also contain any additional `.conf` files as needed.

### Adding Plugins and Addons

- **Plugins**: Place JAR files containing plugins in the `plugins` directory. JwissTool will automatically detect and load these plugins at startup.

- **Addons**: Place addon JAR files in the `addons` directory. Addons will be loaded and executed as part of the application initialization process.

## Usage

For detailed usage instructions, refer to the [Documentation](docs/README.md) or explore the available commands and options in the application.

## Contributing

Contributions to JwissTool are welcome! Please follow the guidelines in the [CONTRIBUTING.md](CONTRIBUTING.md) file for submitting issues and pull requests.

## License

JwissTool is licensed under the [MIT License](LICENSE). See the LICENSE file for more details.

## Contact

For any questions or support, please contact [vinimaffioli@gmail.com](mailto:vinimaffioli@gmail.com).

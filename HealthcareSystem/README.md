# Medex

## Description
This project is a JavaFX-based application that provides a graphical user interface for healthcare and vaccinations patient management. The application is built using Java, JavaFX. For creating unit tests used JUnit library.

## Prerequisites
To run this project, you need to have the following installed:

* Java Development Kit (JDK) 17 or later
* JavaFX SDK
* Maven

## Installation
#### Clone this repository
`git clone https://github.com/OlehKulyniak/Healthcare-Management-System.git`

#### Build Project
`mvn clean install`

#### Connecting JavaFX
Ensure JavaFX libraries are correctly linked in your IDE or build system. Change the Main configuration module path to the JavaFX library on your computer.
`--module-path "/path/to/JavaFX/lib"`

## Running the Application Using Command Line
`java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar medex.jar`

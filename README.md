# webstorm-log-plugin

_A 'console.log' plugin for JetBrains WebStorm_

## Installation

First solution - Download it from JetBrains: [webstorm-log-plugin](https://plugins.jetbrains.com/plugin/11989-webstorm-log-plugin)

Second solution -
* Download `webstorm-log-plugin.jar`
* In the plugins section of the preferences of WebStorm, choose 'Install from disk' and select `webstorm-log-plugin.jar`.
* Restart IDE

![alt text](https://user-images.githubusercontent.com/10620919/52919951-9c1ad700-336c-11e9-950f-43e1dd9179db.png)

## Usage

`ctrl + alt + C`: create a new console log line with the content under the current caret

`ctrl + alc + V`: create a new console log line with the content of the clipboard

*NOTE:* Each new console log line will update the content of the ones below in the file (see Format section)

## Format

The chosen format for the console logs is:
`console.log('${fileName} l.${lineNumber} ${content}', ${content});`

*NOTE:* If the current position of the caret is on a blank space the log will have the format:
`console.log('${fileName} l.${lineNumber}');`

## Demo

New console line from the content of under the current caret:

![newline](https://user-images.githubusercontent.com/10620919/52918743-61f70880-335f-11e9-96a1-52e783bc295a.gif)

New console line from the content of the clipboard and update:

![clipboard](https://user-images.githubusercontent.com/10620919/52918732-370cb480-335f-11e9-9564-162f2ac29319.gif)

Update of the console logs below in the file:

![updatelogs](https://user-images.githubusercontent.com/10620919/52918756-8fdc4d00-335f-11e9-8122-e32b4215d2ac.gif)

## Improvements ideas

* Right click on folder/file to remove all console logs
* Right click on folder/file to comment/uncomment all console logs
* Config option to change the log shape and the shortcuts

## [Changelog](https://github.com/Haskkor/webstorm-log-plugin/blob/master/CHANGELOG.md)

## Contributing

Pull requests are welcome.

## [License](https://github.com/Haskkor/webstorm-log-plugin/blob/master/LICENSE)
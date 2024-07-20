CuteTranslationAPI
======================
__[English](https://github.com/gdrfgdrf/CuteTranslationAPI/blob/1.14.4/README.md)__ | [简体中文](https://github.com/gdrfgdrf/CuteTranslationAPI/blob/1.14.4/README_ChineseSimplified.md)  
Allows the player to choose the language of the mod as needed,  
it also allows the mod to retrieve language strings based on the player's settings.

Commands
---------------
### Public
Root command: /language

| Content                         | Description                                       |
|---------------------------------|---------------------------------------------------|
| /language                       | Root command                                      |
| /language list-settings         | Lists your current language Settings for each mod |
| /language <mod-id> \<language\> | Sets the language of a mod                        |

### Admin
Root command: /language-admin

| Content                   | Description                       |
|---------------------------|-----------------------------------|
| /language-admin           | Root command                      |
| /language-admin save-data | Save the player language settings |


The player's language settings are saved every five minutes

For developers
----------------
You need to add this mod to your build.gradle.  

```groovy
modImplementation "io.github.gdrfgdrf:cutetranslationapi:VERSION"
```

Then, you need to add a cutetranslationapi.json file to the root of your mod's resource folder.  
The contents are as follows
```json
{
  "display-name": "The name of the mod you want to display in the game",
  "default-language": "Default language",
  "languages": [
    {
      "name": "en_us"
    },
    {
      "name": "zh_cn"
    },
    "..."
  ]
}
```

All languages are stored under the "languages" key.  
The value of the name key for these languages corresponds to the target language file.  
Language file storage path for the assets/modid/cutetranslationapi/

For example, when the name key values for en_us,  
assets/modid/cutetranslationapi must exist an en_us.json file.

The format of the language file is the same as the vanilla minecraft.

License
----------------
This project uses Apache License 2.0


CuteTranslationAPI
======================
__[English](https://github.com/gdrfgdrf/CuteTranslationAPI/blob/main/README.md)__ | [简体中文](https://github.com/gdrfgdrf/CuteTranslationAPI/blob/main/README_ChineseSimplified.md)  
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

Dependencies
--------------- 
[Fabric Language Kotlin](https://github.com/FabricMC/fabric-language-kotlin)  
[Fabric API](https://github.com/FabricMC/fabric)

Project dependencies are as follows

| Dependency                                                      | Use                               |
|-----------------------------------------------------------------|-----------------------------------|
| [Protocol Buffers](https://github.com/protocolbuffers/protobuf) | Store player and transaction data |

For developers
----------------
You need to add this mod to your build.gradle.  

```groovy
modImplementation "io.github.gdrfgdrf.cutetranslationapi:cute-translation-api:VERSION"
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

----

After that, you need to add a piece of code that registers the event listener.
The content of the listener is the fetch translation provider

#### Java
```java
ServerLifecycleEvents.SERVER_STARTING.register { _, ->
    something = TranslationProviderManager.INSTANCE.getOrCreate("mod id here");
    something = PlayerTranslationProviderManager.INSTANCE.getOrCreate("mod id here");
}
```

#### Kotlin
```kotlin
ServerLifecycleEvents.SERVER_STARTING.register { _, ->
    something = TranslationProviderManager.getOrCreate("mod id here")
    something = PlayerTranslationProviderManager.getOrCreate("mod id here")
}
```

The listener will get the translation provider when the server starts

### Code
To get a string for the default language,  
please use TranslationProviderManager getOrCreate(modId) method,  
"modId" indicates the id of the mod to be obtained.  
Generally, modid indicates the id of your mod.  
The obtained provider will always provide only the default language.  

To get the language string according to the player settings,  
please use PlayerTranslationProviderManager getOrCreate(modId) method,  
"modId" indicates the id of the mod to be obtained.  
Generally, modid indicates the id of your mod.  
The obtained provider provides the language based on the player's settings


### Version

| Minecraft version                                                  | Dependency version |
|--------------------------------------------------------------------|--------------------|
| 1.14.4, 1.15, 1.15.1, 1.15.2                                       | 1.1.0+1.14.4       |
| 1.16.2, 1.16.3, 1.16.4, 1.16.5, 1.17.1, 1.18, 1.18.1, 1.18.2       | 1.1.0+1.16.2       |
| 1.19, 1.19.1, 1.19.2, 1.19.3, 1.19.4, 1.20, 1.20.1, 1.20.2, 1.20.3 | 1.1.0+1.19         |
| 1.20.3                                                             | 1.1.0+1.20.3       |
| 1.20.4                                                             | 1.1.0+1.20.4       |
| 1.20.5, 1.20.6                                                     | 1.1.0+1.20.5       |
| 1.21                                                               | 1.1.0+1.21         |

The version of the game supported by the project corresponds to  
the version of the game with full support for the Fabric API.

### Wiki
Check out the GitHub Wiki page

License
----------------
This project uses Apache License 2.0


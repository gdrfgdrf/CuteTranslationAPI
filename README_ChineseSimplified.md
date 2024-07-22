CuteTranslationAPI
======================
[English](https://github.com/gdrfgdrf/CuteTranslationAPI/blob/main/README.md) | __[简体中文](https://github.com/gdrfgdrf/CuteTranslationAPI/blob/main/README_ChineseSimplified.md)__  
允许玩家根据需要设置模组语言，同时也允许模组根据玩家设置来获取语言字符串

指令
---------------
### 公共
根指令: /language

| 内容                      | 描述               |
|-------------------------|------------------|
| /language               | 根指令              |
| /language list-settings | 列出您当前对于各个模组的语言设置 |
| /language <模组 id> <语言>  | 设置模组语言           |

### 管理员
根指令: /language-admin

| 内容                        | 描述        |
|---------------------------|-----------|
| /language-admin           | 根指令       |
| /language-admin save-data | 保存玩家的语言设置 |

玩家的语言设置每五分钟保存一次

依赖
--------------- 
[Fabric Language Kotlin](https://github.com/FabricMC/fabric-language-kotlin)  
[Fabric API](https://github.com/FabricMC/fabric)

项目依赖如下

| Dependency                                                      | Use                               |
|-----------------------------------------------------------------|-----------------------------------|
| [Protocol Buffers](https://github.com/protocolbuffers/protobuf) | Store player and transaction data |

开发
----------------
您需要添加该模组到您的 build.gradle 文件

```groovy
modImplementation "io.github.gdrfgdrf:cutetranslationapi:VERSION"
```

然后，您需要添加一个 cutetranslationapi.json 文件到您模组的资源文件夹的根目录
该文件内容如下
```json
{
  "display-name": "您希望在游戏中显示的名字",
  "default-language": "默认语言",
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

所有语言均存储在 languages 键下，  
同时，languages 键下的各个 name 键的值都有其对应的语言文件.  
语言文件需存储在 assets/modid/cutetranslationapi/

例如，有一个 name 键的值为 en_us,  
那么 assets/modid/cutetranslationapi 就需要拥有一个 en_us.json 文件

语言文件的格式同原版 minecraft.

最后，您需要在您的 fabric.mod.json 文件的 depends 键中加入 cutetranslationapi，  
这样才能让 CuteTranslationAPI 先加载

### 代码
若要获取默认语言的字符串  
请使用 TranslationProviderManager 的 getOrCreate(modId) 方法  
其中 modId 为需要获取的模组，一般为您的模组的id，  
获取到的 provider 永远只会提供默认语言

若要根据玩家设置获取语言字符串  
请使用 PlayerTranslationProviderManager 的 getOrCreate(modId) 方法  
其中 modId 为需要获取的模组，一般为您的模组的id，  
获取到的 provider 会根据玩家的配置提供语言

### 版本

| 游戏版本                                                               | 依赖版本         |
|--------------------------------------------------------------------|--------------|
| 1.14.4, 1.15, 1.15.1, 1.15.2                                       | 1.1.0+1.14.4 |
| 1.16.2, 1.16.3, 1.16.4, 1.16.5, 1.17.1, 1.18, 1.18.1, 1.18.2       | 1.1.0+1.16.2 |
| 1.19, 1.19.1, 1.19.2, 1.19.3, 1.19.4, 1.20, 1.20.1, 1.20.2, 1.20.3 | 1.1.0+1.19   |
| 1.20.3                                                             | 1.1.0+1.20.3 |
| 1.20.4                                                             | 1.1.0+1.20.4 |
| 1.20.5, 1.20.6                                                     | 1.1.0+1.20.5 |
| 1.21                                                               | 1.1.0+1.21   |

项目支持的游戏版本与拥有 Fabric API 完整支持的游戏版本相同

### Wiki
请在 GitHub Wiki 页面查看

协议
----------------
该项目使用 Apache License 2.0 协议


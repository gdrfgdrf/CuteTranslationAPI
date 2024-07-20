CuteTranslationAPI
======================
[English](https://github.com/gdrfgdrf/CuteTranslationAPI/blob/1.14.4/README.md) | __[简体中文](https://github.com/gdrfgdrf/CuteTranslationAPI/blob/1.14.4/README_ChineseSimplified.md)__  
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

协议
----------------
该项目使用 Apache License 2.0 协议


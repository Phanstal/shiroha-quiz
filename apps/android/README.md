# FUFUQuiz — Android 工程

单版本原生 Compose 刷题应用，只构建一个 APK。

| 项目 | 值 |
|------|-----|
| 应用名 | FUFUQuiz |
| applicationId | `com.fufu.quiz` |
| 版本 | 1.0.0 (versionCode 1) |
| 入口 | `MainActivity`（Kotlin + Jetpack Compose + Material3） |
| minSdk / targetSdk | 26 / 34 |

## 编译

```bash
cd apps/android
./gradlew assembleNativeRelease     # 唯一的 release APK
```

APK 输出：`app/build/outputs/fufuquiz/FUFUQuiz-v1.0.0.apk`

也可以直接用 GitHub Actions：Actions → `Build APK` → Run workflow，产物在运行页面的 Artifacts 里。

## 目录说明

- `app/src/main/`：清单、启动主题、通用图标
- `app/src/native/`：Compose 界面、状态仓库、导入与解析、AI 与 WebDAV
- `app/src/katex/`：KaTeX 公式渲染（WebView + 本地资源）
- `app/src/testNative/`、`app/src/testKatex/`：单元测试
- `gradle/`：Gradle Wrapper

## 功能

题库导入（文本 / Excel / ZIP / OCR / AI 补全）、练习、考试、错题本、收藏夹、学习记录、备份与 WebDAV 同步。

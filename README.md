# FUFUQuiz

FUFUQuiz 是一个本地题库刷题工具，Android 原生实现（Kotlin + Jetpack Compose + Material3）。

> 你手里有 Word、Excel、TXT、JSON，或者题目与答案分开的文件。FUFUQuiz 会把这些内容解析成可练习、可考试、可复盘的个人题库。

当前仓库只保留 **一个构建目标**：Android 原生版，产出单个 APK。

## 下载与构建

| 方式 | 说明 |
| --- | --- |
| GitHub Actions | Actions → `Build APK` → Run workflow，运行结束后在 Artifacts 下载 `FUFUQuiz-apk` |
| 本地构建 | `cd apps/android && ./gradlew assembleNativeRelease`，产物在 `app/build/outputs/fufuquiz/FUFUQuiz-v1.0.0.apk` |

系统要求：Android 8.0（API 26）及以上。

## 功能

- **题库导入**：文本、Excel、JSON、ZIP、DOCX 图片提取、MinerU 在线 OCR、AI 补全
- **练习**：顺序刷题、随机抽题、背题模式、斩题模式、批量分组提交
- **考试**：计时交卷、按题型配分、自动判分与成绩统计
- **复习**：错题本、收藏夹、学习记录、智能复习
- **效率**：KaTeX 离线公式渲染、图片题、多空填空、暗夜模式、平板适配
- **数据**：本地备份 / 恢复、WebDAV 自动同步

## 目录结构

```
apps/android/
├── app/src/main/       # 清单、启动主题、通用图标
├── app/src/native/     # Compose 界面、状态仓库、导入解析、AI 与 WebDAV
├── app/src/katex/      # KaTeX 公式渲染
├── app/src/testNative/ # 单元测试
└── app/src/testKatex/  # 公式渲染测试
```

## 分支

- `FUFUQuiz`：当前开发分支，单 APK 版本

## 许可

见 [LICENSE](LICENSE) 与 [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md)。

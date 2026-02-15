# AI 编排平台 Phase 1 API 示例

## 1. 创建 Client

- `POST /ai/client`

```json
{
  "appId": "demo-app",
  "clientCode": "summary-client",
  "clientName": "摘要客户端",
  "description": "用于文本摘要"
}
```

## 2. 创建/发布 Client 版本

- `POST /ai/client/{id}/version`

```json
{
  "version": "v1",
  "status": "published",
  "modelName": "default-model",
  "promptTemplate": "请总结以下内容：${content}",
  "outputSchema": "plain-text"
}
```

## 3. 调试运行

- `POST /ai/client/{id}/debug-run`

```json
{
  "appId": "demo-app",
  "version": "v1",
  "variables": {
    "content": "若依 + Spring AI 结合示例"
  }
}
```

## 4. 失败返回示例

```json
{
  "code": 500,
  "msg": "AI Client version not found.",
  "error": {
    "traceId": "9c5bc95e8d4a4f5ea556f12eb66fa8e4",
    "errorCode": "AI-CLIENT-VERSION-404",
    "message": "AI Client version not found."
  }
}
```

## 5. 配置说明

- 需要将 `sql/ai_phase1_mvp.sql` 导入到业务数据库。
- `ruoyi-admin` 已引入 `ruoyi-ai-api` 依赖，应用启动后自动暴露 `/ai/client/**` 接口。

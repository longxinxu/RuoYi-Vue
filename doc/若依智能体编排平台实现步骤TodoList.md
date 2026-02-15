# 若依 + Spring AI 智能体编排平台实现步骤 TodoList（AI Coding 执行版）

> 目标：把《若依 + Spring AI 智能体编排平台规划方案（组件驱动版 V2）》落地为“可连续执行、可验收、可回滚”的开发清单。

---

## 0. 使用方式（给 AI Coding）

- 每次只做 **1~2 个 Task**，完成后先跑测试再进入下一项。
- 每个 Task 必须包含：
  - 代码实现
  - 最小验证（接口自测/单测/集成测试）
  - 文档更新（接口示例、配置说明）
- 采用分支命名：`feat/ai-orchestration-<phase>-<topic>`。

---

## Phase 1：基础骨架与最小可运行 Client

### P1-1 项目与模块骨架
- [x] 新建（或确认）模块分层：
  - `ruoyi-modules/ruoyi-ai-api`（对外 API）
  - `ruoyi-modules/ruoyi-ai-core`（引擎与编排核心）
  - `ruoyi-modules/ruoyi-ai-infra`（DAO、三方适配）
- [x] 建立基础包结构：`client/agent/graph/runtime/governance/eval`
- [x] 增加统一异常码与错误模型（AI 领域错误码）

### P1-2 数据库初始化（MVP）
- [ ] 创建首批 DDL（最小闭环）：
  - `ai_client_def`
  - `ai_client_version`
  - `ai_agent_def`
  - `ai_agent_version`
  - `ai_agent_client_node`
  - `ai_agent_client_edge`
  - `ai_agent_graph_def`
  - `ai_agent_graph_version`
- [ ] 建立版本状态流转字段：`draft/published/deprecated`
- [ ] 建立运行状态字段：`created/running/success/failed`

### P1-3 ClientDefinition 与 ClientEngine
- [ ] 定义 `AiClientDefinition`、`AiClientVersion` 领域对象
- [ ] 实现 `ClientResolver`（按 appId/clientId/version 解析）
- [ ] 实现 `ClientEngine` 最小链路：
  - Prompt 渲染
  - Model 调用（先接 1 个默认模型）
  - 输出解析
- [ ] 实现 `POST /ai/client`、`POST /ai/client/{id}/version`、`POST /ai/client/{id}/debug-run`

### P1-4 验收（Phase 1 Gate）
- [ ] 可以创建 Client + 发布版本
- [ ] `debug-run` 能返回模型结果
- [ ] 失败时可返回统一错误码与 traceId

---

## Phase 2：治理能力注入（审计、成本、权限）

### P2-1 ExecutionContext 与 Trace
- [ ] 设计 `ExecutionContext`：`traceId/sessionId/tenantId/userId/variables/artifacts`
- [ ] 请求入口自动注入 trace 与租户上下文
- [ ] 关键链路日志标准化（输入摘要/输出摘要/耗时/token）

### P2-2 Advisor 机制
- [ ] 定义 Advisor SPI：`preAdvisor`、`postAdvisor`
- [ ] 默认内置：
  - 上下文注入 Advisor
  - 输出结构校验 Advisor
  - 风险词检测 Advisor
- [ ] ClientVersion 支持绑定 Advisor 列表

### P2-3 成本与配额
- [ ] 记录 token 用量与估算成本
- [ ] 增加 `tokenBudget/toolBudget` 限制
- [ ] 超预算中断策略 + 明确错误码

### P2-4 验收（Phase 2 Gate）
- [ ] 每次调用可在日志中追踪 token、耗时、状态
- [ ] Advisor 可配置启停并生效
- [ ] 预算策略可触发并阻断执行

---

## Phase 3：RAG 插件化接入

### P3-1 知识库数据模型
- [ ] 建表并建索引：
  - `ai_kb`
  - `ai_kb_doc`
  - `ai_kb_chunk`
  - `ai_kb_embedding`
  - `ai_retriever_def`
  - `ai_retriever_param_version`
- [ ] 文档状态机：`uploaded/parsed/chunked/embedded/indexed/failed`

### P3-2 Ingestion Pipeline
- [ ] 实现文档上传接口：`POST /ai/kb/doc/upload`
- [ ] 实现解析、切片、向量化流水线
- [ ] 支持失败重试与断点续跑

### P3-3 Retriever 接入 ClientEngine
- [ ] 定义 `Retriever` SPI + 默认实现（向量检索）
- [ ] 支持 `topK/scoreThreshold` 参数化
- [ ] 将检索结果注入 Prompt 变量（`knowledge`）
- [ ] 输出 `citation` 片段

### P3-4 验收（Phase 3 Gate）
- [ ] `POST /ai/retriever/{id}/debug-search` 可返回命中文档
- [ ] Client 在启用 retrieverRef 后可引用知识片段
- [ ] 回答中包含 citation 信息

---

## Phase 4：MCP 工具体系接入

### P4-1 MCP 元数据与权限
- [ ] 建表：
  - `ai_mcp_server`
  - `ai_mcp_tool`
  - `ai_mcp_tool_schema`
  - `ai_mcp_tool_auth_policy`
  - `ai_tool_call_log`
- [ ] 实现 `McpServerRegistry` 与工具元数据同步
- [ ] 实现 `ToolPermissionGuard`（租户/角色/Agent 维度）

### P4-2 ToolExecutor 与 Client 工具调用
- [ ] 定义 `ToolExecutor` 统一执行入口
- [ ] 在 ClientEngine 增加工具调用阶段
- [ ] 记录工具调用日志（入参摘要、出参摘要、耗时、状态）

### P4-3 API
- [ ] `POST /ai/mcp/server`
- [ ] `POST /ai/mcp/server/{id}/sync-tools`

### P4-4 验收（Phase 4 Gate）
- [ ] Client 可按 toolsetRef 调用 MCP 工具
- [ ] 越权工具被拦截且有审计日志
- [ ] 工具失败可重试/降级并返回可解释错误

---

## Phase 5：单 Agent 编排（Client DAG）

### P5-1 AgentDefinition 与 DAG
- [ ] 实现 `AgentDefinition` / `AgentVersion`
- [ ] 支持节点类型：`prompt_node/llm_node/retriever_node/tool_node/condition_node/review_node/output_node`
- [ ] 支持边条件表达式与编排校验（有向无环、入口出口唯一）

### P5-2 AgentEngine
- [ ] 实现串行、条件、并行执行
- [ ] 支持节点级重试策略（次数、退避）
- [ ] 运行轨迹落库：`ai_agent_run`、`ai_agent_node_run`

### P5-3 API
- [ ] `POST /ai/agent`
- [ ] `POST /ai/agent/{id}/version`
- [ ] `POST /ai/agent/{id}/run`

### P5-4 验收（Phase 5 Gate）
- [ ] 一个 Agent 可编排 2+ Client 节点并执行成功
- [ ] 条件分支可按变量路由
- [ ] 失败节点可定位到具体 nodeRun 记录

---

## Phase 6：Multi-Agent 编排（AgentGraph DAG）

### P6-1 AgentGraph 模型
- [ ] 实现 `AgentGraphDefinition` / `AgentGraphVersion`
- [ ] 节点类型：`agent_call_node/agent_router_node/agent_merge_node/human_approval_node/end_node`
- [ ] 支持 `Coordinator/Domain/Judge/Delivery` 角色建模

### P6-2 AgentGraphEngine
- [ ] 执行 AgentGraph DAG（串行 + 并行 + 汇聚）
- [ ] 节点间变量传递与产物共享
- [ ] 运行轨迹落库：`ai_agent_graph_run`、`ai_agent_graph_node_run`

### P6-3 API
- [ ] `POST /ai/agent-graph`
- [ ] `POST /ai/agent-graph/{id}/version`
- [ ] `POST /ai/agent-graph/{id}/run`

### P6-4 验收（Phase 6 Gate）
- [ ] 2 个 Agent 串联可跑通
- [ ] 主管-专家模式可并行分发并汇总
- [ ] 可逐级追踪 `graph -> agent -> client -> tool`

---

## Phase 7：评测、发布与运营能力

### P7-1 Eval 框架
- [ ] 建立评测集管理（样本、期望、标签）
- [ ] 实现 `Eval API` 批量执行
- [ ] 输出指标：准确率、引用覆盖率、平均耗时、平均成本

### P7-2 A/B 与回滚
- [ ] 支持 Prompt 版本 A/B 实验
- [ ] 支持 Retriever 参数版本 A/B
- [ ] 支持一键回滚到上一发布版本

### P7-3 运维可观测
- [ ] 仪表盘：调用量、成功率、P95 延迟、token 成本
- [ ] 告警：错误率突增、成本异常、工具超时

### P7-4 验收（Phase 7 Gate）
- [ ] 可对固定样本集产出稳定评测报告
- [ ] 可基于指标比较不同版本并择优发布
- [ ] 线上故障可回滚且可追溯

---

## AI Coding 执行顺序（建议直接照此迭代）

1. [ ] **Iteration 1**：P1-1 ~ P1-4（拿到单 Client 闭环）
2. [ ] **Iteration 2**：P2-1 ~ P2-4（治理能力上线）
3. [ ] **Iteration 3**：P3-1 ~ P3-4（RAG 接入）
4. [ ] **Iteration 4**：P4-1 ~ P4-4（MCP 接入）
5. [ ] **Iteration 5**：P5-1 ~ P5-4（单 Agent DAG）
6. [ ] **Iteration 6**：P6-1 ~ P6-4（Multi-Agent DAG）
7. [ ] **Iteration 7**：P7-1 ~ P7-4（评测与运营）

---

## 每次迭代完成后的固定检查清单（DoD）

- [ ] 编译通过 + 单测通过 + 关键集成测试通过
- [ ] 新增 API 有 OpenAPI 文档与调用示例
- [ ] 新增表有 DDL、索引与迁移脚本
- [ ] 运行链路可追踪（traceId 贯穿）
- [ ] 关键失败场景有错误码与可读提示
- [ ] 变更日志（CHANGELOG）已更新


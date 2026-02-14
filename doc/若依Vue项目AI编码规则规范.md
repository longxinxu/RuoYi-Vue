# 若依 Vue 项目 AI Coding 规则规范

> 目标：让 AI 产出的代码与当前 `RuoYi-Vue` 项目风格一致、可维护、可审计、可快速合并。

## 1. 通用原则

1. **先复用后新增**：优先复用现有基础能力（`BaseController`、`AjaxResult`、`TableDataInfo`、`request` 封装、字典能力、权限指令），避免“平行实现”。
2. **最小改动原则**：只改动需求相关模块，不做与需求无关的重构、重命名、格式大洗牌。
3. **分层清晰**：严格遵循 `Controller -> Service -> Mapper`，禁止跨层直连（如 Controller 直接调 Mapper）。
4. **命名语义化**：变量/方法名和业务语义一致，避免 `data1`、`temp`、`doIt` 之类弱语义命名。
5. **与现有代码风格保持一致**：Java 使用项目现有大括号风格；Vue/JS 保持 2 空格缩进及现有注释风格。

---

## 2. 后端（Spring Boot / MyBatis）规范

## 2.1 包结构与文件落位

1. 管理端接口放在 `ruoyi-admin` 的 `com.ruoyi.web.controller.<模块>`。
2. 业务接口放在 `ruoyi-system` 的 `service`，实现放 `service.impl`。
3. 数据访问放在 `mapper` 与对应 XML。
4. 领域模型优先复用：
   - 系统通用实体优先放/复用 `com.ruoyi.common.core.domain`。
   - 业务模块实体在对应模块 `domain`。

## 2.2 Controller 编写要求

1. Controller 继承 `BaseController`。
2. 列表查询接口统一：
   - 调用 `startPage()`。
   - 返回 `getDataTable(list)`（`TableDataInfo`）。
3. 新增/修改/删除统一返回 `AjaxResult`，写法优先 `toAjax(rows)`。
4. 权限控制必须补齐 `@PreAuthorize("@ss.hasPermi('模块:资源:动作')")`。
5. 关键业务操作（增删改、导入导出）必须补 `@Log` + `BusinessType`。
6. 入参校验：需要校验时用 `@Validated` + `@RequestBody`，并复用项目已有校验逻辑。

## 2.3 Service 层要求

1. 接口定义在 `IxxxService`，实现类 `XxxServiceImpl`。
2. 涉及多表写入必须加 `@Transactional(rollbackFor = Exception.class)`（或按现有模块默认风格）。
3. 涉及数据权限的查询需使用 `@DataScope`（参考系统模块既有实现）。
4. 抛业务异常使用 `ServiceException`，错误信息可读、可定位。
5. 审计字段遵循现有机制：创建人/更新人、创建时间/更新时间按现有工具与基类逻辑处理。

## 2.4 Mapper / SQL 规范

1. Mapper 方法命名使用 `select/insert/update/delete` 前缀，语义清晰。
2. XML 中 SQL 片段尽量复用，避免重复拼装。
3. 严禁字符串拼接形成 SQL；统一使用参数绑定，防止注入。
4. 列表查询注意排序与分页兼容（配合 PageHelper）。

## 2.5 安全与合规

1. 涉及用户、角色、部门等数据必须补数据范围校验（如 `check*DataScope`）。
2. 涉及敏感数据（密码等）必须使用现有加密工具（如 `SecurityUtils.encryptPassword`）。
3. 禁止在日志、接口返回中暴露敏感信息（密码、token、密钥等）。

---

## 3. 前端（Vue2 + Element UI）规范

## 3.1 API 层（`src/api`）

1. 所有请求统一通过 `@/utils/request`。
2. 一个资源一个 API 文件，函数命名与后端动作对应：`listXxx/getXxx/addXxx/updateXxx/delXxx`。
3. GET 参数用 `params`，POST/PUT 请求体用 `data`，保持与现有风格一致。

## 3.2 页面层（`src/views`）

1. 列表页遵循若依通用 CRUD 模板结构：
   - 查询表单 + 工具栏 + 表格 + 分页 + 弹窗表单。
2. 权限按钮统一加 `v-hasPermi`。
3. 字典值展示优先走 `dict` 机制，不硬编码文本映射。
4. 状态切换、删除操作等高风险动作必须二次确认。
5. 与后端字段保持一致，避免前端临时字段造成序列化/反序列化混乱。

## 3.3 代码风格

1. 2 空格缩进，UTF-8，LF 换行。
2. `data()` 中状态按“查询参数、表格数据、弹窗状态、表单对象、校验规则”分组。
3. `methods` 按“查询->重置->新增->修改->提交->删除->辅助方法”组织，提升可读性。
4. 样式尽量复用现有类（如 `app-container`、`mb8`），减少孤立样式。

---

## 4. AI 生成代码的“必须执行”清单

1. **改动前定位**：先查找同类功能实现，按同构方式扩展。
2. **接口对齐**：前后端字段名、类型、枚举值、空值语义一致。
3. **权限对齐**：后端 `@PreAuthorize` 与前端 `v-hasPermi` 同步。
4. **日志对齐**：涉及增删改导入导出时补齐 `@Log`。
5. **异常对齐**：统一返回 `AjaxResult`，禁止返回风格混杂。
6. **自检清单**：
   - 编译通过；
   - 受影响模块无明显 lint/语法错误；
   - 核心流程可手工走通（列表/新增/编辑/删除）；
   - 无调试代码（`console.log`、无用注释、临时开关）。

---

## 5. 禁止项（AI 不应产出）

1. 绕过权限、绕过数据范围、绕过参数校验。
2. 在 Controller 写业务细节或直接操作 Mapper。
3. 新增重复工具类/重复 DTO，而不复用现有公共能力。
4. 无需求依据地引入新依赖或大规模重构。
5. 修改与任务无关的格式、注释、导入顺序，制造噪音 diff。

---

## 6. 推荐的 AI 提示词模板（供团队内部复用）

```text
你正在 RuoYi-Vue 项目中开发，请严格遵循项目既有风格：
1) 后端遵循 Controller->Service->Mapper 分层，Controller 继承 BaseController；
2) 列表接口返回 TableDataInfo，增删改返回 AjaxResult(toAjax)；
3) 所有增删改补 @Log，所有接口补 @PreAuthorize；
4) 前端使用 src/api 的 request 封装，按钮加 v-hasPermi；
5) 仅做最小必要改动，禁止无关重构；
6) 输出时给出改动文件、关键逻辑和自测命令。
```

---

## 7. 适用范围与维护

1. 本规范适用于本仓库内若依 Vue 前后端代码的 AI 生成与人工审阅。
2. 当项目框架升级（如 Vue3、权限框架调整、返回体规范调整）时，需同步更新本文档。

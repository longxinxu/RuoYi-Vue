-- Phase 2 governance extension for AI orchestration platform

alter table ai_client_version
    add column advisor_codes varchar(512) null comment 'comma-separated advisor codes' after output_schema,
    add column token_budget int null comment 'max token budget per execution' after advisor_codes,
    add column tool_budget int null comment 'max tool calls per execution' after token_budget;

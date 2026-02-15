-- Phase 1 MVP tables for AI orchestration platform

create table if not exists ai_client_def
(
    client_id    bigint       not null auto_increment,
    app_id       varchar(64)  not null,
    client_code  varchar(64)  not null,
    client_name  varchar(128) not null,
    description  varchar(500) default null,
    create_time  datetime,
    update_time  datetime,
    primary key (client_id),
    unique key uk_ai_client_def_app_code (app_id, client_code)
) engine = innodb;

create table if not exists ai_client_version
(
    version_id       bigint       not null auto_increment,
    client_id        bigint       not null,
    version          varchar(32)  not null,
    status           varchar(16)  not null default 'draft' comment 'draft/published/deprecated',
    model_name       varchar(64)  not null,
    prompt_template  text,
    output_schema    text,
    run_status       varchar(16)  not null default 'created' comment 'created/running/success/failed',
    create_time      datetime,
    update_time      datetime,
    primary key (version_id),
    unique key uk_ai_client_version (client_id, version)
) engine = innodb;

create table if not exists ai_agent_def
(
    agent_id      bigint       not null auto_increment,
    app_id        varchar(64)  not null,
    agent_code    varchar(64)  not null,
    agent_name    varchar(128) not null,
    description   varchar(500) default null,
    create_time   datetime,
    update_time   datetime,
    primary key (agent_id),
    unique key uk_ai_agent_def_app_code (app_id, agent_code)
) engine = innodb;

create table if not exists ai_agent_version
(
    version_id    bigint      not null auto_increment,
    agent_id      bigint      not null,
    version       varchar(32) not null,
    status        varchar(16) not null default 'draft' comment 'draft/published/deprecated',
    run_status    varchar(16) not null default 'created' comment 'created/running/success/failed',
    create_time   datetime,
    update_time   datetime,
    primary key (version_id),
    unique key uk_ai_agent_version (agent_id, version)
) engine = innodb;

create table if not exists ai_agent_client_node
(
    node_id                  bigint      not null auto_increment,
    agent_version_id         bigint      not null,
    node_code                varchar(64) not null,
    client_id                bigint      not null,
    client_version_id        bigint      not null,
    input_mapping_json       text,
    output_mapping_json      text,
    run_status               varchar(16) not null default 'created' comment 'created/running/success/failed',
    create_time              datetime,
    update_time              datetime,
    primary key (node_id),
    unique key uk_ai_agent_client_node_code (agent_version_id, node_code)
) engine = innodb;

create table if not exists ai_agent_client_edge
(
    edge_id                  bigint      not null auto_increment,
    agent_version_id         bigint      not null,
    from_node_code           varchar(64) not null,
    to_node_code             varchar(64) not null,
    condition_expr           varchar(500) default null,
    create_time              datetime,
    update_time              datetime,
    primary key (edge_id),
    key idx_ai_agent_client_edge_version (agent_version_id)
) engine = innodb;

create table if not exists ai_agent_graph_def
(
    graph_id      bigint       not null auto_increment,
    app_id        varchar(64)  not null,
    graph_code    varchar(64)  not null,
    graph_name    varchar(128) not null,
    description   varchar(500) default null,
    create_time   datetime,
    update_time   datetime,
    primary key (graph_id),
    unique key uk_ai_agent_graph_def_app_code (app_id, graph_code)
) engine = innodb;

create table if not exists ai_agent_graph_version
(
    version_id      bigint      not null auto_increment,
    graph_id        bigint      not null,
    version         varchar(32) not null,
    status          varchar(16) not null default 'draft' comment 'draft/published/deprecated',
    graph_dag_json  text,
    run_status      varchar(16) not null default 'created' comment 'created/running/success/failed',
    create_time     datetime,
    update_time     datetime,
    primary key (version_id),
    unique key uk_ai_agent_graph_version (graph_id, version)
) engine = innodb;

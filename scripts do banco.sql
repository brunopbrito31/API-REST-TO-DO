-- Hibernate: 
    drop table if exists tb_tarefas CASCADE 
    drop table if exists tb_usuarios CASCADE 

    
-- Hibernate: 
    
    create table tb_tarefas (
       id bigint generated by default as identity,
        descricao varchar(255) not null,
        nivel_prioridade integer not null,
        status integer not null,
        titulo varchar(255) not null,
        id_usuario bigint,
        primary key (id)
    )
    
    create table tb_usuarios (
       id bigint generated by default as identity,
        email varchar(255) not null,
        nome varchar(255) not null,
        senha varchar(255) not null,
        primary key (id)
    )
    
    alter table tb_usuarios 
       add constraint UK_hymsg6hpnk88xrsy9kdsuhur9 unique (email)

    
    alter table tb_tarefas 
       add constraint FKoqrrr0as6hhle71epbfrvigrq 
       foreign key (id_usuario) 
       references tb_usuarios
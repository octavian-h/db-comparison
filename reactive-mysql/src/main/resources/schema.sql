drop table if exists articles;
create table articles
(
    author_id     bigint,
    creation_date datetime(6),
    id            bigint not null auto_increment,
    content       varchar(1024),
    likes         int,
    primary key (id)
) engine = InnoDB;

drop table if exists authors;
create table authors
(
    id    bigint not null auto_increment,
    email varchar(255),
    name  varchar(255),
    likes int,
    primary key (id)
) engine = InnoDB;

alter table authors
    add constraint uk_authors_email unique (email);
alter table articles
    add constraint fk_articles_authors foreign key (author_id) references authors (id);

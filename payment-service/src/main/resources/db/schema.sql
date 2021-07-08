DROP TABLE payments IF EXISTS;
DROP TABLE paymentStatus IF EXISTS;

create table payments (
    id bigint generated by default as identity,
    orderId bigint not null,
    valueBilled bigint not null,
    paymentStatus varchar(255) not null
);
create table paymentStatus (
    id varchar(50) generated by default as identity,
    status varchar(255) not null
);
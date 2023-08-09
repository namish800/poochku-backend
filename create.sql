create table poochku.pet (pet_id bigserial not null, age integer, breed varchar(255), crt_ts date, description varchar(255), father_breed varchar(255), gender varchar(255), location varchar(255), mother_breed varchar(255), name varchar(255), status varchar(255), pet_type varchar(255), quality varchar(255), vaccination_status boolean, seller_id bigint, user_id bigint, primary key (pet_id));
create table poochku.pet_images (pet_image_id bigserial not null, url varchar(255) array, pet_id bigint, primary key (pet_image_id));
create table poochku.pet_service (pet_service_id bigserial not null, price integer, service_code varchar(255), service_name varchar(255), pet_id bigint, primary key (pet_service_id));
create table poochku.seller (seller_id bigserial not null, crt_ts timestamp(6), email varchar(255), f_name varchar(255), l_name varchar(255), last_login timestamp(6), m_name varchar(255), password varchar(255), phone_no varchar(255), user_role varchar(255), primary key (seller_id));
create table poochku.user (user_id bigserial not null, crt_ts timestamp(6), email varchar(255), f_name varchar(255), l_name varchar(255), last_login timestamp(6), m_name varchar(255), password varchar(255), phone_no varchar(255), user_role varchar(255), primary key (user_id));
create table swipe (swipe_id bigserial not null, created_at bytea, direction varchar(255), status varchar(255), swiper_id bigint, target_id bigint, primary key (swipe_id));
alter table if exists poochku.pet add constraint FKb7i04g9ccv3p6nllnijodhkth foreign key (seller_id) references poochku.seller;
alter table if exists poochku.pet add constraint FK9hxka0oqkd15dmqstdarori08 foreign key (user_id) references poochku.user;
alter table if exists poochku.pet_images add constraint FKjv5ge6puunoipe1pkod8dlmwq foreign key (pet_id) references poochku.pet;
alter table if exists poochku.pet_service add constraint FK62niqikbhm9jxhjvd2uke5obm foreign key (pet_id) references poochku.pet;
alter table if exists swipe add constraint FKdy7fujon7ethifsruaucndu6q foreign key (swiper_id) references poochku.user;
alter table if exists swipe add constraint FKcv5wkg13yqgvpwwxfbr2xuh5c foreign key (target_id) references poochku.user;

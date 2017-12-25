create table location
(
  locationId int not null
    primary key,
  locationName enum('NUS', 'NTU', 'SMU', 'SP', 'RP', 'NP', 'NYP', 'TP') not null,
  locationDetails varchar(50) not null,
  constraint location_location_id_uindex
  unique (locationId),
  constraint location_location_details_uindex
  unique (locationDetails)
)
  engine=InnoDB
;

create table locationsub
(
  id char(40) not null
    primary key,
  userId char(64) not null,
  locationId int not null,
  constraint locationId_location_locationId_fk
  foreign key (locationId) references location (locationId)
)
  engine=InnoDB
;

create index locationsub_user_userId_fk
  on locationsub (userId)
;

create index locationId_location_locationId_fk
  on locationsub (locationId)
;

create table post
(
  id char(64) not null
    primary key,
  locationId int not null,
  expiryTime timestamp not null on update CURRENT_TIMESTAMP,
  images varchar(4000) null,
  dietary enum('HALAL', 'VEGETARIAN') null,
  description varchar(200) null,
  foodAvailability enum('ABUNDANT', 'FINISHING', 'FINISHED') not null,
  createdAt timestamp not null,
  updatedAt timestamp not null,
  posterId char(64) not null,
  constraint post_id_uindex
  unique (id),
  constraint post_location_location_id_fk
  foreign key (locationId) references location (locationId)
)
  engine=InnoDB
;

create index post_location_location_id_fk
  on post (locationId)
;

create index post_user_userId_fk
  on post (posterId)
;

create table postsub
(
  id char(40) not null
    primary key,
  userId char(64) null,
  postId char(64) not null,
  constraint postSub_post_id_fk
  unique (postId),
  constraint postsub_post_id_fk
  foreign key (postId) references post (id)
    on delete cascade
)
  engine=InnoDB
;

create index postSub_user_userId_fk
  on postsub (userId)
;

create table user
(
  userId char(64) not null
    primary key,
  fullName varchar(150) not null,
  email varchar(150) not null,
  facebookId varchar(150) not null,
  constraint user_user_id_uindex
  unique (userId),
  constraint user_email_uindex
  unique (email),
  constraint user_facebook_id_uindex
  unique (facebookId)
)
  engine=InnoDB
;

alter table locationsub
  add constraint locationsub_user_userId_fk
foreign key (userId) references user (userId)
;

alter table post
  add constraint post_user_userId_fk
foreign key (posterId) references user (userId)
;

alter table postsub
  add constraint postsub_user_userId_fk
foreign key (userId) references user (userId)
  on delete cascade
;


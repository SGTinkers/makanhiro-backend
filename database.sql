CREATE TABLE location
(
  locationId      INT                                                       NOT NULL
    PRIMARY KEY,
  locationName    ENUM ('NUS', 'NTU', 'SMU', 'SP', 'RP', 'NP', 'NYP', 'TP') NOT NULL,
  locationDetails VARCHAR(50)                                               NOT NULL,
  CONSTRAINT location_location_id_uindex
  UNIQUE (locationId),
  CONSTRAINT location_location_details_uindex
  UNIQUE (locationDetails)
)
  ENGINE = InnoDB;

CREATE TABLE locationsub
(
  id         CHAR(40) NOT NULL
    PRIMARY KEY,
  userId     CHAR(64) NOT NULL,
  locationId INT      NOT NULL,
  CONSTRAINT locationId_location_locationId_fk
  FOREIGN KEY (locationId) REFERENCES location (locationId)
)
  ENGINE = InnoDB;

CREATE INDEX locationsub_user_userId_fk
  ON locationsub (userId);

CREATE INDEX locationId_location_locationId_fk
  ON locationsub (locationId);

CREATE TABLE post
(
  id               CHAR(64)                                   NOT NULL
    PRIMARY KEY,
  locationId       INT                                        NOT NULL,
  expiryTime       TIMESTAMP                                  NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  images           VARCHAR(4000)                              NULL,
  dietary          ENUM ('HALAL', 'VEGETARIAN')               NULL,
  description      VARCHAR(200)                               NULL,
  foodAvailability ENUM ('ABUNDANT', 'FINISHING', 'FINISHED') NOT NULL,
  createdAt        TIMESTAMP DEFAULT '0000-00-00 00:00:00'    NOT NULL,
  updatedAt        TIMESTAMP DEFAULT '0000-00-00 00:00:00'    NOT NULL,
  posterId         CHAR(64)                                   NOT NULL,
  CONSTRAINT post_id_uindex
  UNIQUE (id),
  CONSTRAINT post_location_location_id_fk
  FOREIGN KEY (locationId) REFERENCES location (locationId)
)
  ENGINE = InnoDB;

CREATE INDEX post_location_location_id_fk
  ON post (locationId);

CREATE INDEX post_user_userId_fk
  ON post (posterId);

CREATE TABLE postsub
(
  id     CHAR(40) NOT NULL
    PRIMARY KEY,
  userId CHAR(64) NULL,
  postId CHAR(64) NOT NULL,
  CONSTRAINT postSub_post_id_fk
  UNIQUE (postId),
  CONSTRAINT postsub_post_id_fk
  FOREIGN KEY (postId) REFERENCES post (id)
)
  ENGINE = InnoDB;

CREATE INDEX postSub_user_userId_fk
  ON postsub (userId);

CREATE TABLE user
(
  userId     CHAR(64)     NOT NULL
    PRIMARY KEY,
  fullName   VARCHAR(150) NOT NULL,
  email      VARCHAR(150) NOT NULL,
  facebookId VARCHAR(150) NOT NULL,
  CONSTRAINT user_user_id_uindex
  UNIQUE (userId),
  CONSTRAINT user_email_uindex
  UNIQUE (email),
  CONSTRAINT user_facebook_id_uindex
  UNIQUE (facebookId)
)
  ENGINE = InnoDB;

ALTER TABLE locationsub
  ADD CONSTRAINT locationsub_user_userId_fk
FOREIGN KEY (userId) REFERENCES user (userId);

ALTER TABLE post
  ADD CONSTRAINT post_user_userId_fk
FOREIGN KEY (posterId) REFERENCES user (userId);

ALTER TABLE postsub
  ADD CONSTRAINT postsub_user_userId_fk
FOREIGN KEY (userId) REFERENCES user (userId);


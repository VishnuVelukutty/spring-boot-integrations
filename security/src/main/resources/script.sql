CREATE TABLE "public"."user_details" ( 
  "id" BIGINT NOT NULL,
  "last_name" VARCHAR(255) NULL,
  "first_name" VARCHAR(255) NULL,
  "password" VARCHAR(255) NULL,
  "role" VARCHAR(255) NULL,
  "user_name" VARCHAR(255) NULL,
  CONSTRAINT "user_details_pkey" PRIMARY KEY ("id")
);
CREATE TABLE "public"."ourusers" ( 
  "id" SERIAL,
  "city" VARCHAR(255) NULL,
  "email" VARCHAR(255) NULL,
  "name" VARCHAR(255) NULL,
  "password" VARCHAR(255) NULL,
  "role" VARCHAR(255) NULL,
  CONSTRAINT "ourusers_pkey" PRIMARY KEY ("id")
);
ALTER TABLE "public"."user_details" DISABLE TRIGGER ALL;
ALTER TABLE "public"."ourusers" DISABLE TRIGGER ALL;
INSERT INTO "public"."user_details" ("id", "last_name", "first_name", "password", "role", "user_name") VALUES ('252', 'Velukutty', 'Vishnu', '$2a$12$AjmYBuSFRdl9d3jCc4LcRefvIhMZPaXEqgzcoB6PZ9iHGKhiEhyHC', 'USER', 'Vishnu');
INSERT INTO "public"."user_details" ("id", "last_name", "first_name", "password", "role", "user_name") VALUES ('253', 'Velukutty', 'Raveena', '$2a$12$pq/iZjAjpr7AHVuejQRV5eRirp/Tc3/OcWo2YKQhk/gSU1aK2/RZ2', 'ADMIN', 'raveena');
ALTER TABLE "public"."user_details" ENABLE TRIGGER ALL;
ALTER TABLE "public"."ourusers" ENABLE TRIGGER ALL;

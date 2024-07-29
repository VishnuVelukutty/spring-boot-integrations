CREATE TABLE "public"."user_details" ( 
  "id" INTEGER NOT NULL,
  "password" VARCHAR(255) NULL,
  "role" VARCHAR(255) NULL,
  "user_name" VARCHAR(255) NULL,
  CONSTRAINT "user_details_pkey" PRIMARY KEY ("id")
);
CREATE TABLE "public"."jwt_token" ( 
  "id" INTEGER NOT NULL,
  "expired" BOOLEAN NOT NULL,
  "revoked" BOOLEAN NOT NULL,
  "token" VARCHAR(255) NULL,
  "token_type" VARCHAR(255) NULL,
  "userid" INTEGER NULL,
  CONSTRAINT "jwt_token_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "uk_9sdq03sivblynhc1d3bhrhvxm" UNIQUE ("token")
);
ALTER TABLE "public"."jwt_token" ADD CONSTRAINT "fklt27fakw1xc4ghn0jw68mrjqq" FOREIGN KEY ("userid") REFERENCES "public"."user_details" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;

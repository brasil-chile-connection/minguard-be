ALTER TABLE incident
    ALTER COLUMN location DROP NOT NULL;

ALTER TABLE incident
    ALTER COLUMN description TYPE VARCHAR;

ALTER TABLE incident
    ALTER COLUMN title TYPE VARCHAR;

ALTER TABLE incident
    ALTER COLUMN location TYPE VARCHAR;
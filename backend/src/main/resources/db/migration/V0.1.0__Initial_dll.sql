CREATE TABLE Project
(
    id         INTEGER primary key,
    lastUpdate INTEGER NOT NULL
);

CREATE TABLE File
(
    projectId INTEGER NOT NULL REFERENCES Project ON DELETE CASCADE,
    id        INTEGER PRIMARY KEY
);

CREATE TABLE Author
(
    id        INTEGER PRIMARY KEY,
    projectId INTEGER NOT NULL REFERENCES Project ON DELETE CASCADE,
    name      TEXT
);

CREATE TABLE "Commit"
(
    projectId INTEGER NOT NULL REFERENCES Project ON DELETE CASCADE,
    hash      TEXT PRIMARY KEY,
    authorId  INTEGER NOT NULL REFERENCES Author ON DELETE CASCADE,
    timestamp INTEGER NOT NULL,
    message   TEXT    NOT NULL
);

CREATE TABLE FileChange
(
    commitHash TEXT    NOT NULL REFERENCES "Commit" ON DELETE CASCADE,
    fileId     INTEGER NOT NULL REFERENCES File ON DELETE CASCADE,
    path       TEXT,
    insertions INTEGER CHECK ( insertions >= 0 ),
    deletions  INTEGER CHECK ( deletions >= 0 ),
    CONSTRAINT FileChange_pk PRIMARY KEY (commitHash, fileId)
);

CREATE VIEW CurrentPath AS
SELECT fc.fileId, c.hash, fc.path
from FileChange fc
         join "Commit" c on c.hash = fc.commitHash
group by fc.fileId
having c.hash = max(c.hash);
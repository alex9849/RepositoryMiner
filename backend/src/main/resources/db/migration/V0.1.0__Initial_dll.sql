CREATE TABLE Project
(
    id         INTEGER primary key,
    lastUpdate INTEGER NOT NULL
);

CREATE TABLE File
(
    projectId   INTEGER NOT NULL REFERENCES Project,
    id          INTEGER primary key
);

CREATE TABLE Author
(
    id   INTEGER PRIMARY KEY,
    name TEXT
);

CREATE TABLE "Commit"
(
    projectId INTEGER NOT NULL REFERENCES Project,
    hash      TEXT PRIMARY KEY,
    authorId  INTEGER NOT NULL REFERENCES Author,
    timestamp INTEGER NOT NULL,
    message   TEXT    NOT NULL
);

CREATE TABLE FileChange
(
    commitHash TEXT    NOT NULL REFERENCES "Commit",
    fileId     INTEGER NOT NULL REFERENCES File,
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
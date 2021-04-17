CREATE TABLE Project
(
    id         INTEGER primary key,
    name       TEXT NOT NULL,
    lastUpdate INTEGER NOT NULL
);

CREATE TABLE File
(
    projectId INTEGER NOT NULL REFERENCES Project ON DELETE CASCADE,
    id        INTEGER PRIMARY KEY
);

CREATE TABLE LogAuthor
(
    id        INTEGER PRIMARY KEY,
    projectId INTEGER NOT NULL REFERENCES Project ON DELETE CASCADE,
    name      TEXT    NOT NULL
);

CREATE TABLE Author
(
    id        INTEGER PRIMARY KEY,
    projectId INTEGER NOT NULL REFERENCES Project ON DELETE CASCADE,
    name      TEXT    NOT NULL
);

CREATE TABLE AuthorLogAuthor
(
    authorId    INTEGER REFERENCES Author,
    logAuthorId INTEGER REFERENCES LogAuthor,
    CONSTRAINT author_log_author_pk PRIMARY KEY (authorId, logAuthorId)
);

CREATE TABLE "Commit"
(
    id        INTEGER PRIMARY KEY,
    projectId INTEGER REFERENCES Project ON DELETE CASCADE,
    isMerge   INTEGER NOT NULL,
    hash      TEXT,
    authorId  INTEGER NOT NULL REFERENCES Author ON DELETE CASCADE,
    timestamp INTEGER NOT NULL,
    message   TEXT    NOT NULL,
    CONSTRAINT commit_hash_unique UNIQUE (projectId, hash)
);

CREATE TABLE FileChange
(
    commitId   INTEGER REFERENCES "Commit" ON DELETE CASCADE,
    fileId     INTEGER REFERENCES File ON DELETE CASCADE,
    path       TEXT,
    insertions INTEGER CHECK ( insertions >= 0 ),
    deletions  INTEGER CHECK ( deletions >= 0 ),
    CONSTRAINT FileChange_pk PRIMARY KEY (commitId, fileId)
);

CREATE VIEW CurrentPath AS
SELECT c.projectId, fc.fileId, c.hash, fc.path
from FileChange fc
         join "Commit" c on c.id = fc.commitId
group by fc.fileId
having c.timestamp = max(c.timestamp)
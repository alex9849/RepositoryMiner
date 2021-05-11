CREATE TABLE Project
(
    id         INTEGER primary key,
    name       TEXT    NOT NULL,
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
    authorId  INTEGER REFERENCES Author ON DELETE SET NULL,
    name      TEXT    NOT NULL
);

CREATE TABLE Author
(
    id        INTEGER PRIMARY KEY,
    projectId INTEGER NOT NULL REFERENCES Project ON DELETE CASCADE,
    name      TEXT    NOT NULL
);

CREATE TABLE "Commit"
(
    id        INTEGER PRIMARY KEY,
    projectId INTEGER REFERENCES Project ON DELETE CASCADE,
    isMerge   INTEGER NOT NULL,
    hash      TEXT,
    authorId  INTEGER NOT NULL REFERENCES LogAuthor ON DELETE CASCADE,
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
having c.timestamp = max(c.timestamp);

select sum(commitsTogether) as totalCommits, *
FROM (select count(*) as commitsTogether,
             cp1.path,
             cp1.fileId,
             fc1.fileId,
             fc1.commitId,
             cp2.path,
             cp2.fileId,
             fc2.fileId,
             fc2.commitId
      from CurrentPath cp1
               join CurrentPath cp2 on cp2.projectId = 1
               left join FileChange fc1 on cp1.fileId = fc1.fileId
               left join FileChange fc2 on cp2.fileId = fc2.fileId
      where fc1.commitId = fc2.commitId
        and cp1.path like '%' --and fc1.fileId != fc2.fileId
      group by cp1.path, cp2.path
      having cp1.path not null
         and cp2.path not null
      order by commitsTogether desc) as sub;
--group by sub.cp1Path;


select count(*) as commitsTogether,
       cp1.path as cp1Path,
       cp1.fileId,
       fc1.fileId,
       fc1.commitId,
       cp2.path,
       cp2.fileId,
       fc2.fileId,
       fc2.commitId
from CurrentPath cp1
         join CurrentPath cp2 on cp2.projectId = 1
         left join FileChange fc1 on cp1.fileId = fc1.fileId
         left join FileChange fc2 on cp2.fileId = fc2.fileId
where fc1.commitId = fc2.commitId
  and cp1.path like 'src/%' --and fc1.fileId != fc2.fileId
group by cp1.path, cp2.path
having cp1.path not null
   and cp2.path not null
order by commitsTogether desc;
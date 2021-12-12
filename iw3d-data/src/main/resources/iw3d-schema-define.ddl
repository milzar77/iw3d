CREATE SCHEMA sm3d;

CREATE TABLE iw3d.iwebipv6 (
    iwid6 INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    CONSTRAINT iwebipv6_pk PRIMARY KEY (iwid6)
);

CREATE TABLE iw3d.iwebipv4 (
    iwid4 INTEGER NOT NULL DEFAULT 0,
    -- World identifier
    iwtitle VARCHAR(1024) DEFAULT 'Bookmark title',
    iwcategoryname VARCHAR(1024) DEFAULT 'Bookmark category',
    iwcategoryid INTEGER DEFAULT 0,
    iwurl VARCHAR(4096) DEFAULT 'Bookmark url',
    iwhookurl VARCHAR(255) DEFAULT 'Bookmark hook url',
    iwdomaintld VARCHAR(60) DEFAULT 'org',
    iwdomain1stlev VARCHAR(60) DEFAULT 'w3',
    iwdomain2ndlev VARCHAR(60) DEFAULT 'www',
    iwdomainname VARCHAR(255) DEFAULT 'www.w3.org',
    iwipaddress VARCHAR(60) DEFAULT '127.0.0.1',
    iwipclassa SMALLINT NOT NULL DEFAULT 0,
    iwipclassb SMALLINT NOT NULL DEFAULT 0,
    iwipclassc SMALLINT NOT NULL DEFAULT 0,
    iwipclassd SMALLINT NOT NULL DEFAULT 0,
    iwwebshotid INTEGER NOT NULL DEFAULT 1,
    iwwebdepthlevel INTEGER NOT NULL DEFAULT 1,
    iwhttpcode SMALLINT NOT NULL DEFAULT 200,
    iwhashttps BOOLEAN DEFAULT false,
    CONSTRAINT iwebipv4_pk PRIMARY KEY (iwid4)
);

-- temporary internet ip routes
CREATE TABLE iw3d.itemproutesipv4 (
    itrid4 INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    itrhop SMALLINT NOT NULL DEFAULT 0,
    itrdomainname VARCHAR(255) DEFAULT 'www.w3.org',
    itripaddress VARCHAR(60) DEFAULT '127.0.0.1',
    itripclassa SMALLINT NOT NULL DEFAULT 0,
    itripclassb SMALLINT NOT NULL DEFAULT 0,
    itripclassc SMALLINT NOT NULL DEFAULT 0,
    itripclassd SMALLINT NOT NULL DEFAULT 0,
    itiwid4 INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT itemproutesipv4_pk PRIMARY KEY (itrid4)
);

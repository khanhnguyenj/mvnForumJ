/*
-- $Header: /cvsroot/mvnforum/mvnad/sql/mvnad_interbase.sql,v 1.4 2010/04/06 11:01:58 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.4 $
-- $Date: 2010/04/06 11:01:58 $
-- Database : Interbase/Firebird
-- Driver   : org.firebirdsql.jdbc.FBDriver
-- Url      : jdbc:firebirdsql:<host>/3050:<database>
*/

/*
-- DROP TABLE mvnadBanner;
-- DROP TABLE mvnadZone;
-- DROP TABLE mvnadZoneBanner;
*/

/* DROP DOMAIN                                                               */
/*
DROP DOMAIN text;
*/

/* Create GENERATOR                                                          */
CREATE GENERATOR mvnadBanner_seq;
CREATE GENERATOR mvnadZone_seq;

/* It should be modeled after a CLOB, but then, JDBC code that expect  */
/* to use getString() could be broken.                                 */
CREATE DOMAIN text AS VARCHAR(4096);

COMMIT;

CREATE TABLE mvnadBanner
(
    BannerID                        INTEGER         NOT NULL,
    MemberName                      VARCHAR(30)     NOT NULL,
    BannerName                      VARCHAR(50)     NOT NULL,
    BannerDesc                      VARCHAR(250)    NOT NULL,
    BannerAltText                   VARCHAR(250)    NOT NULL,
    BannerMimeType                  VARCHAR(100)    NOT NULL,
    BannerPreText                   VARCHAR(250)    NOT NULL,
    BannerAfterText                 VARCHAR(250)    NOT NULL,
    BannerTargetURL                 VARCHAR(250)    NOT NULL,
    BannerImageURL                  VARCHAR(250)    NOT NULL,
    BannerWidth                     INTEGER         NOT NULL,
    BannerHeight                    INTEGER         NOT NULL,
    BannerWeight                    INTEGER         NOT NULL,
    BannerMaxImpression             INTEGER         NOT NULL,
    BannerReceivedImpression        INTEGER         NOT NULL,
    BannerMaxClick                  INTEGER         NOT NULL,
    BannerReceivedClick             INTEGER         NOT NULL,
    BannerZonePositionX             INTEGER         NOT NULL,
    BannerZonePositionY             INTEGER         NOT NULL,
    BannerStartDate                 TIMESTAMP       NOT NULL,
    BannerEndDate                   TIMESTAMP       NOT NULL,
    BannerIsHtml                    INTEGER         NOT NULL,
    BannerHtmlCode                  TEXT            NOT NULL,
    BannerCanTrackClicks            INTEGER         NOT NULL,
    BannerOption                    INTEGER         NOT NULL,
    BannerStatus                    INTEGER         NOT NULL,
    BannerType                      INTEGER         NOT NULL,
    BannerCreationDate              TIMESTAMP       NOT NULL,
    BannerModifiedDate              TIMESTAMP       NOT NULL,
    PRIMARY KEY (BannerID),
    UNIQUE (MemberName, BannerName)
);

CREATE TABLE mvnadZone
(
    ZoneID                          INTEGER         NOT NULL,
    MemberName                      VARCHAR(30)     NOT NULL,
    ZoneName                        VARCHAR(150)    NOT NULL,
    ZoneDesc                        VARCHAR(250)    NOT NULL,
    ZoneTargetWindow                VARCHAR(250)    NOT NULL,
    ZoneCellWidth                   INTEGER         NOT NULL,
    ZoneCellHeight                  INTEGER         NOT NULL,
    ZoneCellHorizontalCount         INTEGER         NOT NULL,
    ZoneCellVerticalCount           INTEGER         NOT NULL,
    ZoneMaxBanners                  INTEGER         NOT NULL,
    ZoneDirection                   INTEGER         NOT NULL,
    ZoneMaxImpression               INTEGER         NOT NULL,
    ZoneReceivedImpression          INTEGER         NOT NULL,
    ZoneMaxClick                    INTEGER         NOT NULL,
    ZoneReceivedClick               INTEGER         NOT NULL,
    ZoneAutoReloadTime              INTEGER         NOT NULL,
    ZoneOption                      INTEGER         NOT NULL,
    ZoneStatus                      INTEGER         NOT NULL,
    ZoneType                        INTEGER         NOT NULL,
    ZoneCreationDate                TIMESTAMP       NOT NULL,
    ZoneModifiedDate                TIMESTAMP       NOT NULL,
    PRIMARY KEY (ZoneID),
    UNIQUE (MemberName, ZoneName)
);

CREATE TABLE mvnadZoneBanner
(
    ZoneID                          INTEGER         NOT NULL,
    BannerID                        INTEGER         NOT NULL,
    RelationCellX                   INTEGER         NOT NULL,
    RelationCellY                   INTEGER         NOT NULL,
    RelationCellOption              INTEGER         NOT NULL,
    RelationWeight                  INTEGER         NOT NULL,
    RelationOption                  INTEGER         NOT NULL,
    RelationStatus                  INTEGER         NOT NULL,
    RelationType                    INTEGER         NOT NULL,
    RelationPublishStartDate        TIMESTAMP       NOT NULL,
    RelationPublishEndDate          TIMESTAMP       NOT NULL,
    RelationCreationDate            TIMESTAMP       NOT NULL,
    RelationModifiedDate            TIMESTAMP       NOT NULL,
    PRIMARY KEY (ZoneID, BannerID)
);

COMMIT;

CREATE TRIGGER mvnadBanner_trig_autoinc FOR mvnadBanner
ACTIVE BEFORE INSERT POSITION 1
AS
BEGIN
  IF (new.BannerID IS NULL) THEN
     new.BannerID = gen_id(mvnadBanner_seq, 1);
END
^

CREATE TRIGGER mvnadZone_trig_autoinc FOR mvnadZone
ACTIVE BEFORE INSERT POSITION 1
AS
BEGIN
  IF (new.ZoneID IS NULL) THEN
     new.ZoneID = gen_id(mvnadZone_seq, 1);
END
^

/* Return sentence finalizer to ';'                                          */
SET term ;^

COMMIT;

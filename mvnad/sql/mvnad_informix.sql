
-- $Header: /cvsroot/mvnforum/mvnad/sql/mvnad_informix.sql,v 1.4 2010/04/06 11:01:58 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.4 $
-- $Date: 2010/04/06 11:01:58 $
-- Database : JDBC Database
-- Driver   :
-- Url      :

-- DROP TABLE if exists mvnadBanner;
-- DROP TABLE if exists mvnadZone;
-- DROP TABLE if exists mvnadZoneBanner;

-- ALTER TABLE mvnadZone ADD ZoneAutoReloadTime INT AFTER ZoneDirection;
-- ALTER TABLE mvnadZone ADD MemberName VARCHAR(30) NOT NULL AFTER ZoneID;
-- ALTER TABLE mvnadZone DROP INDEX ZoneName, ADD UNIQUE ZoneName (ZoneName,MemberName)

CREATE TABLE mvnadBanner
(
    BannerID                        SERIAL                          NOT NULL,
    MemberName                      VARCHAR(30)                     NOT NULL,
    BannerName                      VARCHAR(50)                     NOT NULL,
    BannerDesc                      VARCHAR(250)                    NOT NULL,
    BannerAltText                   VARCHAR(250)                    NOT NULL,
    BannerMimeType                  VARCHAR(100)                    NOT NULL,
    BannerPreText                   VARCHAR(250)                    NOT NULL,
    BannerAfterText                 VARCHAR(250)                    NOT NULL,
    BannerTargetURL                 VARCHAR(250)                    NOT NULL,
    BannerImageURL                  VARCHAR(250)                    NOT NULL,
    BannerWidth                     INT                             NOT NULL,
    BannerHeight                    INT                             NOT NULL,
    BannerWeight                    INT                             NOT NULL,
    BannerMaxImpression             INT                             NOT NULL,
    BannerReceivedImpression        INT                             NOT NULL,
    BannerMaxClick                  INT                             NOT NULL,
    BannerReceivedClick             INT                             NOT NULL,
    BannerZonePositionX             INT                             NOT NULL,
    BannerZonePositionY             INT                             NOT NULL,
    BannerStartDate                 DATETIME YEAR TO SECOND         NOT NULL,
    BannerEndDate                   DATETIME YEAR TO SECOND         NOT NULL,
    BannerIsHtml                    INT                             NOT NULL,
    BannerHtmlCode                  LVARCHAR                        NOT NULL,
    BannerCanTrackClicks            INT                             NOT NULL,
    BannerOption                    INT                             NOT NULL,
    BannerStatus                    INT                             NOT NULL,
    BannerType                      INT                             NOT NULL,
    BannerCreationDate              DATETIME YEAR TO SECOND         NOT NULL,
    BannerModifiedDate              DATETIME YEAR TO SECOND         NOT NULL,
    PRIMARY KEY (BannerID),
    UNIQUE (MemberName, BannerName)
);

CREATE TABLE mvnadZone
(
    ZoneID                          SERIAL                          NOT NULL,
    MemberName                      VARCHAR(30)                     NOT NULL,
    ZoneName                        VARCHAR(150)                    NOT NULL,
    ZoneDesc                        VARCHAR(250)                    NOT NULL,
    ZoneTargetWindow                VARCHAR(250)                    NOT NULL,
    ZoneCellWidth                   INT                             NOT NULL,
    ZoneCellHeight                  INT                             NOT NULL,
    ZoneCellHorizontalCount         INT                             NOT NULL,
    ZoneCellVerticalCount           INT                             NOT NULL,
    ZoneMaxBanners                  INT                             NOT NULL,
    ZoneDirection                   INT                             NOT NULL,
    ZoneMaxImpression               INT                             NOT NULL,
    ZoneReceivedImpression          INT                             NOT NULL,
    ZoneMaxClick                    INT                             NOT NULL,
    ZoneReceivedClick               INT                             NOT NULL,
    ZoneAutoReloadTime              INT                             NOT NULL,
    ZoneOption                      INT                             NOT NULL,
    ZoneStatus                      INT                             NOT NULL,
    ZoneType                        INT                             NOT NULL,
    ZoneCreationDate                DATETIME YEAR TO SECOND         NOT NULL,
    ZoneModifiedDate                DATETIME YEAR TO SECOND         NOT NULL,
    PRIMARY KEY (ZoneID),
    UNIQUE (MemberName, ZoneName)
);

CREATE TABLE mvnadZoneBanner
(
    ZoneID                          INT                             NOT NULL,
    BannerID                        INT                             NOT NULL,
    RelationCellX                   INT                             NOT NULL,
    RelationCellY                   INT                             NOT NULL,
    RelationCellOption              INT                             NOT NULL,
    RelationWeight                  INT                             NOT NULL,
    RelationOption                  INT                             NOT NULL,
    RelationStatus                  INT                             NOT NULL,
    RelationType                    INT                             NOT NULL,
    RelationPublishStartDate        DATETIME YEAR TO SECOND         NOT NULL,
    RelationPublishEndDate          DATETIME YEAR TO SECOND         NOT NULL,
    RelationCreationDate            DATETIME YEAR TO SECOND         NOT NULL,
    RelationModifiedDate            DATETIME YEAR TO SECOND         NOT NULL,
    PRIMARY KEY (ZoneID, BannerID)
);


-- $Header: /cvsroot/mvnforum/mvnad/sql/mvnad_JDBC.sql,v 1.3 2008/05/16 08:09:52 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.3 $
-- $Date: 2008/05/16 08:09:52 $
-- Database : JDBC Database
-- Driver   :
-- Url      :

-- DROP TABLE mvnadBanner;
-- DROP TABLE mvnadZone;
-- DROP TABLE mvnadZoneBanner;

CREATE TABLE mvnadBanner
(
    BannerID                        INT             NOT NULL AUTO_INCREMENT,
    MemberName                      VARCHAR(30)     NOT NULL,
    BannerName                      VARCHAR(50)     NOT NULL,
    BannerDesc                      VARCHAR(250)    NOT NULL,
    BannerAltText                   VARCHAR(250)    NOT NULL,
    BannerMimeType                  VARCHAR(100)    NOT NULL,
    BannerPreText                   VARCHAR(250)    NOT NULL,
    BannerAfterText                 VARCHAR(250)    NOT NULL,
    BannerTargetURL                 VARCHAR(250)    NOT NULL,
    BannerImageURL                  VARCHAR(250)    NOT NULL,
    BannerWidth                     INT             NOT NULL,
    BannerHeight                    INT             NOT NULL,
    BannerWeight                    INT             NOT NULL,
    BannerMaxImpression             INT             NOT NULL,
    BannerReceivedImpression        INT             NOT NULL,
    BannerMaxClick                  INT             NOT NULL,
    BannerReceivedClick             INT             NOT NULL,
    BannerZonePositionX             INT             NOT NULL,
    BannerZonePositionY             INT             NOT NULL,
    BannerStartDate                 TIMESTAMP       NOT NULL,
    BannerEndDate                   TIMESTAMP       NOT NULL,
    BannerIsHtml                    INT             NOT NULL,
    BannerHtmlCode                  LONGVARCHAR     NOT NULL,
    BannerCanTrackClicks            INT             NOT NULL,
    BannerOption                    INT             NOT NULL,
    BannerStatus                    INT             NOT NULL,
    BannerType                      INT             NOT NULL,
    BannerCreationDate              TIMESTAMP       NOT NULL,
    BannerModifiedDate              TIMESTAMP       NOT NULL,
    PRIMARY KEY (BannerID),
    UNIQUE (MemberName, BannerName)
);

CREATE TABLE mvnadZone
(
    ZoneID                          INT             NOT NULL AUTO_INCREMENT,
    MemberName                      VARCHAR(30)     NOT NULL,
    ZoneName                        VARCHAR(150)    NOT NULL,
    ZoneDesc                        VARCHAR(250)    NOT NULL,
    ZoneTargetWindow                VARCHAR(250)    NOT NULL,
    ZoneCellWidth                   INT             NOT NULL,
    ZoneCellHeight                  INT             NOT NULL,
    ZoneCellHorizontalCount         INT             NOT NULL,
    ZoneCellVerticalCount           INT             NOT NULL,
    ZoneMaxBanners                  INT             NOT NULL,
    ZoneDirection                   INT             NOT NULL,
    ZoneMaxImpression               INT             NOT NULL,
    ZoneReceivedImpression          INT             NOT NULL,
    ZoneMaxClick                    INT             NOT NULL,
    ZoneReceivedClick               INT             NOT NULL,
    ZoneAutoReloadTime              INT             NOT NULL,
    ZoneOption                      INT             NOT NULL,
    ZoneStatus                      INT             NOT NULL,
    ZoneType                        INT             NOT NULL,
    ZoneCreationDate                TIMESTAMP       NOT NULL,
    ZoneModifiedDate                TIMESTAMP       NOT NULL,
    PRIMARY KEY (ZoneID),
    UNIQUE (MemberName, ZoneName)
);

CREATE TABLE mvnadZoneBanner
(
    ZoneID                          INT             NOT NULL,
    BannerID                        INT             NOT NULL,
    RelationCellX                   INT             NOT NULL,
    RelationCellY                   INT             NOT NULL,
    RelationCellOption              INT             NOT NULL,
    RelationWeight                  INT             NOT NULL,
    RelationOption                  INT             NOT NULL,
    RelationStatus                  INT             NOT NULL,
    RelationType                    INT             NOT NULL,
    RelationPublishStartDate        TIMESTAMP       NOT NULL,
    RelationPublishEndDate          TIMESTAMP       NOT NULL,
    RelationCreationDate            TIMESTAMP       NOT NULL,
    RelationModifiedDate            TIMESTAMP       NOT NULL,
    PRIMARY KEY (ZoneID, BannerID)
);

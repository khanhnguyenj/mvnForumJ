
-- $Header: /cvsroot/mvnforum/mvnad/sql/mvnad_Sysbase.sql,v 1.4 2010/04/06 11:01:58 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.4 $
-- $Date: 2010/04/06 11:01:58 $
-- Database : Sybase
-- Driver   :
-- Url      :

-- DROP TABLE mvnadBanner;
-- DROP TABLE mvnadZone;
-- DROP TABLE mvnadZoneBanner;

CREATE TABLE mvnadBanner
(
    BannerID                        INT             NOT NULL IDENTITY,
    MemberName                      NVARCHAR(30)    NOT NULL,
    BannerName                      NVARCHAR(50)    NOT NULL,
    BannerDesc                      NVARCHAR(250)   NOT NULL,
    BannerAltText                   NVARCHAR(250)   NOT NULL,
    BannerMimeType                  NVARCHAR(100)   NOT NULL,
    BannerPreText                   NVARCHAR(250)   NOT NULL,
    BannerAfterText                 NVARCHAR(250)   NOT NULL,
    BannerTargetURL                 NVARCHAR(250)   NOT NULL,
    BannerImageURL                  NVARCHAR(250)   NOT NULL,
    BannerWidth                     INT             NOT NULL,
    BannerHeight                    INT             NOT NULL,
    BannerWeight                    INT             NOT NULL,
    BannerMaxImpression             INT             NOT NULL,
    BannerReceivedImpression        INT             NOT NULL,
    BannerMaxClick                  INT             NOT NULL,
    BannerReceivedClick             INT             NOT NULL,
    BannerZonePositionX             INT             NOT NULL,
    BannerZonePositionY             INT             NOT NULL,
    BannerStartDate                 DATETIME        NOT NULL,
    BannerEndDate                   DATETIME        NOT NULL,
    BannerIsHtml                    INT             NOT NULL,
    BannerHtmlCode                  TEXT            NOT NULL,
    BannerCanTrackClicks            INT             NOT NULL,
    BannerOption                    INT             NOT NULL,
    BannerStatus                    INT             NOT NULL,
    BannerType                      INT             NOT NULL,
    BannerCreationDate              DATETIME        NOT NULL,
    BannerModifiedDate              DATETIME        NOT NULL,
    PRIMARY KEY (BannerID),
    UNIQUE (MemberName, BannerName)
);

CREATE TABLE mvnadZone
(
    ZoneID                          INT             NOT NULL IDENTITY,
    MemberName                      NVARCHAR(30)    NOT NULL,
    ZoneName                        NVARCHAR(150)   NOT NULL,
    ZoneDesc                        NVARCHAR(250)   NOT NULL,
    ZoneTargetWindow                NVARCHAR(250)   NOT NULL,
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
    ZoneCreationDate                DATETIME        NOT NULL,
    ZoneModifiedDate                DATETIME        NOT NULL,
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
    RelationPublishStartDate        DATETIME        NOT NULL,
    RelationPublishEndDate          DATETIME        NOT NULL,
    RelationCreationDate            DATETIME        NOT NULL,
    RelationModifiedDate            DATETIME        NOT NULL,
    PRIMARY KEY (ZoneID, BannerID)
);

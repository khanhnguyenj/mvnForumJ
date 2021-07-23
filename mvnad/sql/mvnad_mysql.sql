
# $Header: /cvsroot/mvnforum/mvnad/sql/mvnad_mysql.sql,v 1.3 2010/04/06 11:01:58 minhnn Exp $
# $Author: minhnn $
# $Revision: 1.3 $
# $Date: 2010/04/06 11:01:58 $
# Database : MySql
# Driver   : com.mysql.jdbc.Driver or org.gjt.mm.mysql.Driver
# Url      : jdbc:mysql://localhost/<database>?useUnicode=true&characterEncoding=utf-8&useOldUTF8Behavior=true
# Url for MySql 4.1 or later : jdbc:mysql://localhost/mvnforum?useServerPrepStmts=false
# MySql 4.1.x or later should read this: http://dev.mysql.com/doc/refman/5.0/en/cj-upgrading.html

# DROP TABLE if exists mvnadBanner;
# DROP TABLE if exists mvnadZone;
# DROP TABLE if exists mvnadZoneBanner;

# ALTER TABLE mvnadZone ADD ZoneAutoReloadTime INT AFTER ZoneDirection;
# ALTER TABLE mvnadZone ADD MemberName VARCHAR(30) NOT NULL AFTER ZoneID;
# ALTER TABLE mvnadZone ADD ZoneMaxImpression INT NOT NULL AFTER ZoneDirection;
# ALTER TABLE mvnadZone ADD ZoneReceivedImpression INT NOT NULL AFTER ZoneMaxImpression;
# ALTER TABLE mvnadZone ADD ZoneMaxClick INT NOT NULL AFTER ZoneReceivedImpression;
# ALTER TABLE mvnadZone ADD ZoneReceivedClick INT NOT NULL AFTER ZoneMaxClick;
# ALTER TABLE mvnadZone DROP INDEX ZoneName, ADD UNIQUE ZoneName (ZoneName,MemberName);

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

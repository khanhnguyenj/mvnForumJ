
-- $Header: /cvsroot/mvnforum/mvnad/sql/mvnad_oracle8.sql,v 1.4 2010/04/06 11:01:58 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.4 $
-- $Date: 2010/04/06 11:01:58 $
-- Database : Oracle 8i
-- Driver   : oracle.jdbc.driver.OracleDriver
-- Url      : jdbc:oracle:thin:@<host>:1521:<database>

-- DROP TABLE mvnadBanner;
-- DROP TABLE mvnadZone;
-- DROP TABLE mvnadZoneBanner;
--
-- DROP sequence
--
-- DROP SEQUENCE mvnadBanner_seq;
-- DROP SEQUENCE mvnadZone_seq;
--
-- CREATE sequences
--
CREATE SEQUENCE mvnadBanner_seq;
CREATE SEQUENCE mvnadZone_seq;

CREATE TABLE mvnadBanner
(
    BannerID                        INT             NOT NULL,
    MemberName                      VARCHAR2(30)    NOT NULL,
    BannerName                      VARCHAR2(50)    NOT NULL,
    BannerDesc                      VARCHAR2(250)   NULL,
    BannerAltText                   VARCHAR2(250)   NULL,
    BannerMimeType                  VARCHAR2(100)   NOT NULL,
    BannerPreText                   VARCHAR2(250)   NULL,
    BannerAfterText                 VARCHAR2(250)   NULL,
    BannerTargetURL                 VARCHAR2(250)   NULL,
    BannerImageURL                  VARCHAR2(250)   NULL,
    BannerWidth                     INT             NOT NULL,
    BannerHeight                    INT             NOT NULL,
    BannerWeight                    INT             NOT NULL,
    BannerMaxImpression             INT             NOT NULL,
    BannerReceivedImpression        INT             NOT NULL,
    BannerMaxClick                  INT             NOT NULL,
    BannerReceivedClick             INT             NOT NULL,
    BannerZonePositionX             INT             NOT NULL,
    BannerZonePositionY             INT             NOT NULL,
    BannerStartDate                 DATE            NOT NULL,
    BannerEndDate                   DATE            NOT NULL,
    BannerIsHtml                    INT             NOT NULL,
    BannerHtmlCode                  LONG VARCHAR    NULL,
    BannerCanTrackClicks            INT             NOT NULL,
    BannerOption                    INT             NOT NULL,
    BannerStatus                    INT             NOT NULL,
    BannerType                      INT             NOT NULL,
    BannerCreationDate              DATE            NOT NULL,
    BannerModifiedDate              DATE            NOT NULL,
    PRIMARY KEY (BannerID),
    UNIQUE (MemberName, BannerName)
);

CREATE TABLE mvnadZone
(
    ZoneID                          INT             NOT NULL,
    MemberName                      VARCHAR2(30)    NOT NULL,
    ZoneName                        VARCHAR2(150)   NOT NULL,
    ZoneDesc                        VARCHAR2(250)   NULL,
    ZoneTargetWindow                VARCHAR2(250)   NOT NULL,
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
    ZoneCreationDate                DATE            NOT NULL,
    ZoneModifiedDate                DATE            NOT NULL,
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
    RelationPublishStartDate        DATE            NOT NULL,
    RelationPublishEndDate          DATE            NOT NULL,
    RelationCreationDate            DATE            NOT NULL,
    RelationModifiedDate            DATE            NOT NULL,
    PRIMARY KEY (ZoneID, BannerID)
);

CREATE OR REPLACE TRIGGER mvnadBanner_trig_autoinc
BEFORE INSERT ON mvnadBanner
FOR EACH ROW
BEGIN
  IF (:new.BannerID IS NULL) THEN
    SELECT mvnadBanner_seq.nextval into :new.BannerID FROM DUAL;
  END IF;
END;
/

CREATE OR REPLACE TRIGGER mvnadZone_trig_autoinc
BEFORE INSERT ON mvnadZone
FOR EACH ROW
BEGIN
  IF (:new.ZoneID IS NULL) THEN
    SELECT mvnadZone_seq.nextval into :new.ZoneID FROM DUAL;
  END IF;
END;
/

COMMIT;

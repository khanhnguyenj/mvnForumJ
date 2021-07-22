-- This script is used to upgrade mvnForum from beta2 to beta3
-- The purpose is to support 2 forum with the same name in a category

ALTER TABLE mvnforumForum DROP INDEX ForumName, ADD UNIQUE ForumName (ForumName,CategoryID)
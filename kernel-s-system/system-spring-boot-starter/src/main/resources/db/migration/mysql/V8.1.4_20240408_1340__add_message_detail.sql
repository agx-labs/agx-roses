ALTER TABLE `sys_message`
ADD COLUMN `business_detail` json NULL COMMENT '业务的详细信息json' AFTER `business_type`;
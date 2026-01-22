-- 用户机构表，增加状态，代表是否启用
ALTER TABLE `sys_user_org`
ADD COLUMN `status_flag` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用：1-启用，2-禁用' AFTER `main_flag`;

-- 更新通知表
ALTER TABLE `sys_notice`
CHANGE COLUMN `notice_scope` `notice_user_scope` json NULL COMMENT '通知范围，存用户id集合' AFTER `notice_end_time`,
MODIFY COLUMN `notice_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知标题' AFTER `notice_id`,
MODIFY COLUMN `notice_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '通知内容' AFTER `notice_summary`,
MODIFY COLUMN `priority_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '优先级，来自字典：high-高优先级，middle-中，low-低' AFTER `notice_content`,
ADD COLUMN `publish_status` tinyint NULL COMMENT '是否发布：1-已发布，2-未发布' AFTER `notice_user_scope`,
ADD COLUMN `version_flag` bigint NULL DEFAULT NULL COMMENT '乐观锁' AFTER `publish_status`,
ADD COLUMN `expand_field` json NULL COMMENT '拓展字段' AFTER `version_flag`,
MODIFY COLUMN `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'N' COMMENT '是否删除：Y-被删除，N-未删除' AFTER `expand_field`,
ADD COLUMN `tenant_id` bigint NULL COMMENT '租户号' AFTER `del_flag`;

-- 更新个人信息表
ALTER TABLE `sys_message`
MODIFY COLUMN `receive_user_id` bigint NOT NULL COMMENT '接收用户id' AFTER `message_id`,
MODIFY COLUMN `message_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息标题' AFTER `send_user_id`,
MODIFY COLUMN `message_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '消息内容' AFTER `message_title`,
MODIFY COLUMN `message_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NORMAL' COMMENT '消息类型：NORMAL-普通类型，URL-带链接跳转' AFTER `message_content`,
MODIFY COLUMN `priority_level` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'low' COMMENT '优先级：high-高优先级，middle-中，low-低' AFTER `message_url`,
MODIFY COLUMN `business_id` varchar(255) NULL DEFAULT NULL COMMENT '关联业务id' AFTER `message_send_time`,
ADD COLUMN `version_flag` bigint NULL DEFAULT NULL COMMENT '乐观锁' AFTER `read_flag`,
ADD COLUMN `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户号' AFTER `update_time`;
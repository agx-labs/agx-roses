ALTER TABLE `sys_log_security`
MODIFY COLUMN `request_url` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '当前用户请求的url' AFTER `security_log_id`,
MODIFY COLUMN `log_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '安全日志内容' AFTER `client_os`;
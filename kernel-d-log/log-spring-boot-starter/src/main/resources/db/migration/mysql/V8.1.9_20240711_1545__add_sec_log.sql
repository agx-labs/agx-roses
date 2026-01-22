ALTER TABLE `sys_login_log` ADD COLUMN `account` varchar(255) NULL COMMENT '账号' AFTER `user_id`;

CREATE TABLE `sys_log_security` (
  `security_log_id` bigint NOT NULL COMMENT '主键',
  `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '当前用户请求的url',
  `request_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT 'http或方法的请求参数体',
  `server_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '当前服务器的ip',
  `client_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '客户端的ip',
  `http_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请求http方法',
  `client_browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '客户浏览器标识',
  `client_os` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '客户操作系统',
  `log_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '安全日志内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`security_log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='安全日志';

INSERT INTO `sys_menu_options`(`menu_option_id`, `app_id`, `menu_id`, `option_name`, `option_code`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1811330833279811585, 1671406745336016898, 1673525659931275265, '安全日志', 'SECURITY_LOG', '2024-07-11 17:24:58', 1339550467939639299, NULL, NULL);
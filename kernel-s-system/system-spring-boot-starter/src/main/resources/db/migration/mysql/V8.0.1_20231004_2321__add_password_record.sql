CREATE TABLE `sys_user_password_record`  (
  `record_id` bigint NOT NULL COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `history_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '历史密码记录',
  `history_password_salt` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '历史密码记录盐值',
  `update_password_time` datetime(0) NOT NULL COMMENT '修改密码时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户历史密码记录' ROW_FORMAT = Dynamic;

INSERT INTO `sys_menu`(`menu_id`, `menu_parent_id`, `menu_pids`, `menu_name`, `menu_code`, `app_id`, `menu_sort`, `status_flag`, `remark`, `menu_type`, `antdv_router`, `antdv_component`, `antdv_icon`, `antdv_link_url`, `antdv_active_url`, `antdv_visible`, `expand_field`, `version_flag`, `del_flag`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1710533513796653058, 1673524613037191169, '[-1],[1673524613037191169],', '安全策略', 'anquancelve', 1671406745336016898, 10310.00, 1, NULL, 10, '/backend/security', '/system/backend/security/index', 'icon-update-password', NULL, NULL, 'Y', NULL, 1, 'N', '2023-10-07 13:52:25', 1339550467939639299, '2023-10-07 13:54:50', 1339550467939639299);
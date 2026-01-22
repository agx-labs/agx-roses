CREATE TABLE `sys_role_limit`  (
  `role_limit_id` bigint NOT NULL COMMENT '主键',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `limit_type` tinyint NOT NULL COMMENT '角色限制类型：1-角色可分配的菜单，2-角色可分配的功能',
  `business_id` bigint NOT NULL COMMENT '业务id，为菜单id或菜单功能id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_user` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`role_limit_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限限制' ROW_FORMAT = Dynamic;
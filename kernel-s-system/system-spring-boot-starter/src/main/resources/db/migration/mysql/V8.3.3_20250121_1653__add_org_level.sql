ALTER TABLE `sys_hr_organization`
ADD COLUMN `level_code` varchar(100) NULL COMMENT '层级的编码，来自sys_hr_organization_level表' AFTER `expand_field`;

CREATE TABLE `sys_hr_organization_level` (
  `org_level_id` bigint NOT NULL COMMENT '层级的id',
  `level_number` int NOT NULL COMMENT '层级的级别，例如：1、2',
  `level_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '层级的名称',
  `level_code` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '层级的编码，需要填在org表中',
  `level_color` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '层级的颜色，16进制，不带#',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N' COMMENT '删除标记：Y-已删除，N-未删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` bigint DEFAULT NULL COMMENT '更新人',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户号',
  PRIMARY KEY (`org_level_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='组织机构层级';
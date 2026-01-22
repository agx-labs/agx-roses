CREATE TABLE `sys_role_category` (
  `id` bigint NOT NULL COMMENT '主键id',
  `category_parent_id` bigint NOT NULL COMMENT '父级角色分类id',
  `category_pids` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '父级角色分类id集合',
  `role_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色分类名称',
  `category_type` tinyint NOT NULL DEFAULT '1' COMMENT '角色分类类型：15-业务角色，20-公司角色',
  `company_id` bigint DEFAULT NULL COMMENT '所属公司id，当类型为20-公司角色时使用',
  `fld_sort` decimal(10,2) DEFAULT NULL COMMENT '角色分类排序',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N' COMMENT '删除标记：Y-已删除，N-未删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` bigint DEFAULT NULL COMMENT '更新人',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色分类';

-- 创建默认的类型
INSERT INTO `sys_role_category`(`id`, `category_parent_id`, `category_pids`, `role_category_name`, `category_type`, `fld_sort`, `del_flag`, `create_time`, `create_user`, `update_time`, `update_user`, `tenant_id`) VALUES (1881997026537611265, -1, '[-1],', '默认业务分类', 15, 100.00, 'N', '2025-01-22 17:26:26', 1339550467939639299, NULL, NULL, NULL);

-- 角色增加分类id
ALTER TABLE `sys_role`
ADD COLUMN `role_category_id` bigint NULL COMMENT '所属的角色分类id，如果是业务角色和公司角色可以加上所属分类' AFTER `role_type`;
UPDATE `sys_role` SET `role_category_id` = 1881997026537611265 WHERE `role_type` = 15;
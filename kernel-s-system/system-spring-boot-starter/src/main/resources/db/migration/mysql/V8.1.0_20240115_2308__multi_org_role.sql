ALTER TABLE `sys_role`
DROP COLUMN `role_system_flag`,
ADD COLUMN `role_type` tinyint NOT NULL DEFAULT 10 COMMENT '角色类型：10-系统角色，15-业务角色，20-公司角色' AFTER `remark`,
ADD COLUMN `role_company_id` bigint NULL COMMENT '角色所属公司id，当角色类型为20时传此值' AFTER `role_type`;

ALTER TABLE `sys_user_role`
ADD COLUMN `role_type` tinyint NOT NULL DEFAULT 10 COMMENT '角色类型：10-系统角色，15-业务角色，20-公司角色' AFTER `role_id`,
ADD COLUMN `role_org_id` bigint NULL DEFAULT NULL COMMENT '用户所属机构id' AFTER `role_type`;

INSERT INTO `sys_menu`(`menu_id`, `menu_parent_id`, `menu_pids`, `menu_name`, `menu_code`, `app_id`, `menu_sort`, `status_flag`, `remark`, `menu_type`, `antdv_router`, `antdv_component`, `antdv_icon`, `antdv_link_url`, `antdv_active_url`, `antdv_visible`, `expand_field`, `version_flag`, `del_flag`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1747806631403159554, 1671407312775016450, '[-1],[1671407312775016450],', '授权', 'shouquan', 1671406745336016898, 10104.00, 1, NULL, 10, '/system/structure/empower', '/system/structure/empower/index', 'icon-menu-linshimiyao', NULL, NULL, 'Y', NULL, 0, 'N', '2024-01-18 10:22:28', 1339550467939639299, NULL, NULL);
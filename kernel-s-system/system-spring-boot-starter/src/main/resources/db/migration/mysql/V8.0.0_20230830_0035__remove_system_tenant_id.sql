ALTER TABLE `sys_app` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_group` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_menu` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_menu_options` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_table_width` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_user_org` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_user_role` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_role_menu_options` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_hr_org_approver` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_portal_user_app` DROP COLUMN `tenant_id`;

-- 增加默认租户数据，默认租户id是1
update sys_user set tenant_id = 1 where tenant_id is null;

update sys_role set tenant_id = 1 where tenant_id is null;

update sys_hr_position set tenant_id = 1 where tenant_id is null;

update sys_hr_organization set tenant_id = 1 where tenant_id is null;

-- 增加租户相关配置
INSERT INTO `sys_dict`(`dict_id`, `dict_type_id`, `dict_code`, `dict_name`, `dict_name_pinyin`, `dict_encode`, `dict_short_name`, `dict_short_code`, `dict_parent_id`, `dict_pids`, `status_flag`, `dict_sort`, `version_flag`, `del_flag`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1697301378511056897, 1353547215422132226, 'TENANT_CONFIG', '租户配置', 'zhpz', NULL, NULL, NULL, -1, '[-1],', 1, 80.00, 1, 'N', '2023-09-01 01:32:38', 1339550467939639299, '2023-09-01 01:32:46', 1339550467939639299);
INSERT INTO `sys_config`(`config_id`, `config_name`, `config_code`, `config_value`, `sys_flag`, `remark`, `status_flag`, `group_code`, `del_flag`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1697301485767798785, '默认根租户的ID', 'DEFAULT_ROOT_TENANT_ID', '1', 'Y', NULL, 1, 'TENANT_CONFIG', 'N', '2023-09-01 01:33:03', 1339550467939639299, NULL, NULL);
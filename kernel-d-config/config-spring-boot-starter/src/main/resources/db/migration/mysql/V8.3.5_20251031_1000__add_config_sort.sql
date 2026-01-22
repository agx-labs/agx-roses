-- 系统配置表增加一个排序字段
ALTER TABLE `sys_config` ADD COLUMN `fld_sort` decimal(10, 2) NULL DEFAULT 1000 COMMENT '排序' AFTER `group_code`;

-- 新增一个druid配置分组
INSERT INTO `sys_dict` (`dict_id`, `dict_type_id`, `dict_code`, `dict_name`, `dict_name_pinyin`, `dict_encode`, `dict_short_name`, `dict_short_code`, `dict_parent_id`, `dict_pids`, `status_flag`, `dict_sort`, `version_flag`, `del_flag`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1984089168132517889, 1353547215422132226, 'DRUID_CONFIG', 'Druid配置', 'Druidpz', NULL, NULL, NULL, -1, '[-1],', 1, 110.00, 0, 'N', '2025-10-31 10:44:55', 1339550467939639299, NULL, NULL);

-- 更新druid配置排序和分组
UPDATE `sys_config` SET `group_code` = 'DRUID_CONFIG', `fld_sort` = 201.00 WHERE `config_id` = 33;
UPDATE `sys_config` SET `group_code` = 'DRUID_CONFIG', `fld_sort` = 202.00 WHERE `config_id` = 34;
UPDATE `sys_config` SET `group_code` = 'DRUID_CONFIG', `fld_sort` = 203.00 WHERE `config_id` = 35;
UPDATE `sys_config` SET `group_code` = 'DRUID_CONFIG', `fld_sort` = 204.00 WHERE `config_id` = 36;
UPDATE `sys_config` SET `group_code` = 'DRUID_CONFIG', `fld_sort` = 205.00 WHERE `config_id` = 37;
UPDATE `sys_config` SET `group_code` = 'DRUID_CONFIG', `fld_sort` = 206.00 WHERE `config_id` = 38;
UPDATE `sys_config` SET `group_code` = 'DRUID_CONFIG', `fld_sort` = 207.00 WHERE `config_id` = 39;
UPDATE `sys_config` SET `group_code` = 'DRUID_CONFIG', `fld_sort` = 208.00 WHERE `config_id` = 40;
UPDATE `sys_config` SET `group_code` = 'DRUID_CONFIG', `fld_sort` = 209.00 WHERE `config_id` = 41;
UPDATE `sys_config` SET `group_code` = 'DRUID_CONFIG', `fld_sort` = 210.00 WHERE `config_id` = 42;
UPDATE `sys_config` SET `group_code` = 'DRUID_CONFIG', `fld_sort` = 211.00 WHERE `config_id` = 43;

-- 更新系统配置的排序
UPDATE `sys_config` SET `fld_sort` = 5 WHERE `config_id` = 1356246056131649538;
UPDATE `sys_config` SET `fld_sort` = 10 WHERE `config_id` = 6;
UPDATE `sys_config` SET `fld_sort` = 20 WHERE `config_id` = 1350666094452482049;
UPDATE `sys_config` SET `config_name` = '图片文字验证码开关', `fld_sort` = 30, `remark` = '如若开启，请同时将前端/src/config/settings.js中的CAPTCHA_FLAG改为true' WHERE `config_id` = 23;
UPDATE `sys_config` SET `fld_sort` = 40, `remark` = '如若开启，请同时将前端/src/config/settings.js中的DRAW_CAPTCHA_FLAG改为true' WHERE `config_id` = 1402549781675610125;
UPDATE `sys_config` SET `fld_sort` = 50 WHERE `config_id` = 1402549781675610400;

-- 更新文件相关的配置排序
UPDATE `sys_config` SET `fld_sort` = 111 WHERE `config_id` = 1829152372339773442;
UPDATE `sys_config` SET `fld_sort` = 112 WHERE `config_id` = 1402549781675610405;
UPDATE `sys_config` SET `fld_sort` = 113 WHERE `config_id` = 30;

-- 删除没用的配置
-- SYS_TENANT_OPEN 多租户开关
DELETE FROM `sys_config` WHERE `config_id` = 22;
-- 开发模式开关
DELETE FROM `sys_config` WHERE `config_id` = 1402549781675610505;
ALTER TABLE `sys_role_data_scope`
DROP COLUMN `organization_id`,
ADD COLUMN `data_scope_type` tinyint NOT NULL DEFAULT 20 COMMENT '数据范围类型：10-仅本人数据，20-本部门数据，30-本部门及以下数据，31-本公司及以下数据，32-指定机构层级及以下，40-指定机构集合数据，41-指定机构及以下，50-全部数据' AFTER `role_id`,
ADD COLUMN `org_level_code` varchar(100) NULL COMMENT '层级的编码，用在类型为32-指定层级及以下，情况时使用' AFTER `data_scope_type`,
ADD COLUMN `define_org_list` json NULL COMMENT '指定机构集合列表，用在类型为40-指定机构集合数据，情况时使用' AFTER `org_level_code`,
ADD COLUMN `define_org_id` bigint NULL COMMENT '指定机构的id，用在类型为41-指定机构及以下，情况时使用' AFTER `define_org_list`;